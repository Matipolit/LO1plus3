package pl.matmar.matipolit.lo1plus.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.database.LO1Database
import pl.matmar.matipolit.lo1plus.data.database.asDomainModel
import pl.matmar.matipolit.lo1plus.data.network.GradesResponse
import pl.matmar.matipolit.lo1plus.data.network.MyApi
import pl.matmar.matipolit.lo1plus.data.network.SafeApiRequest
import pl.matmar.matipolit.lo1plus.data.network.asDatabaseModel
import pl.matmar.matipolit.lo1plus.domain.Grades
import pl.matmar.matipolit.lo1plus.utils.ApiException
import pl.matmar.matipolit.lo1plus.utils.asSemesterID

class GradesRepository(private val api: MyApi,
                       private val database: LO1Database
) : SafeApiRequest(){
    suspend fun refreshGrades(_userID : String, semesterID: Int? = null){
        withContext(Dispatchers.IO){
            val response = apiRequest { api.grades(_userID, semesterID?.asSemesterID()) }

            if(response.correct == "true"){
                saveGrades(response)
            }else{
                response.info?.let {
                    throw ApiException(it)
                }
            throw ApiException("Błąd w żądaniu na serwer")
            }
        }
    }

    private fun saveGrades(gradesResponse: GradesResponse) =
        database.gradesDao.upsert(gradesResponse.asDatabaseModel())

    val grades: LiveData<Grades> = Transformations.map(database.gradesDao.getGrades()){
        it?.asDomainModel()
    }

}