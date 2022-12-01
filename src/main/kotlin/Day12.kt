object Day12 : PuzzleSolver(12) {

    private const val START_NODE = "start"
    private const val END_NODE = "end"

    override fun solve1(input: String) =
        input.toEdges()
            .let { with(START_NODE) { toPaths(it, listOf(this)) } }
            .size

    override fun solve2(input: String) =
        input.toEdges()
            .let { edges -> edges.keys.flatMap { with(START_NODE) { toPaths(edges, listOf(this), it) } } }
            .toSet()
            .size

    private fun String.toEdges() = lines()
        .map { line -> line.split("-").let { it[0] to it[1] } }
        .flatMap { listOf(it, it.second to it.first) }
        .groupBy({ it.first }) { it.second }

    private fun String.toPaths(
        edges: Map<String, List<String>>,
        visitedNodes: List<String>,
        nodeToVisitTwice: String? = null
    ): List<List<String>> =
        if (this == END_NODE) {
            listOf(listOf(END_NODE))
        } else {
            edges.getOrDefault(this, listOf())
                .filter { it !in visitedNodes }
                .flatMap {
                    it.toPaths(
                        edges,
                        if (it != nodeToVisitTwice && it.isAllLowerCase()) visitedNodes + it else visitedNodes,
                        if (it == nodeToVisitTwice) null else nodeToVisitTwice
                    )
                }
                .map { listOf(this) + it }
        }

    private fun String.isAllLowerCase() = all { char -> char.isLowerCase() }
}
