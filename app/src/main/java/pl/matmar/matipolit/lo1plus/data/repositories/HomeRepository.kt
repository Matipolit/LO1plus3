package pl.matmar.matipolit.lo1plus.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.database.LO1Database
import pl.matmar.matipolit.lo1plus.data.database.asDomainModel
import pl.matmar.matipolit.lo1plus.data.network.HomeResponse
import pl.matmar.matipolit.lo1plus.data.network.MyApi
import pl.matmar.matipolit.lo1plus.data.network.SafeApiRequest
import pl.matmar.matipolit.lo1plus.domain.HomeCard
import pl.matmar.matipolit.lo1plus.utils.ApiException
import pl.matmar.matipolit.lo1plus.utils.DEFAULT_CARD_LIST
import pl.matmar.matipolit.lo1plus.utils.asDatabaseModel
import pl.matmar.matipolit.lo1plus.utils.readInstanceProperty
import timber.log.Timber

class HomeRepository(private val api: MyApi,
                     private val database: LO1Database
) : SafeApiRequest(){
    suspend fun refreshHome(_cardlist: List<String>?){
        lateinit var cardlist: List<String>
        _cardlist?.let {
            cardlist = _cardlist
        } ?: run{
            cardlist = DEFAULT_CARD_LIST
        }
        lateinit var homeResponse: HomeResponse
        val gson = Gson()
        user.observe(this, Observer {

        })
        withContext(Dispatchers.IO){

            val cardJson = gson.toJson(cardlist)
            Timber.d("user id: " + id)
            homeResponse = apiRequest{api.home(id!!, cardJson)}

            if(homeResponse.correct == "true"){
                var cards = mutableListOf<HomeCard>()
                for(card in cardlist){
                    cards.add(readInstanceProperty(homeResponse, card))
                }
                saveHome(cards)
            }else{
                homeResponse.info?.let {
                    throw ApiException(homeResponse.info!!)
                }
                throw ApiException("Błąd w żądaniu na serwer")
            }

        }

    }

    private suspend fun saveHome(home: List<HomeCard>) = database.homeDao.insertCards(*home.asDatabaseModel())
    val home : LiveData<List<HomeCard>> = Transformations.map(database.homeDao.getCards()){
        it.asDomainModel()
    }
    private val user = database.userDao.getUser()

}