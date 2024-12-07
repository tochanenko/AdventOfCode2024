package day06

import readLines

const val DEBUG = false

fun main(args: Array<String>) {
    val lines = readLines("./src/day06/input.txt")
    val (matrix, startPosition) = buildMatrix(lines)
    val visitedMatrix = buildVisitedMatrix(matrix, startPosition, Direction.UP)
    val visited = calculateVisited(visitedMatrix)
    val withAdditionalObstacles = calculateAdditionalObstaclePositions(matrix, startPosition, Direction.UP)
    println("SCORE: $visited")
    println("With Additional Obstacles: $withAdditionalObstacles")
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

// Recursion just for funzies
fun buildVisitedMatrixRecursion(
    matrix: Array<Array<Cell>>,
    position: Pair<Int, Int>,
    direction: Direction
): Array<Array<Cell>> {
    val visitedMatrix = Array(matrix.size) { row -> matrix[row].copyOf() }
    val nextCell = checkNextCell(matrix, position, direction)
    visitedMatrix[position.first][position.second] = Cell.VISITED

    printMatrix(visitedMatrix)

    return when (nextCell) {
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

fun isMatrixLooped(matrix: Array<Array<Cell>>, startPosition: Pair<Int, Int>, startDirection: Direction): Boolean {
    val visitedStates = mutableSetOf<Triple<Int, Int, Direction>>()
    var position = startPosition
    var direction = startDirection

    while (true) {
        val state = Triple(position.first, position.second, direction)
        if (state in visitedStates) {
            return true
        }
        visitedStates.add(state)

        val nextCell = checkNextCell(matrix, position, direction)
        position = when (nextCell) {
            Cell.OUTSIDE -> return false
            Cell.OBSTACLE -> {
                direction = nextDirection(direction)
                position
            }
            else -> nextPosition(position, direction)
        }
    }
}

// Doesn't work :(
fun isMatrixLoopedOld(matrix: Array<Array<Cell>>, position: Pair<Int, Int>, direction: Direction): Boolean {
    val visitedMatrix = Array(matrix.size) { row -> matrix[row].copyOf() }
    var currentPosition = Pair(position.first, position.second)
    var currentDirection = direction
    var isOutside = false
    val bumpedInto = mutableListOf<Pair<Int, Int>>()

    while (!isOutside) {
//        println("boop")
        if (checkNextCell(visitedMatrix, currentPosition, currentDirection) == Cell.OBSTACLE) {
            val moveToPosition = nextPosition(currentPosition, currentDirection)
            bumpedInto.add(moveToPosition)
            if (bumpedIntoLoop(bumpedInto)) break
//            println(bumpedInto)
        }
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

    return !isOutside
}

fun isLoop(matrix: Array<Array<Cell>>, position: Pair<Int, Int>, direction: Direction): Boolean {
    val moveToPosition = nextPosition(position, nextDirection(direction))
    return checkNextCell(matrix, position, direction) == Cell.OBSTACLE &&
            matrix[moveToPosition.first][moveToPosition.second] == Cell.VISITED
}

fun bumpedIntoLoop(bumpedInto: List<Pair<Int, Int>>): Boolean {
    if (bumpedInto.size <= 2) return false

    for (i in 0..<bumpedInto.size - 2) {
        for (j in (i + 2)..<bumpedInto.size - 2) {
            if (
                bumpedInto[i].first == bumpedInto[j].first &&
                bumpedInto[i].second == bumpedInto[j].second &&
                bumpedInto[i + 1].first == bumpedInto[j + 1].first &&
                bumpedInto[i + 1].second == bumpedInto[j + 1].second
            ) return true
        }
    }

    return false
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

fun nextDirection(direction: Direction): Direction = when (direction) {
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

fun calculateVisited(matrix: Array<Array<Cell>>) = matrix.sumOf { row -> row.count { cell -> cell == Cell.VISITED } }

fun calculateAdditionalObstaclePositions(
    matrix: Array<Array<Cell>>,
    position: Pair<Int, Int>,
    direction: Direction
): Int {
    var loopedMatrix = 0

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            println("$i: $j")
            if ((i == position.first && j == position.second)) continue
            val matrixWithObstacle = Array(matrix.size) { row -> matrix[row].copyOf() }
            matrixWithObstacle[i][j] = Cell.OBSTACLE
            if (isMatrixLooped(matrixWithObstacle, position, direction)) {
                loopedMatrix++

//                println()
//                for (i in matrixWithObstacle.indices) {
//                    for (j in matrixWithObstacle[i].indices) {
//                        if (i == position.first && j == position.second) print('^')
//                        else when (matrixWithObstacle[i][j]) {
//                            Cell.EMPTY -> print('.')
//                            Cell.OBSTACLE -> print('#')
//                            Cell.VISITED -> print('X')
//                            else -> print('?')
//                        }
//                    }
//                    println()
//                }
//                println()
            }
        }
    }

    return loopedMatrix
}

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