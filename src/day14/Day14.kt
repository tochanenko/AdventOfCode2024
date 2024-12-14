package day14

import readLines
import Coords
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

data class Bot(
    val pos: Coords,
    val velocity: Coords
)

const val SIZE_X = 101 // 11 or 101
const val SIZE_Y = 103 // 7 or 103
const val LOWER_FLOOR = 2000
const val MOVES = 10000

fun main(args: Array<String>) {
    val lines = readLines("./src/day14/input.txt")
    val bots = parseBots(lines)
    bots.forEach { println(it) }
    println(calculateSafetyValue(bots))
}

fun parseBots(lines: List<String>): List<Bot> {
    fun makePositive(coords: Coords): Coords {
        return Coords(
            if (coords.x < 0) SIZE_X + coords.x else coords.x,
            if (coords.y < 0) SIZE_Y + coords.y else coords.y
        )
    }

    val bots = mutableListOf<Bot>()

    lines.forEach { line ->
        val coords = line.split(" ")
        val pos = coords[0].substring(2).split(",").map { it.toInt() }
        val velocity = coords[1].substring(2).split(",").map { it.toInt() }
        bots.add(
            Bot(
                pos = makePositive(Coords(pos[0], pos[1])),
                velocity = makePositive(Coords(velocity[0], velocity[1]))
            )
        )
    }

    return bots
}

fun calculateSafetyValueMathematically(bots: List<Bot>): Int {
    val safetyValues = Array(4) { 0 }

    bots.forEach { bot ->
        val newPos = Coords(
            (bot.pos.x + bot.velocity.x * MOVES) % SIZE_Y,
            (bot.pos.y + bot.velocity.y * MOVES) % SIZE_X
        )
        try {
            safetyValues[checkQuadrant(newPos)]++
        } catch (_: ArrayIndexOutOfBoundsException) { println("Out of boundaries") }
    }

    safetyValues.forEach { println(it) }

    return safetyValues.reduce { acc, value -> acc * value}
}

fun calculateSafetyValue(bots: List<Bot>): Int {
    val currentBots = buildList { addAll(bots) }

//    printMatrix(buildMatrix(currentBots))

    for (i in 1..MOVES) {
        currentBots.forEach { bot ->
            bot.pos.x = (bot.pos.x + bot.velocity.x) % SIZE_X
            bot.pos.y = (bot.pos.y + bot.velocity.y) % SIZE_Y
        }
        if (i > LOWER_FLOOR)
            drawMatrix(buildMatrix(currentBots), i)
        println("Drawn Matrix $i")
    }

    val safetyValues = Array(4) { 0 }

    currentBots.forEach { bot ->
        try {
            safetyValues[checkQuadrant(bot.pos)]++
        } catch (_: ArrayIndexOutOfBoundsException) { }
    }

    return safetyValues.reduce { acc, value -> acc * value }
}

fun buildMatrix(bots: List<Bot>): Array<Array<Int>> {
    val matrix = Array(SIZE_Y) { Array(SIZE_X) { 0 } }
    bots.forEach { bot ->
        matrix[bot.pos.y][bot.pos.x]++
    }
    return matrix
}

fun printMatrix(matrix: Array<Array<Int>>) {
    matrix.forEach { row ->
        row.forEach { if (it > 0) print(it) else print('.') }
        println()
    }
}

fun drawMatrix(matrix: Array<Array<Int>>, id: Int) {
    val squareSize = 25 //px
    val image = BufferedImage(SIZE_X * squareSize, SIZE_Y * squareSize, BufferedImage.TYPE_INT_RGB)
    val graphics = image.createGraphics()

    matrix.forEachIndexed { i, row ->
        row.forEachIndexed{ j, cell ->
            graphics.color = if (cell == 0) Color.WHITE else Color.BLACK
            graphics.fillRect(j * squareSize, i * squareSize, squareSize, squareSize)
        }
    }

    graphics.dispose()
    val output = File("./src/day14/images/$id.png")
    ImageIO.write(image, "png", output)
}


// Returns 0 for top-left quadrant, 1 for top-right quadrant,
//         2 for bottom-left quadrant, 3 for bottom-right quadrant
//         -1 if position is on the division line
fun checkQuadrant(pos: Coords): Int {
    if (SIZE_X / 2 == pos.x || SIZE_Y / 2 == pos.y)
        return -1

    return if (pos.x < SIZE_X / 2) {
        if (pos.y < SIZE_Y / 2) 0 else 1
    } else {
        if (pos.y < SIZE_Y / 2) 2 else 3
    }
}