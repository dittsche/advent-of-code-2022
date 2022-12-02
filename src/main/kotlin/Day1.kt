object Day1 : PuzzleSolver(1) {

    override fun solve1(input: String) = input.toCaloriesSums()
        .max()

    override fun solve2(input: String) = input.toCaloriesSums()
        .sortedDescending()
        .take(3)
        .sum()

    private fun String.toCaloriesSums(): List<Int> =
        split(System.lineSeparator().repeat(2))
            .map { it.toCaloriesSum() }

    private fun String.toCaloriesSum(): Int = lines()
        .map { it.toInt() }
        .sum()
}
