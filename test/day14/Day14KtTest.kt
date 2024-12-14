package day14

import Coords
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day14KtTest {
    @Test
    fun checkQuadrant() {
        assertEquals(0, checkQuadrant(Coords(2, 0)))
        assertEquals(1, checkQuadrant(Coords(0, 6)))
        assertEquals(1, checkQuadrant(Coords(0, 9)))
        assertEquals(2, checkQuadrant(Coords(5, 3)))
        assertEquals(2, checkQuadrant(Coords(5, 4)))
        assertEquals(2, checkQuadrant(Coords(6, 1)))
        assertEquals(3, checkQuadrant(Coords(5, 6)))
        assertEquals(-1, checkQuadrant(Coords(3, 1)))
        assertEquals(-1, checkQuadrant(Coords(3, 2)))
        assertEquals(-1, checkQuadrant(Coords(4, 5)))
    }
}