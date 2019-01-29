package org.hexworks.zircon.internal.uievent.impl

import org.hexworks.cobalt.events.api.CancelState
import org.hexworks.cobalt.events.api.NotCancelled
import org.hexworks.cobalt.events.api.Subscription
import org.hexworks.zircon.api.uievent.*
import org.hexworks.zircon.internal.uievent.UIEventProcessor
import org.hexworks.zircon.internal.util.ThreadSafeQueue
import org.hexworks.zircon.platform.factory.ThreadSafeMapFactory
import org.hexworks.zircon.platform.factory.ThreadSafeQueueFactory

class DefaultUIEventProcessor : UIEventProcessor, UIEventSource {

    private val listeners = ThreadSafeMapFactory.create<UIEventType, ThreadSafeQueue<InputEventSubscription>>()

    /**
     * Internal API. This will return `false` if there are no listeners
     * for [Event.eventType] or if invoking all listeners returned `false`.
     * Note that this will short-circuit when any of the listeners returns `true`.
     */
    override fun process(event: UIEvent, phase: UIEventPhase): Boolean {
        return listeners[event.type]?.let { list ->
            list.any { it.listener.invoke(event, phase) }
        } ?: false
    }

    // TODO: we can add an `onEvent` later?

    // By using this setup we don't have a proliferation of `on*` functions but we still
    // retain all functionality.
    /**
     * External API.
     */
    @Suppress("UNCHECKED_CAST")
    override fun onMouseEvent(eventType: MouseEventType, fn: (MouseEvent, UIEventPhase) -> Boolean): Subscription {
        return buildSubscription(eventType, fn as (UIEvent, UIEventPhase) -> Boolean)
    }

    /**
     * External API.
     */
    @Suppress("UNCHECKED_CAST")
    override fun onKeyboardEvent(eventType: KeyboardEventType, fn: (KeyboardEvent, UIEventPhase) -> Boolean): Subscription {
        return buildSubscription(eventType, fn as (UIEvent, UIEventPhase) -> Boolean)
    }

    private fun buildSubscription(eventType: UIEventType, listener: (UIEvent, UIEventPhase) -> Boolean): Subscription {
        return listeners.getOrPut(eventType) { ThreadSafeQueueFactory.create() }.let {
            val subscription = InputEventSubscription(
                    listener = listener,
                    subscriptions = it)
            it.add(subscription)
            subscription
        }
    }

    class InputEventSubscription(
            val listener: (UIEvent, UIEventPhase) -> Boolean,
            private val subscriptions: ThreadSafeQueue<InputEventSubscription>) : Subscription {

        override var cancelState: CancelState = NotCancelled

        override fun cancel(cancelState: CancelState) {
            subscriptions.remove(this)
            this.cancelState = cancelState
        }
    }
}
