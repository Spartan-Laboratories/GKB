package bottools.main

import bottools.dataprocessing.D
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory


internal class BotListener : ListenerAdapter() {
    private final val log = LoggerFactory.getLogger(BotListener::class.java)
    private val ignoredChannels = ArrayList<MessageChannel>()
    internal val responder:Responder = Responder()

    init {
        Parser addTrigger "/"
        responder newGuildMemberJoinAction this::defaultOnGuildMemberJoinAction
    }

    private fun defaultOnGuildMemberJoinAction(event: GuildMemberJoinEvent) {
        log.info("The user {} has joined the guild {}", event.user.name, event.guild.name)
        D.updateServerDatabase()
        log.info("Guild member database has been updated")

        // Gives the new guild member the specified default role
        log.info("Adding the default role to the new member")
        event.guild.let {
            with(it.getRoleById(D / it - "defaultrole")) {
                it.addRoleToMember(event.member, this!!).complete()
            }
        }
    }

    private fun startsWithTrigger(event: MessageReceivedEvent) = Parser `starts with trigger` event.message.contentRaw
    fun ignoreChannel(channel: MessageChannel) = ignoredChannels.add(channel)
    fun unignoreChannel(channel: MessageChannel) = ignoredChannels.remove(channel)
    infix fun ignore(channel: MessageChannel) = ignoreChannel(channel)
    infix fun listenTo(channel: MessageChannel) = unignoreChannel(channel)
    operator fun minus(channel: MessageChannel) = this ignore channel
    operator fun plus(channel: MessageChannel) = this listenTo channel
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (startsWithTrigger(event) and (event.channel !in ignoredChannels))
            responder reactTo event
    }
    override fun onGuildJoin(event: GuildJoinEvent) = responder reactTo event
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) = responder reactTo event
    override fun onGuildUpdateName(event: GuildUpdateNameEvent) = responder reactTo event
    override fun onMessageReactionAdd       (event: MessageReactionAddEvent)        = responder reactTo event
    override fun onMessageDelete            (event: MessageDeleteEvent)             = responder reactTo event
    override fun onUserUpdateOnlineStatus   (event: UserUpdateOnlineStatusEvent)    = responder reactTo event
    override fun onSlashCommandInteraction  (event: SlashCommandInteractionEvent)   = responder reactTo event
    override fun onMessageContextInteraction(event: MessageContextInteractionEvent) = responder reactTo event
    override fun onUserContextInteraction   (event: UserContextInteractionEvent)    = responder reactTo event
}