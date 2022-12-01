object Day9 : PuzzleSolver(9) {

    override fun solve1(input: String): Number {
        val width = input.lines().first().length
        val heights = input.lines().flatMap { line -> line.toList().map { it.digitToInt() } }

        return HeightMap(heights, width).lowPoints.sumOf { it + 1 }
    }

    override fun solve2(input: String): Number {
        val width = input.lines().first().length
        val heights = input.lines().flatMap { line -> line.toList().map { it.digitToInt() } }

        return HeightMap(heights, width).basins
            .map { it.size }
            .sorted()
            .takeLast(3)
            .fold(1) { a, b -> a * b }
    }

    private data class HeightMap(private val heights: List<Int>, private val width: Int) {

        val lowPoints get() = heights.filterIndexed { index, _ -> hasLowPointIn(index) }
        val basins get() = heights.indices.filter { hasLowPointIn(it) }.map { it.computeBasin(mutableSetOf()) }

        fun hasLowPointIn(index: Int) =
            with(heights[index]) {
                this != 9 &&
                    index.heightUp > this &&
                    index.heightDown > this &&
                    index.heightLeft > this &&
                    index.heightRight > this
            }

        private fun Int.computeBasin(indicesInBasin: MutableSet<Int>): MutableSet<Int> {
            if (heights.getOrElse(this) { 9 } != 9 && this !in indicesInBasin) {
                indicesInBasin.add(this)
                indicesInBasin.addAll(indexUp.computeBasin(indicesInBasin))
                indicesInBasin.addAll(indexDown.computeBasin(indicesInBasin))
                indicesInBasin.addAll(indexLeft.computeBasin(indicesInBasin))
                indicesInBasin.addAll(indexRight.computeBasin(indicesInBasin))
            }
            return indicesInBasin
        }

        private val Int.indexUp get() = this - width
        private val Int.indexDown get() = this + width
        private val Int.indexLeft get() = if (this % width != 0) this - 1 else -1
        private val Int.indexRight get() = if (this % width != width - 1) this + 1 else -1

        private val Int.heightUp get() = heights.getOrElse(indexUp) { 9 }
        private val Int.heightDown get() = heights.getOrElse(indexDown) { 9 }
        private val Int.heightLeft get() = heights.getOrElse(indexLeft) { 9 }
        private val Int.heightRight get() = heights.getOrElse(indexRight) { 9 }
    }
}
