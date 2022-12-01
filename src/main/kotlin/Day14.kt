object Day14 : PuzzleSolver(14) {

    override fun solve1(input: String): Number {
        val template = input.lines().first()
        val insertionRules = input.toInsertionRules()
        return (0 until 10)
            .fold(template.toList()) { acc, _ -> acc.step(insertionRules) }
            .groupingBy { it }
            .fold(0L) { count, _ -> count + 1 }
            .subtractMinValueFromMaxValue()
    }

    override fun solve2(input: String): Number {
        val template = input.lines().first()
        val insertionRules = input.toInsertionRules()

        val iterations = 40
        val result = mutableMapOf<Char, Long>()
        val stack = ArrayDeque(template.drop(1).map { it to iterations }.reversed())
        func(result, template[0], iterations, stack, insertionRules)

        return result.subtractMinValueFromMaxValue()
    }

    private fun Pair<Char, Char>.insertChar(insertionRules: Map<Pair<Char, Char>, Char>) =
        insertionRules.getValue(this)

    private fun String.toInsertionRules() = lines().drop(2)
        .associate { line -> line.split(" -> ").let { it[0].toPair() to it[1].first() } }

    private fun String.toPair() = this[0] to this[1]

    private fun List<Char>.step(insertionRules: Map<Pair<Char, Char>, Char>) =
        zipWithNext().flatMap { listOf(it.first, it.insertChar(insertionRules)) } + last()

    private fun Map<Char, Long>.subtractMinValueFromMaxValue() = values
        .let { occurrences -> occurrences.maxOf { it } - occurrences.minOf { it } }

    private tailrec fun func(
        acc: MutableMap<Char, Long>,
        curr: Char,
        level: Int,
        stack: ArrayDeque<Pair<Char, Int>>,
        insertionRules: Map<Pair<Char, Char>, Char>
    ) {
        if (stack.isEmpty()) {
            acc.increaseForChar(curr)
        } else {
            if (level == 0) {
                acc.increaseForChar(curr)
                acc.increaseForChar(stack.removeLast().first)
                val last = stack.removeLast()
                func(acc, last.first, last.second, stack, insertionRules)
            } else {
                val newLevel = level - 1
                (curr to stack.last().first)
                    .let { it.insertChar(insertionRules) to newLevel }
                    .let { stack.addLast(it) }
                func(acc, curr, newLevel, stack, insertionRules)
            }
        }
    }

    private fun MutableMap<Char, Long>.increaseForChar(char: Char) = compute(char) { _, count -> (count ?: 0) + 1 }
}
