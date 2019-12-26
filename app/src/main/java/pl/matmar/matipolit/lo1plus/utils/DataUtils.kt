package pl.matmar.matipolit.lo1plus.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import android.view.View
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.xwray.groupie.Section
import org.json.JSONObject
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.data.database.DatabaseCard
import pl.matmar.matipolit.lo1plus.domain.HomeCard
import pl.matmar.matipolit.lo1plus.domain.Subject
import pl.matmar.matipolit.lo1plus.ui.grades.GradeHeaderItem
import pl.matmar.matipolit.lo1plus.ui.grades.GradeItem
import pl.matmar.matipolit.lo1plus.ui.home.GodzinyCardItem
import pl.matmar.matipolit.lo1plus.ui.home.HomeCardItem
import timber.log.Timber
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

const val LESSON_TIME_MILIS = 2700000

data class Godzina(
    val index: Int,
    val startTime: Date,
    val endTime: Date,
    val name: String
)

data class Jutro(
    val name: String?,
    val date: Date?,
    val lessonName: String?,
    val lessonTime: Date?
)

data class GodzinyJSON(
    val godziny: JSONObject,
    val jutro: String?,
    val jutroTime: String?,
    val jutroName: String?,
    val jutroData: String?,
    val dzwonekDelay: Int?,
    val data: String?
){
    val godzinyObject: Godziny
        get() {
            val dzisiejszaData = data?.toDate()
            val jutroDataObj = jutroData?.toDate()
            val iter: Iterator<String> = godziny.keys()
            var index: Int
            val godzinyGodziny = mutableListOf<Godzina>()

            //creates a list of Godzina
            while (iter.hasNext()){
                val sIndex = iter.next()
                val godzinaJSON: JSONObject = godziny.getJSONObject(sIndex)

                val startEnd = godzinaJSON.getJSONArray("godziny")
                val startArray = startEnd.getString(0).split(":")
                val endArray = startEnd.getString(1).split(":")

                val godzinaStart = dzisiejszaData?.let {
                    Date(
                        dzisiejszaData.year, dzisiejszaData.month,
                        dzisiejszaData.day, startArray[0].toInt()-1, startArray[1].toInt()-1)
                }

                val godzinaEnd = dzisiejszaData?.let {
                    Date(dzisiejszaData.year, dzisiejszaData.month, dzisiejszaData.day,
                        endArray[0].toInt()-1, endArray[1].toInt()-1)
                }
                val godzinaGodzina = Godzina(sIndex.toInt(),godzinaStart!!, godzinaEnd!!
                    ,godzinaJSON.getString("name"))
                godzinyGodziny.add(godzinaGodzina)

            }
            val jutroTimeArray = jutroTime?.split(":")
            var jutroObject: Jutro? = null
            jutroTimeArray?.let {
                jutroObject = Jutro(jutro, jutroDataObj, jutroName,
                jutroDataObj?.let {Date(jutroDataObj.year, jutroDataObj.month, jutroDataObj?.day,
                    jutroTimeArray[1].toInt()-1, jutroTimeArray[0].toInt()-1)  }
                )
            }
            return Godziny(godzinyGodziny, jutroObject, dzwonekDelay, dzisiejszaData)
        }
}
data class Godziny(
    var godzinyGodziny: List<Godzina>?,
    var jutro: Jutro?,
    var dzwonekDelay: Int?,
    var date: Date?
)

data class GodzinyView(
    var TitleText: String,
    var FirstMediumText: String?,
    var FirstSmallText: String?,
    var TimeHeaderText: String?,
    var TimerText: String?,
    var TimerSubText: String?,
    var ProgressBar: Int?,
    var SecondMediumText: String?,
    var SecondSmallText1: String?,
    var SecondSmallText2: String?,
    var Visibility: Int = View.VISIBLE
)

data class Tag(var tag: Char?, var start: Int, var end: Int)


@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.memberProperties
        // don't cast here to <Any, R>, it would succeed silently
        .first { it.name == propertyName } as KProperty1<Any, *>
    // force a invalid cast exception if incorrect type here
    return property.get(instance) as R
}

//EXTENSION FUNCTIONS

