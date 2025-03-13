package com.youllbecold.trustme.ui.navigation

import androidx.annotation.StringRes
import com.youllbecold.trustme.common.ui.components.themed.IconType

/**
 * Interface to be implemented by items that have toolbar.
 */
interface Toolbar {
    /**
     * Toolbar title.
     */
    @get:StringRes
    val toolbarTitle: Int

    /**
     * Toolbar icon.
     */
    val toolbarIcon: IconType?
        get() = null

    /**
     * Whether to show the back button.
     */
    val showBackButton: Boolean
        get() = false

    /**
     * Whether to show the info action.
     */
    val showInfoAction: Boolean
        get() = false
}
