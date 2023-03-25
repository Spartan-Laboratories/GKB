package bottools.plugins.reactionroles

import bottools.commands.Command

class CreateMainWelcomeMessage : Command("createmainwelcomemessage") {
    init {
        makeInteractible()
    }

    public override fun invoke(args: Array<String>) = ReactionRoleActions.createWelcomeMessage(guild, channel)

    companion object {



    }
}