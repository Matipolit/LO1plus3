package pl.matmar.matipolit.lo1plus.ui.home

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.matmar.matipolit.lo1plus.data.repositories.HomeRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository
import pl.matmar.matipolit.lo1plus.utils.ApiException
import pl.matmar.matipolit.lo1plus.utils.DEFAULT_CARD_LIST
import pl.matmar.matipolit.lo1plus.utils.NoInternetException
import timber.log.Timber

class HomeViewModel(mHomeRepository: HomeRepository, mUserRepository: UserRepository) : ViewModel(){
    val repository = mHomeRepository
    val userRepository = mUserRepository

    val user = userRepository.user
    val godziny = repository.godziny
    val home = repository.home

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //for the godziny card

    companion object {
        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L
    }


    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time).split(":")[0] + " min" + DateUtils.formatElapsedTime(time).split(":")[1] + " s"
    }


    init {
        Timber.d("Init")
    }

    private val _onStartedEvent = MutableLiveData<Boolean>()
    val onStartedEvent : LiveData<Boolean>
        get() = _onStartedEvent

    private val _onSuccessEvent = MutableLiveData<String?>()
    val onSuccessEvent : LiveData<String?>
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

    fun refreshHome(userId: String){
        _onStartedEvent.value = true
        viewModelScope.launch {
            try {
                repository.refreshHome(DEFAULT_CARD_LIST, userId)
                _onSuccessEvent.value = "Pomyślnie odświeżono home"
                return@launch
            }catch (e: ApiException){
                _onFailureEvent.value = e.message
                return@launch
            }catch (e: NoInternetException){
                _onFailureEvent.value = e.message
                return@launch
            }
        }
    }


}