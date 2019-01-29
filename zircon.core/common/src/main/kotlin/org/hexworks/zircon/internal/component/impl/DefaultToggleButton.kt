package org.hexworks.zircon.internal.component.impl

import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.behavior.Selectable
import org.hexworks.zircon.api.behavior.TextHolder
import org.hexworks.zircon.api.builder.component.ComponentStyleSetBuilder
import org.hexworks.zircon.api.builder.graphics.StyleSetBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.ColorTheme
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.component.ToggleButton
import org.hexworks.zircon.api.component.data.ComponentMetadata
import org.hexworks.zircon.api.component.renderer.ComponentRenderingStrategy
import org.hexworks.zircon.api.uievent.Input
import org.hexworks.zircon.api.uievent.MouseAction

class DefaultToggleButton(componentMetadata: ComponentMetadata,
                          initialText: String,
                          private val renderingStrategy: ComponentRenderingStrategy<ToggleButton>)
    : ToggleButton, DefaultComponent(
        componentMetadata = componentMetadata,
        renderer = renderingStrategy),
        TextHolder by TextHolder.create(initialText),
        Selectable by Selectable.create() {

    init {
        render()
        textProperty.onChange {
            render()
        }
        selectedProperty.onChange {
            if (it.newValue) {
                applyIsSelectedStyle()
            } else {
                componentStyleSet.reset()
            }
            render()
        }
    }

    override fun mousePressed(action: MouseAction) {
        selectedProperty.value = !isSelected
    }

    override fun mouseExited(action: MouseAction) {
        if (isSelected.not()) {
            super.mouseExited(action)
        }
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
        if (isSelected) {
            applyIsSelectedStyle()
        } else {
            componentStyleSet.reset()
        }
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

    private fun applyIsSelectedStyle() {
        componentStyleSet.applyMouseOverStyle()
    }
}
