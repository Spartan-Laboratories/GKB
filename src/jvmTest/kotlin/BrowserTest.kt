import java.awt.Desktop
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO


fun main(){
    Desktop.getDesktop().browse(URL("https://poe.ninja/challenge/currency/divine-orb").toURI())
    Thread.sleep(1000)
    //val image:BufferedImage = Robot().createScreenCapture(Rectangle(Toolkit.getDefaultToolkit().screenSize))
    val image:BufferedImage = Robot().createScreenCapture(Rectangle().apply { x = 40; y = 100; width = 2560-x; height = 1380-y; })

    ImageIO.write(image, "png", File("screenshot.png"))
    val startx = 832
    val starty = 205
    val width = 1050
    val height = 868
    val endx = startx + width
    val endy = starty + height
    val cutout = BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB)
    cutout.graphics.drawImage(image, 0,0,width, height, startx, starty, endx, endy, null )
    ImageIO.write(cutout, "png", File("cutout.png"))
}