@file:Suppress("UNUSED_VARIABLE")

package org.hexworks.zircon.examples.playground

import java.awt.Canvas
import java.awt.Dimension
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JFrame

object KotlinPlayground {

    @JvmStatic
    fun main(args: Array<String>) {

        val frame = JFrame().apply {
            setSize(800, 600)
            val canvas = Canvas()
            add(canvas)
            canvas.preferredSize = Dimension(
                    800,
                    600)
            canvas.isFocusable = true
            canvas.requestFocusInWindow()
            canvas.addKeyListener(object : KeyAdapter() {

                override fun keyPressed(e: KeyEvent) {
                    println("press: key code: '${e.keyCode}', key char: '${e.keyChar}'")
                }

                override fun keyReleased(e: KeyEvent) {
                    println("release: key code: '${e.keyCode}', key char: '${e.keyChar}'")
                }

                override fun keyTyped(e: KeyEvent) {
                    println("type: key code: '${e.keyCode}', key char: '${e.keyChar}'")
                }
            })

            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            pack()
            setLocationRelativeTo(null)
            isVisible = true
        }
    }

}
