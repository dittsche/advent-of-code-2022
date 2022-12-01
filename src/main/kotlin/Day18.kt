import kotlin.math.roundToInt

object Day18 : PuzzleSolver(18) {

    override fun solve1(input: String) =
        input.lines().map { it.parseSnailFishNumber() }.reduce { acc, curr -> acc + curr }.magnitude

    override fun solve2(input: String) =
        with(input.lines()) { flatMap { first -> map { second -> first to second } } }
            .filterNot { (first, second) -> first == second }
            .map { (first, second) -> first.parseSnailFishNumber() + second.parseSnailFishNumber() }
            .maxOf { it.magnitude }

    private fun String.parseSnailFishNumber(): SnailFishNumber {
        val builders = ArrayDeque<SnailFishNumber.Builder>()
        builders.addLast(SnailFishNumber.Builder())
        for (char in this)
            when (char) {
                '[' -> with(SnailFishNumber.Builder()) {
                    builders.last().left = this
                    builders.addLast(this)
                }
                ',' -> with(SnailFishNumber.Builder()) {
                    builders.removeLast()
                    builders.last().right = this
                    builders.addLast(this)
                }
                ']' -> builders.removeLast()
                else -> builders.last().value = char.digitToInt()
            }

        return builders.first().build()
    }

    private data class SnailFishNumber(
        private var left: SnailFishNumber? = null,
        private var right: SnailFishNumber? = null,
        private var value: Int? = null,
        private var parent: SnailFishNumber? = null,
        private var isLeft: Boolean = false
    ) {

        fun reduce(): SnailFishNumber {
            val pairToExplode = firstWithPairAtLevel(4)
            if (pairToExplode != null) {
                pairToExplode.explode()
                return this.reduce()
            }

            val valueToSplit = firstValueToSplit()
            if (valueToSplit != null) {
                valueToSplit.split()
                return this.reduce()
            }

            return this
        }

        val magnitude: Int get() = if (hasValue) value!! else left!!.magnitude * 3 + right!!.magnitude * 2

        private fun firstWithPairAtLevel(level: Int): SnailFishNumber? =
            if (level == 0)
                if (!hasValue) this else null
            else listOf(left, right).firstNotNullOfOrNull { it?.firstWithPairAtLevel(level - 1) }

        private fun explode() {
            nextNumberWithValueLeft()?.let { it + left?.value!! }
            nextNumberWithValueRight()?.let { it + right?.value!! }
            clear()
        }

        private fun nextNumberWithValueLeft(): SnailFishNumber? =
            when (this) {
                parent?.left -> parent?.nextNumberWithValueLeft()
                parent?.right -> parent?.left?.rightMostNumberWithValue()
                else -> null
            }

        private fun nextNumberWithValueRight(): SnailFishNumber? =
            when (this) {
                parent?.right -> parent?.nextNumberWithValueRight()
                parent?.left -> parent?.right?.leftMostNumberWithValue()
                else -> null
            }

        private fun leftMostNumberWithValue(): SnailFishNumber? =
            if (hasValue) this else left?.leftMostNumberWithValue()

        private fun rightMostNumberWithValue(): SnailFishNumber? =
            if (hasValue) this else right?.rightMostNumberWithValue()

        private fun clear() {
            left = null
            right = null
            value = 0
        }

        private fun firstValueToSplit(): SnailFishNumber? =
            if (hasValue && value!! >= 10)
                this
            else
                listOf(left, right).firstNotNullOfOrNull { it?.firstValueToSplit() }

        private fun split() {
            val (firstValue, secondValue) = value!! / 2 to ((value!! * 1.0) / 2).roundToInt()
            value = null
            left = SnailFishNumber(value = firstValue, parent = this, isLeft = true)
            right = SnailFishNumber(value = secondValue, parent = this, isLeft = true)
        }

        operator fun plus(other: SnailFishNumber) = SnailFishNumber(left = this, right = other)
            .also {
                this.parent = it
                other.parent = it
                isLeft = true
                other.isLeft = false
            }
            .reduce()

        operator fun plus(otherValue: Int) {
            value = value?.plus(otherValue)
        }

        private val hasValue get() = value != null

        override fun toString() = if (hasValue) "$value" else "[$left,$right]"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SnailFishNumber

            if (left != other.left) return false
            if (right != other.right) return false
            if (value != other.value) return false
            if (isLeft != other.isLeft) return false

            return true
        }

        override fun hashCode(): Int {
            var result = left?.hashCode() ?: 0
            result = 31 * result + (right?.hashCode() ?: 0)
            result = 31 * result + (value ?: 0)
            result = 31 * result + isLeft.hashCode()
            return result
        }

        class Builder {
            var left: Builder? = null
            var right: Builder? = null
            var value: Int? = null

            fun build(): SnailFishNumber = SnailFishNumber(left?.build(), right?.build(), value)
                .also {
                    it.left?.apply {
                        parent = it
                        isLeft = true
                    }
                    it.right?.apply {
                        parent = it
                        isLeft = false
                    }
                }
        }
    }
}
