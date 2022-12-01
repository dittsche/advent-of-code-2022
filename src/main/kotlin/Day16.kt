import Day16.PacketType.EQ
import Day16.PacketType.GT
import Day16.PacketType.LITERAL
import Day16.PacketType.LT
import Day16.PacketType.MAX
import Day16.PacketType.MIN
import Day16.PacketType.PRODUCT
import Day16.PacketType.SUM

object Day16 : PuzzleSolver(16) {

    override fun solve1(input: String): Number {
        return input.toBinary().toPacket().first.sumVersions()
    }

    override fun solve2(input: String): Number {
        return input.toBinary().toPacket().first.value
    }

    private fun String.toBinary() =
        map {
            it.digitToInt(16).toString(2).padStart(4, '0')
        }.joinToString("") { it }

    private const val LITERAL_LENGTH = 5

    private fun String.toPacket(): Pair<Packet, String> {
        val version = extractVersion()
        val type = extractType()
        val remainder = drop(6)
        return when (type) {
            LITERAL -> remainder.parseLiteral()
                .let { (literalGroups, value) ->
                    Literal(version, value) to remainder.drop(LITERAL_LENGTH * literalGroups.count())
                }

            else -> remainder.parseSubPackets()
                .let { (subPackets, remainderAfterSubPackets) ->
                    Operator(version, type, subPackets) to remainderAfterSubPackets
                }
        }
    }

    private fun String.parseLiteral(): Pair<List<String>, Long> {
        val literalGroups = with(chunked(LITERAL_LENGTH)) {
            takeWhile { it[0] == '1' }
                .let { it + this[it.count()] }
                .map { it.drop(1) }
        }
        val value = literalGroups.joinToString("") { it }.toLong(2)
        return Pair(literalGroups, value)
    }

    private fun String.parseSubPackets() =
        if (this[0] == '0') parseSubPacketsWithTotalLength() else parseSubPacketsWithTotalNumber()

    private fun String.parseSubPacketsWithTotalLength(): Pair<List<Packet>, String> {
        val subPacketsBits = drop(1).take(15).toInt(2)
        var subPacketsString = drop(16).take(subPacketsBits)

        val packets = mutableListOf<Packet>()
        while (subPacketsString.isNotEmpty()) {
            val (packet, remainder) = subPacketsString.toPacket()
            packets.add(packet)
            subPacketsString = remainder
        }
        return packets to drop(16 + subPacketsBits)
    }

    private fun String.parseSubPacketsWithTotalNumber(): Pair<MutableList<Packet>, String> {
        val subPacketsCount = drop(1).take(11).toInt(2)
        var subPacketsString = drop(12)

        val packets = mutableListOf<Packet>()
        for (i in 0 until subPacketsCount) {
            val (packet, remainder) = subPacketsString.toPacket()
            packets.add(packet)
            subPacketsString = remainder
        }

        return packets to subPacketsString
    }

    private abstract class Packet(open val version: Int, open val type: PacketType?) {
        abstract fun sumVersions(): Int
        abstract val value: Long
    }

    private data class Literal(override val version: Int, override val value: Long) : Packet(version, LITERAL) {
        override fun sumVersions() = version
    }

    private data class Operator(
        override val version: Int,
        override val type: PacketType,
        val subPackets: List<Packet>
    ) : Packet(version, type) {
        override fun sumVersions(): Int = version + subPackets.sumOf { it.sumVersions() }
        override val value: Long
            get() = when (type) {
                SUM -> subPackets.sumOf { it.value }
                PRODUCT -> subPackets.fold(1) { acc, curr -> acc * curr.value }
                MIN -> subPackets.minOf { it.value }
                MAX -> subPackets.maxOf { it.value }
                LITERAL -> value
                GT -> if (subPackets[0].value > subPackets[1].value) 1 else 0
                LT -> if (subPackets[0].value < subPackets[1].value) 1 else 0
                EQ -> if (subPackets[0].value == subPackets[1].value) 1 else 0
            }
    }

    private enum class PacketType(val id: Int) {
        SUM(0),
        PRODUCT(1),
        MIN(2),
        MAX(3),
        LITERAL(4),
        GT(5),
        LT(6),
        EQ(7)
    }

    private fun String.extractVersion() = take(3).toInt(2)
    private fun String.extractType() = drop(3).take(3).toPacketType()
    private fun String.toPacketType() = PacketType.values().single { it.id == this.toInt(2) }
}
