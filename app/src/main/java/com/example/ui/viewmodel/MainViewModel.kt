package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.data.RepositoryProvider
import com.example.data.local.CooldownEntity
import com.example.data.model.Announcement
import com.example.data.model.CardInfo
import com.example.data.model.CompanyInfo
import com.example.data.model.CreditInfo
import com.example.data.model.CreditStatement
import com.example.data.model.CreditTransaction
import com.example.data.model.CustomerCase
import com.example.data.model.FaqItem
import com.example.data.model.FeedbackItem
import com.example.data.model.IssueType
import com.example.data.model.LoanApplicationRequest
import com.example.data.model.LoanAppSubmissionResult
import com.example.data.model.LoanDetail
import com.example.data.model.PrivacySection
import com.example.data.model.RepaymentStatus
import com.example.data.model.UserProfile
import com.example.data.model.VirtualRepaymentAccount
import com.example.data.repository.AnnouncementRepository
import com.example.data.repository.AppConfigRepository
import com.example.data.repository.CreditRepository
import com.example.data.repository.CustomerRepository
import com.example.data.repository.FeedbackRepository
import com.example.data.repository.LoanRepository
import com.example.data.repository.RepaymentRepository
import com.example.data.repository.RepaymentWebhookPayload
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class MainUiState(
    val selectedIssueType: IssueType = IssueType.SUGGESTIONS_OTHERS,
    val feedbackDescription: String = "",
    val isSubmittingFeedback: Boolean = false,
    val feedbackSuccess: Boolean = false,
    val isSubmittingLoan: Boolean = false,
    val loanSubmissionResult: LoanAppSubmissionResult? = null,
    val feedbackErrorMessage: String? = null,
    val activeFaqExpandedId: String? = null,
    val showLogoutDialog: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val activeCooldown: CooldownEntity? = null
)

