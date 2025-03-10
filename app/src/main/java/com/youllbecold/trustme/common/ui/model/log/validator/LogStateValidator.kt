package com.youllbecold.trustme.common.ui.model.log.validator

import android.content.Context
import android.widget.Toast
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.model.log.LogState

/**
 * Validates the log state and shows a toast if the state is invalid.
 */
object LogStateValidator {
    /**
     * Validates the log state and shows a toast if the state is invalid.
     */
    fun validate(context: Context, logState: LogState): Boolean {
        if (logState.timeTo.isBefore(logState.timeFrom) || logState.timeTo == logState.timeFrom) {
            Toast.makeText(
                context,
                context.getString(R.string.toast_invalid_time_range),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (logState.clothes.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.toast_no_clothes_selected),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }
}
