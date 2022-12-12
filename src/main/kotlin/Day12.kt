import java.util.LinkedList

object Day12 : PuzzleSolver(12) {

    override fun solve1(input: String): Number {
        val nodes = input.toNodes()
            .apply { addNeighbours() }
        val startNode = nodes.single { it.value == 'S' }
        val endNode = nodes.single { it.value == 'E' }

        return startNode.bfs(endNode)!!
    }

    override fun solve2(input: String): Number {
        val nodes = input.toNodes()
            .apply { addNeighbours() }
        val endNode = nodes.single { it.value == 'E' }

        return nodes
            .filter { it.value == 'a' || it.value == 'S' }
            .mapNotNull { startNode -> startNode.bfs(endNode) }
            .minOf { it }
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

    private fun Node.bfs(endNode: Node): Int? {
        val queue = LinkedList(listOf(this))
        val visited = mutableSetOf(this)
        val levels = mutableMapOf(this to 0)
        while (queue.isNotEmpty()) {
            val node = queue.remove()
            if (node == endNode) {
                return levels[endNode]
            }
            node.neighbours.forEach { neighbour ->
                if (!visited.contains(neighbour)) {
                    queue.add(neighbour)
                    visited.add(neighbour)
                    levels[neighbour] = levels[node]!! + 1
                }
            }
        }
        return null
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
