import androidx.compose.ui.text.capitalize
import bottools.botactions.capitalizeEveryWord
import bottools.botactions.online.skipLinesTo
import bottools.commands.MethodCommand
import bottools.commands.OnlineCommand
import bottools.commands.Option
import bottools.commands.OrganizationCommand
import bottools.commands.OrganizationCommand.*
import net.dv8tion.jda.api.utils.FileUpload
import java.awt.Desktop
import java.awt.Rectangle
import java.awt.Robot
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class PalOfExile() : OnlineCommand("poe") {
    override var brief = "Your best pal in all of Oriath"
    override val details = "Helps find Path of Exile information"
    override fun invoke(args: Array<String>) {}

    init{
        MethodCommand(onExecute = ::showDivPic, name = "pricediv", brief = "shows the price of a divine orb in chaos", parent = this)
        OrganizationCommand(name = "price", this).addCommand(alias = "div", command = "pricediv")

        MethodCommand(::wikiItem, "wikiitem", "provides a description of the item", this)+
                Option(name = "itemname", type = "string", description = "which item do you want to search for?", required = true)
        this + "wiki" + orgData("item", "wikiitem")

        makeInteractive()
    }
    private fun showDivPic(args: Array<String>){
        Desktop.getDesktop().browse(URL("https://poe.ninja/challenge/currency/divine-orb").toURI())
        Thread.sleep(1000)
        //val image:BufferedImage = Robot().createScreenCapture(Rectangle(Toolkit.getDefaultToolkit().screenSize))
        val image: BufferedImage = Robot().createScreenCapture(Rectangle().apply { x = 40; y = 100; width = 2560-x; height = 1380-y; })

        ImageIO.write(image, "png", File("screenshot.png"))

        val startx = 832
        val starty = 205
        val width = 1050
        val height = 868
        val endx = startx + width
        val endy = starty + height

        val cutout = BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB)
        cutout.graphics.drawImage(image, 0,0,width, height, startx, starty, endx, endy, null )

        val cutoutFile = File("cutout.png")
        ImageIO.write(cutout, "png", cutoutFile)
        //show(cutoutFile)
        reply?.addFiles(FileUpload.fromData(cutoutFile))?.complete()
    }
    private fun wikiItem(itemName:Array<String>){
        val itemName = getOption("itemname")?.asString?.capitalizeEveryWord()?.replace(' ','_')
        if(itemName.isNullOrBlank())reply("Could not find the specified item")
        val address = "https://www.poewiki.net/wiki/$itemName"
        open(address){
            reply(data)
        }
    }
}