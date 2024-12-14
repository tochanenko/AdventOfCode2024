import java.io.File
import java.io.InputStream

fun readLines(filename: String): List<String> {
    val input: InputStream = File(filename).inputStream()
    val lines = mutableListOf<String>()
    input.bufferedReader().forEachLine { lines.add(it) }

    return lines
}

data class Coords(
    var x: Int,
    var y: Int
)