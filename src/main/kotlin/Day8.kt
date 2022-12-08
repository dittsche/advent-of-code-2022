object Day8 : PuzzleSolver(8) {

    override fun solve1(input: String) = input.toTreeGrid().visibleTrees.count()

    override fun solve2(input: String) = input.toTreeGrid().mostScenicTreeScore

    private fun String.toTreeGrid() = lines()
        .map { line -> line.map { it.digitToInt() } }
        .let { TreeGrid(it) }

    private data class TreeGrid(private val trees: List<List<Int>>) {
        val visibleTrees: List<TreeWithCoords>
            get() = trees.flatMapIndexed { row, treesInRow ->
                treesInRow.mapIndexed { col, height -> TreeWithCoords(row, col, height) }
                    .filter { it.visible }
            }

        val mostScenicTreeScore: Int
            get() = trees.flatMapIndexed { row, treesInRow ->
                treesInRow.mapIndexed { col, height -> TreeWithCoords(row, col, height) }
            }
                .maxBy { it.scenicScore }
                .scenicScore

        private val TreeWithCoords.visible
            get() = visibleFromTop || visibleFromRight || visibleFromBottom || visibleFromLeft
        private val TreeWithCoords.visibleFromTop get() = treesUp.all { it < height }
        private val TreeWithCoords.visibleFromRight get() = treesRight.all { it < height }
        private val TreeWithCoords.visibleFromBottom get() = treesDown.all { it < height }
        private val TreeWithCoords.visibleFromLeft get() = treesLeft.all { it < height }

        private val TreeWithCoords.scenicScore
            get() = scenicScoreUp * scenicScoreRight * scenicScoreDown * scenicScoreLeft

        private val TreeWithCoords.scenicScoreUp
            get() = treesUp.takeLastWhile { it < height }
                .let { it.count() + (if (it.count() != row) 1 else 0) }
        private val TreeWithCoords.scenicScoreRight
            get() = treesRight.takeWhile { it < height }
                .let { it.count() + (if (it.count() != trees[row].size - col - 1) 1 else 0) }
        private val TreeWithCoords.scenicScoreDown
            get() = treesDown.takeWhile { it < height }
                .let { it.count() + (if (it.count() != trees.size - row - 1) 1 else 0) }
        private val TreeWithCoords.scenicScoreLeft
            get() = treesLeft.takeLastWhile { it < height }
                .let { it.count() + (if (it.count() != col) 1 else 0) }

        private val TreeWithCoords.treesUp get() = trees.take(row).map { it[col] }
        private val TreeWithCoords.treesRight get() = trees[row].drop(col + 1)
        private val TreeWithCoords.treesDown get() = trees.drop(row + 1).map { it[col] }
        private val TreeWithCoords.treesLeft get() = trees[row].take(col)
    }

    private data class TreeWithCoords(val row: Int, val col: Int, val height: Int)
}
