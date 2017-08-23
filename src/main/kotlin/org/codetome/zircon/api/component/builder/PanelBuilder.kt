package org.codetome.zircon.api.component.builder

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.Symbols
import org.codetome.zircon.api.builder.Builder
import org.codetome.zircon.api.builder.ComponentStylesBuilder
import org.codetome.zircon.api.component.ComponentStyles
import org.codetome.zircon.api.component.Panel
import org.codetome.zircon.internal.component.WrappingStrategy
import org.codetome.zircon.internal.component.impl.BoxWrappingStrategy
import org.codetome.zircon.internal.component.impl.DefaultPanel
import org.codetome.zircon.internal.component.impl.ShadowWrappingStrategy
import org.codetome.zircon.internal.graphics.BoxType

class PanelBuilder : Builder<Panel> {

    private var boxType = BoxType.SINGLE
    private var title = ""
    private var position = Position.DEFAULT_POSITION
    private var componentStyles: ComponentStyles = ComponentStylesBuilder.DEFAULT
    private var size = Size.UNKNOWN
    private var drawBox = false
    private var drawShadow = false

    fun wrapInBox() = also {
        drawBox = true
    }

    fun addShadow() = also {
        drawShadow = true
    }

    fun boxType(boxType: BoxType) = also {
        this.boxType = boxType
    }

    fun size(size: Size) = also {
        this.size = size
    }

    fun title(title: String) = also {
        this.title = title
    }

    fun position(position: Position) = also {
        this.position = position
    }

    fun componentStyles(componentStyles: ComponentStyles) = also {
        this.componentStyles = componentStyles
    }

    override fun build(): Panel {
        require(size != Size.UNKNOWN) {
            "You must set a size for a Panel!"
        }
        val wrappers = mutableListOf<WrappingStrategy>()
        if(drawBox) {
            wrappers.add(BoxWrappingStrategy(boxType))
        }
        if(drawShadow) {
            wrappers.add(ShadowWrappingStrategy())
        }
        val panel = DefaultPanel(
                title = title,
                initialSize = size,
                position = position,
                componentStyles = componentStyles,
                wrappers = wrappers)

        return panel
    }

    companion object {

        @JvmStatic
        fun newBuilder() = PanelBuilder()
    }
}