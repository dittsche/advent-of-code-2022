typealias Crate = ArrayDeque<Char>

private val moveRegex = "move (\\d*) from (\\d*) to (\\d*)".toRegex()

object Day5 : PuzzleSolver(5) {

    override fun solve1(input: String) =
        input.toCrates()
            .apply { input.toMoves().forEach { it.executeOnPuzzle1(this) } }
            .topElements

    override fun solve2(input: String) =
        input.toCrates()
            .apply { input.toMoves().forEach { it.executeOnPuzzle2(this) } }
            .topElements

    private fun String.toCrates(): List<Crate> {
        val cratesInput = lines().takeWhile { it.isNotBlank() }
        val cratesCount = cratesInput.last().last().digitToInt()

        return List<Crate>(cratesCount) { ArrayDeque() }
            .apply {
                cratesInput
                    .take(cratesInput.size - 1)
                    .forEach { line ->
                        line.windowed(4, 4, partialWindows = true)
                            .mapIndexed { i, it -> i to it[1] }
                            .filter { (_, it) -> it != ' ' }
                            .forEach { (i, char) -> this[i].addFirst(char) }
                    }
            }
    }

    private fun String.toMoves() =
        lines().asSequence()
            .dropWhile { it.isNotBlank() }
            .filter { it.isNotBlank() }
            .mapNotNull { moveRegex.find(it)?.groups }
            .map { it.toMoveValues() }
            .toList()

    private fun MatchGroupCollection.toMoveValues() = Move(
        this[1]?.value?.toInt()!!,
        this[2]?.value?.toInt()!! - 1,
        this[3]?.value?.toInt()!! - 1
    )

    private val List<Crate>.topElements
        get() = map { it.last() }
            .joinToString("")

    private data class Move(private val quantity: Int, private val crateFrom: Int, private val crateTo: Int) {
        fun executeOnPuzzle1(crates: List<Crate>) {
            repeat(quantity) { crates[crateTo].addLast(crates[crateFrom].removeLast()) }
        }

        fun executeOnPuzzle2(crates: List<Crate>) {
            (1..quantity)
                .map { crates[crateFrom].removeLast() }
                .reversed().toList()
                .let { crates[crateTo].addAll(it) }
        }
    }
}
