package com.example.data.repository

import android.content.Context
import com.example.data.model.Announcement
import com.example.data.supabase.SupabaseClientProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Supplies the announcement content (news ticker + popup modal) fetched from the
 * public.brand_settings table, and throttles how often the popup modal is shown.
 */
interface AnnouncementRepository {
    suspend fun fetchAnnouncement(): Announcement

    /** True when the popup may be shown again (never shown, or more than an hour ago). */
    fun shouldShowPopup(): Boolean

    /** Records that the popup was just shown, starting the one hour cooldown. */
    fun markPopupShown()
}

class AnnouncementRepositoryImpl(
    private val context: Context
) : AnnouncementRepository {

    override suspend fun fetchAnnouncement(): Announcement = withContext(Dispatchers.IO) {
        try {
            val api = SupabaseClientProvider.getApi() ?: return@withContext Announcement()
            val response = api.getBrandSettings()
            val dto = if (response.isSuccessful) response.body()?.firstOrNull() else null
            if (dto == null) {
                Announcement()
            } else {
                Announcement(
                    newsTickerEnabled = dto.newsTickerEnabled ?: false,
                    newsTickerText = dto.newsTickerText,
                    popupModalEnabled = dto.popupModalEnabled ?: false,
                    popupModalHtml = dto.popupModalHtml
                )
            }
        } catch (e: Exception) {
            Announcement()
        }
    }

    override fun shouldShowPopup(): Boolean {
        val lastShownAt = prefs().getLong(KEY_POPUP_LAST_SHOWN, 0L)
        if (lastShownAt == 0L) return true
        return System.currentTimeMillis() - lastShownAt >= POPUP_INTERVAL_MS
    }

    override fun markPopupShown() {
        prefs().edit().putLong(KEY_POPUP_LAST_SHOWN, System.currentTimeMillis()).apply()
    }

    private fun prefs() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "pixkudi_announcement_prefs"
        private const val KEY_POPUP_LAST_SHOWN = "popup_last_shown_at"
        private const val POPUP_INTERVAL_MS = 60L * 60L * 1000L
    }
}
