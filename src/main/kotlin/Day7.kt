object Day7 : PuzzleSolver(7) {
    private const val maxSizeThreshold = 100_000
    private const val totalDiskSpace = 70_000_000
    private const val neededDiskSpace = 30_000_000

    override fun solve1(input: String) = TerminalOutputParser(input)
        .rootDirectory
        .allDirectoriesIncludingSelf
        .filter { it.size <= maxSizeThreshold }
        .sumOf { it.size }

    override fun solve2(input: String): Number {
        val rootDirectory = TerminalOutputParser(input).rootDirectory
        val diskSpaceToDelete = rootDirectory
            .size
            .let { totalDiskSpace - it }
            .let { neededDiskSpace - it }

        return rootDirectory.allDirectoriesIncludingSelf
            .filter { it.size > diskSpaceToDelete }
            .minBy { it.size }
            .size
    }

    private class TerminalOutputParser(terminalOutput: String) {
        var rootDirectory: Directory = Directory("/", mutableSetOf())
        private var currentWorkingDirectory: Directory = rootDirectory

        init {
            terminalOutput.lines()
                .forEach { it.parseOutputLine() }
        }

        private fun String.parseOutputLine() = if (startsWith('$')) parseCommand() else parseFileSystemEntry()

        private fun String.parseCommand() {
            val matchResult = "\\$ (cd|ls)( .*)?".toRegex().find(this)?.groupValues!!
            when (matchResult[1]) {
                "cd" -> changeCurrentWorkingDirectory(matchResult[2].trim())
                "ls" -> {}
            }
        }

        private fun String.parseFileSystemEntry() {
            when {
                startsWith("dir") -> currentWorkingDirectory.createDirectory(split(" ")[1])
                else -> split(" ").let { currentWorkingDirectory.createFile(it[1], it[0].toInt()) }

            }
        }

        private fun changeCurrentWorkingDirectory(directoryName: String) {
            currentWorkingDirectory = when (directoryName) {
                "/" -> rootDirectory
                ".." -> currentWorkingDirectory.parent!!
                else -> currentWorkingDirectory.entries
                    .filterIsInstance<Directory>()
                    .find { it.name == directoryName }
                    ?: currentWorkingDirectory.createDirectory(directoryName)
            }
        }

        private fun Directory.createDirectory(directoryName: String) =
            Directory(directoryName, mutableSetOf())
                .also {
                    entries.add(it)
                    it.parent = this
                }

        private fun Directory.createFile(fileName: String, size: Int) {
            File(fileName, size)
                .also { entries.add(it) }
        }
    }

    private data class Directory(
        override val name: String,
        val entries: MutableSet<FileSystemEntry>
    ) : FileSystemEntry(name) {
        var parent: Directory? = null
        override fun toString() = "Directory(name=${name}, entries=${entries})"

        override val size: Int
            get() = entries.sumOf { it.size }
        val allDirectoriesIncludingSelf: Set<Directory>
            get() = setOf(this) + entries.filterIsInstance<Directory>().flatMap { it.allDirectoriesIncludingSelf }
                .toSet()
    }

    private data class File(override val name: String, override val size: Int) : FileSystemEntry(name)

    private abstract class FileSystemEntry(open val name: String) {
        abstract val size: Int
    }
}
