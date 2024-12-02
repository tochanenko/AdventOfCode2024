package day02

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day02KtTest {

    @Test
    fun checkSequence() {
        assertTrue(checkSequence(listOf(7, 6, 4, 2, 1)))
        assertFalse(checkSequence(listOf(1, 2, 7, 8, 9)))
        assertFalse(checkSequence(listOf(9, 7, 6, 2, 1)))
        assertTrue(checkSequence(listOf(1, 3, 2, 4, 5)))
        assertTrue(checkSequence(listOf(8, 6, 4, 4, 1)))
        assertTrue(checkSequence(listOf(1, 3, 6, 7, 9)))
        assertFalse(checkSequence(listOf(92, 91, 92, 93, 99)))
    }
}