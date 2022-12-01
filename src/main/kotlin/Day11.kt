object Day11 : PuzzleSolver(11) {

    override fun solve1(input: String): Number {
        val width = input.lines().first().length
        val energyLevels = input.lines().flatMap { line -> line.toList().map { it.digitToInt() } }.toMutableList()

        val grid = OctopusGrid(energyLevels, width)

        return (0 until 100).sumOf { grid.tic() }
    }

    override fun solve2(input: String): Number {
        val width = input.lines().first().length
        val energyLevels = input.lines().flatMap { line -> line.toList().map { it.digitToInt() } }.toMutableList()

        val grid = OctopusGrid(energyLevels, width)

        return generateSequence(1) { it + 1 }.first { grid.tic() == energyLevels.size }
    }

    data class OctopusGrid(private val energyLevels: MutableList<Int>, private val width: Int) {

        fun tic(): Int {
            energyLevels.indices.forEach { energyLevels[it]++ }
            while (energyLevels.any { it > 9 }) {
                energyLevels.indices
                    .filter { energyLevels[it] > 9 }
                    .forEach { flash(it) }
            }
            return energyLevels.indices
                .filter { energyLevels[it] < 0 }
                .onEach { energyLevels[it] = 0 }
                .count()
        }

        private fun flash(index: Int) {
            increaseAtIndex(index.indexUpLeft)
            increaseAtIndex(index.indexUp)
            increaseAtIndex(index.indexUpRight)
            increaseAtIndex(index.indexLeft)
            increaseAtIndex(index.indexRight)
            increaseAtIndex(index.indexDownLeft)
            increaseAtIndex(index.indexDown)
            increaseAtIndex(index.indexDownRight)

            energyLevels[index] = Int.MIN_VALUE
        }

        private fun increaseAtIndex(index: Int) {
            energyLevels.getOrNull(index)?.let { energyLevels.set(index, it + 1) }
        }

        private val Int.indexUpLeft get() = if (this % width != 0) this - width - 1 else -1
        private val Int.indexUp get() = this - width
        private val Int.indexUpRight get() = if (this % width != width - 1) this - width + 1 else -1
        private val Int.indexLeft get() = if (this % width != 0) this - 1 else -1
        private val Int.indexRight get() = if (this % width != width - 1) this + 1 else -1
        private val Int.indexDownLeft get() = if (this % width != 0) this + width - 1 else -1
        private val Int.indexDown get() = this + width
        private val Int.indexDownRight get() = if (this % width != width - 1) this + width + 1 else -1
    }
}
