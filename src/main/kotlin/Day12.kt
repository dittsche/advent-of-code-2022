object Day12 : PuzzleSolver(12) {

    override fun solve1(input: String): Number {
        val nodes = input.toNodes()
            .apply { addNeighbours() }
        val startNode = nodes.single { it.value == 'S' }
        val endNode = nodes.single { it.value == 'E' }

        return dijkstra(nodes, startNode)
            .shortestPathTo(endNode)
            .size - 1
    }

    override fun solve2(input: String): Number {
        val nodes = input.toNodes()
            .apply { addNeighbours() }
        val endNode = nodes.single { it.value == 'E' }

        return nodes
            .filter { it.value == 'a' || it.value == 'S' }
            .map { dijkstra(nodes, it) }
            .minOf { it.shortestPathTo(endNode).size - 1 }
    }

    private fun List<Node>.addNeighbours() {
        val nodesByPair = this.associateBy { it.row to it.col }
        forEach { node ->
            listOfNotNull(
                nodesByPair[node.row - 1 to node.col],
                nodesByPair[node.row + 1 to node.col],
                nodesByPair[node.row to node.col - 1],
                nodesByPair[node.row to node.col + 1]
            )
                .filter { node.canReach(it) }
                .let { node.neighbours.addAll(it) }
        }
    }

    private fun Node.canReach(next: Node) =
        value == 'S' ||
                (next.value == 'E' && (value == 'y' || value == 'z')) ||
                (next.value != 'E' && next.value.code <= value.code + 1)

    private fun dijkstra(nodes: List<Node>, startNode: Node): Map<Node, Node?> {
        val distances = nodes.associateWith { Int.MAX_VALUE }.toMutableMap()
        val predecessors: MutableMap<Node, Node?> = nodes.associateWith { null }.toMutableMap()
        val nodesToProcess = nodes.toMutableList()
        distances[startNode] = 0
        while (nodesToProcess.isNotEmpty()) {
            val next = nodesToProcess.minBy { distances[it]!! }
            nodesToProcess.remove(next)
            next.neighbours
                .forEach {
                    if (it in nodesToProcess) {
                        val alternative = distances[next]!! + 1
                        if (alternative < distances[it]!!) {
                            distances[it] = alternative
                            predecessors[it] = next
                        }
                    }
                }
        }
        return predecessors.toMap()
    }

    private fun Map<Node, Node?>.shortestPathTo(endNode: Node): MutableList<Node> {
        val path = mutableListOf(endNode)
        var u = endNode
        while (this[u] != null) {
            u = this[u]!!
            path.add(0, u)
        }
        return path
    }

    private fun String.toNodes() = lines()
        .flatMapIndexed { row, line -> line.toList().mapIndexed { col, char -> Node(row, col, char) } }

    private data class Node(
        val row: Int,
        val col: Int,
        val value: Char
    ) {
        val neighbours: MutableSet<Node> = mutableSetOf()
    }
}
