package pl.matmar.matipolit.lo1plus.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import android.util.Log
import org.json.JSONObject
import java.util.*

data class Godzina(
    val index: Int,
    val startTime: Date,
    val endTime: Date,
    val name: String
)

data class Jutro(
    val name: String,
    val date: Date,
    val lessonName: String?,
    val lessonTime: Date
)

data class GodzinyJSON(
    val godziny: JSONObject,
    val jutro: String,
    val jutroTime: String,
    val jutroName: String,
    val jutroData: String,
    val dzwonekDelay: Int,
    val data: String
){
    val godzinyObject: Godziny
        get() {
            val dzisiejszaData = data.toDate()
            val jutroDataObj = jutroData.toDate()
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

                val godzinaGodzina = Godzina(sIndex.toInt(), Date(
                    dzisiejszaData.year, dzisiejszaData.month,
                    dzisiejszaData.day, startArray[0].toInt(), startArray[1].toInt()),
                    Date(dzisiejszaData.year, dzisiejszaData.month, dzisiejszaData.day,
                        endArray[0].toInt(), endArray[1].toInt()), godzinaJSON.getString("name"))
                godzinyGodziny.add(godzinaGodzina)

            }
            var jutroTimeArray = jutroTime.split(":")
            var jutroObject = Jutro(jutro, jutroDataObj, jutroName,
                Date(jutroDataObj.year, jutroDataObj.month, jutroDataObj.day,
                    jutroTimeArray[1].toInt(), jutroTimeArray[0].toInt()))
            return Godziny(godzinyGodziny, jutroObject, dzwonekDelay, dzisiejszaData)
        }
}
data class Godziny(
    var godzinyGodziny: List<Godzina>?,
    var jutro: Jutro?,
    var dzwonekDelay: Int?,
    var date: Date
)

data class Tag(var tag: Char?, var start: Int, var end: Int)


//EXTENSION FUNCTIONS

fun String.format(): SpannableStringBuilder{
    var tagi = ArrayList<Tag>()
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
            Log.i("Nowy tag", index.toString() + start.toString() + end.toString())
            tagi.add(tag)
        }
        i++
    }

    //this = this.replace("<i>", "");
    val str = SpannableStringBuilder(this)
    Log.i("Spannable length: ", str.length.toString())
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

fun String.toDate() : Date{
    val dateStringArray = this.split(".")
    return Date(dateStringArray[2].toInt(),
        dateStringArray[1].toInt(), dateStringArray[0].toInt())
}