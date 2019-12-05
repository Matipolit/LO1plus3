package pl.matmar.matipolit.lo1plus.data.repositories

import pl.matmar.matipolit.lo1plus.data.database.LO1Database
import pl.matmar.matipolit.lo1plus.data.network.MyApi
import pl.matmar.matipolit.lo1plus.data.network.SafeApiRequest

class HomeRepository(private val api: MyApi,
                     private val database: LO1Database
) : SafeApiRequest(){
    suspend fun homeResponse(){
        
    }

}