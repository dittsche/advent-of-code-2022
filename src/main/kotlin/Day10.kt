object Day10 : PuzzleSolver(10) {

    override fun solve1(input: String) = CPU()
        .apply {
            input.lines()
                .map { it.toInstruction() }
                .forEach { executeInstruction(it) }
        }
        .signalStrengths
        .filter { (cycle, _) -> (cycle + 20) % 40 == 0 }
        .map { (cycle, strength) -> cycle * strength }
        .sum()

    override fun solve2(input: String): String {
        val cpu = CPU()
            .apply {
                input.lines()
                    .map { it.toInstruction() }
                    .forEach { executeInstruction(it) }
            }
        return CRT(cpu).toString()
    }

    private fun String.toInstruction() =
        when {
            this == "noop" -> Noop
            startsWith("addx") -> AddX(this.split(" ")[1].toInt())
            else -> throw NotImplementedError()
        }

    private class CRT(private val cpu: CPU) {
        private fun spritePositionAtCycle(cycle: Int) =
            cpu.signalStrengths[cycle+1]!!
                .let { (it - 1..it + 1) }

        override fun toString(): String {
            return (0 until 6)
                .joinToString(System.lineSeparator()) { row ->
                    (0 until 40).joinToString("") { pixel ->
                        val cycle = row * 40 + pixel
                        if (spritePositionAtCycle(cycle).contains(pixel)) "#" else "."
                    }
                }
        }
    }

    private class CPU {
        private var registerX = 1
        private var cycleCount = 0
        val signalStrengths: MutableMap<Int, Int> = mutableMapOf()

        fun executeInstruction(instruction: Instruction) {
            when (instruction) {
                Noop -> increaseCycleAndRecord()
                is AddX -> instruction.execute()
            }
        }

        private fun AddX.execute() {
            increaseCycleAndRecord()
            increaseCycleAndRecord()
            registerX += value
        }

        private fun increaseCycleAndRecord() {
            cycleCount++
            signalStrengths[cycleCount] = registerX
        }
    }

    private sealed interface Instruction

    private object Noop : Instruction

    private class AddX(val value: Int) : Instruction
}
