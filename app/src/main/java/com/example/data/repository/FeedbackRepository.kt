package com.example.data.repository

import com.example.data.local.FeedbackDao
import com.example.data.local.FeedbackEntity
import com.example.data.model.FeedbackItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

interface FeedbackRepository {
    val submittedFeedback: Flow<List<FeedbackItem>>
    suspend fun submitFeedback(feedback: FeedbackItem): Result<String>
}

class FeedbackRepositoryImpl(
    private val feedbackDao: FeedbackDao,
    private val customerRepository: CustomerRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : FeedbackRepository {

    private val _submittedFeedback = MutableStateFlow<List<FeedbackItem>>(emptyList())
    override val submittedFeedback: Flow<List<FeedbackItem>> = _submittedFeedback.asStateFlow()

    override suspend fun submitFeedback(feedback: FeedbackItem): Result<String> = withContext(Dispatchers.IO) {
        val currentCust = customerRepository.currentCustomer.value
        val entity = FeedbackEntity(
            id = "fb_${System.currentTimeMillis()}",
            customerId = currentCust.id,
            issueType = feedback.issueType.name,
            description = feedback.description,
            submittedAt = System.currentTimeMillis()
        )
        feedbackDao.insertFeedback(entity)
        _submittedFeedback.value = _submittedFeedback.value + entity.toFeedbackItem()
        Result.success("Feedback submitted successfully")
    }
}
