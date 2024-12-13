package day11

import readLines
import kotlin.math.log10
import kotlin.math.pow

fun main(args: Array<String>) {
    val lines = readLines("./src/day11/input_smol.txt")
    println(processStones(lines[0].split(" ").map { it.toInt() }, 6))
}

fun processStone(stones: List<Int>, times: Int): Int {
    fun blink(stones: List<Int>): List<Int> {
        val blinked = mutableListOf<Int>()
        stones.forEach {
            val stoneSize = log10(it.toDouble()).toInt() + 1
            when {
                it == 0 -> blinked.add(1)
                stoneSize % 2 == 0 -> {
                    val splitPoint = 10.0.pow((stoneSize / 2).toDouble()).toInt()
                    blinked.add(it / splitPoint)
                    blinked.add(it % splitPoint)
                }
                else -> {
                    blinked.add(it * 2024)
                }
            }
        }
        println(blinked)
        return blinked
    }

    var processedStones = stones
    for (i in 1..times) {
        processedStones = blink(processedStones)
    }
    return processedStones.size
}

fun processStones(stones: List<Int>, times: Int): Long {
    var res = 0L

    stones.forEach { res += processStone(listOf(it), times) }

    return res
}