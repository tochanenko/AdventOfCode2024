package day11

import readLines
import kotlin.math.log10
import kotlin.math.pow

fun main(args: Array<String>) {
    val lines = readLines("./src/day11/input.txt")

    val startTime = System.currentTimeMillis()
    println(processStones(lines[0].split(" ").map { it.toLong() }, 75))
    val finishTime = System.currentTimeMillis()
    println("Calculated in ${finishTime - startTime} ms")
}

// Pair of stoneNumber and stoneTimesLeft as a key to result that they give
val calculationTable = HashMap<Pair<Long, Long>, Long>()

fun processStoneRecursive(stone: Long, times: Long): Long {
    val currentStone = Pair(stone, times)
    if (calculationTable.containsKey(currentStone))
        return calculationTable[currentStone]!!

    if (times == 0L) return 1L.also { calculationTable[currentStone] = it }

    val stoneSize = log10(stone.toDouble()).toLong() + 1
    return when {
        stone == 0L -> processStoneRecursive(1L, times - 1)

        stoneSize % 2 == 0L -> {
            val splitPoint = 10.0.pow((stoneSize / 2).toDouble()).toLong()
            return processStoneRecursive(stone / splitPoint, times - 1) +
                        processStoneRecursive(stone % splitPoint, times - 1)
        }

        else -> processStoneRecursive(stone * 2024, times - 1)
    }.also { calculationTable[currentStone] = it }
}

fun processStones(stones: List<Long>, times: Long) = stones.sumOf { processStoneRecursive(it, times) }

// Slow solution
fun processStone(stones: List<Long>, times: Long): Long {
    fun blink(stones: List<Long>): List<Long> {
        val blinked = mutableListOf<Long>()
        stones.forEach {
            val stoneSize = log10(it.toDouble()).toLong() + 1
            when {
                it == 0L -> blinked.add(1)
                stoneSize % 2 == 0L -> {
                    val splitPoint = 10.0.pow((stoneSize / 2).toDouble()).toLong()
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
    return processedStones.size.toLong()
}