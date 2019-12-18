package pl.matmar.matipolit.lo1plus.ui.home

import android.os.CountDownTimer
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
import pl.matmar.matipolit.lo1plus.utils.*
import timber.log.Timber
import java.util.*

class HomeViewModel(mHomeRepository: HomeRepository, mUserRepository: UserRepository) : ViewModel(){
    val repository = mHomeRepository
    val userRepository = mUserRepository

    val user = userRepository.user
    val godziny = repository.godziny
    val home = repository.home

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private lateinit var timer: CountDownTimer

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

    private val _titleText = MutableLiveData<String>()
    val titleText: LiveData<String>
        get() = _titleText


    init {
        Timber.d("Init")
        testTimer()
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

    private val _timerData = MutableLiveData<String>()
    val timerData : LiveData<String>
        get() = _timerData

    fun testTimer(){
        kotlin.run {
            val countdownTime = (60*1000).toLong()
            val ONE_SECOND = 1000L
            timer = object : CountDownTimer(countdownTime, ONE_SECOND) {
                override fun onFinish() {
                    _timerData.value = "finish"
                    Timber.d("finish")
                }

                override fun onTick(millisUntilFinished: Long) {
                    _timerData.value = (millisUntilFinished/ONE_SECOND).toString()
                    Timber.d("tick")
                }
            }
            timer.start()
        }
    }

    fun cancelTimer(){
        timer.cancel()
    }

    fun startTimer(godzinyJSON: GodzinyJSON){
        var aktualnaLekcja : Godzina?= null
        var przerwatime : Long? = null
        var następnaLekcja : Godzina? = null
        val godzinyObj = godzinyJSON.godzinyObject
        val godzinyList = godzinyObj.godzinyGodziny
        val dzwonekDelay = godzinyObj.dzwonekDelay
        val jutro = godzinyObj.jutro
        val date = godzinyObj.date
        val currentDate = godzinyObj.dzwonekDelay?.let { Date().toDateWithDelay(it) }?: Date()
        var i = 0
        if(currentDate.time > godzinyObj.date?.time!! && currentDate.time < jutro?.date!!.time) {
            if (godzinyList != null) {
                for (godzina in godzinyList) {
                    if (godzina != null) {
                        if (godzina.startTime.before(currentDate) && godzina.endTime.after(currentDate)) {
                            aktualnaLekcja = godzina
                            break
                        } else if (godzina.endTime.before(currentDate) && godzinyList.size == i){
                            następnaLekcja = godzinyList[i+1]
                            if (następnaLekcja != null) {
                                przerwatime = następnaLekcja.startTime.time - godzina.endTime.time
                                break
                            }
                        }

                    }
                    i++
                }
                if(aktualnaLekcja!=null){

                }
            }
        }else{

        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}