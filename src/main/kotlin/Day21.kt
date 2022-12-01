object Day21 : PuzzleSolver(21) {

    override fun solve1(input: String): Number {
        val playersWithPositions = input.lines().map { it.toPlayer() }
        val players = playersWithPositions.map { it.first }

        val game = Game(players)
        game.play(1000, playersWithPositions.toMap().toMutableMap())

        return players.minOf { it.score } * game.rollCount
    }

    override fun solve2(input: String): Number {
        return 0
    }

    private fun String.toPlayer() =
        split(" starting position: ")
            .let { (name, position) -> Player(name) to position.toInt() }

    private data class Player(val name: String) {
        var score: Int = 0
    }

    private data class Game(
        val players: List<Player>,
        private val positions: MutableMap<Player, Int> = mutableMapOf(),
        private var currentPlayerIndex: Int = 0,
        private val die: Die = Die()
    ) {
        fun play(scoreToWin: Int, initialPositions: MutableMap<Player, Int>) {
            positions.putAll(initialPositions)

            while (players.none { it.score >= scoreToWin }) {
                val currentPlayer = players[currentPlayerIndex]

                currentPlayer.score += currentPlayer.move(die.rollThrice().sum())

                currentPlayerIndex = (currentPlayerIndex + 1) % players.size
            }
        }

        private fun Player.move(fields: Int): Int {
            val currentPosition = positions[this]!!
            val newPosition = ((currentPosition + fields - 1) % 10 + 1)
            positions[this] = newPosition
            return newPosition
        }

        val rollCount get() = die.rollCount
    }

    private data class Die(var rollCount: Int = 0) {
        fun rollThrice() = Triple(nextValue(), nextValue(), nextValue())

        private fun nextValue() = rollCount++ % 100 + 1
    }

    private fun Triple<Int, Int, Int>.sum() = first + second + third
}
