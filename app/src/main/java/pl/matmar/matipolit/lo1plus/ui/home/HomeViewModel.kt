package pl.matmar.matipolit.lo1plus.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.matmar.matipolit.lo1plus.data.database.User
import pl.matmar.matipolit.lo1plus.data.repositories.HomeRepository
import pl.matmar.matipolit.lo1plus.utils.ApiException
import pl.matmar.matipolit.lo1plus.utils.DEFAULT_CARD_LIST
import pl.matmar.matipolit.lo1plus.utils.NoInternetException
import timber.log.Timber

class HomeViewModel(mRepository : HomeRepository) : ViewModel(){
    val repository = mRepository

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    init {
        Timber.d("Init")
    }

    val home = repository.home
    val testString = "wiadomosci"
    private val _onStartedEvent = MutableLiveData<Boolean>()
    val onStartedEvent : LiveData<Boolean>
        get() = _onStartedEvent

    private val _onSuccessEvent = MutableLiveData<String>()
    val onSuccessEvent : LiveData<String>
        get() = _onSuccessEvent

    private val _onFailureEvent = MutableLiveData<String>()
    val onFailureEvent : LiveData<String>
        get() = _onFailureEvent

    fun onStartedEventFinished(){
        _onStartedEvent.value = false
    }

    fun onSuccessEventFinished(){
        _onSuccessEvent.value = null
    }

    fun onFailureEventFinished(){
        _onFailureEvent.value = null
    }

    fun refreshHome(user: User?){
        viewModelScope.launch {
            try {
                repository.refreshHome(DEFAULT_CARD_LIST)
            }catch (e: ApiException){
                _onFailureEvent.value = e.message
                return@launch
            }catch (e: NoInternetException){
                _onFailureEvent.value = e.message
            }
            _onFailureEvent.value = "Błąd odświeżania"
        }
    }

}