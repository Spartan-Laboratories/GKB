package bottools.commands

import bottools.main.Bot

class HelpCommand : Command("help") {
    override val brief = "See what this bot can do"
    override val details = "Gives a detailed description of this Bot's capabilities"
    init {
        makeInteractive()
    }

    public override fun invoke(args: Array<String>){
        reply("Hello, I am " + jda.selfUser.name)
        if (args.isEmpty()) executeGenericVersion()
        else executeCommandNameVersion(args)
    }

    private fun executeGenericVersion() {
        eb = eb //.setAuthor("Hello, I am TrumpBot")
            .setTitle("Here is what I can do:")
        createFields()
        sendEmbed()
    }

    private fun executeCommandNameVersion(commandAlias: Array<String>) {
        if (!Bot.commands.keys.contains(commandAlias[0])) {
            say("No such command exists")
            executeGenericVersion()
            return
        }
        Bot.commands[commandAlias[0]]!!.setEvent(messageEvent!!).help(getSecondaryArgs(commandAlias))
    }

    private fun createFields() {
        for (name in Bot.commandNameList) eb = eb.addField("/$name", Bot.commands[name]!!.helpMessage!!, false)
    }
}