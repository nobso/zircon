package org.hexworks.zircon.internal.uievent

import org.hexworks.zircon.api.uievent.UIEvent
import org.hexworks.zircon.api.uievent.UIEventType
import org.hexworks.zircon.api.uievent.UIEventPhase

/**
 * An [UIEventDispatcher] implements event propagation.
 * See [UIEventPhase] for more info
 * about event propagation.
 */
interface UIEventDispatcher {


    /**
     * Dispatches the given [UIEvent] and propagates it throughout the UI controls
     * this [UIEventDispatcher] has. This will return `false` if there are no listeners
     * for the given [event]'s [UIEventType] or if invoking all listeners returned `false`.
     * Note that this will short-circuit when any of the listeners returns `true`.
     */
    fun dispatch(event: UIEvent): Boolean
}
