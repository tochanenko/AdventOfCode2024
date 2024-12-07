package day07

import readLines
import kotlin.math.pow
import kotlin.random.Random

const val DEBUG = false

data class Equation(
    val result: Long,
    val numbers: List<Long>,
    var isCorrect: Boolean = false
)

enum class Operation { ADD, MULTIPLY, CONCATENATE }

fun main(args: Array<String>) {
    val lines = readLines("./src/day07/input.txt")
    val equations = lines.map { line -> lineToEquation(line) }
//    output(equations) // it was too slow
    outputRandom(equations) // :P
}

// LOL it worked
fun outputRandom(equations: List<Equation>) {
    val calculations = 5

    for (i in 1..calculations) {
        for (j in equations.indices) {
            if (!equations[j].isCorrect && checkEquationRandom(equations[j])) {
                if (DEBUG) print("($j) ")
                equations[j].isCorrect = true
            }
        }
        println("\n[ ${equations.count { it.isCorrect }} / ${equations.size} ]\n\n")
    }
    println("Final sum: ${equations.filter { it.isCorrect }.sumOf { it.result }}")
}

fun lineToEquation(line: String): Equation = Equation(
    line.substring(0, line.indexOfFirst { it == ':' }).toLong(),
    line.substring(line.indexOfFirst{ it == ':'} + 1).trim().split(' ').map(String::toLong)
)

fun checkEquationRandom(equation: Equation): Boolean {
    val totalCalculations: Int = 3.0.pow((equation.numbers.size - 1).toDouble()).toInt() * 2

    for (calculation in 0..totalCalculations) {
        val operations = Array(equation.numbers.size - 1) { Operation.ADD }
        for (i in operations.indices) {
            val randomOperation  = Random.nextInt(3)
            when (randomOperation) {
                1 -> operations[i] = Operation.MULTIPLY
                2 -> operations[i] = Operation.CONCATENATE
            }
        }
        if (calculateEquation(equation, operations)) return true
    }

    return false
}

fun calculateEquation(equation: Equation, operations: Array<Operation>): Boolean {
    var res = equation.numbers[0]
    for (i in 1..<equation.numbers.size) {
        when (operations[i - 1]) {
            Operation.ADD -> res += equation.numbers[i]
            Operation.MULTIPLY -> res *= equation.numbers[i]
            Operation.CONCATENATE -> res = (res.toString() + equation.numbers[i].toString()).toLong()
        }
        if (res > equation.result) return false
    }
    return equation.result == res
}

// ============ OLD SLOW SOLUTION ============

fun output(equations: List<Equation>) {
    var sum = 0L
    var result = 0L
    for (i in equations.indices) {
        print("Checking $i")
        result = if (checkEquation(equations[i])) equations[i].result else 0L
        sum += result
        println(" [ $result ]")
    }
    println("Final sum: $sum")
}

fun checkEquation(equation: Equation): Boolean {
    val operations = Array(equation.numbers.size - 1) { Operation.ADD }
    if (calculateEquation(equation, operations)) return true
    return checkNextOperation(equation, operations)
}

fun checkNextOperation(equation: Equation, operations: Array<Operation>): Boolean {
    printEquation(equation, operations)
    if (calculateEquation(equation, operations)) return true
    for (i in operations.indices) {
        if (operations[i] == Operation.ADD) {
            // *
            val testOperationsMultiplication = operations.copyOf()
            testOperationsMultiplication[i] = Operation.MULTIPLY
            if (checkNextOperation(equation, testOperationsMultiplication)) return true

            // ||
            val testOperationsConcatenation = operations.copyOf()
            testOperationsConcatenation[i] = Operation.CONCATENATE
            if (checkNextOperation(equation, testOperationsConcatenation)) return true
        }
    }
    return false
}

fun printEquation(equation: Equation, operations: Array<Operation>) {
    if (!DEBUG) return
    print("[${if (calculateEquation(equation, operations)) "RIGHT" else "WRONG"}] ${equation.result}: ${equation.numbers[0]}")
    for (i in 1..<equation.numbers.size) {
        when (operations[i - 1]) {
            Operation.ADD -> print(" + ")
            Operation.MULTIPLY -> print(" * ")
            Operation.CONCATENATE -> print(" || ")
        }
        print(equation.numbers[i])
    }
    println()
}