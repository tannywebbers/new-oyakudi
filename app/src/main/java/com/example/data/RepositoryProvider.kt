package com.example.data

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.CardStorage
import com.example.data.repository.AnnouncementRepository
import com.example.data.repository.AnnouncementRepositoryImpl
import com.example.data.repository.AppConfigRepository
import com.example.data.repository.AppConfigRepositoryImpl
import com.example.data.repository.CreditRepository
import com.example.data.repository.CreditRepositoryImpl
import com.example.data.repository.CustomerRepository
import com.example.data.repository.CustomerRepositoryImpl
import com.example.data.repository.FeedbackRepository
import com.example.data.repository.FeedbackRepositoryImpl
import com.example.data.repository.LoanRepository
import com.example.data.repository.LoanRepositoryImpl
import com.example.data.repository.RepaymentRepository
import com.example.data.repository.RepaymentRepositoryImpl
import com.example.data.repository.UserRepository
import com.example.data.repository.UserRepositoryImpl

/**
 * Global Dependency Provider for repositories connecting to SQLite Room and Supabase.
 */
object RepositoryProvider {
    private var applicationContext: Context? = null

    fun initialize(context: Context) {
        applicationContext = context.applicationContext
        com.example.data.supabase.SupabaseConfig.init(context.applicationContext)
        com.example.data.supabase.SupabaseClient.initialize(
            com.example.data.supabase.SupabaseConfig.supabaseUrl,
            com.example.data.supabase.SupabaseConfig.supabaseAnonKey
        )
    }

    private val database: AppDatabase by lazy {
        val ctx = applicationContext ?: throw IllegalStateException("RepositoryProvider not initialized with Context")
        AppDatabase.getDatabase(ctx)
    }

    val customerRepository: CustomerRepository by lazy {
        CustomerRepositoryImpl(
            customerDao = database.customerDao(),
            transactionDao = database.transactionDao()
        )
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(
            userSessionDao = database.userSessionDao(),
            customerRepository = customerRepository
        )
    }

    private val cardStorage: CardStorage by lazy {
        val ctx = applicationContext ?: throw IllegalStateException("RepositoryProvider not initialized with Context")
        CardStorage(ctx)
    }

    val creditRepository: CreditRepository by lazy {
        CreditRepositoryImpl(
            customerRepository = customerRepository,
            transactionDao = database.transactionDao(),
            cardStorage = cardStorage
        )
    }

    val loanRepository: LoanRepository by lazy {
        LoanRepositoryImpl(
            customerRepository = customerRepository,
            transactionDao = database.transactionDao()
        )
    }

    val feedbackRepository: FeedbackRepository by lazy {
        FeedbackRepositoryImpl(
            feedbackDao = database.feedbackDao(),
            customerRepository = customerRepository
        )
    }

    val appConfigRepository: AppConfigRepository by lazy {
        AppConfigRepositoryImpl()
    }

    val announcementRepository: AnnouncementRepository by lazy {
        AnnouncementRepositoryImpl(
            context = applicationContext
                ?: throw IllegalStateException("RepositoryProvider not initialized with Context")
        )
    }

    val repaymentRepository: RepaymentRepository by lazy {
        RepaymentRepositoryImpl()
    }
}
