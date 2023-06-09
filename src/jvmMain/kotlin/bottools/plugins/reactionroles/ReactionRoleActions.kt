package bottools.plugins.reactionroles

import bottools.botactions.say
import bottools.dataprocessing.D
import bottools.dataprocessing.minus
import bottools.main.Bot
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent

object ReactionRoleActions {
    fun giveReactionRole(event: MessageReactionAddEvent) {
        if(event.member?.user == Bot.jda?.selfUser)return
        val role = getCorrespondingRole(event.guild, event.emoji)
        event.guild.addRoleToMember(event.member!!.user, role!!).complete()
    }

    fun removeDeletedWelcomeMessage(event: MessageDeleteEvent) =
        with(D/event.guild/"welcomeMessages"){
            readAll().forEach {
                if(it-"id" == event.messageId){
                    this remove it
                    return
                }
            }
        }

    fun createWelcomeMessage(guild: Guild, channel: MessageChannel) {
        (D/guild-"welcomeMessage").let {
            say(channel, it).let {
                setGuildWelcomeMessageID(it)
                addGameEmotes(it)
            }
        }
    }

    private fun setGuildWelcomeMessageID(message: Message) {
        with(D/message.guild/"welcomeMessages" create "welcomeMessage") {
            this/"channel" + message.channel.id
            this/"id" + message.id
        }
    }

    private fun addGameEmotes(welcomeMessage: Message) =
        getEmoteList(welcomeMessage.guild).forEach{
            bottools.botactions.addReactionToMessage(it!!, welcomeMessage)
        }
    private fun getEmoteList(guild: Guild) = (D/guild/"reactionRoles").readAll().map { it-"emote"}.map(guild::getEmojiById)
    private fun getCorrespondingRole(guild: Guild, emote: Emoji) : Role?{
        (D/guild/"reactionRoles").readAll().forEach {
            if(it-"emote" == emote.asReactionCode.split(":")[1])
                return guild.getRoleById(it-"role")
        }
        return null
    }
    private fun getGuildWelcomeMessages(guild: Guild) =
        (D/guild/"welcomeMessages").readAll().map { it-"channel" to it-"id" }.map {
            guild.getTextChannelById(it.first)!!.retrieveMessageById(it.second).complete()
        }
    fun updateMessages(guild: Guild, emote: Emoji) = with(getGuildWelcomeMessages(guild)) {
        if (isNotEmpty())forEach{
            bottools.botactions.addReactionToMessage(emote, it)
        }
    }
}