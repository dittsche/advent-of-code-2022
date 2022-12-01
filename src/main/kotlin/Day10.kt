object Day10 : PuzzleSolver(10) {

    override fun solve1(input: String) = input.lines()
        .map { it.toIncorrectCharacter() }
        .sumOf { (incorrectChar, _) -> incorrectChar.score1 }

    override fun solve2(input: String) = input.lines()
        .asSequence()
        .map { it.toIncorrectCharacter() }
        .filter { (incorrectChar, _) -> incorrectChar == null }
        .map { (_, stack) -> stack.foldRight(0L) { curr, acc -> acc * 5 + curr.score2 } }
        .sorted()
        .toList()
        .let { it[it.size / 2] }

    private fun String.toIncorrectCharacter(): Pair<Char?, ArrayDeque<Char>> {
        val stack = ArrayDeque<Char>()

        fun checkLastAndRemove(toCheck: Char) = if (stack.lastOrNull() == toCheck) {
            stack.removeLast()
            true
        } else {
            false
        }

        return this.toList()
            .dropWhile {
                when (it) {
                    '(', '[', '{', '<' -> stack.add(it)
                    ')' -> checkLastAndRemove('(')
                    ']' -> checkLastAndRemove('[')
                    '}' -> checkLastAndRemove('{')
                    '>' -> checkLastAndRemove('<')
                    else -> true
                }
            }
            .firstOrNull() to stack
    }

    private val Char?.score1
        get() = when (this) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }

    private val Char.score2
        get() = when (this) {
            '(' -> 1
            '[' -> 2
            '{' -> 3
            '<' -> 4
            else -> 0
        }
}
