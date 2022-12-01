object Day1 : PuzzleSolver(1) {

    override fun solve1(input: String) = input.toAllCaloriesSum()
        .max()

    override fun solve2(input: String) = input.toAllCaloriesSum()
        .sortedDescending()
        .take(3)
        .sum()

    private fun String.toAllCaloriesSum(): List<Int> =
        split(System.lineSeparator().repeat(2))
            .map { it.toCaloriesSum() }

    private fun String.toCaloriesSum(): Int = lines()
        .map { it.toInt() }
        .sum()
}
