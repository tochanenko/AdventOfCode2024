package day01

import readLines
import kotlin.math.abs

fun main(args: Array<String>) {
    val lines = readLines("./src/day01/input.txt")
    val orderedLists = getOrderedLists(lines)
    println("Distances: ${getSimilarityScore(orderedLists[0], orderedLists[1])}")
}

fun getOrderedLists(lines: List<String>): List<List<Int>> {
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()
    lines.forEach{ line -> run {
        val numbers = line.split("   ")
        firstList.add(numbers[0].toInt())
        secondList.add(numbers[1].toInt())
    }}
    return listOf(firstList.sorted(), secondList.sorted())
}

fun getDistances(santaList: List<Int>, elvesList: List<Int>): Int {
    var distances = 0
    for (i in santaList.indices) {
        distances += abs(santaList[i] - elvesList[i])
    }
    return distances
}

fun getSimilarityScore(santaList: List<Int>, elvesList: List<Int>): Int {
    var similarityScore = 0
    for (i in santaList.indices) {
        similarityScore += santaList[i] * elvesList.count { it == santaList[i] }
    }
    return similarityScore
}