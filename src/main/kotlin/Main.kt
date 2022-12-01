import kotlin.system.measureTimeMillis

fun main() {
    println("Hello Advent of Code 2021!")

    listOf(
        Day1,
        Day2,
        Day3,
        Day4,
//        Day5,
//        Day6,
        Day7,
        Day8,
        Day9,
        Day10,
        Day11,
        Day12,
        Day13,
//      Day14,
        Day15,
        Day16,
        Day17,
        Day18,
//        Day19,
        Day20,
        Day21,
        Day22,
//        Day23,
//        Day24,
//        Day25,
    ).forEach { solver ->
        println("Advent of Code 2021 - Day ${solver.day}")
        measureTimeMillis {
            print("Puzzle 1: ${solver.solve1(readInput(solver.day))}")
        }.let { println(" in $it ms") }
        measureTimeMillis {
            print("Puzzle 2: ${solver.solve2(readInput(solver.day))}")
        }.let { println(" in $it ms") }

        println("-".repeat(30))
    }
}
