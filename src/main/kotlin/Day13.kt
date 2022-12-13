import java.lang.System.lineSeparator
import kotlin.math.min

object Day13 : PuzzleSolver(13) {

    override fun solve1(input: String) = input.split(lineSeparator().repeat(2))
        .mapIndexed { index, pair -> index to pair.toPacketPair() }
        .filter { (_, pair) -> pair.first < pair.second }
        .sumOf { (index, _) -> index + 1 }

    override fun solve2(input: String): Number {
        val wrapped2 = 2.toPacket().wrapPacket()
        val wrapped6 = 6.toPacket().wrapPacket()
        return input.lines()
            .filter { it.isNotBlank() }
            .map { it.toPacket() }
            .toMutableList()
            .apply {
                add(wrapped2)
                add(wrapped6)
            }
            .sorted()
            .let { (it.indexOf(wrapped2) + 1) * (it.indexOf(wrapped6) + 1) }
    }

    private fun String.toPacketPair() = lines()[0].toPacket() to lines()[1].toPacket()

    private fun String.toPacket() = PacketParser.parsePacket(this)

    private data class Packet(val int: Int?, val list: MutableList<Packet>?) : Comparable<Packet> {
        override fun compareTo(other: Packet): Int = when {
            int != null && other.int != null -> int - other.int
            list != null && other.list != null -> (0 until min(list.size, other.list.size))
                .asSequence()
                .map { list[it].compareTo(other.list[it]) }
                .find { it != 0 }
                ?: (list.size - other.list.size)

            list == null && other.int == null -> Packet(null, mutableListOf(this)).compareTo(other)
            list != null && other.int != null -> this.compareTo(Packet(null, mutableListOf(other)))
            else -> 0
        }
    }

    private fun Int.toPacket() = Packet(this, null)
    private fun Packet.wrapPacket() = Packet(null, mutableListOf(this))

    private object PacketParser {
        fun parsePacket(input: String): Packet {
            val packets = mutableListOf<Packet>()
            var lastPacket: Packet? = null
            var numberCharBuffer = ""

            val addNumberFromBuffer = {
                if (numberCharBuffer.isNotEmpty()) {
                    packets.last().list?.add(numberCharBuffer.toInt().toPacket())
                    numberCharBuffer = ""
                }
            }

            input.forEach {
                when {
                    it == '[' -> with(Packet(null, mutableListOf())) {
                        packets.lastOrNull()?.list?.add(this)
                        packets.add(this)
                    }

                    it == ']' -> {
                        addNumberFromBuffer()
                        lastPacket = packets.removeLast()
                    }

                    it == ',' -> addNumberFromBuffer()
                    it.isDigit() -> numberCharBuffer += it
                }
            }
            return lastPacket!!
        }
    }
}
