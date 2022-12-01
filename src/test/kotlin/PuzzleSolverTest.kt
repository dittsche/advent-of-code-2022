import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.Test
import kotlin.test.expect

class Day01Test : AbstractPuzzleSolverTest(Day1, 7, 5)
class Day02Test : AbstractPuzzleSolverTest(Day2, 150, 900)
class Day03Test : AbstractPuzzleSolverTest(Day3, 198, 230)
class Day04Test : AbstractPuzzleSolverTest(Day4, 4512, 1924)
class Day05Test : AbstractPuzzleSolverTest(Day5, 5, 12)

// class Day06Test : AbstractPuzzleSolverTest(Day6, 5934, 26984457539)
class Day07Test : AbstractPuzzleSolverTest(Day7, 37, 168)
class Day08Test : AbstractPuzzleSolverTest(Day8, 26, 61229)
class Day09Test : AbstractPuzzleSolverTest(Day9, 15, 1134)
class Day10Test : AbstractPuzzleSolverTest(Day10, 26397, 288957)
class Day11Test : AbstractPuzzleSolverTest(Day11, 1656, 195)
class Day12Test : AbstractPuzzleSolverTest(Day12, 19, 103)
class Day13Test : AbstractPuzzleSolverTest(Day13, 17, 0)

// class Day14Test : AbstractPuzzleSolverTest(Day14, 1588L, 2188189693529L)
class Day15Test : AbstractPuzzleSolverTest(Day15, 40, 315)
class Day16Test : AbstractPuzzleSolverTest(Day16, 31, 1L, true)
class Day17Test : AbstractPuzzleSolverTest(Day17, 45, 112)
class Day18Test : AbstractPuzzleSolverTest(Day18, 4140, 3993)
class Day19Test : AbstractPuzzleSolverTest(Day19, 79, 3621)
class Day20Test : AbstractPuzzleSolverTest(Day20, 35, 3351)
class Day21Test : AbstractPuzzleSolverTest(Day21, 739785, 444356092776315)
class Day22Test : AbstractPuzzleSolverTest(Day22, 590784, 2758514936282235, true)
class Day23Test : AbstractPuzzleSolverTest(Day23, -1, -1)
class Day24Test : AbstractPuzzleSolverTest(Day24, -1, -1)
class Day25Test : AbstractPuzzleSolverTest(Day25, -1, -1)

abstract class AbstractPuzzleSolverTest(
    private val sut: PuzzleSolver,
    private val expected1: Number,
    private val expected2: Number,
    private val differentInput: Boolean = false
) {

    @Test
    fun solve1() {
        Assumptions.assumeTrue(expected1 != -1)
        expect(expected1) { sut.solve1(readInput(sut.day, if (differentInput) 1 else null)) }
    }

    @Test
    fun solve2() {
        Assumptions.assumeTrue(expected2 != -1)
        expect(expected2) { sut.solve2(readInput(sut.day, if (differentInput) 2 else null)) }
    }
}
