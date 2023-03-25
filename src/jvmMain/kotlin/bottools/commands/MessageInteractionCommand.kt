package bottools.commands

import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

abstract class MessageInteractionCommand protected constructor(commandName: String)
    :Command(commandName) {
    init {
        makeInteractible()
    }

    override val commandData: CommandData
        get() = Commands.message(name)
}