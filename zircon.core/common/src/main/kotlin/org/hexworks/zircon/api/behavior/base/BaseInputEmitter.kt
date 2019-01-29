package org.hexworks.zircon.api.behavior.base

import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.sam.Consumer
import org.hexworks.cobalt.events.api.Subscription
import org.hexworks.zircon.api.behavior.InputEmitter
import org.hexworks.zircon.api.behavior.input.KeyCombination
import org.hexworks.zircon.api.uievent.*
import org.hexworks.zircon.api.listener.InputListener
import org.hexworks.zircon.api.listener.KeyStrokeListener
import org.hexworks.zircon.api.listener.MouseListener

abstract class BaseInputEmitter : InputEmitter {

    override fun onKeyStroke(listener: KeyStrokeListener): Subscription {
        return onInput(object : InputListener {
            override fun inputEmitted(input: Input) {
                input.asKeyStroke().map {
                    listener.keyStroked(it)
                }
            }
        })
    }

    override fun onKeyCombination(keyCombination: KeyCombination,
                         listener: Consumer<KeyStroke>): Subscription {
        val (char, inputType, shiftState, ctrlState, altState) = keyCombination
        return onInput(object : InputListener {
            override fun inputEmitted(input: Input) {
                input.asKeyStroke().map { ks ->
                    if (shiftState.matches(ks)
                            && ctrlState.matches(ks)
                            && altState.matches(ks)
                            && char == ks.getCharacter()
                            && inputType == ks.inputType()) {
                        listener.accept(ks)
                    }
                }
            }
        })
    }

    override fun onKeyPressed(key: Char,
                              listener: Consumer<KeyStroke>): Subscription {
        return onInput(object : InputListener {
            override fun inputEmitted(input: Input) {
                input.asKeyStroke().map { ks ->
                    if (ks.getCharacter() == key) {
                        listener.accept(ks)
                    }
                }
            }
        })
    }

    override fun onInputOfType(inputType: InputType,
                               listener: Consumer<Input>): Subscription {
        return onInput(object : InputListener {
            override fun inputEmitted(input: Input) {
                if (input.inputType() == inputType) {
                    listener.accept(input)
                }
            }
        })
    }

    override fun onMouseAction(listener: MouseListener): Subscription {
        return onInput(object : InputListener {
            override fun inputEmitted(input: Input) {
                input.asMouseAction().map {
                    when (it.eventType) {
                        MouseEventType.MOUSE_CLICKED -> listener.mouseClicked(it)
                        MouseEventType.MOUSE_PRESSED -> listener.mousePressed(it)
                        MouseEventType.MOUSE_RELEASED -> listener.mouseReleased(it)
                        MouseEventType.MOUSE_ENTERED -> listener.mouseEntered(it)
                        MouseEventType.MOUSE_EXITED -> listener.mouseExited(it)
                        MouseEventType.MOUSE_WHEEL_ROTATED_UP -> listener.mouseWheelRotatedUp(it)
                        MouseEventType.MOUSE_WHEEL_ROTATED_DOWN -> listener.mouseWheelRotatedDown(it)
                        MouseEventType.MOUSE_DRAGGED -> listener.mouseDragged(it)
                        MouseEventType.MOUSE_MOVED -> listener.mouseMoved(it)
                    }
                }
            }
        })
    }

    override fun onMouseClicked(consumer: Consumer<MouseAction>): Subscription {
        return onMouseAction(object : MouseListener {
            override fun mouseClicked(action: MouseAction) {
                consumer.accept(action)
            }
        })
    }

    override fun onMousePressed(consumer: Consumer<MouseAction>): Subscription {
        return onMouseAction(object : MouseListener {
            override fun mousePressed(action: MouseAction) {
                consumer.accept(action)
            }
        })
    }

    override fun onMouseReleased(consumer: Consumer<MouseAction>): Subscription {
        return onMouseAction(object : MouseListener {
            override fun mouseReleased(action: MouseAction) {
                consumer.accept(action)
            }
        })
    }

    override fun onMouseEntered(consumer: Consumer<MouseAction>): Subscription {
        return onMouseAction(object : MouseListener {
            override fun mouseEntered(action: MouseAction) {
                consumer.accept(action)
            }
        })
    }

    override fun onMouseExited(consumer: Consumer<MouseAction>): Subscription {
        return onMouseAction(object : MouseListener {
            override fun mouseExited(action: MouseAction) {
                consumer.accept(action)
            }
        })
    }

    override fun onMouseWheelRotatedUp(consumer: Consumer<MouseAction>): Subscription {
        return onMouseAction(object : MouseListener {
            override fun mouseWheelRotatedUp(action: MouseAction) {
                consumer.accept(action)
            }
        })
    }

    override fun onMouseWheelRotatedDown(consumer: Consumer<MouseAction>): Subscription {
        return onMouseAction(object : MouseListener {
            override fun mouseWheelRotatedDown(action: MouseAction) {
                consumer.accept(action)
            }
        })
    }

    override fun onMouseDragged(consumer: Consumer<MouseAction>): Subscription {
        return onMouseAction(object : MouseListener {
            override fun mouseDragged(action: MouseAction) {
                consumer.accept(action)
            }
        })
    }

    override fun onMouseMoved(consumer: Consumer<MouseAction>): Subscription {
        return onMouseAction(object : MouseListener {
            override fun mouseMoved(action: MouseAction) {
                consumer.accept(action)
            }
        })
    }
}
