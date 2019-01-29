package org.hexworks.zircon.api.uievent

/**
 * Callback interface for [MouseEvent]s which can typically be implemented
 * by long-lived objects. See [MouseEventType].
 */
interface MouseEventListener {

    fun mouseClicked(event: MouseEvent, phase: UIEventPhase): Boolean = false

    fun mousePressed(event: MouseEvent, phase: UIEventPhase): Boolean = false

    fun mouseReleased(event: MouseEvent, phase: UIEventPhase): Boolean = false

    fun mouseEntered(event: MouseEvent, phase: UIEventPhase): Boolean = false

    fun mouseExited(event: MouseEvent, phase: UIEventPhase): Boolean = false

    fun mouseWheelRotatedUp(event: MouseEvent, phase: UIEventPhase): Boolean = false

    fun mouseWheelRotatedDown(event: MouseEvent, phase: UIEventPhase): Boolean = false

    fun mouseDragged(event: MouseEvent, phase: UIEventPhase): Boolean = false

    fun mouseMoved(event: MouseEvent, phase: UIEventPhase): Boolean = false
}
