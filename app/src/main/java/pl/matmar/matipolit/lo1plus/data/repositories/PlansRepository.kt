package pl.matmar.matipolit.lo1plus.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.database.LO1Database
import pl.matmar.matipolit.lo1plus.data.database.asDomainModel
import pl.matmar.matipolit.lo1plus.data.network.*
import pl.matmar.matipolit.lo1plus.domain.PlansLegend
import pl.matmar.matipolit.lo1plus.domain.PlansPlan
import pl.matmar.matipolit.lo1plus.domain.asDatabaseModel
import timber.log.Timber

class PlansRepository(private val api: MyApi,
                      private val database: LO1Database) : SafeApiRequest(){

    suspend fun refreshPlans(){
        withContext(Dispatchers.IO){

            val response = apiRequest { api.plans() }

            if(response.legend != null){
                Timber.d("saving plans")
                savePlans(response)
            }
        }
    }

    private fun savePlans(plansResponse: PlansResponse){
        database.plansDao.upsertLegend(plansResponse.legend!!.asDatabaseModel())
        val databasePlans = plansResponse.plany.asDomainModel().asDatabasePlansPlans()
        for(plan in databasePlans){
            database.plansDao.upsertPlan(plan)
        }

    }

    fun getPlan(id: String): PlansPlan? =
        database.plansDao.getPlan(id)?.asDomainModel()

    val legend: LiveData<PlansLegend> = Transformations.map(database.plansDao.getLegend()){
        it.asDomainModel()
    }

}