class MainViewModel(
    private val customerRepository: CustomerRepository = RepositoryProvider.customerRepository,
    private val userRepository: UserRepository = RepositoryProvider.userRepository,
    private val creditRepository: CreditRepository = RepositoryProvider.creditRepository,
    private val loanRepository: LoanRepository = RepositoryProvider.loanRepository,
    private val feedbackRepository: FeedbackRepository = RepositoryProvider.feedbackRepository,
    private val appConfigRepository: AppConfigRepository = RepositoryProvider.appConfigRepository,
    private val announcementRepository: AnnouncementRepository = RepositoryProvider.announcementRepository,
    private val repaymentRepository: RepaymentRepository = RepositoryProvider.repaymentRepository
) : ViewModel() {

    val customerCase: StateFlow<CustomerCase> = customerRepository.currentCustomer
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CustomerCase()
        )

    private val _announcement = MutableStateFlow(Announcement())
    val announcement: StateFlow<Announcement> = _announcement.asStateFlow()

    private val _showAnnouncementPopup = MutableStateFlow(false)
    val showAnnouncementPopup: StateFlow<Boolean> = _showAnnouncementPopup.asStateFlow()

    private val _announcementPopupHtml = MutableStateFlow("")
    val announcementPopupHtml: StateFlow<String> = _announcementPopupHtml.asStateFlow()

    private val _showRepaymentModal = MutableStateFlow(false)
    val showRepaymentModal: StateFlow<Boolean> = _showRepaymentModal.asStateFlow()

    private val _repaymentStatus = MutableStateFlow(RepaymentStatus.IDLE)
    val repaymentStatus: StateFlow<RepaymentStatus> = _repaymentStatus.asStateFlow()

    val creditInfo: StateFlow<CreditInfo> = creditRepository.creditInfo
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CreditInfo()
        )

    val currentLoan: StateFlow<LoanDetail?> = loanRepository.currentLoan
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LoanDetail()
        )

    private val _selectedWithdrawAmount = MutableStateFlow(5000L)
    val selectedWithdrawAmount: StateFlow<Long> = _selectedWithdrawAmount.asStateFlow()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        checkCooldown()
    }

    fun checkCooldown() {
        viewModelScope.launch {
            val cooldown = customerRepository.checkWithdrawCooldown()
            _uiState.value = _uiState.value.copy(activeCooldown = cooldown)
        }
    }

    /**
     * Fetches the announcement (news ticker + popup) from the database and, when the
     * popup is enabled and the one-hour cooldown has elapsed, queues it for display.
     */
    fun loadAnnouncementAndMaybeShowPopup() {
        viewModelScope.launch {
            val announcement = announcementRepository.fetchAnnouncement()
            _announcement.value = announcement
            if (announcement.hasPopup && announcementRepository.shouldShowPopup()) {
                _announcementPopupHtml.value = announcement.popupModalHtml.orEmpty()
                _showAnnouncementPopup.value = true
            }
        }
    }

    fun dismissAnnouncementPopup() {
        _showAnnouncementPopup.value = false
        announcementRepository.markPopupShown()
    }

    /** Opens the repayment modal and resets the "Paid" button to its idle state. */
    fun openRepaymentModal() {
        _repaymentStatus.value = RepaymentStatus.IDLE
        _showRepaymentModal.value = true
    }

    /** Closes the repayment modal. */
    fun dismissRepaymentModal() {
        _showRepaymentModal.value = false
        _repaymentStatus.value = RepaymentStatus.IDLE
    }

    /**
     * Marks the customer's repayment as paid: posts to the open payment webhook and
     * updates the modal button state accordingly.
     */
    fun submitRepayment() {
        if (_repaymentStatus.value == RepaymentStatus.LOADING) return
        val customer = customerRepository.currentCustomer.value
        viewModelScope.launch {
            _repaymentStatus.value = RepaymentStatus.LOADING
            val payload = RepaymentWebhookPayload(
                event = "repayment_marked_paid",
                customerName = customer.customerName,
                amount = customer.outstandingLoanBalance,
                customerId = customer.id,
                loanId = customer.referenceNumber.ifBlank { customer.id },
                bank = VirtualRepaymentAccount.BANK,
                accountNumber = VirtualRepaymentAccount.ACCOUNT_NUMBER,
                accountHolder = VirtualRepaymentAccount.ACCOUNT_HOLDER,
                timestamp = utcIsoTimestamp()
            )
            val result = repaymentRepository.markRepaymentPaid(payload)
            _repaymentStatus.value = result.fold(
                onSuccess = { RepaymentStatus.SUCCESS },
                onFailure = { error ->
                    Log.e(TAG, "Repayment submission failed", error)
                    RepaymentStatus.ERROR
                }
            )
        }
    }

    /** Re-reads the customer's debt/status from the backend so the screen reflects the latest state. */
    suspend fun refreshCustomerDebt() {
        customerRepository.refreshCustomerFromDb()
    }

    private fun utcIsoTimestamp(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date())
    }

    fun setSelectedWithdrawAmount(amount: Long) {
        _selectedWithdrawAmount.value = amount
    }

    fun calculateRepaymentAmount(amount: Long, periodDays: Int = 180, dailyRate: Double = 0.08): Long {
        val totalInterest = (amount * (dailyRate / 100.0) * periodDays).toLong()
        return amount + totalInterest
    }

    fun getCompanyInfo(): CompanyInfo = appConfigRepository.getCompanyInfo()
    fun getFaqs(): List<FaqItem> = appConfigRepository.getFaqs()
    fun getPrivacyPolicy(): List<PrivacySection> = appConfigRepository.getPrivacyPolicy()

    suspend fun getCreditStatement(): CreditStatement = creditRepository.getCreditStatement()
    suspend fun getUsedCreditHistory(): List<CreditTransaction> = creditRepository.getUsedCreditHistory()

    fun onIssueTypeSelected(issueType: IssueType) {
        _uiState.value = _uiState.value.copy(selectedIssueType = issueType)
    }

    fun onFeedbackDescriptionChanged(description: String) {
        _uiState.value = _uiState.value.copy(feedbackDescription = description, feedbackErrorMessage = null)
    }

    fun toggleFaq(id: String) {
        val current = _uiState.value.activeFaqExpandedId
        _uiState.value = _uiState.value.copy(
            activeFaqExpandedId = if (current == id) null else id
        )
    }

    fun submitFeedback(onSuccess: () -> Unit) {
        val desc = _uiState.value.feedbackDescription.trim()
        if (desc.isBlank()) {
            _uiState.value = _uiState.value.copy(
                feedbackErrorMessage = "Please enter a description for your feedback"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmittingFeedback = true, feedbackErrorMessage = null)
            val item = FeedbackItem(
                issueType = _uiState.value.selectedIssueType,
                description = desc
            )
            val result = feedbackRepository.submitFeedback(item)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSubmittingFeedback = false,
                    feedbackSuccess = true,
                    feedbackDescription = ""
                )
                onSuccess()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isSubmittingFeedback = false,
                    feedbackErrorMessage = error.localizedMessage ?: "Failed to submit feedback"
                )
            }
        }
    }

    fun submitLoanApplication(
        amount: Long = _selectedWithdrawAmount.value,
        periodDays: Int = 180,
        onResult: (LoanAppSubmissionResult) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmittingLoan = true)
            val result = customerRepository.submitLoanApplication(amount, periodDays)
            result.onSuccess { submissionResult ->
                _uiState.value = _uiState.value.copy(
                    isSubmittingLoan = false,
                    loanSubmissionResult = submissionResult
                )
                checkCooldown()
                onResult(submissionResult)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isSubmittingLoan = false)
            }
        }
    }

    fun setCustomerStatusForTesting(status: String, loanAppStatus: String) {
        viewModelScope.launch {
            customerRepository.updateCustomerStatusForTesting(status, loanAppStatus)
            checkCooldown()
        }
    }

    fun setCreditLimitForTesting(newLimit: Long) {
        viewModelScope.launch {
            customerRepository.setCreditLimitForTesting(newLimit)
        }
    }

    fun clearCooldownForTesting() {
        viewModelScope.launch {
            customerRepository.clearCooldown()
            _uiState.value = _uiState.value.copy(activeCooldown = null)
        }
    }

    fun setLogoutDialogVisible(visible: Boolean) {
        _uiState.value = _uiState.value.copy(showLogoutDialog = visible)
    }

    fun setDeleteAccountDialogVisible(visible: Boolean) {
        _uiState.value = _uiState.value.copy(showDeleteAccountDialog = visible)
    }

    fun dismissSubmissionDialog() {
        _uiState.value = _uiState.value.copy(loanSubmissionResult = null)
    }

    fun dismissSuccessDialog() {
        _uiState.value = _uiState.value.copy(feedbackSuccess = false, loanSubmissionResult = null)
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}

