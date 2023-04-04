package bottools.commands

import bottools.main.Parser.CommandContainer


class OrganizationCommand(name: String, parent:Command) : SubCommand(name, parent) {
    override var brief = "This should not be visible"
    override var details = brief
    private val orgCommands = HashMap<String, String>()
    data class orgData(val alias: String, val trueName: String)
    init {
        subCommandRequired = true
        helpMessage = "Can be followed by:"

    }

    override fun invoke(commandData: CommandContainer){
        commandData.args[0] = orgCommands[commandData.args[0]]!!
        parent(commandData)
    }

    override fun invoke(args: Array<String>) {}

    fun addCommand(alias: String, command: String = alias): OrganizationCommand {
        orgCommands[alias] = command
        helpMessage = "$helpMessage $command"
        if(command !in parent.subCommands.keys)
            throw IllegalArgumentException("Organization attempted to add subcommand: $command, which is not a valid subcommand of it's parent: ${parent.name}")
        parent.subCommands[command]!!.addOrganization(this, alias)
        return this
    }

    operator fun plus(command: orgData) = addCommand(command.alias, command.trueName)
}