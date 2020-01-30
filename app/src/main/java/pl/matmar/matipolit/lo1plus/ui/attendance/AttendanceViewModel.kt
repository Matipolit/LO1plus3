package pl.matmar.matipolit.lo1plus.ui.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.matmar.matipolit.lo1plus.data.repositories.AttendanceRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository
import pl.matmar.matipolit.lo1plus.domain.AttendanceWrapper
import pl.matmar.matipolit.lo1plus.utils.ApiException
import pl.matmar.matipolit.lo1plus.utils.NoInternetException
import pl.matmar.matipolit.lo1plus.utils.asFormattedString
import timber.log.Timber
import java.util.*

class AttendanceViewModel(mRepository: AttendanceRepository, mUserRepository: UserRepository): ViewModel(){

    private val repository = mRepository
    private val userRepository = mUserRepository

    val user = userRepository.user

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val cal = Calendar.getInstance()
        .apply {
            set(Calendar.HOUR_OF_DAY, 0)
            clear(Calendar.MINUTE)
            clear(Calendar.SECOND)
            clear(Calendar.MILLISECOND)
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        }

    private val _calendar = MutableLiveData<Calendar>()
    val calendar : LiveData<Calendar>
        get() = _calendar

    private val _attWrapper = MutableLiveData<AttendanceWrapper>()
    val attWrapper : LiveData<AttendanceWrapper>
        get() = _attWrapper

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

    fun refreshAtt(userId: String){
        _onStartedEvent.value = true
        viewModelScope.launch {
            try {
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                repository.refreshAtt(userId, cal)
                _onSuccessEvent.value = "Pomyślnie odświeżono frekwencję"
                getAtt()
                return@launch
            }catch (e: ApiException){
                _onFailureEvent.value = e.message
                getAtt()
                return@launch
            }catch (e: NoInternetException){
                _onFailureEvent.value = e.message
                getAtt()
                return@launch
            }
        }
    }

    fun getAtt(){
        _attWrapper.value = repository.getAtt(cal)
    }

    fun nextWeek(){
        cal.add(Calendar.WEEK_OF_YEAR, 1)
        _calendar.value = cal
        user.value?.userID?.let {
            refreshAtt(it)
        }
        Timber.d("Added one week: ${cal.asFormattedString()}")
    }

    fun prevWeek(){
        cal.add(Calendar.WEEK_OF_YEAR, -1)
        _calendar.value = cal
        user.value?.userID?.let {
            refreshAtt(it)
        }
        Timber.d("Substracted one week: ${cal.asFormattedString()}")
    }

    init {
        _calendar.value = cal
        getAtt()
    }
}