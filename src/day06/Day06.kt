package day06

import readLines

const val DEBUG = false

fun main(args: Array<String>) {
    val lines = readLines("./src/day06/input.txt")
    val (matrix, startPosition) = buildMatrix(lines)
    val visitedMatrix = buildVisitedMatrix(matrix, startPosition, Direction.UP)
    val visited = calculateVisited(visitedMatrix)
    println("SCORE: $visited")
}

enum class Cell { OBSTACLE, VISITED, EMPTY, OUTSIDE }

enum class Direction { UP, RIGHT, DOWN, LEFT }

fun buildMatrix(lines: List<String>): Pair<Array<Array<Cell>>, Pair<Int, Int>> {
    val matrix = Array(lines.size) { Array(lines.size) { Cell.EMPTY } }
    var startPosition = Pair(0, 0)

    for (i in lines.indices) {
        for (j in lines[i].indices) {
            if (lines[i][j] == '#') matrix[i][j] = Cell.OBSTACLE
            else if (lines[i][j] == '.') matrix[i][j] = Cell.EMPTY
            else if (lines[i][j] == '^') {
                matrix[i][j] = Cell.EMPTY
                startPosition = Pair(i, j)
            }
        }
    }

    return Pair(matrix, startPosition)
}

//fun buildVisitedMatrix(matrix: Array<Array<Cell>>, startPosition: Pair<Int, Int>): Array<Array<Cell>> {
//    return buildVisitedMatrix(matrix, startPosition, Direction.UP)
//}

// Recursion just for funzies
fun buildVisitedMatrixRecursion(matrix: Array<Array<Cell>>, position: Pair<Int, Int>, direction: Direction): Array<Array<Cell>> {
    val visitedMatrix = Array(matrix.size) { row -> matrix[row].copyOf() }
    val nextCell = checkNextCell(matrix, position, direction)
    visitedMatrix[position.first][position.second] = Cell.VISITED

    printMatrix(visitedMatrix)

    return when(nextCell) {
        Cell.OUTSIDE -> visitedMatrix
        Cell.EMPTY, Cell.VISITED -> buildVisitedMatrix(visitedMatrix, nextPosition(position, direction), direction)
        Cell.OBSTACLE -> buildVisitedMatrix(visitedMatrix, position, nextDirection(direction))
    }
}

fun buildVisitedMatrix(matrix: Array<Array<Cell>>, position: Pair<Int, Int>, direction: Direction): Array<Array<Cell>> {
    val visitedMatrix = Array(matrix.size) { row -> matrix[row].copyOf() }
    var currentPosition = Pair(position.first, position.second)
    var currentDirection = direction
    var isOutside = false

    while (!isOutside) {
        visitedMatrix[currentPosition.first][currentPosition.second] = Cell.VISITED
        printMatrix(visitedMatrix)
        when (checkNextCell(visitedMatrix, currentPosition, currentDirection)) {
            Cell.OUTSIDE -> {
                isOutside = true
            }
            Cell.EMPTY, Cell.VISITED -> {
                currentPosition = nextPosition(currentPosition, currentDirection)
            }
            Cell.OBSTACLE -> {
                currentDirection = nextDirection(currentDirection)
                currentPosition = nextPosition(currentPosition, currentDirection)
            }
        }
    }

    return visitedMatrix
}

fun checkNextCell(matrix: Array<Array<Cell>>, position: Pair<Int, Int>, direction: Direction): Cell = when (direction) {
    Direction.UP -> if (position.first == 0) {
        Cell.OUTSIDE
    } else {
        matrix[position.first - 1][position.second]
    }

    Direction.RIGHT -> if (position.second == matrix.size - 1) {
        Cell.OUTSIDE
    } else {
        matrix[position.first][position.second + 1]
    }

    Direction.DOWN -> if (position.first == matrix.size - 1) {
        Cell.OUTSIDE
    } else {
        matrix[position.first + 1][position.second]
    }

    Direction.LEFT -> if (position.second == 0) {
        Cell.OUTSIDE
    } else {
        matrix[position.first][position.second - 1]
    }
}

fun nextDirection(direction: Direction): Direction = when(direction) {
    Direction.UP -> Direction.RIGHT
    Direction.RIGHT -> Direction.DOWN
    Direction.DOWN -> Direction.LEFT
    Direction.LEFT -> Direction.UP
}

fun nextPosition(position: Pair<Int, Int>, direction: Direction): Pair<Int, Int> = when (direction) {
    Direction.UP -> Pair(position.first - 1, position.second)
    Direction.RIGHT -> Pair(position.first, position.second + 1)
    Direction.DOWN -> Pair(position.first + 1, position.second)
    Direction.LEFT -> Pair(position.first, position.second - 1)
}

fun nextCell(position: Pair<Int, Int>, direction: Direction): Pair<Pair<Int, Int>, Direction> = Pair(
    nextPosition(position, direction),
    nextDirection(direction)
)

fun calculateVisited(matrix: Array<Array<Cell>>) = matrix.sumOf { row -> row.count { cell -> cell == Cell.VISITED } }

// ============ DEBUG ============
fun printMatrix(matrix: Array<Array<Cell>>) {
    if (!DEBUG) return
    println()
    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            when (matrix[i][j]) {
                Cell.EMPTY -> print('.')
                Cell.OBSTACLE -> print('#')
                Cell.VISITED -> print('X')
                else -> print('?')
            }
        }
        println()
    }
    println()
}