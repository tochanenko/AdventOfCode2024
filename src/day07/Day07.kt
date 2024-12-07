package day07

import readLines

const val DEBUG = false

data class Equation(
    val result: Long,
    val numbers: List<Long>
)

enum class Operation { ADD, MULTIPLY }

fun main(args: Array<String>) {
    val lines = readLines("./src/day07/input.txt")
    val equations = lines.map { line -> lineToEquation(line) }
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

fun lineToEquation(line: String): Equation = Equation(
    line.substring(0, line.indexOfFirst { it == ':' }).toLong(),
    line.substring(line.indexOfFirst{ it == ':'} + 1).trim().split(' ').map(String::toLong)
)

fun checkEquation(equation: Equation): Boolean {
    val operations = Array(equation.numbers.size - 1) { Operation.ADD }
    if (calculateEquation(equation, operations)) return true
    return checkNextOperation(equation, operations)
}

fun checkNextOperation(equation: Equation, operations: Array<Operation>): Boolean {
    printEquation(equation, operations)
    if (calculateEquation(equation, operations)) return true
    for (i in operations.indices) {
        if (operations[i] != Operation.MULTIPLY) {
            val testOperations = operations.copyOf()
            testOperations[i] = Operation.MULTIPLY
            if (checkNextOperation(equation, testOperations)) return true
        }
    }
    return false
}

fun calculateEquation(equation: Equation, operations: Array<Operation>): Boolean {
    var res = equation.numbers[0]
    for (i in 1..<equation.numbers.size) {
        when (operations[i - 1]) {
            Operation.ADD -> res += equation.numbers[i]
            Operation.MULTIPLY -> res *= equation.numbers[i]
        }
    }
    return equation.result == res
}

fun printEquation(equation: Equation, operations: Array<Operation>) {
    if (!DEBUG) return
    print("[${if (calculateEquation(equation, operations)) "RIGHT" else "WRONG"}] ${equation.result}: ${equation.numbers[0]}")
    for (i in 1..<equation.numbers.size) {
        print("${if (operations[i - 1] == Operation.ADD) " + " else " * "}${equation.numbers[i]}")
    }
    println()
}