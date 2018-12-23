package org.hexworks.zircon.api.behavior

import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.zircon.api.component.ColorTheme
import org.hexworks.zircon.api.component.ComponentStyleSet

/**
 * A [Themeable] object can be themed by using [ColorTheme]s.
 */
interface Themeable {

    val themeValue: ObservableValue<ColorTheme>

    /**
     * The current theme of this component.
     */
    val currentTheme: ColorTheme
        get() = themeValue.value

    /**
     * Applies a [ColorTheme] to this component and recursively to all its children (if any).
     * @return the [ComponentStyleSet] which the [ColorTheme] was converted to.
     */
    fun applyColorTheme(colorTheme: ColorTheme): ComponentStyleSet
}
