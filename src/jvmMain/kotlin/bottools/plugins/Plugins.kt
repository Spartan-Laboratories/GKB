package bottools.plugins

import bottools.dataprocessing.D
import bottools.main.Bot
import bottools.main.newMessageDeleteAction
import bottools.main.newMessageReactionAddAction
import bottools.plugins.reactionroles.AddReactionRole
import bottools.plugins.reactionroles.CreateMainWelcomeMessage
import bottools.plugins.reactionroles.ReactionRoleActions
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

fun interface Plugin {
    operator fun invoke()
}

object Plugins {
    val REACTIONROLES = Plugin {
        D.addTagFile("src/jvmMain/resources/ReactionRoles.xml")
        Bot createCommand AddReactionRole()
        Bot createCommand CreateMainWelcomeMessage()
        Bot.responder!!.let{
            it newMessageReactionAddAction  ReactionRoleActions::giveReactionRole
            it newMessageDeleteAction       ReactionRoleActions::removeDeletedWelcomeMessage
        }
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