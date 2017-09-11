package core

import java.awt.Color
import java.awt.Frame
import java.awt.Graphics
import java.awt.Image
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.*

class Windows: Frame() {

    companion object {

        val start_x = 200//窗体属性
        val start_y = 200
        val width = 900
        val height = 600

        val DEATH = 2
        val ALIVE = 1

        private val differ = 70//初始化时每个格子出现生命的概率为20%

        private val r = Random()

        fun getRandom(): Int {
            return r.nextInt(height)
        }

        fun isAvailable(): Boolean {
            return r.nextInt(100) <= differ
        }
    }

    private var list = Array(160) { Array(120, { Cell() }) }//细胞池
    private var state_list = Array(160) { Array(120, { 0 }) }//状态池

    var timeout = 100L//默认刷新间隔

    init {
        setBounds(start_x, start_y, Windows.width, Windows.height)
        title = "LifeProgram"
        isVisible = true
        isResizable = false
        addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                System.exit(0)
            }
        })
        startRunning()
        timer().start()
    }

    override fun paint(g: Graphics) {
        for(a in list) {
            for(c in a) {
                c.paint(g)
            }
        }
    }

    private var offScreenImage: Image? = null

    override fun update(g: Graphics) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(Windows.width, Windows.height)
        }
        val ig = offScreenImage!!.graphics
        val c = ig.color
        ig.color = Color.WHITE
        ig.fillRect(0, 0, Windows.width, Windows.height)
        ig.color = c
        paint(ig)
        g.drawImage(offScreenImage, 0, 0, null)
    }

    private inner class timer: Thread() {
        override fun run() {
            while(true) {
                repaint()
                Thread.sleep(timeout)
                checkState()
            }
        }
    }

    fun flush() {
        for(i in 0 until 160) {
            for(j in 0 until 120) {
                list[i][j].state = when(state_list[i][j]) {
                    ALIVE -> STATE.ALIVE
                    DEATH -> STATE.DEATH
                    else -> STATE.BLOCK
                }
            }
        }
    }

    fun checkState() {
        for(i in 0 until 160) {
            for(j in 0 until 120) {
                checkSurround(i, j)
            }
        }
        flush()
    }

    private fun checkSurround(x: Int, y: Int) {
        var count = 0
        ((x - 1)..(x + 1))
                .filter { it in 0..159 }
                .forEach {
                    ((y - 1)..(y + 1))
                            .filter { j -> j in 0..119 && !(it == x && j == y) && list[it][j].isAlive() }
                            .forEach {
                                count++
                            }
                }
        if(count < 2 || count > 3) state_list[x][y] = DEATH
        if(count == 3 && (list[x][y].isDeath() || list[x][y].state == STATE.BLOCK)) state_list[x][y] = ALIVE
        count = 0
    }

    private fun startRunning() {
        for(i in 0 until 160) {
            for(j in 0 until 120) {
                list[i][j] = Cell()
                        .setState(if(isAvailable()) STATE.ALIVE else STATE.BLOCK)
                        .setLocation(intArrayOf(5 * i, 5 * j))
            }
        }
    }

}
