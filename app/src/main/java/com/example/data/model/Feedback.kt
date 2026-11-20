package com.example.data.model

enum class IssueType(val displayName: String) {
    ACTIVATION("Activation Issues"),
    WITHDRAWAL("Withdrawal Issues"),
    APP_TECHNICAL("App Technical Issues"),
    SUGGESTIONS_OTHERS("Suggestions & Others")
}

data class FeedbackItem(
    val id: String = "",
    val issueType: IssueType = IssueType.SUGGESTIONS_OTHERS,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
