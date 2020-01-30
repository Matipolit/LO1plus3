package pl.matmar.matipolit.lo1plus.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import pl.matmar.matipolit.lo1plus.domain.Attendance
import pl.matmar.matipolit.lo1plus.domain.Plan
import java.util.*

class Converters {
    @TypeConverter
    fun fromStringToPlan(value: String): Plan {
        val gson = Gson()
        return gson.fromJson(value, Plan::class.java)
    }

    @TypeConverter
    fun fromPlanToString(plan: Plan): String? {
        val gson = Gson()
        return gson.toJson(plan)
    }

    @TypeConverter
    fun fromStringToAtt(value: String): Attendance {
        val gson = Gson()
        return gson.fromJson(value, Attendance::class.java)
    }

    @TypeConverter
    fun fromAttToString(attendance: Attendance): String? {
        val gson = Gson()
        return gson.toJson(attendance)
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