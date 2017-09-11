package core

import java.awt.Color
import java.awt.Graphics
import java.lang.reflect.Field
import java.util.*

class Cell {

    var state = STATE.BLOCK
    private var location_x = 0
    private var location_y = 0
    private val default_size = 4

    constructor() {
        setLocation(intArrayOf(Windows.getRandom(), Windows.getRandom()))
    }

    constructor(x: Int, y: Int): this() {
        setLocation(intArrayOf(x, y))
    }

    fun getLocation(): IntArray {
        return intArrayOf(location_x, location_y)
    }

    fun setState(state: STATE): Cell {
        this.state = state
        return this
    }

    fun setLocation(array: IntArray): Cell {
        location_x = array[0]
        location_y = array[1]
        return this
    }

    fun isAlive(): Boolean {
        return state == STATE.ALIVE
    }

    fun isDeath(): Boolean {
        return state == STATE.DEATH
    }

    fun paint(g: Graphics) {
        g.color = getColor(state)
        g.fillRect(location_x, location_y, default_size, default_size)
    }

    private fun getColor(state: STATE): Color {
        return when(state) {
            STATE.ALIVE -> Color.RED
            STATE.DEATH -> Color.LIGHT_GRAY
            STATE.BLOCK -> Color.WHITE
        }
    }

}
