package pl.matmar.matipolit.lo1plus.utils

const val BASE_URL = "http://lo1app.mati2002.ayz.pl/"
val STANDARD_CARD_LIST = mutableListOf("planLekcji", "ostatnieOceny", "wiadomosci", "obiady", "ogloszenia", "terminySprawdzianow", "godziny")
fun String.toCardType(): Int{
    return STANDARD_CARD_LIST.indexOf(this)
}