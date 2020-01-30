package pl.matmar.matipolit.lo1plus.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.database.LO1Database
import pl.matmar.matipolit.lo1plus.data.database.asDomainModel
import pl.matmar.matipolit.lo1plus.data.network.MyApi
import pl.matmar.matipolit.lo1plus.data.network.PlanResponse
import pl.matmar.matipolit.lo1plus.data.network.SafeApiRequest
import pl.matmar.matipolit.lo1plus.data.network.asDatabaseModel
import pl.matmar.matipolit.lo1plus.domain.Plan
import pl.matmar.matipolit.lo1plus.utils.ApiException
import pl.matmar.matipolit.lo1plus.utils.asFormattedString
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class PlanRepository(private val api: MyApi,
                     private val database: LO1Database):SafeApiRequest(){

    suspend fun refreshPlan(_userID : String, cal: Calendar){
        withContext(Dispatchers.IO){

            val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
            val date = format.format(cal.time)

            Timber.d("Date: $date")

            val response = apiRequest {
                api.plan(_userID, date) }

            if (response.correct == "true"){
                savePlan(response, cal)
            }else{
                response.info?.let {
                    throw ApiException(it)
                }?:throw ApiException("Błąd w żądaniu na serwer")
            }
        }
    }

    private fun savePlan(planResponse: PlanResponse, cal: Calendar) =
        database.planDao.upsert(planResponse.asDatabaseModel(cal))

    private fun clearPlans(){
        Timber.d("Clearing plans")
        val cal = Calendar.getInstance()
            .apply {
                set(Calendar.HOUR_OF_DAY, 0)
                clear(Calendar.MINUTE)
                clear(Calendar.SECOND)
                clear(Calendar.MILLISECOND)
                set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            }
        cal.add(Calendar.WEEK_OF_YEAR, 5)
        val maxCal = cal
        Timber.d(maxCal.asFormattedString())
        cal.add(Calendar.WEEK_OF_YEAR, -10)
        val minCal = cal
        Timber.d(minCal.asFormattedString())
        database.planDao.clearPlans(minCal.timeInMillis, maxCal.timeInMillis)
    }

    fun getPlan(cal: Calendar): Plan? =
        database.planDao.getPlan(cal.timeInMillis)?.asDomainModel()

    init {
        clearPlans()
    }
}