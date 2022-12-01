import kotlin.math.abs

object Day7 : PuzzleSolver(7) {

    override fun solve1(input: String): Number {
        val positions = input.splitToInts()
        val maxPosition = positions.maxOf { it }

        return (0..maxPosition).minOf { positions.consumptionForPosition(it) }
    }

    override fun solve2(input: String): Number {
        val positions = input.splitToInts()
        val maxPosition = positions.maxOf { it }

        return (0..maxPosition).minOf { positions.correctConsumptionForPosition(it) }
    }

    private fun List<Int>.consumptionForPosition(position: Int) =
        sumOf { abs(it - position) }

    private fun List<Int>.correctConsumptionForPosition(position: Int) =
        sumOf { (0..abs(it - position)).sum() }
}
