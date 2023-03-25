package bottools.botactions

import bottools.main.Bot
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.utils.FileUpload
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import org.slf4j.LoggerFactory
import java.io.File

/**
 * The bottom layer wrapper responsible for the simplest bot interactions with discord.
 * Meant to be used only within the project. Although this class' methods are public,
 * typically specific commands should extend the Command
 * class and use its methods instead.
 *
 * @see [bottools.commands.Command]
 *
 *
 * @author Spartak
 */

/** The logger that will track BotAction activity
 * Most of the logged messages should be considered 'trace'
 */
private val log = LoggerFactory.getLogger("BotAction")
fun say(channel: MessageChannel, message:MessageCreateData) = channel.sendMessage(message).complete()
/**
 * Sends the given message in the given MessageChannel
 * @param channel in which you want to send a message
 * @param message that you want to send
 * @return the message that was sent
 */
fun say(channel: MessageChannel, message: String): Message {
    checkAndLog(channel, message)
    return channel.sendMessage(message).complete()
}

/**
 * Sends the given file in the given MessageChannel
 * @param channel in which you want to send a file
 * @param file that you want to send
 * @return the message that was sent
 */
fun sendFileInChannel(channel: MessageChannel, file: File): Message {
    channelCheck(channel)
    fileCheck(file)
    log.debug("Sending a file")
    log.trace("File: \'{}\', \tIn channel: []", file.name, channel)
    val fu = FileUpload.fromData(file, file.name)
    return channel.sendFiles(fu).complete()
}

/**
 * Creates a new message that contains data based on the
 * given messages and sends it to the given channel.
 * Returns the created message
 * @param channel in which you want to send a message
 * @param message that you want to send
 * @return the message that was created and sent
 */
fun say(channel: MessageChannel, message: Message) = say(channel, MessageCreateData.fromMessage(message))

/**
 * Sends the given message in the given MessageChannel as a
 * Text-to-Speech message causing it to be read out loud to
 * the online members of the guild.
 * @param channel in which you want to send a message
 * @param message that you want to be sent
 * @return the message that was sent
 */
fun tts(channel: MessageChannel, message: String): Message {
    checkAndLog(channel, message)
    return channel.sendMessage(message).setTTS(true).complete()
}

fun addReactionToMessage(emoji: Emoji, message: Message) = message.addReaction(emoji).complete()

private fun channelCheck(channel: MessageChannel?) {
    if (channel == null) {
        log.warn("An attempt was made to send a message to a null channel")
        throw IllegalArgumentException("Cannot send message. Channel is null.")
    }
    if (!channel.canTalk()) {
        log.warn(
            "An attempt was made to send a message to the channel: \"{}\", but {} does not have permission to speak in this channel",
            channel.name, Bot.jda!!.selfUser.name
        )
        throw IllegalArgumentException("Cannot send message. Insufficient permissions.")
    }
}

private fun fileCheck(file: File?) {
    if (file == null) {
        log.warn("An attempt was made to send a file that is null")
        throw IllegalArgumentException("Cannot send a null file")
    }
}

private fun messageCheck(message: Message?) {
    if (message == null) {
        log.warn("An attempt was made to send a message that is null.")
        throw IllegalArgumentException("cannot send a null message")
    }
}

private fun messageLogging(channel: MessageChannel, message: String) {
    log.debug("Sending a message")
    log.trace("Message: \"{}\", \tIn channel: {}", message, channel)
}

private fun checkAndLog(channel: MessageChannel, message: String) {
    channelCheck(channel)
    messageLogging(channel, message)
}
infix fun Bot.Companion.say(message:String) = MessageCreateData.fromContent(message)
operator fun MessageChannel.contains(message:MessageCreateData) = true.also{ say(this,message) }
infix fun Bot.Companion.show(url:String) = MessageCreateData.fromContent(url)
infix fun Bot.Companion.send(file: File) = MessageCreateData.fromFiles(FileUpload.fromData(file))
infix fun Bot.Companion.send(message:Message) = MessageCreateData.fromMessage(message)

