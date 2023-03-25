import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import bottools.main.Bot
import bottools.plugins.Plugins
import java.util.concurrent.CompletableFuture.runAsync


fun main() = application{
    //KotBot()

    Window(onCloseRequest = ::exitApplication){
        BotUI()
    }
}
@OptIn(ExperimentalUnitApi::class)
@Composable
private fun BotUI(){
    var initializerText by remember{mutableStateOf("Start!")}
    var statusText by remember{mutableStateOf("Not started")}
    var botStarted by remember{ mutableStateOf(false) }
    var readyToken: Boolean = false
    var readyTokenState = remember{mutableStateOf(readyToken)}
    var bot by remember{ mutableStateOf(lazy{KotBot(readyTokenState)})}
    MaterialTheme {Row {
        Column {
            Text(statusText)
            //Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    statusText = "Please wait, initializing!"
                    initializerText = statusText
                    runAsync{
                        bot.value
                    }
                    botStarted = true
                },
                modifier = Modifier.height(60.dp).width(250.dp)
            ) {
                Column {
                    Text(
                        text = initializerText,
                        fontSize = 25.sp,
                        fontWeight = FontWeight(10),
                        modifier = Modifier.fillMaxSize().weight(2F).align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "note: application will lag on start",
                        fontSize = TextUnit(12F, TextUnitType.Sp),
                        fontWeight = FontWeight(1),
                        modifier = Modifier.fillMaxSize().weight(1F)
                    )
                }

            }
        }
        Spacer(Modifier.width(1.dp))
        if (readyTokenState.value){
            Thread.sleep(1000L)
            stateField(bot.value)
        }
    }}
}
@Composable
private fun stateField(bot:Bot){
    Column {
        commandsField(bot)
        dateField(bot.centralProcess?.currentDate ?: "not started")
    }
}
@Composable
private fun commandsField(bot:Bot){
    var uptime by remember { bot.formattedUptime}
    Text(uptime)
}
@Composable
private fun dateField(date:String){
    val date by remember { mutableStateOf(date) }
    Text(date)
}
class KotBot(tokenState:MutableState<Boolean>) : Bot(tokenState) {
    override fun listCommands() {
        Plugins.REACTIONROLES()
        Bot createCommand DotaCommand()
    }

    override fun applyDailyUpdate(currentDate: String?) {
    }

}