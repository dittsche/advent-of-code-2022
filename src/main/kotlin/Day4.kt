typealias Elf = IntRange

object Day4 : PuzzleSolver(4) {

    override fun solve1(input: String) = input.lines()
        .map { it.toElvesPair() }
        .count { (first, second) -> first.contains(second) || second.contains(first) }

    override fun solve2(input: String) = input.lines()
        .map { it.toElvesPair() }
        .count { (first, second) -> first.tailOverlapsWith(second) || second.tailOverlapsWith(first) }

    private fun String.toElvesPair(): Pair<Elf, Elf> = split(",")
        .map { elf -> elf.toElf() }
        .let { (first, second) -> first to second }

    private fun String.toElf() = split("-")
        .map { it.toInt() }
        .let { (first, second) -> Elf(first, second) }

    private fun Elf.contains(other: Elf) = this.first <= other.first && this.last >= other.last
    private fun Elf.tailOverlapsWith(other: Elf) = this.first >= other.first && this.first <= other.last
}
