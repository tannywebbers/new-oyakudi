package com.example.data.model

/**
 * Announcement content pulled from the public.brand_settings table.
 * Drives the news ticker shown under the credit card and the popup modal
 * shown once per hour when the app is opened.
 */
data class Announcement(
    val newsTickerEnabled: Boolean = false,
    val newsTickerText: String? = null,
    val popupModalEnabled: Boolean = false,
    val popupModalHtml: String? = null
) {
    val hasTicker: Boolean get() = newsTickerEnabled && !newsTickerText.isNullOrBlank()
    val hasPopup: Boolean get() = popupModalEnabled && !popupModalHtml.isNullOrBlank()
}
