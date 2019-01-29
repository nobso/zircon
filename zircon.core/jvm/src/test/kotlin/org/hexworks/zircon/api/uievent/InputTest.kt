package org.hexworks.zircon.api.uievent

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.zircon.api.data.Position
import org.junit.Test

class InputTest {

    @Test
    fun keyStrokeShouldBeKeyStroke() {
        assertThat(KeyStroke.EOF_STROKE.isKeyStroke()).isTrue()
    }

    @Test
    fun mouseActionShouldBeMouseAction() {
        assertThat(fetchMouseAction().isMouseAction()).isTrue()
    }

    @Test
    fun aMouseActionShouldHaveMouseActionInputType() {
        assertThat(fetchMouseAction().inputType())
                .isEqualTo(InputType.MouseEvent)
    }

    @Test
    fun shouldProperlyReportInputType() {
        assertThat(KeyStroke.EOF_STROKE.inputTypeIs(InputType.EOF)).isTrue()
    }

    @Test
    fun shouldProperlyReportCtrlDown() {
        assertThat(KeyStroke(ctrlDown = true).isCtrlDown()).isTrue()
    }

    @Test
    fun shouldProperlyReportAltDown() {
        assertThat(KeyStroke(altDown = true).isAltDown()).isTrue()
    }

    @Test
    fun shouldProperlyReportShiftDown() {
        assertThat(KeyStroke(shiftDown = true).isShiftDown()).isTrue()
    }

    @Test
    fun shouldBeAbleToGetKeyStrokeAsKeyStroke() {
        val input: Input = KeyStroke.EOF_STROKE
        input.asKeyStroke()
    }

    @Test
    fun shouldBeAbleToGetMouseActionAsMouseAction() {
        val input: Input = MouseAction(MouseEventType.MOUSE_CLICKED, 1, Position.defaultPosition())
        input.asMouseAction()
    }

    private fun fetchMouseAction(): MouseAction {
        return MouseAction(
                eventType = MouseEventType.MOUSE_WHEEL_ROTATED_UP,
                button = 1,
                position = Position.defaultPosition()
        )
    }

}
