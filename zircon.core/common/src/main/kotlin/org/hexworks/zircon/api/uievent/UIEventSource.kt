package org.hexworks.zircon.api.uievent

import org.hexworks.cobalt.events.api.Subscription

/**
 * An [UIEventSource] is an object which emits [UIEvent]s and can be used to listen to
 * those events.
 */
interface UIEventSource {

    /**
     * Adds a listener for [MouseEvent]s.
     */
    fun onMouseEvent(eventType: MouseEventType, fn: (MouseEvent, UIEventPhase) -> Boolean): Subscription

    /**
     * Adds a listener for [KeyboardEvent]s.
     */
    fun onKeyboardEvent(eventType: KeyboardEventType, fn: (KeyboardEvent, UIEventPhase) -> Boolean): Subscription
}
