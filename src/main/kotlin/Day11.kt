import java.lang.System.lineSeparator
import java.math.BigInteger
import java.math.BigInteger.ZERO
import java.util.LinkedList
import java.util.Queue

object Day11 : PuzzleSolver(11) {
    private val startingItemsRegex = " {2}Starting items: (.*)".toRegex()
    private val operationRegex = " {2}Operation: new = old ([+\\-*/]) (old|[0-9]*)".toRegex()
    private val testDivisionRegex = " {2}Test: divisible by ([0-9]*)".toRegex()
    private val testPositiveResultRegex = " {3}If true: throw to monkey ([0-9]*)".toRegex()
    private val testNegativeResultRegex = " {3}If false: throw to monkey ([0-9]*)".toRegex()

    override fun solve1(input: String) = input.toMonkeys()
        .also { monkeys -> repeat(20) { _ -> monkeys.round { it.div(BigInteger.valueOf(3)) } } }
        .map { it.inspectedItemsCount }
        .sortedDescending()
        .take(2)
        .let { (a, b) -> a * b }
        .toInt()

    override fun solve2(input: String) = input.toMonkeys()
        .also { monkeys ->
            monkeys.map { it.testDivisor }.getLcm()
                .let { lcm -> repeat(10000) { monkeys.round { it.mod(lcm) } } }
        }
        .map { it.inspectedItemsCount }.sortedDescending()
        .take(2)
        .let { (a, b) -> a * b }

    private fun String.toMonkeys() = split(lineSeparator().repeat(2))
        .map { it.toMonkey() }

    private fun List<Int>.getLcm() = fold(1) { a, b -> a * b }.toBigInteger()

    private fun List<Monkey>.round(postOperation: (BigInteger) -> BigInteger) =
        forEach { monkey -> repeat(monkey.items.size) { _ -> monkey.turn(this, postOperation) } }

    private fun Monkey.turn(monkeys: List<Monkey>, postOperation: (BigInteger) -> BigInteger) {
        items.remove()
            .let { operation(it) }
            .let { postOperation(it) }
            .let { it to test(it) }
            .let { (newItem, newMonkey) -> monkeys[newMonkey].items.add(newItem) }
            .also { visitedItem() }
    }

    private fun String.toMonkey() = testDivisionRegex.find(this)!!.groupValues[1]
        .let { divisor ->
            Monkey(
                items = toStartingItems(),
                operation = toOperation(),
                test = toTest(divisor.toBigInteger()),
                divisor.toInt()
            )
        }

    private fun String.toStartingItems(): Queue<BigInteger> = startingItemsRegex.find(this)!!
        .groupValues[1]
        .split(", ")
        .map { it.toBigInteger() }
        .let { LinkedList(it) }

    private fun String.toOperation(): (BigInteger) -> BigInteger = operationRegex.find(this)!!.groupValues
        .let { (_, operator, secondOperand) ->
            when (operator) {
                "+" -> { it -> it + secondOperand.toSecondOperandInt(it) }
                "-" -> { it -> it - secondOperand.toSecondOperandInt(it) }
                "*" -> { it -> it * secondOperand.toSecondOperandInt(it) }
                "/" -> { it -> it / secondOperand.toSecondOperandInt(it) }
                else -> throw NotImplementedError()
            }
        }

    private fun String.toSecondOperandInt(first: BigInteger): BigInteger = when (this) {
        "old" -> first
        else -> toBigInteger()
    }

    private fun String.toTest(testDivisor: BigInteger): (BigInteger) -> Int {
        val positiveResult = testPositiveResultRegex.find(this)!!.groupValues[1].toInt()
        val negativeResult = testNegativeResultRegex.find(this)!!.groupValues[1].toInt()
        return { toTest -> if (toTest.mod(testDivisor).equals(ZERO)) positiveResult else negativeResult }
    }

    private data class Monkey(
        val items: Queue<BigInteger>,
        val operation: (BigInteger) -> BigInteger,
        val test: (BigInteger) -> Int,
        val testDivisor: Int
    ) {
        private var _inspectedItemsCount = 0L
        val inspectedItemsCount: Long
            get() = _inspectedItemsCount

        fun visitedItem() {
            _inspectedItemsCount++
        }
    }
}
