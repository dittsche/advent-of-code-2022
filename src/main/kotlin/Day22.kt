import Day22.State.OFF
import Day22.State.ON

typealias Cube = Triple<Int, Int, Int>
typealias Range3D = Triple<IntRange, IntRange, IntRange>

object Day22 : PuzzleSolver(22) {

    override fun solve1(input: String) =
        mutableSetOf<Cube>()
            .apply {
                input.lines()
                    .map { it.toRebootStep() }
                    .filter { it.range in Range3D(-50..50, -50..50, -50..50) }
                    .forEach { it.execute(this) }
            }.size

    override fun solve2(input: String) =
        mutableSetOf<Cube>()
            .apply {
                input.lines()
                    .map { it.toRebootStep() }
                    .forEach { it.execute(this) }
            }.size

    private fun String.toRebootStep() =
        split(" ")
            .let { (state, ranges) -> State.valueOf(state.uppercase()) to ranges.toRange3D() }
            .let { (state, ranges) -> RebootStep(state, ranges) }

    private fun String.toRange3D() =
        split(",")
            .map { it.drop(2) }
            .map { it.split("..").let { (start, end) -> IntRange(start.toInt(), end.toInt()) } }
            .let { (xRange, yRange, zRange) -> Range3D(xRange, yRange, zRange) }

    private fun RebootStep.execute(cubes: MutableSet<Cube>) {
        when (state) {
            ON -> cubes.addAll(range.containedCubes)
            OFF -> cubes.removeAll(range.containedCubes)
        }
    }

    private data class RebootStep(val state: State, val range: Range3D)

    private enum class State { ON, OFF }

    private operator fun Range3D.contains(other: Range3D) =
        other.first.first in this.first && other.first.last in this.first &&
            other.second.first in this.second && other.second.last in this.second &&
            other.third.first in this.third && other.third.last in this.third

    private val Range3D.containedCubes
        get() = (this.first.first..this.first.last)
            .flatMap { x -> (this.second.first..this.second.last).map { y -> x to y } }
            .flatMap { (x, y) -> (this.third.first..this.third.last).map { z -> Cube(x, y, z) } }
            .toSet()
}