fun String.toFormattedSpannable(): SpannableStringBuilder{
    val tagi = ArrayList<Tag>()
    var i = 0
    while (i < this.length - 1) {
        if (this[i] == '<') {
            val start = i + 3
            i++
            val index = this[i]
            while (this[i] != '<') {
                i++
            }
            val end = i
            val tag = Tag(index, start, end)
            tagi.add(tag)
        }
        i++
    }

    //this = this.replace("<i>", "");
    val str = SpannableStringBuilder(this)
    for (i in tagi.indices) {
        val tag = tagi[i]
        val start = tag.start
        val end = tag.end
        if (tag.tag == 'b') {
            str.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), tag.start, tag.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (tag.tag == 'i') {
            str.setSpan(android.text.style.StyleSpan(Typeface.ITALIC), tag.start, tag.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (tag.tag == 'd') {
            str.setSpan(StrikethroughSpan(), tag.start, tag.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }


    }
    for (i in tagi.size downTo 1) {
        val tag = tagi[i - 1]
        str.delete(tag.end, tag.end + 4)
        str.delete(tag.start - 3, tag.start)
    }
    return str
}

fun String.toDate() : Date? {
    val dateStringArray = this.split(".")
    Timber.d("Date - array: " + dateStringArray.toString())
    var date: Date? = null
    if (dateStringArray.size==3){
        date = Date(dateStringArray[2].toInt()-1900,
            dateStringArray[1].toInt()-1, dateStringArray[0].toInt())
        Timber.d("Date - converted: " + date)
    }
    return date
}

fun String.asGodzinyJSON() : GodzinyJSON = Gson().fromJson(this, GodzinyJSON::class.java)

fun String.toCardType(): Int{
    return DEFAULT_CARD_LIST.indexOf(this)
}

fun Int.toCardName(): String{
    return DEFAULT_CARD_LIST[this]
}

fun String.toCardIcon(): Int = when(this){
        "planLekcji" -> R.drawable.ic_home_plan
        "ostatnieOceny" -> R.drawable.ic_home_grades
        "wiadomosci" -> R.drawable.ic_home_messages
        "obiady" -> R.drawable.ic_home_lunch
        "ogloszenia" -> R.drawable.ic_home_announcements
        "terminySprawdzianow" -> R.drawable.ic_home_test
        "godziny" -> R.drawable.ic_home_godziny
        else -> R.drawable.ic_home_plan
    }


fun String.toCardTitle(): String = when(this){
    "planLekcji" -> "Plan lekcji"
    "ostatnieOceny" -> "Ostatnie oceny"
    "wiadomosci" -> "Wiadomości"
    "obiady" -> "Obiady"
    "ogloszenia" -> "Ogłoszenia"
    "terminySprawdzianow" -> "Terminy sprawdzianów"
    "godziny" -> "Godziny"
    else -> "-"
}

fun String.toCardColorInt(): Int = when(this){
    "planLekcji" -> R.color.colorPlanLekcji
    "ostatnieOceny" -> R.color.colorOstatnieOceny
    "wiadomosci" -> R.color.colorWiadomosci
    "obiady" -> R.color.colorObiady
    "ogloszenia" -> R.color.colorOgloszenia
    "terminySprawdzianow" -> R.color.colorTerminySprawdzianow
    "godziny" -> R.color.godzinyColor
    else -> R.color.colorPlanLekcji
}

fun List<HomeCard>.asDatabaseModel(): Array<DatabaseCard> {
    return map {
            DatabaseCard(
                name = it.title,
                content = it.content
            )
    }.toTypedArray()
}

fun List<HomeCard>.asHomeCardItem() : List<HomeCardItem> = this.map{
    HomeCardItem(it)
}

fun Date.toDateWithDelay(delay: Int) = Date(this.time - delay*1000)

fun GodzinyJSON.asGodzinyCardItem(time : String) : GodzinyCardItem = GodzinyCardItem()


fun Long.asFormattedTime() : String = "${this / 1000 / 60}min ${this / 1000 % 60}s"

fun Godzina.asLessonTime() : String = "${this.startTime.hours}:${this.startTime.minutes} - ${this.endTime.hours}:${this.endTime.minutes}"

fun Date.asFormattedHourString() : String = "${this.hours}:${this.minutes}"

fun Int.asSemesterID() : String = "${11381+Date().year+this}"

fun List<Subject>.asSections() : List<Section> = this.map {
    Section().apply {
        setHeader(GradeHeaderItem(it))
        addAll(it.oceny.map { GradeItem(it) })
    }
}

object DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}

