package uz.gita.lesson20.controller

import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import uz.gita.lesson20.MainActivity
import uz.gita.lesson20.pref.Mypref
import java.lang.StringBuilder
import kotlin.random.Random

class AppController private constructor() {

    private val myPref = Mypref.getShared()
    var score = myPref?.getInt("score", 0)
    var record = myPref?.getInt("record", 0)

    companion object {
        private lateinit var instance: AppController

        fun getInstance(): AppController {
            if (!Companion::instance.isInitialized)
                instance = AppController()
            return instance
        }
    }

    private lateinit var errorListener: () -> Unit
    private val matrix = Array(4) { Array(4) { 0 } }  // 4x4 matrix

    init {
        addNewElement()
        addNewElement()
    }

    fun getMatrix(): Array<Array<Int>> = matrix

    private fun addNewElement() {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (matrix[i][j] == 0) {
                    emptyCells.add(i to j)
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val (x, y) = emptyCells[Random.nextInt(emptyCells.size)]
            matrix[x][y] = if (Random.nextDouble() < 0.9) 2 else 4
        } else {
            check()
        }
    }


    private fun compressRow(row: Array<Int>): List<Int> {
        val result = row.filter { it != 0 }.toMutableList()
        while (result.size < 4) {
            result.add(0)
        }
        return result
    }

    private fun mergeRow(row: List<Int>): List<Int> {
        val result = mutableListOf<Int>()
        var i = 0
        while (i < 4) {
            i += if (i < 3 && row[i] == row[i + 1]) {
                val newValue = row[i] * 2
                score = score?.plus(newValue)
                result.add(newValue)
                2
            } else {
                result.add(row[i])
                1
            }
        }
        while (result.size < 4) {
            result.add(0)
        }
        return result
    }

    private fun transposeMatrix() {
        for (i in 0 until 4) {
            for (j in i + 1 until 4) {
                val temp = matrix[i][j]
                matrix[i][j] = matrix[j][i]
                matrix[j][i] = temp
            }
        }
    }

    private fun invertMatrix() {
        for (i in 0 until 4) {
            for (j in 0 until 2) {
                val temp = matrix[i][j]
                matrix[i][j] = matrix[i][3 - j]
                matrix[i][3 - j] = temp
            }
        }
    }

    private fun moveLeft() {
        for (i in 0 until 4) {
            val compressedRow = compressRow(matrix[i])
            val mergedRow = mergeRow(compressedRow)
            for (j in 0 until 4) {
                matrix[i][j] = mergedRow[j]
            }
        }
    }

    private fun moveRight() {
        invertMatrix()
        moveLeft()
        invertMatrix()
    }

    private fun moveUp() {
        transposeMatrix()
        moveLeft()
        transposeMatrix()
    }

    private fun moveDown() {
        transposeMatrix()
        moveRight()
        transposeMatrix()
    }

    fun movementToLeft() {
        var b = false

        for (i in 0 until 4) {
            for (j in 3 downTo 1) {
                if ((matrix[i][j] == matrix[i][j - 1] && matrix[i][j] != 0) || (matrix[i][j] != 0 && matrix[i][j - 1] == 0)) b =
                    true
            }
        }

        check()

        if (b) {
            moveLeft()
            addNewElement()
        }
    }

    fun movementToRight() {
        var b = false

        for (i in 0 until 4) {
            for (j in 0 until 3) {
                if ((matrix[i][j] == matrix[i][j + 1] && matrix[i][j] != 0) || (matrix[i][j] != 0 && matrix[i][j + 1] == 0)) b =
                    true
            }
        }

        check()

        if (b) {
            moveRight()
            addNewElement()
        }
    }

    fun movementToUp() {
        var b = false

        for (i in 1 until 4) {
            for (j in 3 downTo 0) {
                if ((matrix[i][j] == matrix[i - 1][j] && matrix[i][j] != 0) || (matrix[i][j] != 0 && matrix[i - 1][j] == 0)) b =
                    true
            }
        }

        check()

        if (b) {
            moveUp()
            addNewElement()
        }
    }

    fun movementToDown() {
        var b = false

        for (i in 0 until 3) {
            for (j in 0 until 4) {
                if ((matrix[i][j] == matrix[i + 1][j] && matrix[i][j] != 0) || (matrix[i][j] != 0 && matrix[i + 1][j] == 0)) b =
                    true
            }
        }

        check()

        if (b) {
            moveDown()
            addNewElement()
        }
    }

    fun reverseAllItem() {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                matrix[i][j] = 0
            }
        }
        addNewElement()
        addNewElement()
    }

    private fun check() {
        if (isTrue()) {
            errorListener.invoke()
        }
    }

    private fun isTrue(): Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 3) {
                if (matrix[i][j] == matrix[i][j + 1] || matrix[i][j] == 0) return false
            }
        }

        for (i in 0 until 4) {
            for (j in 3 downTo 1) {
                if (matrix[i][j] == matrix[i][j - 1] || matrix[i][j] == 0) return false
            }
        }

        for (i in 0 until 3) {
            for (j in 0 until 4) {
                if (matrix[i][j] == matrix[i + 1][j] || matrix[i][j] == 0) return false
            }
        }

        for (i in 1 until 4) {
            for (j in 3 downTo 0) {
                if (matrix[i][j] == matrix[i - 1][j] || matrix[i][j] == 0) return false
            }
        }

        return true
    }

    fun save() {
        val sb = StringBuilder()

        for (i in 0 until 16) {
            sb.append(matrix[i / 4][i % 4].toString()).append("/")
        }

        myPref?.edit()?.putString("numbers", sb.toString())?.apply()
        myPref?.edit()?.putBoolean("check", true)?.apply()
        myPref?.edit()?.putInt("score", score!!)?.apply()
        myPref?.edit()?.putInt("record", record!!)?.apply()
    }

    fun clear() {
        score = 0
        myPref?.edit()?.putInt("score", 0)?.apply()
    }

    fun saveNewMatrix() {
        val s = myPref?.getString("numbers", "")?.split("/")
        for (i in 0 until 16) {
            matrix[i / 4][i % 4] = s!![i].toInt()
        }
    }

    fun setErrorListener(block: () -> Unit) {
        errorListener = block
    }
}
