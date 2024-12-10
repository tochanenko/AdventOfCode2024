package day10

import readLines

fun main(args: Array<String>) {
    val lines = readLines("./src/day10/input.txt")
    val matrix = createMatrix(lines)
    println(dfs(matrix))
}

fun createMatrix(lines: List<String>): Array<Array<Int>> {
    val matrix = Array(lines.size) { Array(lines[0].length) { 0 } }
    lines.forEachIndexed { i, row ->
        row.forEachIndexed { j, cell -> matrix[i][j] = cell.toString().toInt() }
    }
    return matrix
}

fun dfs(matrix: Array<Array<Int>>): Int {
    val zeroVertices = ArrayDeque<MutableList<Triple<Int, Int, Int>>>()
    var res = 0

    matrix.forEachIndexed { i, row ->
        row.forEachIndexed { j, cell -> if (cell == 0) zeroVertices.add(mutableListOf(Triple(i, j, matrix[i][j]))) }
    }

    zeroVertices.forEach { vertices ->
        run {
            val visitedVertices = mutableSetOf(vertices[0])
            var currentPathScore = 0

            while (vertices.isNotEmpty()) {
                val cell = vertices.removeFirst()

                if (cell.third == 9) {
                    currentPathScore++
                    continue
                }

                val adjacentCells = getAdjacent(cell, matrix)
                adjacentCells.forEach {
                    if (it !in visitedVertices) {
                        vertices.add(it)
                        visitedVertices.add(it)
                    }
                }
            }

            res += currentPathScore
        }
    }

    return res
}

fun getAdjacent(cell: Triple<Int, Int, Int>, matrix: Array<Array<Int>>): List<Triple<Int, Int, Int>> {
    val directions = listOf(
        Pair(-1, 0),
        Pair(1, 0),
        Pair(0, -1),
        Pair(0, 1)
    )

    val adjacentCells = mutableListOf<Triple<Int, Int, Int>>()

    if (cell.third == 9) return emptyList()

    directions.forEach {
        val i = cell.first + it.first
        val j = cell.second + it.second

        if (i in matrix.indices && j in matrix[0].indices && matrix[i][j] == cell.third + 1)
            adjacentCells.add(Triple(i, j, matrix[i][j]))
    }

    return adjacentCells
}