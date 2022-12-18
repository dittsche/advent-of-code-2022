object Day18 : PuzzleSolver(18) {

    override fun solve1(input: String) = input.lines()
        .map { it.toCube() }
        .exposedSides()
        .count()

    override fun solve2(input: String): Number {
        return 0
    }

    private fun List<Cube>.exposedSides() = this.flatMap { it.adjacentCubes() } - this.toSet()

    private fun String.toCube() = splitToInts(",")
        .let { (x, y, z) -> Cube(x, y, z) }

    private data class Cube(val x: Int, val y: Int, val z: Int) {
        fun adjacentCubes() = listOf(
            Cube(x - 1, y, z),
            Cube(x + 1, y, z),
            Cube(x, y - 1, z),
            Cube(x, y + 1, z),
            Cube(x, y, z - 1),
            Cube(x, y, z + 1),
        )
    }
}
