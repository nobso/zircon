package org.hexworks.zircon.api.uievent

/**
 * Callback interface for [KeyboardEvent]s which can typically be implemented
 * by long-lived objects. See [KeyboardEventType].
 */
interface KeyboardEventListener {

    fun keyPressed(event: KeyboardEvent, phase: UIEventPhase): Boolean = false

    fun keyTyped(event: KeyboardEvent, phase: UIEventPhase): Boolean = false

    fun keyReleased(event: KeyboardEvent, phase: UIEventPhase): Boolean = false
}
