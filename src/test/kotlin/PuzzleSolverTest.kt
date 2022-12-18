import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.Test
import kotlin.test.expect

class Day01Test : AbstractPuzzleSolverTest(Day1, 24000, 45000)
class Day02Test : AbstractPuzzleSolverTest(Day2, 15, 12)
class Day03Test : AbstractPuzzleSolverTest(Day3, 157, 70)
class Day04Test : AbstractPuzzleSolverTest(Day4, 2, 4)
class Day05Test : AbstractPuzzleSolverTest(Day5, "CMZ", "MCD")
class Day06Test : AbstractPuzzleSolverTest(Day6, 10, 29)
class Day07Test : AbstractPuzzleSolverTest(Day7, 95437, 24933642)
class Day08Test : AbstractPuzzleSolverTest(Day8, 21, 8)
class Day09Test : AbstractPuzzleSolverTest(Day9, 13, 1)
class Day10Test : AbstractPuzzleSolverTest(
    Day10, 13140, "\n##  ##  ##  ##  ##  ##  ##  ##  ##  ##  \n" +
            "###   ###   ###   ###   ###   ###   ### \n" +
            "####    ####    ####    ####    ####    \n" +
            "#####     #####     #####     #####     \n" +
            "######      ######      ######      ####\n" +
            "#######       #######       #######     "
)

class Day11Test : AbstractPuzzleSolverTest(Day11, 10605, 2713310158)
class Day12Test : AbstractPuzzleSolverTest(Day12, 31, 29)
class Day13Test : AbstractPuzzleSolverTest(Day13, 13, 140)
class Day14Test : AbstractPuzzleSolverTest(Day14, 24, 93)
class Day15Test : AbstractPuzzleSolverTest(Day15(10, (0..20)), 26, 56000011)
class Day16Test : AbstractPuzzleSolverTest(Day16, -1, -1)
class Day17Test : AbstractPuzzleSolverTest(Day17, -1, -1)
class Day18Test : AbstractPuzzleSolverTest(Day18, -1, -1)
class Day19Test : AbstractPuzzleSolverTest(Day19, -1, -1)
class Day20Test : AbstractPuzzleSolverTest(Day20, -1, -1)
class Day21Test : AbstractPuzzleSolverTest(Day21, -1, -1)
class Day22Test : AbstractPuzzleSolverTest(Day22, -1, -1)
class Day23Test : AbstractPuzzleSolverTest(Day23, -1, -1)
class Day24Test : AbstractPuzzleSolverTest(Day24, -1, -1)
class Day25Test : AbstractPuzzleSolverTest(Day25, -1, -1)

abstract class AbstractPuzzleSolverTest(
    private val sut: PuzzleSolver,
    private val expected1: Any,
    private val expected2: Any,
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
