object Day8 : PuzzleSolver(8) {

    override fun solve1(input: String): Number {
        return input.splitToParts()
            .flatMap { it.second }
            .count { it.size in listOf(2, 3, 4, 7) }
    }

    override fun solve2(input: String): Number {
        return input.splitToParts()
            .map { it.toDigitValues() }
            .sumOf { it[0] * 1000 + it[1] * 100 + it[2] * 10 + it[3] }
    }

    private fun String.splitToParts() =
        lines().map { line ->
            line.split(" | ")
                .let { it[0].split(" ").map(String::toSet) to it[1].split(" ").map(String::toSet) }
        }

    private fun Pair<List<Set<Char>>, List<Set<Char>>>.toDigitValues() = with(first.toMutableList()) {
        val one = singleWithSizeAndRemoveFromList(2)
        val four = singleWithSizeAndRemoveFromList(4)
        val seven = singleWithSizeAndRemoveFromList(3)
        val eight = singleWithSizeAndRemoveFromList(7)
        val three = singleWithSizeAndRemoveFromList(5, seven)

        val top = seven.minus(one).single()
        val upperLeft = four.minus(three).single()
        val lowerLeft = eight.minus(three).minus(upperLeft).single()
        val center = four.minus(one).minus(upperLeft).single()

        val nine = singleWithSizeAndRemoveFromList(6, four.plus(top))
        val six = singleWithSizeAndRemoveFromList(6, setOf(center, lowerLeft))
        val zero = singleWithSizeAndRemoveFromList(6)

        val lowerRight = one.intersect(six).single()

        val five = singleWithSizeAndRemoveFromList(5, setOf(upperLeft, lowerRight))
        val two = singleWithSizeAndRemoveFromList(5)

        mapOf(
            zero to 0,
            one to 1,
            two to 2,
            three to 3,
            four to 4,
            five to 5,
            six to 6,
            seven to 7,
            eight to 8,
            nine to 9
        )
    }.let { digitMap -> second.map { digitMap[it] ?: 0 } }

    private fun MutableList<Set<Char>>.singleWithSizeAndRemoveFromList(
        size: Int,
        containing: Set<Char> = emptySet()
    ) = single { it.size == size && it.containsAll(containing) }
        .also { this.remove(it) }
}
