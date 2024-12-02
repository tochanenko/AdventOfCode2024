package Day2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day2KtTest {

    @Test
    fun checkSequence() {
        assertTrue(checkSequence(listOf(16, 18, 20, 22, 23)))
        assertFalse(checkSequence(listOf(16, 18, 20, 22, 23, 22)))
        assertFalse(checkSequence(listOf(16, 18, 20, 22, 26)))
        assertTrue(checkSequence(listOf(0, 3)))
        assertFalse(checkSequence(listOf(0, 4)))
        assertFalse(checkSequence(listOf(11, 12, 13, 16, 17, 24, 27, 29)))
        assertFalse(checkSequence(listOf(3, 2, 5, 5, 6, 12)))
        assertTrue(checkSequence(listOf(5, 4, 3, 2, 1)))
        assertTrue(checkSequence(listOf(0)))
        assertFalse(checkSequence(listOf(0, 0)))
    }
}