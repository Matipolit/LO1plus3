package pl.matmar.matipolit.lo1plus.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import pl.matmar.matipolit.lo1plus.domain.PlanLekcji
import java.util.*

class Converters {
    @TypeConverter
    fun fromString(value: String): PlanLekcji {
        val gson = Gson()
        return gson.fromJson(value, PlanLekcji::class.java)
    }

    @TypeConverter
    fun toString(planLekcji: PlanLekcji): String? {
        val gson = Gson()
        return gson.toJson(planLekcji)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun calFromTimestamp(value: Long): Calendar{
        val cal = Calendar.getInstance()
        cal.timeInMillis = value
        return cal
    }

    @TypeConverter
    fun calToTimestamp(cal: Calendar): Long{
        return cal.timeInMillis
    }

}