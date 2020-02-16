package pl.matmar.matipolit.lo1plus.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.database.LO1Database
import pl.matmar.matipolit.lo1plus.data.database.asDomainModel
import pl.matmar.matipolit.lo1plus.data.network.MyApi
import pl.matmar.matipolit.lo1plus.data.network.PlansResponse
import pl.matmar.matipolit.lo1plus.data.network.SafeApiRequest
import pl.matmar.matipolit.lo1plus.data.network.asDatabasePlansPlans
import pl.matmar.matipolit.lo1plus.domain.PlansPlan
import pl.matmar.matipolit.lo1plus.domain.asDatabaseModel

class PlansRepository(private val api: MyApi,
                      private val database: LO1Database) : SafeApiRequest(){

    suspend fun refreshPlans(){
        withContext(Dispatchers.IO){

            val response = apiRequest { api.plans() }

            if(response.legend != null && response.plany != null){
                savePlans(response)
            }
        }
    }

    private fun savePlans(plansResponse: PlansResponse){
        database.plansDao.upsertLegend(plansResponse.legend!!.asDatabaseModel())
        val databasePlans = plansResponse.plany!!.asDatabasePlansPlans(plansResponse.legend)
        for(plan in databasePlans){
            database.plansDao.upsertPlan(plan)
        }

    }

    fun getPlan(id: String): PlansPlan? =
        database.plansDao.getPlan(id)?.asDomainModel()

}