object Day20 : PuzzleSolver(20) {

    override fun solve1(input: String): Number {
        val (image, algorithm) = input.toImageAndAlgorithm()

        return image.enhance(2, algorithm).lightPixelCount
    }

    override fun solve2(input: String): Number {
        val (image, algorithm) = input.toImageAndAlgorithm()

        return image.enhance(50, algorithm).lightPixelCount
    }

    private fun String.toImageAndAlgorithm(): Pair<Image, List<Boolean>> {
        val algorithm = lines().first().map { it == '#' }
        val width = lines().drop(2).first().length
        val pixels = lines().drop(2).joinToString("").map { it == '#' }
        return Image(pixels, width) to algorithm
    }

    data class Image(private val pixels: List<Boolean>, private val width: Int) {

        fun enhance(rounds: Int, algorithm: List<Boolean>): Image {
            var newImage = this
            for (round in 0 until rounds) {
                newImage = newImage.enhance((if (round % 2 != 0) algorithm.first() else false), algorithm)
            }
            return newImage
        }

        private fun enhance(infiniteValue: Boolean, algorithm: List<Boolean>) =
            with(extend(infiniteValue)) {
                this.pixels.indices
                    .map { algorithm[this.valueAtIndex(it, infiniteValue)] }
                    .let { Image(it, this.width) }
            }

        val lightPixelCount get() = pixels.count { it }

        private fun extend(infiniteValue: Boolean) =
            (List(width) { infiniteValue } + pixels + List(width) { infiniteValue })
                .chunked(width)
                .flatMap { listOf(infiniteValue) + it + listOf(infiniteValue) }
                .let { Image(it, width + 2) }

        private fun valueAtIndex(index: Int, infiniteValue: Boolean) =
            listOf(
                index.indexUpLeft, index.indexUp, index.indexUpRight,
                index.indexLeft, index, index.indexRight,
                index.indexDownLeft, index.indexDown, index.indexDownRight
            ).map { pixels.getOrElse(it) { infiniteValue }.let { value -> if (value) '1' else '0' } }
                .joinToString("").toInt(2)

        private val Int.indexUpLeft get() = if (this % width != 0) this - width - 1 else -1
        private val Int.indexUp get() = this - width
        private val Int.indexUpRight get() = if (this % width != width - 1) this - width + 1 else -1
        private val Int.indexLeft get() = if (this % width != 0) this - 1 else -1
        private val Int.indexRight get() = if (this % width != width - 1) this + 1 else -1
        private val Int.indexDownLeft get() = if (this % width != 0) this + width - 1 else -1
        private val Int.indexDown get() = this + width
        private val Int.indexDownRight get() = if (this % width != width - 1) this + width + 1 else -1

        override fun toString() =
            pixels.map { if (it) '#' else '.' }
                .chunked(width)
                .joinToString(System.lineSeparator()) { it.joinToString(" ") } + System.lineSeparator()
    }
}
