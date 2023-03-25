package bottools.botactions.online

import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import org.jsoup.Jsoup


class STest {

}
fun main(){
    val URL = "https://www.dotabuff.com"
    val cookieList = skrape(HttpFetcher){
        request { url = URL }
        response { cookies }
    }
    val attempt = Jsoup.connect(URL).cookies(cookieList.map { it.name to it.value }.toMap())
        .execute().body()
    println(attempt)
    //println(body)
    /*
    val input = BufferedInputStream(body)
    val os: OutputStream = FileOutputStream("test.jpg")
    val b = ByteArray(2048)
    var length: Int
    while (input.read(b).also { length = it } != -1) os.write(b, 0, length)

     */

}