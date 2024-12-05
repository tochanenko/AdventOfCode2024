package day05

import readLines

const val DEBUG = false

fun main(args: Array<String>) {
    val lines = readLines("./src/day05/input.txt")
    val (rules, prints) = splitRulesAndPrints(lines)

    println("Correct:   ${prints.sumOf { calculateRule(rules, it) }}")
    println("Corrected: ${prints.sumOf { calculateCorrected(rules, it) }}")

    for (i in rules.indices) {
        printRules(i, rules[i])
    }
}

fun splitRulesAndPrints(lines: List<String>): Pair<ArrayList<List<Int>>, List<List<Int>>> {
    val rules = Array<MutableList<Int>>(100) { mutableListOf() }
    val prints = mutableListOf<List<Int>>()

    lines.forEach { line ->
        run {
            if (line.contains('|')) {
                val numbers = line.split('|').map { it.toInt() }
                rules[numbers[1]].add(numbers[0])
            } else if (line.contains(',')) {
                val numbers = line.split(',').map { it.toInt() }
                prints.add(numbers)
            }
        }
    }

    return Pair(
        rules.map { it.toList() } as ArrayList<List<Int>>,
        prints
    )
}

fun calculateRule(rules: ArrayList<List<Int>>, page: List<Int>): Int {
    val pages = page.size
    for (i in 0..<pages) {
        for (j in i..<pages) {
            if (rules[page[i]].contains(page[j]))
                return 0
        }
    }
    return page[pages / 2]
}

fun calculateCorrected(rules: ArrayList<List<Int>>, page: List<Int>): Int {
    val pages = page.size
    val rulesForPage = Array<MutableList<Int>?>(100) { null }

    for (i in 0..<pages) {
        rulesForPage[page[i]] = mutableListOf()
    }

    printPage("Page", page)
    var isBroken = false
    for (i in 0..<pages) {
        for (j in i..<pages) {
            if (rules[page[i]].contains(page[j])) {
                printRule("BROKEN RULE ", page[j], page[i])
                isBroken = true
                rulesForPage[page[i]]?.add(page[j])
            }
            if (rules[page[j]].contains(page[i])) {
                printRule("Correct Rule", page[i], page[j])
                rulesForPage[page[j]]?.add(page[i])
            }
        }
    }

    val pairs = mutableListOf<Pair<Int, Int>>()

    for (i in rulesForPage.indices) {
        if (rulesForPage[i] != null) {
            printRules(i, rulesForPage[i]!!.toList())
            pairs.add(Pair(i, rulesForPage[i]!!.size))
        }
    }

    val sorted = pairs.sortedBy { it.second }.map { it.first }
    printPage("SORTED", sorted)

    return if (isBroken) sorted[pairs.size / 2] else 0
}

fun printRule(txt: String, left: Int, right: Int) {
    if (DEBUG) println("$txt: $left|$right")
}

fun printRules(num: Int, rules: List<Int>) {
    if (DEBUG) println("$num: [$rules")
}

fun printPage(txt: String, page: List<Int>) {
    if (DEBUG) println("$txt: [$page]")
}