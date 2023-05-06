package bottools.botactions

fun String.capitalizeEveryWord() =
    split(' ').map { it.capitalize()}.toString().let {
        it.substring(1,it.length-1).replace(",","")
    }

fun main(){
    print("test test1 test2".capitalizeEveryWord())
}