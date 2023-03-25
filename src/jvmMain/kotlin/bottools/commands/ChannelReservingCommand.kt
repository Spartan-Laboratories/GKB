package bottools.commands

import bottools.main.SingleChannelListener
import net.dv8tion.jda.api.entities.Guild

abstract class ChannelReservingCommand protected constructor(commandName: String) : Command(commandName) {
    private val reserved = HashMap<Guild, SingleChannelListener>()

    init {
        SCReserve()
        SCUnreserve()
    }

    abstract override fun invoke(args: Array<String>)
    protected fun preExecute() {}
    protected inner class SCReserve : SubCommand("reserve", this@ChannelReservingCommand) {
        init {
            helpMessage = """
                Reserves this text channel to be used exclusively by this command.
                After reserving, subcommands can be called directly.
                Example: `/play` instead of`/music play`
                """.trimIndent()
        }

        public override fun invoke(args: Array<String>){
            if (reserved.containsKey(guild)) say("Unable to reserve this channel, a reservation for this command already exists in this server") else {
                reserved[guild] = SingleChannelListener(channel, this@ChannelReservingCommand)
                say("This channel is now reserved for the purposes of the command: $name\nYou can now use its sub-commands directly")
            }
        }
    }

    protected inner class SCUnreserve : SubCommand("unreserve", this@ChannelReservingCommand) {
        init {
            helpMessage = "Removes the reservation for this channel"
        }

        public override fun invoke(args: Array<String>){
            reserved[guild]!!.destroy()
            reserved.remove(guild)
            say("channel is no longer reserved")
        }
    }
}