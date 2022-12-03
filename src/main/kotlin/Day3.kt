typealias ItemType = Char

object Day3 : PuzzleSolver(3) {

    override fun solve1(input: String) = input.lines()
        .map { it.toRucksack() }
        .sumOf { it.errorItemType.priority }

    override fun solve2(input: String) = input.lines()
        .map { it.toRucksack().contents }
        .windowed(3, 3)
        .map { (first, second, third) -> first.intersect(second).intersect(third).single() }
        .sumOf { it.priority }

    private fun String.toRucksack() =
        let { it.substring(0, it.length / 2) to it.substring(it.length / 2) }
            .let { (first, second) -> Compartment(first.toSet()) to Compartment(second.toSet()) }
            .let { (first, second) -> Rucksack(first, second) }

    private val ItemType.priority
        get() = code - if (isLowerCase()) 96 else 38

    private data class Rucksack(private val firstCompartment: Compartment, private val secondCompartment: Compartment) {
        val errorItemType = firstCompartment.contents.intersect(secondCompartment.contents).single()
        val contents = firstCompartment.contents + secondCompartment.contents
    }

    private data class Compartment(val contents: Set<ItemType>)
}
