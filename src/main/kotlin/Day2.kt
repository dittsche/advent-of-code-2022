import Day2.Result.*
import Day2.Shape.*
import java.lang.RuntimeException

object Day2 : PuzzleSolver(2) {

    override fun solve1(input: String) =
        input.lines()
            .map { it.toRoundPuzzle1() }
            .sumOf { it.evaluate() }

    override fun solve2(input: String) =
        input.lines()
            .map { it.toRoundPuzzle2() }
            .sumOf { it.evaluate() }


    private fun String.toShape() = when (this) {
        "A", "X" -> ROCK
        "B", "Y" -> PAPER
        "C", "Z" -> SCISSORS
        else -> throw RuntimeException()
    }

    private fun String.toExpectedResult() = when (this) {
        "X" -> LOSE
        "Y" -> DRAW
        "Z" -> WIN
        else -> throw RuntimeException()
    }

    private fun String.toRoundPuzzle1() = split(" ")
        .let { RoundPuzzle1(it[0].toShape(), it[1].toShape()) }

    private fun String.toRoundPuzzle2() = split(" ")
        .let { RoundPuzzle2(it[0].toShape(), it[1].toExpectedResult()) }

    private data class RoundPuzzle1(private val opponent: Shape, private val own: Shape) {
        private val result = when {
            own == opponent -> DRAW
            own.defeats() == opponent -> WIN
            else -> LOSE
        }

        fun evaluate() = own.score + result.score
    }

    private data class RoundPuzzle2(private val opponent: Shape, private val expectedResult: Result) : Round {
        private val expectedShape = when (expectedResult) {
            DRAW -> opponent
            WIN -> opponent.losesTo()
            LOSE -> opponent.defeats()
        }

        override fun evaluate() = expectedResult.score + expectedShape.score
    }

    private interface Round {
        fun evaluate(): Int
    }

    private enum class Shape(val score: Int) {
        ROCK(1), PAPER(2), SCISSORS(3);

        fun defeats(): Shape = when (this) {
            ROCK -> SCISSORS
            SCISSORS -> PAPER
            PAPER -> ROCK
        }

        fun losesTo(): Shape = when (this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }
    }

    private enum class Result(val score: Int) {
        WIN(6), DRAW(3), LOSE(0)
    }
}