package pl.matmar.matipolit.lo1plus.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
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

class PlanRepository(private val api: MyApi,
                     private val database: LO1Database):SafeApiRequest(){

    suspend fun refreshPlan(_userID : String, date: String? = null){
        withContext(Dispatchers.IO){

            val response = apiRequest {
                api.plan(_userID, date) }

            if (response.correct == "true"){
                savePlan(response)
            }else{
                response.info?.let {
                    throw ApiException(it)
                }?:throw ApiException("Błąd w żądaniu na serwer")
            }
        }
    }

    private fun savePlan(planResponse: PlanResponse) =
        database.planDao.upsert(planResponse.asDatabaseModel())

    val plan: LiveData<Plan> = Transformations.map(database.planDao.getPlan()){
        it?.asDomainModel()
    }
}