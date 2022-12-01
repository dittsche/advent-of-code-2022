@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun readInput(day: Int, part: Int? = null) =
    "day$day${part.toPart()}.txt".let { object {}.javaClass.getResource(it).readText() }

private fun Int?.toPart() = this?.let { ".$it" } ?: ""

fun String.splitToInts(delimiter: String = ",") = split(delimiter).map { it.toInt() }
