package org.hexworks.zircon.internal.grid

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.uievent.Input
import org.hexworks.zircon.api.uievent.MouseAction
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.MouseEventType.*
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.event.ZirconEvent
import org.hexworks.zircon.internal.event.ZirconScope
import org.junit.Before
import org.junit.Test
import java.awt.Component
import java.awt.event.MouseEvent
import java.util.*

class TileGridMouseListenerTest {

    lateinit var target: TileGridMouseListener
    lateinit var operations: Map<(MouseEvent) -> Unit, MouseEventType>

    val inputs = LinkedList<Input>()

    @Before
    fun setUp() {
        target = TileGridMouseListener(FONT_SIZE, FONT_SIZE)
        operations = mapOf(
                Pair(target::mouseClicked, MOUSE_CLICKED),
                Pair(target::mouseDragged, MOUSE_DRAGGED),
                Pair(target::mouseEntered, MOUSE_ENTERED),
                Pair(target::mouseExited, MOUSE_EXITED),
                Pair(target::mousePressed, MOUSE_PRESSED),
                Pair(target::mouseReleased, MOUSE_RELEASED))
        Zircon.eventBus.subscribe<ZirconEvent.Input>(ZirconScope) {
            inputs.add(it.input)
        }
    }

    @Test
    fun shouldProperlyHandleMouseEvents() {
        operations.forEach { (op, event) ->
            op.invoke(MOUSE_EVENT)
            assertThat(inputs.poll()).isEqualTo(MouseAction(
                    eventType = event,
                    button = BUTTON,
                    position = POSITION))

        }

    }

    companion object {
        val FONT_SIZE = 16
        val POSITION = Position.create(2, 3)
        val X = POSITION.x * FONT_SIZE
        val Y = POSITION.y * FONT_SIZE
        val BUTTON = 2
        val DUMMY_COMPONENT = object : Component() {}
        val MOUSE_EVENT = MouseEvent(DUMMY_COMPONENT, 1, 1, 1, X, Y, 1, true, BUTTON)
    }
}
