package org.hexworks.zircon.internal.component.impl

import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.behavior.Selectable
import org.hexworks.zircon.api.behavior.TextHolder
import org.hexworks.zircon.api.builder.component.ComponentStyleSetBuilder
import org.hexworks.zircon.api.builder.graphics.StyleSetBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.CheckBox
import org.hexworks.zircon.api.component.ColorTheme
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.component.data.ComponentMetadata
import org.hexworks.zircon.api.component.renderer.ComponentRenderingStrategy
import org.hexworks.zircon.api.uievent.Input
import org.hexworks.zircon.api.uievent.MouseAction
import org.hexworks.zircon.internal.component.impl.DefaultCheckBox.CheckBoxState.*

class DefaultCheckBox(componentMetadata: ComponentMetadata,
                      initialText: String,
                      private val renderingStrategy: ComponentRenderingStrategy<CheckBox>)
    : CheckBox, DefaultComponent(
        componentMetadata = componentMetadata,
        renderer = renderingStrategy),
        TextHolder by TextHolder.create(initialText),
        Selectable by Selectable.create() {

    override val state: CheckBoxState
        get() = checkBoxState

    private var checkBoxState = UNCHECKED
    private var pressing = false

    init {
        render()
        textProperty.onChange {
            render()
        }
        selectedProperty.onChange {
            render()
        }
    }

    // TODO: test this rudimentary state machine
    override fun mouseEntered(action: MouseAction) {
        componentStyleSet.applyMouseOverStyle()
        render()
    }

    override fun mouseExited(action: MouseAction) {
        pressing = false
        checkBoxState = if (isSelected) CHECKED else UNCHECKED
        componentStyleSet.reset()
        render()
    }

    override fun mousePressed(action: MouseAction) {
        pressing = true
        checkBoxState = if (isSelected) UNCHECKING else CHECKING
        componentStyleSet.applyActiveStyle()
        render()
    }

    override fun mouseReleased(action: MouseAction) {
        componentStyleSet.applyMouseOverStyle()
        pressing = false
        isSelected = isSelected.not()
        checkBoxState = if (isSelected) CHECKED else UNCHECKED
        render()
    }

    override fun acceptsFocus(): Boolean {
        return true
    }

    override fun giveFocus(input: Maybe<Input>): Boolean {
        componentStyleSet.applyFocusedStyle()
        render()
        return true
    }

    override fun takeFocus(input: Maybe<Input>) {
        componentStyleSet.reset()
        render()
    }

    override fun applyColorTheme(colorTheme: ColorTheme): ComponentStyleSet {
        return ComponentStyleSetBuilder.newBuilder()
                .withDefaultStyle(StyleSetBuilder.newBuilder()
                        .withForegroundColor(colorTheme.accentColor)
                        .withBackgroundColor(TileColor.transparent())
                        .build())
                .withMouseOverStyle(StyleSetBuilder.newBuilder()
                        .withForegroundColor(colorTheme.primaryBackgroundColor)
                        .withBackgroundColor(colorTheme.accentColor)
                        .build())
                .withFocusedStyle(StyleSetBuilder.newBuilder()
                        .withForegroundColor(colorTheme.secondaryBackgroundColor)
                        .withBackgroundColor(colorTheme.accentColor)
                        .build())
                .withActiveStyle(StyleSetBuilder.newBuilder()
                        .withForegroundColor(colorTheme.secondaryForegroundColor)
                        .withBackgroundColor(colorTheme.accentColor)
                        .build())
                .build().also {
                    componentStyleSet = it
                    render()
                }
    }

    override fun render() {
        renderingStrategy.render(this, graphics)
    }

    enum class CheckBoxState {
        CHECKING,
        CHECKED,
        UNCHECKING,
        UNCHECKED
    }
}
