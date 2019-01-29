package org.hexworks.zircon.internal.component.impl

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.zircon.api.builder.component.ButtonBuilder
import org.hexworks.zircon.api.builder.component.ComponentStyleSetBuilder
import org.hexworks.zircon.api.builder.component.PanelBuilder
import org.hexworks.zircon.api.builder.graphics.StyleSetBuilder
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.component.data.ComponentMetadata
import org.hexworks.zircon.api.component.renderer.impl.DefaultComponentRenderingStrategy
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.uievent.InputType
import org.hexworks.zircon.api.uievent.KeyStroke
import org.hexworks.zircon.api.uievent.MouseAction
import org.hexworks.zircon.api.uievent.MouseEventType.*
import org.hexworks.zircon.api.kotlin.onMouseEntered
import org.hexworks.zircon.api.kotlin.onMousePressed
import org.hexworks.zircon.api.kotlin.onMouseReleased
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.component.renderer.RootContainerRenderer
import org.hexworks.zircon.internal.event.ZirconEvent
import org.hexworks.zircon.internal.event.ZirconScope
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class DefaultComponentContainerTest {

    lateinit var target: DefaultComponentContainer

    @Before
    fun setUp() {
        target = DefaultComponentContainer(RootContainer(
                componentMetadata = ComponentMetadata(
                        position = Position.defaultPosition(),
                        size = SIZE,
                        tileset = TILESET,
                        componentStyleSet = STYLES),
                renderingStrategy = DefaultComponentRenderingStrategy(
                        decorationRenderers = listOf(),
                        componentRenderer = RootContainerRenderer())))
    }

    @Test
    fun shouldProperlyRemoveComponent() {
        val button = createButton()
        target.addComponent(button)
        assertThat(target.toFlattenedLayers()).hasSize(2)

        val result = target.removeComponent(button)

        assertThat(result).isTrue()
        assertThat(target.toFlattenedLayers()).hasSize(1) // default container
    }

    // TODO: why tf this is working in idea and not in gradle?
    @Ignore
    @Test(expected = IllegalArgumentException::class)
    fun shouldNotLetToAddAComponentWhichIsBiggerThanTheContainer() {
        target.addComponent(PanelBuilder.newBuilder()
                .withSize(Size.create(999, 999))
                .build())
    }

    @Test
    fun shouldProperlyHandleMouseOverWhenActive() {
        target.activate()

        val button = createButton()
        target.addComponent(button)

        val componentHovered = AtomicBoolean(false)
        button.onMouseEntered {
            componentHovered.set(true)
        }

        Zircon.eventBus.publish(
                event = ZirconEvent.Input(MouseAction(MOUSE_MOVED, 1, BUTTON_POSITION)),
                eventScope = ZirconScope)

        assertThat(componentHovered.get()).isTrue()
    }

    @Test
    fun shouldProperlyHandleMouseOverWhenHoveredTwice() {
        target.activate()

        val button = createButton()
        target.addComponent(button)

        Zircon.eventBus.publish(
                event = ZirconEvent.Input(MouseAction(MOUSE_MOVED, 1, BUTTON_POSITION)),
                eventScope = ZirconScope)

        val componentHovered = AtomicBoolean(false)
        button.onMouseEntered {
            componentHovered.set(true)
        }

        Zircon.eventBus.publish(
                event = ZirconEvent.Input(MouseAction(MOUSE_MOVED, 1, BUTTON_POSITION.withRelativeX(1))),
                eventScope = ZirconScope)

        assertThat(componentHovered.get()).isFalse()
    }

    @Test
    fun shouldProperlyHandleMousePressedWhenActive() {
        target.activate()

        val button = createButton()
        target.addComponent(button)

        val pressed = AtomicBoolean(false)
        button.onMousePressed {
            pressed.set(true)
        }
        Zircon.eventBus.publish(
                event = ZirconEvent.Input(MouseAction(MOUSE_PRESSED, 1, BUTTON_POSITION)),
                eventScope = ZirconScope)

        assertThat(pressed.get()).isTrue()
    }

    @Test
    fun shouldProperlyHandleMouseReleasedWhenActive() {
        target.activate()

        val button = createButton()
        target.addComponent(button)

        val released = AtomicBoolean(false)
        button.onMouseReleased {
            released.set(true)
        }

        Zircon.eventBus.publish(
                event = ZirconEvent.Input(MouseAction(MOUSE_RELEASED, 1, BUTTON_POSITION)),
                eventScope = ZirconScope)

        assertThat(released.get()).isTrue()
    }

    @Test
    fun shouldNotHandleEventsWhenInactive() {
        target.deactivate()

        val button = createButton()
        target.addComponent(button)

        val events = mutableListOf<Boolean>()
        button.onMouseEntered {
            events.add(true)
        }
        button.onMousePressed {
            events.add(true)
        }
        button.onMouseReleased {
            events.add(true)
        }

        Zircon.eventBus.publish(
                event = ZirconEvent.Input(MouseAction(MOUSE_MOVED, 1, BUTTON_POSITION)),
                eventScope = ZirconScope)
        Zircon.eventBus.publish(
                event = ZirconEvent.Input(MouseAction(MOUSE_PRESSED, 1, BUTTON_POSITION)),
                eventScope = ZirconScope)
        Zircon.eventBus.publish(
                event = ZirconEvent.Input(MouseAction(MOUSE_RELEASED, 1, BUTTON_POSITION)),
                eventScope = ZirconScope)


        assertThat(events).isEmpty()
    }

    @Test
    fun shouldProperlyFocusNextWhenTabPressed() {
        target.activate()

        val button = createButton()
        target.addComponent(button)

        assertThat(button.componentStyleSet.currentStyle()).isNotEqualTo(FOCUSED_STYLE)

        Zircon.eventBus.publish(
                event = ZirconEvent.Input(KeyStroke(type = InputType.Tab)),
                eventScope = ZirconScope)

        assertThat(button.componentStyleSet.currentStyle()).isEqualTo(FOCUSED_STYLE)
    }

    @Test
    fun shouldProperlyFocusPrevWhenShiftTabPressed() {
        target.activate()

        val button = createButton()
        target.addComponent(button)
        val other = ButtonBuilder.newBuilder()
                .withText(BUTTON_TEXT)
                .withPosition(Position.create(0, 1)
                        .relativeToBottomOf(button))
                .build()
        target.addComponent(other)

        Zircon.eventBus.publish(
                event = ZirconEvent.Input(KeyStroke(type = InputType.Tab)),
                eventScope = ZirconScope)
        Zircon.eventBus.publish(
                event = ZirconEvent.Input(KeyStroke(type = InputType.Tab)),
                eventScope = ZirconScope)

        assertThat(button.componentStyleSet.currentStyle()).isEqualTo(DEFAULT_STYLE)

        Zircon.eventBus.publish(
                event = ZirconEvent.Input(KeyStroke(shiftDown = true, type = InputType.ReverseTab)),
                eventScope = ZirconScope)


        assertThat(button.componentStyleSet.currentStyle()).isEqualTo(FOCUSED_STYLE)
    }

    @Test
    fun shouldProperlyHandleSpacePressedOnFocusedWhenActive() {
        target.activate()

        val button = createButton()
        target.addComponent(button)

        val released = AtomicBoolean(false)
        button.onMouseReleased {
            released.set(true)
        }

        Zircon.eventBus.publish(
                event = ZirconEvent.Input(KeyStroke(type = InputType.Tab)),
                eventScope = ZirconScope)
        Zircon.eventBus.publish(
                event = ZirconEvent.Input(KeyStroke(type = InputType.Character, character = ' ')),
                eventScope = ZirconScope)

        assertThat(released.get()).isTrue()
    }

    private fun createButton() = ButtonBuilder.newBuilder()
            .withPosition(BUTTON_POSITION)
            .withText(BUTTON_TEXT)
            .withComponentStyleSet(STYLES)
            .build()

    companion object {
        val TILESET = BuiltInCP437TilesetResource.REX_PAINT_16X16
        val SIZE = Size.create(30, 20)
        val BUTTON_TEXT = "TEXT"
        val BUTTON_POSITION = Position.create(6, 7)
        val DEFAULT_STYLE = StyleSetBuilder.newBuilder()
                .withBackgroundColor(ANSITileColor.BLUE)
                .withForegroundColor(ANSITileColor.RED)
                .build()
        val ACTIVE_STYLE = StyleSetBuilder.newBuilder()
                .withBackgroundColor(ANSITileColor.GREEN)
                .withForegroundColor(ANSITileColor.YELLOW)
                .build()
        val DISABLED_STYLE = StyleSetBuilder.newBuilder()
                .withBackgroundColor(ANSITileColor.MAGENTA)
                .withForegroundColor(ANSITileColor.BLUE)
                .build()
        val FOCUSED_STYLE = StyleSetBuilder.newBuilder()
                .withBackgroundColor(ANSITileColor.YELLOW)
                .withForegroundColor(ANSITileColor.CYAN)
                .build()
        val MOUSE_OVER_STYLE = StyleSetBuilder.newBuilder()
                .withBackgroundColor(ANSITileColor.RED)
                .withForegroundColor(ANSITileColor.CYAN)
                .build()
        val STYLES = ComponentStyleSetBuilder.newBuilder()
                .withDefaultStyle(DEFAULT_STYLE)
                .withActiveStyle(ACTIVE_STYLE)
                .withDisabledStyle(DISABLED_STYLE)
                .withFocusedStyle(FOCUSED_STYLE)
                .withMouseOverStyle(MOUSE_OVER_STYLE)
                .build()
    }
}
