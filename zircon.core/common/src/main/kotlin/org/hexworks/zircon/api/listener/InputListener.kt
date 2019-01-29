package org.hexworks.zircon.api.listener

import org.hexworks.zircon.api.uievent.Input

/**
 * Listener interface for [Input]s.
 */
interface InputListener {

    fun inputEmitted(input: Input) {}
}
