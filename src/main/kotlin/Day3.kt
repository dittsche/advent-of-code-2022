import kotlin.math.pow

object Day3 : PuzzleSolver(3) {

    override fun solve1(input: String): Number {
        val mostCommonBitValues = (input.firstLineLength - 1 downTo 0)
            .map { input.splitToBits().mostCommonBitValueAtPosition(it) }

        val gammaBitValues = mostCommonBitValues.joinToString("") { if (it) "1" else "0" }
        val epsilonBitValues = mostCommonBitValues.joinToString("") { if (it.not()) "1" else "0" }

        val gamma = gammaBitValues.toInt(2)
        val epsilon = epsilonBitValues.toInt(2)

        return gamma * epsilon
    }

    override fun solve2(input: String): Number {
        val bits = input.splitToBits()
        val oxygen = bits.filterByBitValueCriterion(true, input.firstLineLength)
        val co2Scrubber = bits.filterByBitValueCriterion(false, input.firstLineLength)
        return oxygen * co2Scrubber
    }

    private fun String.splitToBits() = lines().map { it.toInt(2) }

    private fun List<Int>.mostCommonBitValueAtPosition(position: Int) =
        filter { it.bitValueAtPosition(position) }.size.toDouble() >= size / 2.0

    private fun List<Int>.filterByBitValueCriterion(mostCommon: Boolean, length: Int): Int {
        var workingList = listOf(*toTypedArray())
        var position = length - 1
        while (workingList.size > 1) {
            workingList = with(workingList.mostCommonBitValueAtPosition(position)) {
                workingList.filterByBitValueAtPosition(this == mostCommon, position--)
            }
        }

        return workingList.single()
    }

    private fun List<Int>.filterByBitValueAtPosition(bitValue: Boolean, position: Int) =
        filter { it.bitValueAtPosition(position) == bitValue }

    private fun Int.bitValueAtPosition(position: Int) = this and position.powerOfTwo() == position.powerOfTwo()

    private fun Int.powerOfTwo() = 2.0.pow(this).toInt()

    private val String.firstLineLength get() = lines().first().length
}
