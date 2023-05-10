package bottools.botactions

import java.awt.Desktop
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

fun String.capitalizeEveryWord() =
    split(' ').map { it.capitalize()}.toString().let {
        it.substring(1,it.length-1).replace(",","")
    }
fun cropImage(image:BufferedImage, x: Int,y: Int,width: Int,height: Int) =
    BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB).apply{
        graphics.drawImage(image,0,0,width,height,x,y,x+width,y+height,null)
    }
fun saveImage(image:BufferedImage, filePath:String) = File("$filePath.png").apply{
    ImageIO.write(image, "png", this)
}
fun screenshotBrowser(address:String):BufferedImage{
    openInBrowser(address)
    Thread.sleep(1600)
    return screenshotArea(x = 40, y = 100, width = 2520, height = 1280)
}
fun openInBrowser(address:String) = Desktop.getDesktop().browse(URL(address).toURI())
fun screenshotArea(x:Int, y:Int, width:Int, height:Int) =
    screenshot(Rectangle().apply{this.x = x;this.y=y;this.width=width;this.height=height})
fun screenshotFull() =
    screenshot(Rectangle(Toolkit.getDefaultToolkit().screenSize))
private fun screenshot(area:Rectangle) = Robot().createScreenCapture(area)!!

fun main(){
    print("test test1 test2".capitalizeEveryWord())
}