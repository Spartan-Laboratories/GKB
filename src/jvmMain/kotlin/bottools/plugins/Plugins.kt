package bottools.plugins

import bottools.dataprocessing.D
import bottools.main.Bot
import bottools.main.newMessageDeleteAction
import bottools.main.newMessageReactionAddAction
import bottools.main.newMessageReceivedAction
import bottools.plugins.Math.MathCommand
import bottools.plugins.reactionroles.AddReactionRole
import bottools.plugins.reactionroles.CreateMainWelcomeMessage
import bottools.plugins.reactionroles.ReactionRoleActions
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Plugin(val fn:Bot.Companion.()->Unit){
    operator fun invoke() = fn.invoke(Bot)
}

object Plugins {
    val `REACTION ROLES` = Plugin {
        D.addTagFile("src/jvmMain/resources/ReactionRoles.xml")
        createCommand(AddReactionRole())
        createCommand(CreateMainWelcomeMessage())
        responder newMessageReactionAddAction  ReactionRoleActions::giveReactionRole
        responder newMessageDeleteAction       ReactionRoleActions::removeDeletedWelcomeMessage
    }
    val Math = Plugin{
        Bot createCommand MathCommand()
    }
    val SpamControl = Plugin{
        responder newMessageReceivedAction ::isSpam
    }

    private fun isSpam(event: MessageReceivedEvent): Boolean {
        val channel: MessageChannel = event.channel
        val history = channel.history
        val spamThreshold = 10
        history.retrievePast(spamThreshold).complete()
        val retrievedMessages = history.retrievedHistory
        if (retrievedMessages.size > spamThreshold) return false
        for (i in 1 until spamThreshold) if (retrievedMessages[i].contentRaw != event.message.contentRaw) return false
        return true
    }
}