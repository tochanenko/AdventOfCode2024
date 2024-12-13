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
    val zeroVertices = ArrayDeque<Pair<Int, Int>>()
    var secondRes = 0

    matrix.forEachIndexed { i, row ->
        row.forEachIndexed { j, cell ->
            if (cell == 0) zeroVertices.add(Pair(i, j))
        }
    }

    zeroVertices.forEach { start ->
        val visitedPaths = mutableSetOf<List<Pair<Int, Int>>>()
        val stack = ArrayDeque<List<Pair<Int, Int>>>()
        stack.add(listOf(start))

        var currentPathScore = 0

        while (stack.isNotEmpty()) {
            val path = stack.removeLast()
            val cell = path.last()
            val cellHeight = matrix[cell.first][cell.second]

            if (cellHeight == 9) {
                    currentPathScore++
                if (path !in visitedPaths) {
                    visitedPaths.add(path)
                }
                continue
            }

            val adjacentCells = getAdjacent(Triple(cell.first, cell.second, cellHeight), matrix)
            for (adj in adjacentCells) {
                if (Pair(adj.first, adj.second) !in path) {
                    stack.add(path + Pair(adj.first, adj.second))
                }
            }
        }

        val currentPathRating = visitedPaths.size
        secondRes += currentPathRating
    }

    return secondRes
}

fun bfs(matrix: Array<Array<Int>>): Int {
    val zeroVertices = ArrayDeque<MutableList<Triple<Int, Int, Int>>>()
    var firstRes = 0

    matrix.forEachIndexed { i, row ->
        row.forEachIndexed { j, cell -> if (cell == 0) zeroVertices.add(mutableListOf(Triple(i, j, matrix[i][j]))) }
    }

    zeroVertices.forEach { vertices ->
        run {
            val visitedVertices = mutableSetOf(vertices[0])
            var currentPathScore = 0
            var currentPathRating = 1

            while (vertices.isNotEmpty()) {
                val cell = vertices.removeFirst()
                val path = vertices.removeLast()

                if (cell.third == 9) {
                    currentPathScore++
                    continue
                }

                val adjacentCells = getAdjacent(cell, matrix)
                val verticesToAdd = mutableListOf<Triple<Int, Int, Int>>()
                adjacentCells.forEach {
                    if (it !in visitedVertices) {
                        verticesToAdd.add(it)
                    }
                }
                vertices.addAll(verticesToAdd)
                visitedVertices.addAll(verticesToAdd)
                if (verticesToAdd.size > 1) currentPathRating += verticesToAdd.size - 1
            }

            firstRes += currentPathScore
        }
    }

    return firstRes
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