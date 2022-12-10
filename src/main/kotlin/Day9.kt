import Day9.Direction.*
import kotlin.math.abs

object Day9 : PuzzleSolver(9) {

    override fun solve1(input: String): Number {
        val grid = Grid()
        input.lines()
            .map { it.split(" ") }
            .flatMap { (heading, count) -> List(count.toInt()) { heading[0].toDirection() } }
            .forEach { grid.move(it) }

        return grid.visitedTailPositions.count()
    }

    override fun solve2(input: String): Number {
        val grid = Grid(9)
        input.lines()
            .map { it.split(" ") }
            .flatMap { (heading, count) -> List(count.toInt()) { heading[0].toDirection() } }
            .forEach { grid.move(it) }

        return grid.visitedTailPositions.count()
    }

    private class Grid(tailCount: Int = 1) {
        private val head = Position(0, 0)
        private val tails = List(tailCount) { Position(0, 0) }
        val visitedTailPositions = mutableSetOf(tails.last().copy())

        fun move(direction: Direction) {
            moveHead(direction)
            tails.forEachIndexed { index, tail -> tail.moveTail(if (index == 0) head else tails[index - 1]) }

            visitedTailPositions.add(tails.last().copy())
        }

        private fun moveHead(direction: Direction) {
            when (direction) {
                UP -> head.y++
                RIGHT -> head.x++
                DOWN -> head.y--
                LEFT -> head.x--
            }
        }

        private fun Position.moveTail(predecessor: Position) {
            val distance = predecessor.distance(this)
            when {
                (distance.x == 0 && distance.y == 2) -> moveTailBy(0, -1)
                (distance.x == 0 && distance.y == -2) -> moveTailBy(0, 1)
                (distance.x == 2 && distance.y == 0) -> moveTailBy(-1, 0)
                (distance.x == -2 && distance.y == 0) -> moveTailBy(1, 0)
                (abs(distance.x) + abs(distance.y) < 3) -> {}
                (distance.x > 0 && distance.y > 0) -> moveTailBy(-1, -1)
                (distance.x > 0 && distance.y < 0) -> moveTailBy(-1, 1)
                (distance.x < 0 && distance.y > 0) -> moveTailBy(1, -1)
                (distance.x < 0 && distance.y < 0) -> moveTailBy(1, 1)
            }
        }

        private fun Position.moveTailBy(x: Int, y: Int) {
            this.x += x
            this.y += y
        }

        override fun toString(): String {
            val maxX = (visitedTailPositions + head + tails).maxOf { it.x }
            val maxY = (visitedTailPositions + head + tails).maxOf { it.y }

            return (0..maxY).joinToString(System.lineSeparator()) { y ->
                (0..maxX)
                    .joinToString("") { x ->
                        when (val current = Position(x, maxY - y)) {
                            head -> "H"
                            in tails -> (tails.indexOf(current) + 1).toString()
                            //     in visitedTailPositions -> "#"
                            else -> "."
                        }
                    }
            }
        }
    }

    private enum class Direction(val heading: Char) {
        UP('U'), RIGHT('R'), DOWN('D'), LEFT('L');
    }

    private data class Position(var x: Int, var y: Int) {
        fun distance(other: Position) = Position(other.x - x, other.y - y)
    }

    private fun Char.toDirection() = Direction.values().single { it.heading == this }
}
