package jp.co.pise.studyapp.definition

import android.text.TextUtils
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utility {
    companion object {
        fun stringParseDate(value: String?, format: String): Date? {
            var result: Date? = null
            if (!TextUtils.isEmpty(value)) {
                val sdf = SimpleDateFormat(format, Locale.JAPAN)
                try {
                    result = sdf.parse(value)
                } catch (e: ParseException) {
                    result = null
                }

            }
            return result
        }

        fun dateParseString(value: Date?, format: String): String? {
            var result: String? = null
            if (value != null) {
                val sdf = SimpleDateFormat(format, Locale.JAPAN)
                result = sdf.format(value)
            }
            return result
        }

        fun formatJpCurrency(value: Long, format: String): String? {
            var result: String?
            try {
                result = String.format(format, NumberFormat.getCurrencyInstance(Locale.JAPAN).format(value))
            } catch (e: Exception) {
                result = null
            }
            return result
        }
    }
}