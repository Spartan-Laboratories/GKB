package bottools.commands

import java.util.function.Consumer

class MethodCommand(
    private val onExecute: Consumer<Array<String>>,
    name: String = onExecute.toString(),
    override var helpMessage: String? = "default description",
    parent : Command
): SubCommand(name, parent) {
    override fun invoke(args: Array<String>) = onExecute.accept(args)
}