package day09

import readLines

fun main(args: Array<String>) {
    val lines = readLines("./src/day09/input.txt")
    val memoryLane = createMemoryLane(lines[0].map { it.toString().toInt() })
    val checksum = calculate(memoryLane)
    println(checksum)
    val newCompression = calculateFileMoves(memoryLane)
    println("New compression: $newCompression")
}

data class SystemFile(
    var amount: Int,
    var number: Int
)

val Int.isOdd: Boolean
    get() = this % 2 != 0

val Int.isEven: Boolean
    get() = this % 2 == 0

fun createMemoryLane(line: List<Int>): List<Int> {
    val memoryLane = mutableListOf<Int>()

    for (i in line.indices) {
        for (j in 0..<line[i]) {
            if (i.isEven) memoryLane.add(i / 2)
            else memoryLane.add(-1)
        }
    }

    return memoryLane
}

fun calculate(line: List<Int>): Long {
    var res = 0L

    val size = line.size
    var left = 0
    var right = size - 1

    while (left < right) {
        if (line[left] != -1) {
            res += left * line[left]
            left++
        } else if (line[right] != -1) {
            res += left * line[right]
            right--
            left++
        } else {
            right--
        }
    }

    if ((left == right) && line[left] != -1) {
        res += left * line[left]
    }
    return res
}

fun calculateFileMoves(lane: List<Int>): Long {
    val compressedLane = lane.toMutableList()

    fun findEmptySlot(file: SystemFile): Int {
        var emptySlotsSequence = 0
        for (i in compressedLane.indices) {
            if (emptySlotsSequence == file.amount)
                return i - file.amount
            if (compressedLane[i] == -1) emptySlotsSequence++
            else emptySlotsSequence = 0
        }
        return -1
    }

    fun getNextSystemFileToMove(right: Int): SystemFile {
        var foundFile = false
        val systemFile = SystemFile(0, -1)
        for (i in right downTo 0) {
            if (compressedLane[i] != -1) {
                if (foundFile && systemFile.number != compressedLane[i]) {
                    return systemFile
                }
                foundFile = true
                systemFile.number = compressedLane[i]
                systemFile.amount++
            } else if (foundFile) return systemFile
        }
        return systemFile
    }

    var right = lane.size - 1

    var nextSystemFile = getNextSystemFileToMove(right)
    var emptySlot = -1

    while (right > 0) {
        if (lane[right] == -1) {
            right--
            continue
        }

        nextSystemFile = getNextSystemFileToMove(right)
        emptySlot = findEmptySlot(nextSystemFile)

        if (emptySlot != -1 && emptySlot < right) {
            for (i in emptySlot..<(emptySlot + nextSystemFile.amount)) {
                compressedLane[i] = nextSystemFile.number
            }

            for (i in (right - nextSystemFile.amount + 1)..right) {
                compressedLane[i] = -1
            }
        }

        right -= nextSystemFile.amount
    }

    compressedLane.forEach { if (it == -1) print('.') else print(it) }

    var res = 0L
    for (i in compressedLane.indices) {
        res += i * (if (compressedLane[i] != -1) compressedLane[i] else 0)
    }

    return res
}