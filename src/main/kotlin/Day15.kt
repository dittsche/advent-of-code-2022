object Day15 : PuzzleSolver(15) {

    override fun solve1(input: String): Number {
        val values = input.lines()
            .map { line -> line.toList().map { it.digitToInt() } }

        return Grid(values).minSumToEnd()
    }

    override fun solve2(input: String): Number {
        val values = input.lines()
            .map { line -> line.toList().map { it.digitToInt() } }
        val newValues = values
            .map { line ->
                (0 until 5).flatMap { factor -> line.map { it.increaseValue(factor) } }
            }
        val extendedGrid = (0 until 5)
            .flatMap { factor ->
                newValues.map { line -> line.map { it.increaseValue(factor) } }
            }

        return Grid(extendedGrid).minSumToEnd()
    }

    private fun Int.increaseValue(factor: Int) = ((this + factor) % 9).takeIf { it > 0 } ?: 9

    private data class Grid(private val values: List<List<Int>>) {
        private val minSums: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

        fun minSumToEnd() = minSumToEnd(0 to 0) - values[0][0]

        private fun minSumToEnd(start: Pair<Int, Int>): Int =
            if (minSums.containsKey(start))
                minSums.getValue(start)
            else {
                with(values[start.first][start.second]) {
                    if (start.first == maxIndex && start.second == maxIndex) {
                        this
                    } else if (start.first == maxIndex) {
                        this + minSumToEnd(start.first to start.second + 1)
                    } else if (start.second == maxIndex) {
                        this + minSumToEnd(start.first + 1 to start.second)
                    } else {
                        minOf(
                            this + minSumToEnd(start.first to start.second + 1),
                            this + minSumToEnd(start.first + 1 to start.second)
                        )
                    }
                }
                    .also { minSums[start] = it }
            }

        private val maxIndex get() = values.size - 1

        override fun toString() =
            values.joinToString(System.lineSeparator()) { line -> line.joinToString("") { it.toString() } }
    }
}
