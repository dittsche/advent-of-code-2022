import kotlin.math.abs

typealias Beacon = Triple<Int, Int, Int>
typealias Rotation = (Beacon) -> Beacon
typealias Translation = Triple<Int, Int, Int>
typealias Transformation = Pair<Rotation, Translation>

object Day19 : PuzzleSolver(19) {

    private val ROTATIONS = listOf<Rotation>(
        { Beacon(-it.first, -it.second, it.third) },
        { Beacon(-it.first, -it.third, -it.second) },
        { Beacon(-it.first, it.second, -it.third) },
        { Beacon(-it.first, it.third, it.second) },
        { Beacon(-it.second, -it.first, -it.third) },
        { Beacon(-it.second, -it.third, it.first) },
        { Beacon(-it.second, it.first, it.third) },
        { Beacon(-it.second, it.third, -it.first) },
        { Beacon(-it.third, -it.first, it.second) },
        { Beacon(-it.third, -it.second, -it.first) },
        { Beacon(-it.third, it.first, -it.second) },
        { Beacon(-it.third, it.second, it.first) },
        { Beacon(it.first, -it.second, -it.third) },
        { Beacon(it.first, -it.third, it.second) },
        { Beacon(it.first, it.second, it.third) },
        { Beacon(it.first, it.third, -it.second) },
        { Beacon(it.second, -it.first, it.third) },
        { Beacon(it.second, -it.third, -it.first) },
        { Beacon(it.second, it.first, -it.third) },
        { Beacon(it.second, it.third, it.first) },
        { Beacon(it.third, -it.first, -it.second) },
        { Beacon(it.third, -it.second, it.first) },
        { Beacon(it.third, it.first, it.second) },
        { Beacon(it.third, it.second, -it.first) },
    )

    override fun solve1(input: String): Number {
        val scanners = input.parseScanners().toMutableList()
        var mergedScanner = scanners.first()
        scanners.remove(mergedScanner)
        while (scanners.isNotEmpty()) {
            val (matchingScanner, transformation) = scanners.asSequence()
                .firstWithAtLeast12MatchingBeacons(mergedScanner)
                .let { (scanner, matchingBeacon) -> scanner to matchingBeacon.findTransformation() }
            println("${transformation.second}")
            mergedScanner = mergedScanner.merge(matchingScanner, transformation)
            scanners.remove(matchingScanner)
        }

        return mergedScanner.beacons.size
    }

    override fun solve2(input: String): Number {
        val scanners = input.parseScanners().toMutableList()
        var mergedScanner = scanners.first()
        scanners.remove(mergedScanner)
        val scannerPositions = mutableListOf(Translation(0, 0, 0))
        while (scanners.isNotEmpty()) {
            val (matchingScanner, transformation) = scanners.asSequence()
                .firstWithAtLeast12MatchingBeacons(mergedScanner)
                .let { (scanner, matchingBeacon) -> scanner to matchingBeacon.findTransformation() }
            println("${transformation.second}")
            mergedScanner = mergedScanner.merge(matchingScanner, transformation)
            scannerPositions.add(transformation.second)
            scanners.remove(matchingScanner)
        }

        return scannerPositions.flatMap { first -> scannerPositions.map { first to it } }
            .maxOf { (first, second) -> Distance(first, second).distance.value.sum() }
    }

    private fun Sequence<Scanner>.firstWithAtLeast12MatchingBeacons(other: Scanner) =
        associateWithMatchingDistances(other)
            .mapValues { (scanner, commonDistances) -> scanner.beacons.associateWithMatchingBeacon(commonDistances) }
            .toList()
            .first { (_, matchingBeacons) -> matchingBeacons.size >= 12 }

    private fun Sequence<Scanner>.associateWithMatchingDistances(other: Scanner) =
        associateWith { scanner ->
            scanner.beaconDistances.value
                .associateWith { distance ->
                    other.beaconDistances.value
                        .firstOrNull { it.distance.value == distance.distance.value }
                }
                .filterValues { it != null }
                .mapValues { (_, second) -> second!! }
        }

    private fun Set<Beacon>.associateWithMatchingBeacon(distances: Map<Distance, Distance>) =
        associateWith { beacon ->
            distances.getMatchingDistancesForBeacon(beacon).flatMap { listOf(it.start, it.end) }.mostFrequentElement()
        }
            .filterValues { it != null }
            .mapValues { (_, second) -> second!! }

    private fun Map<Distance, Distance>.getMatchingDistancesForBeacon(beacon: Beacon) =
        filter { (first, _) -> beacon in listOf(first.start, first.end) }.map { it.value }

    private fun <T> List<T>.mostFrequentElement() =
        groupingBy { it }.eachCount().toList().maxByOrNull { it.second }?.first

    private fun Map<Beacon, Beacon>.findTransformation() =
        ROTATIONS.asSequence()
            .map { rotation -> rotation to map { (first, second) -> first - second.rotate(rotation) }.distinct() }
            .first { (_, translations) -> translations.size == 1 }
            .let { (transformation, translations) -> transformation to translations.first() }

    private data class Scanner(val beacons: Set<Beacon>) {
        val beaconDistances = lazy {
            beacons.flatMap { first -> beacons.filterNot { it == first }.map { (first to it) } }
                .map { (first, second) -> Distance(first, second) }
        }

        fun merge(scanner: Scanner, transformation: Transformation) =
            scanner.beacons
                .map { it.rotate(transformation.first) + transformation.second }
                .let { Scanner(beacons + it) }
    }

    private operator fun Beacon.plus(other: Beacon) =
        Beacon(first + other.first, second + other.second, third + other.third)

    private operator fun Beacon.minus(other: Beacon) =
        Beacon(first - other.first, second - other.second, third - other.third)

    private fun Beacon.rotate(rotation: Rotation) = rotation(this)

    private data class Distance(val start: Beacon, val end: Beacon) {
        val distance
            get() = lazy {
                setOf(
                    abs(start.first - end.first),
                    abs(start.second - end.second),
                    abs(start.third - end.third)
                )
            }
    }

    private fun String.parseScanners() =
        split(System.lineSeparator().repeat(2))
            .map { it.toScanner() }

    private fun String.toScanner() =
        lines()
            .drop(1)
            .map { it.toBeacon() }
            .toSet()
            .let { Scanner(it) }

    private fun String.toBeacon() =
        split(",")
            .map { it.toInt() }
            .let { (x, y, z) -> Beacon(x, y, z) }
}
