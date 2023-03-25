package bottools.commands

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Option(type: String = "string", name: String, description: String, required: Boolean = false) :
    OptionData(getOptionType(type), name, description, required) {

    companion object {
        private fun getOptionType(type: String): OptionType {
            when (type.lowercase()) {
                "user"      -> return OptionType.USER
                "role"      -> return OptionType.ROLE
                "integer"   -> return OptionType.INTEGER
                "channel"   -> return OptionType.CHANNEL
            }
            return OptionType.STRING
        }
    }
}