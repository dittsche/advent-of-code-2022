object Day4 : PuzzleSolver(4) {

    override fun solve1(input: String): Number {
        val lines = input.lines()
        val numbers = lines[0].splitToInts()
        val boards = lines.drop(1).filterNot { it.isEmpty() }.chunked(5) { it.toBoard() }

        var winningBoard: Board? = null
        val winningNumber =
            numbers.dropWhile { number ->
                boards.onEach { it.mark(number) }
                    .filter { it.wins }
                    .also { if (it.isNotEmpty()) winningBoard = it.single() }
                    .isEmpty()
            }.first()

        return winningBoard!!.score * winningNumber
    }

    override fun solve2(input: String): Number {
        val lines = input.lines()
        val numbers = lines[0].splitToInts()
        val boards = lines.drop(1).filterNot { it.isEmpty() }.chunked(5) { it.toBoard() }

        var lastWinningBoard: Board? = null
        val lastWinningNumber =
            numbers.dropWhile { number ->
                boards.onEach { it.mark(number) }
                    .filterNot { it.wins }
                    .also { if (it.size == 1) lastWinningBoard = it.single() }
                    .isNotEmpty()
            }.first()

        return lastWinningBoard!!.score * lastWinningNumber
    }

    private fun List<String>.toBoard() = Board(map { it.toFields() })

    private fun String.toFields() = split(" ")
        .filterNot { it.isBlank() }
        .map { Field(it.toInt()) }

    private data class Board(val fields: List<List<Field>>) {

        fun mark(number: Int) =
            fields.forEach { it.filter { field -> field.number == number }.forEach { field -> field.marked = true } }

        val score get() = fields.sumOf { it.filterNot { field -> field.marked }.sumOf { field -> field.number } }

        val wins
            get() = fields.indices.any { winsRow(it) } || fields.indices.any { winsColumn(it) } ||
                winsFirstDiagonal || winsSecondDiagonal

        private fun winsRow(row: Int) = fields[row].all { it.marked }
        private fun winsColumn(column: Int) = fields.all { it[column].marked }
        private val winsFirstDiagonal = fields.indices.all { fields[it][it].marked }
        private val winsSecondDiagonal = fields.indices.all { fields[fields.size - 1 - it][it].marked }
    }

    private data class Field(val number: Int, var marked: Boolean = false)
}
