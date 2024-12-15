package day11

import readLines
import kotlin.math.log10
import kotlin.math.pow

fun main(args: Array<String>) {
    val lines = readLines("./src/day11/input.txt")
    println(processStones(lines[0].split(" ").map { it.toLong() }, 75))
}

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

// Pair of stoneNumber and stoneTimesLeft as a key to result that they give
val calculationTable = HashMap<Pair<Long, Long>, Long>()

fun processStoneRecursive(stone: Long, times: Long): Long {
    val currentStone = Pair(stone, times)
    if (calculationTable.containsKey(currentStone))
        return calculationTable[currentStone]!!

    if (times == 0L) {
        calculationTable[currentStone] = 1
        return 1
    } else {
        val stoneSize = log10(stone.toDouble()).toLong() + 1
        when {
            stone == 0L -> {
                val numberOfStones = processStoneRecursive(1L, times - 1)
                calculationTable[currentStone] = numberOfStones
                return numberOfStones
            }

            stoneSize % 2 == 0L -> {
                val splitPoint = 10.0.pow((stoneSize / 2).toDouble()).toLong()
                val numberOfStones =
                    processStoneRecursive(stone / splitPoint, times - 1) +
                            processStoneRecursive(stone % splitPoint, times - 1)
                calculationTable[currentStone] = numberOfStones
                return numberOfStones
            }

            else -> {
                val numberOfStones = processStoneRecursive(stone * 2024, times - 1)
                calculationTable[currentStone] = numberOfStones
                return numberOfStones
            }
        }
    }
}

fun processStones(stones: List<Long>, times: Long): Long {
    var res = 0L

//    stones.forEach { res += processStone(listOf(it), times) }
    stones.forEach { res += processStoneRecursive(it, times) }

    return res
}