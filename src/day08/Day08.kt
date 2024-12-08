package day08

import readLines

fun main(args: Array<String>) {
    val lines = readLines("./src/day08/input.txt")
    val nodes: HashMap<Char, List<Pair<Int, Int>>> = getNodes(lines)

    val antinodeMatrix = buildAntinodeMatrix(nodes, lines.size)

    for (row in antinodeMatrix) {
        for (element in row) {
            print(element)
        }
        println()
    }

    println("Antinodes: ${countAntinodes(antinodeMatrix)}")
}

fun countAntinodes(antinodeMatrix: Array<Array<Char>>): Int = antinodeMatrix.sumOf { row -> row.count { it == '#' } }

fun getNodes(lines: List<String>): HashMap<Char, List<Pair<Int, Int>>> {
    val nodes = HashMap<Char, MutableList<Pair<Int, Int>>>()
    for (i in lines.indices) {
        for (j in lines[i].indices) {
            if (lines[i][j] != '.') {
                if (nodes.containsKey(lines[i][j]))
                    nodes[lines[i][j]]?.add(Pair(i, j))
                else
                    nodes[lines[i][j]] = mutableListOf(Pair(i, j))
            }
        }
    }
    return nodes.mapValues { it.value.toList() }.toMap(HashMap())
}

fun buildAntinodeMatrix(nodes: HashMap<Char, List<Pair<Int, Int>>>, size: Int): Array<Array<Char>> {
    val antinodeMatrix = Array(size) { Array(size) { '.' } }

    for (node in nodes.keys) {
        val towerLocations = nodes.getValue(node)

        for (i in towerLocations.indices) {
            for (j in (i + 1)..< towerLocations.size) {
                val antinodes = calculateAntinodesHarmonics(towerLocations[i], towerLocations[j], size)
                for (antinode in antinodes) {
                    antinodeMatrix[antinode.first][antinode.second] = '#'
                }
            }
        }

    }

    return antinodeMatrix
}

fun calculateAntinodes(towerA: Pair<Int, Int>, towerB: Pair<Int, Int>, size: Int): List<Pair<Int, Int>> {
    val diffTower = Pair(towerA.first - towerB.first, towerA.second - towerB.second)
    val antinodes = mutableListOf<Pair<Int, Int>>()

    val firstAntinode = Pair(towerA.first + diffTower.first, towerA.second + diffTower.second)
    val secondAntinode = Pair(towerB.first - diffTower.first, towerB.second - diffTower.second)

    if (!isOutside(firstAntinode, size)) antinodes.add(firstAntinode)
    if (!isOutside(secondAntinode, size)) antinodes.add(secondAntinode)

    return antinodes
}

fun calculateAntinodesHarmonics(towerA: Pair<Int, Int>, towerB: Pair<Int, Int>, size: Int): List<Pair<Int, Int>> {
    val diffTower = Pair(towerA.first - towerB.first, towerA.second - towerB.second)
    val antinodes = mutableListOf(towerA, towerB)

    var calculatedAntinode = Pair(towerA.first + diffTower.first, towerA.second + diffTower.second)
    while (!isOutside(calculatedAntinode, size)) {
        antinodes.add(calculatedAntinode)
        calculatedAntinode = Pair(calculatedAntinode.first + diffTower.first, calculatedAntinode.second + diffTower.second)
    }

    calculatedAntinode = Pair(towerB.first - diffTower.first, towerB.second - diffTower.second)
    while (!isOutside(calculatedAntinode, size)) {
        antinodes.add(calculatedAntinode)
        calculatedAntinode = Pair(calculatedAntinode.first - diffTower.first, calculatedAntinode.second - diffTower.second)
    }

    return antinodes
}

fun isOutside(node: Pair<Int, Int>, size: Int): Boolean {
    return (node.first < 0 || node.first >= size) || (node.second < 0 || node.second >= size)
}