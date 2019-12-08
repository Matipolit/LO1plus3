package pl.matmar.matipolit.lo1plus.data.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.database.Home
import pl.matmar.matipolit.lo1plus.data.database.LO1Database
import pl.matmar.matipolit.lo1plus.data.database.User
import pl.matmar.matipolit.lo1plus.data.network.HomeResponse
import pl.matmar.matipolit.lo1plus.data.network.MyApi
import pl.matmar.matipolit.lo1plus.data.network.SafeApiRequest
import pl.matmar.matipolit.lo1plus.utils.ApiException
import pl.matmar.matipolit.lo1plus.utils.toCardName

class HomeRepository(private val api: MyApi,
                     private val database: LO1Database
) : SafeApiRequest(){
    suspend fun homeResponse() : HomeResponse{
        lateinit var homeResponse: HomeResponse
        /*withContext(Dispatchers.Main){
            try{
                val response: ResponseBody = LoginApi.retrofitService.userLogin(email, password).await()
                homeResponse.value = response.string()
            }catch (e: Exception) {
                e.printStackTrace()
                homeResponse.value = e.message
            }
        }
         */
        withContext(Dispatchers.IO){
            val id = database.userDao.getUser().value?.userID
            //homeResponse = LoginApi.login.userLogin(email, password)
            //TODO finish implementing this repository
            val cardlist = home.value?.card_list?.map { it.toCardName() }
            homeResponse = apiRequest{api.home(id, home.value.)}
        }
        if(homeResponse.correct.equals("true")){
            val user = User(homeResponse.userID, homeResponse.admin, homeResponse.obiadyAdmin, email)
            withContext(Dispatchers.IO){
                saveUser(user)
            }
            return homeResponse
        }else{
            homeResponse.info?.let {
                throw ApiException(homeResponse.info!!)
            }
            if(homeResponse.correct.equals("notLO1")){
                throw ApiException("Nie jesteś uczniem LO1")
            }
            throw ApiException("Zły email lub hasło")
        }
    }
    val home : LiveData<Home> = database.homeDao.getHome()
}