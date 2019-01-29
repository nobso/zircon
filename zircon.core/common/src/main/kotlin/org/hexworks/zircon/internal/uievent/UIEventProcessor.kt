package org.hexworks.zircon.internal.uievent

import org.hexworks.zircon.api.uievent.UIEvent
import org.hexworks.zircon.api.uievent.UIEventPhase
import org.hexworks.zircon.api.uievent.UIEventSource
import org.hexworks.zircon.internal.uievent.impl.DefaultUIEventProcessor

/**
 * An [UIEventProcessor] is responsible for processing [UIEvent]s
 * by dispatching them to its subscribers.
 * See [UIEventSource].
 */
interface UIEventProcessor : UIEventSource {

    /**
     * Processes the given [UIEvent] in the given [phase].
     * @return `true` if the event was consumed and event
     * propagation should stop, `false` if not.
     */
    fun process(event: UIEvent, phase: UIEventPhase): Boolean

    companion object {

        fun create(): UIEventProcessor = DefaultUIEventProcessor()
    }
}
