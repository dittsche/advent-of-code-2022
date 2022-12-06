object Day6 : PuzzleSolver(6) {

    override fun solve1(input: String) = input.findStartOfWithLength(4)

    override fun solve2(input: String) = input.findStartOfWithLength(14)

    private fun String.findStartOfWithLength(length: Int) = windowed(length)
        .first { it.toList().distinct().size == length }
        .let { indexOf(it) + length }
}
