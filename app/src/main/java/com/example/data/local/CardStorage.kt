package com.example.data.local

import android.content.Context
import java.security.SecureRandom

/**
 * Local storage for the member's virtual credit card number.
 *
 * The number is generated exactly once per install (16 digits, always starting with 6623),
 * persisted on the device, and then reused for every subsequent read so the card the user
 * sees on the dashboard never changes between launches.
 */
class CardStorage(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** Returns the persisted 16-digit card number, generating and storing it on first use. */
    fun getOrCreateCardNumber(): String {
        val stored = prefs.getString(KEY_CARD_NUMBER, null)
            ?.filter { it.isDigit() }
            ?.takeIf { it.length == CARD_LENGTH && it.startsWith(CARD_PREFIX) }
        if (stored != null) return stored

        val generated = generateCardNumber()
        prefs.edit().putString(KEY_CARD_NUMBER, generated).apply()
        return generated
    }

    /** Groups a raw 16-digit number for display, e.g. 6623 1234 5678 9012. */
    fun formatForDisplay(rawDigits: String): String =
        rawDigits.filter { it.isDigit() }.chunked(4).joinToString(" ")

    /** Convenience: the card number already formatted for display. */
    fun getCardNumberForDisplay(): String = formatForDisplay(getOrCreateCardNumber())

    private fun generateCardNumber(): String {
        val digits = StringBuilder(CARD_PREFIX)
        while (digits.length < CARD_LENGTH) {
            digits.append(RANDOM.nextInt(10))
        }
        return digits.toString()
    }

    companion object {
        private const val PREFS_NAME = "pixkudi_virtual_card_storage"
        private const val KEY_CARD_NUMBER = "virtual_card_number"
        private const val CARD_PREFIX = "6623"
        private const val CARD_LENGTH = 16

        private val RANDOM = SecureRandom()
    }
}
