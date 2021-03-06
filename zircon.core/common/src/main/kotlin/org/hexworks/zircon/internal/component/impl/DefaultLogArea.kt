package org.hexworks.zircon.internal.component.impl

import org.hexworks.zircon.api.builder.component.ColorThemeBuilder
import org.hexworks.zircon.api.builder.component.ComponentStyleSetBuilder
import org.hexworks.zircon.api.builder.component.ParagraphBuilder
import org.hexworks.zircon.api.builder.component.TextBoxBuilder
import org.hexworks.zircon.api.builder.graphics.StyleSetBuilder
import org.hexworks.zircon.api.component.*
import org.hexworks.zircon.api.component.data.ComponentMetadata
import org.hexworks.zircon.api.component.renderer.ComponentRenderingStrategy

class DefaultLogArea constructor(componentMetadata: ComponentMetadata,
                                 private val renderingStrategy: ComponentRenderingStrategy<LogArea>)
    : LogArea, DefaultContainer(
        componentMetadata = componentMetadata,
        renderer = renderingStrategy) {

    private var currentInlineBuilder = createTextBoxBuilder()
    private var currentTheme: ColorTheme = ColorThemeBuilder.newBuilder().build()

    // TODO: fix decoration problem
    init {
        render()
    }

    override fun addHeader(text: String, withNewLine: Boolean) {
        addLogElement(createTextBoxBuilder()
                .addHeader(text, withNewLine)
                .build())
    }

    override fun addParagraph(paragraph: String, withNewLine: Boolean, withTypingEffectSpeedInMs: Long) {
        addLogElement(createTextBoxBuilder()
                .addParagraph(paragraph, withNewLine, withTypingEffectSpeedInMs)
                .build())
    }

    override fun addParagraph(paragraphBuilder: ParagraphBuilder, withNewLine: Boolean) {
        addLogElement(createTextBoxBuilder()
                .addParagraph(paragraphBuilder, withNewLine)
                .build(), false)
    }


    override fun addListItem(item: String) {
        addLogElement(createTextBoxBuilder()
                .addListItem(item)
                .build())
    }

    override fun addInlineText(text: String) {
        currentInlineBuilder.addInlineText(text)
    }

    override fun addInlineComponent(component: Component) {
        currentInlineBuilder.addInlineComponent(component)
    }

    override fun commitInlineElements() {
        val builder = currentInlineBuilder
        currentInlineBuilder = createTextBoxBuilder()
        addLogElement(builder.commitInlineElements()
                .build())
    }

    override fun addNewRows(numberOfRows: Int) {
        (0 until numberOfRows).forEach { _ ->
            addLogElement(createTextBoxBuilder()
                    .addNewLine()
                    .build())
        }
    }

    override fun clear() {
        children.forEach { removeComponent(it) }
    }

    override fun applyColorTheme(colorTheme: ColorTheme): ComponentStyleSet {
        currentTheme = colorTheme
        return ComponentStyleSetBuilder.newBuilder()
                .withDefaultStyle(StyleSetBuilder.newBuilder()
                        .withForegroundColor(colorTheme.secondaryForegroundColor)
                        .withBackgroundColor(colorTheme.primaryBackgroundColor)
                        .build())
                .withDisabledStyle(StyleSetBuilder.newBuilder()
                        .withForegroundColor(colorTheme.secondaryForegroundColor)
                        .withBackgroundColor(colorTheme.secondaryBackgroundColor)
                        .build())
                .withFocusedStyle(StyleSetBuilder.newBuilder()
                        .withForegroundColor(colorTheme.primaryBackgroundColor)
                        .withBackgroundColor(colorTheme.primaryForegroundColor)
                        .build())
                .build().also { css ->
                    componentStyleSet = css
                    render()
                    children.forEach {
                        it.applyColorTheme(colorTheme)
                    }
                }
    }

    private fun addLogElement(element: TextBox, applyTheme: Boolean = true) {
        val currentHeight = children.asSequence().map { it.height }.fold(0, Int::plus)
        val maxHeight = height
        val elementHeight = element.height
        val remainingHeight = maxHeight - currentHeight
        require(elementHeight <= height) {
            "Can't add an element which has a bigger height than this LogArea."
        }
        if (currentHeight + elementHeight > maxHeight) {
            val linesToFree = elementHeight - remainingHeight
            var currentFreedSpace = 0
            while (currentFreedSpace < linesToFree) {
                children.firstOrNull()?.let { topChild ->
                    topChild.detach()
                    currentFreedSpace += topChild.height
                }
            }
            children.forEach { child ->
                child.moveUpBy(currentFreedSpace)
            }
        }
        children.lastOrNull()?.let { lastChild ->
            element.moveTo(lastChild.position.minus(contentPosition).withRelativeY(lastChild.height))
        }
        addComponent(element)
        if (applyTheme) {
            element.applyColorTheme(currentTheme)
        }
        render()
    }

    private fun createTextBoxBuilder(): TextBoxBuilder {
        return TextBoxBuilder
                .newBuilder()
                .withTileset(currentTileset())
                .withContentWidth(width)
    }

    override fun render() {
        renderingStrategy.render(this, graphics)
    }

}
