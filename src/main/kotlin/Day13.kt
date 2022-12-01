import Day13.FOLD.X
import Day13.FOLD.Y
import kotlin.math.abs

object Day13 : PuzzleSolver(13) {

    private const val FOLD_ALONG = "fold along "

    override fun solve1(input: String): Number {
        var coords = input.toCoords()

        input.toFolds().take(1).forEach() { coords = coords.fold(it) }
        return coords.size
    }

    override fun solve2(input: String): Number {
        val coords = input.toFolds().fold(input.toCoords()) { acc, curr -> acc.fold(curr) }

        val lines = coords.groupBy({ it.second }) { it.first }
        val lineWidth = coords.maxOf { it.first }
        (0 until lines.size)
            .forEach { lineIndex ->
                (0..lineWidth)
                    .map { if (it in lines.getOrDefault(lineIndex, listOf())) "#" else " " }
                    .let { println(it.joinToString("")) }
            }

        return 0
    }

    private fun String.toCoords() = lines()
        .takeWhile { it.isNotBlank() }
        .filter { it.isNotBlank() }
        .map { line -> line.split(",").let { it[0].toInt() to it[1].toInt() } }
        .toSet()

    private fun String.toFolds() = lines()
        .dropWhile { it.isNotBlank() }
        .drop(1)
        .map { it.removePrefix(FOLD_ALONG) }
        .map { line -> line.split("=").let { FOLD.valueOf(it[0].uppercase()) to it[1].toInt() } }

    private fun Set<Pair<Int, Int>>.fold(fold: Pair<FOLD, Int>) =
        when (fold.first) {
            X -> {
                this.map { fold.second - abs(fold.second - it.first) to it.second }.toSet()
            }
            Y -> {
                this.map { it.first to fold.second - abs(fold.second - it.second) }.toSet()
            }
        }

    private enum class FOLD {
        X, Y
    }
}
