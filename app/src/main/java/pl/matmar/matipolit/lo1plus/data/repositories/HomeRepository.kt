package pl.matmar.matipolit.lo1plus.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.database.DatabaseGodziny
import pl.matmar.matipolit.lo1plus.data.database.LO1Database
import pl.matmar.matipolit.lo1plus.data.database.asDomainModel
import pl.matmar.matipolit.lo1plus.data.database.asGodzinyJSON
import pl.matmar.matipolit.lo1plus.data.network.HomeResponse
import pl.matmar.matipolit.lo1plus.data.network.MyApi
import pl.matmar.matipolit.lo1plus.data.network.SafeApiRequest
import pl.matmar.matipolit.lo1plus.domain.HomeCard
import pl.matmar.matipolit.lo1plus.utils.*
import timber.log.Timber

class HomeRepository(private val api: MyApi,
                     private val database: LO1Database
) : SafeApiRequest() {

    suspend fun refreshHome(_cardlist: List<String>?, _userId: String) {
        if(isFetchNeeded()){
            //val user = getUser()
            val userID = _userId
            lateinit var cardlist: List<String>
            _cardlist?.let {
                cardlist = _cardlist
            } ?: run {
                cardlist = DEFAULT_CARD_LIST
            }
            lateinit var homeResponse: HomeResponse
            val gson = Gson()

            withContext(Dispatchers.IO) {

                val cardJson = gson.toJson(cardlist)
                homeResponse = apiRequest { api.home(userID, cardJson) }

                if (homeResponse.correct == "true") {
                    val cards = mutableListOf<HomeCard>()
                    for (card in cardlist) {
                        val content : String = readInstanceProperty(homeResponse, card)
                        //if(card!="godziny"){
                                cards.add(HomeCard(card, content, card.toCardColorInt(), card.toCardIcon()))
                        /*}else{
                            Timber.d(content)
                            if(content != "n/a"){
                                saveGodziny(JSONObject(content).asDatabaseGodziny())
                            }else{
                                saveGodziny(null)
                            }
                        }*/
                    }
                    saveHome(cards)
                } else {
                    homeResponse.info?.let {
                        throw ApiException(it)
                    }
                    throw ApiException("Błąd w żądaniu na serwer")
                }

            }

            Timber.d("finished home refresh")
        }else{

        }

        ?: kotlin.run {
            throw ApiException("Nie znaleziono danych użytkownika")
        }

    }

    private fun saveHome(home: List<HomeCard>) =
        database.homeDao.insertCards(*home.asDatabaseModel())

    private fun saveGodziny(godziny: DatabaseGodziny?) =
        database.homeDao.upsertGodziny(godziny)

    val home: LiveData<List<HomeCard>> = Transformations.map(database.homeDao.getCards()) {
        it.asDomainModel()
    }

    val godziny: LiveData<GodzinyJSON> = Transformations.map(database.homeDao.getGodziny()) {
        it?.asGodzinyJSON()
    }

    private fun isFetchNeeded(): Boolean{
        return true
    }
}
