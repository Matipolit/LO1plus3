package pl.matmar.matipolit.lo1plus.ui.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.matmar.matipolit.lo1plus.data.repositories.PlansRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository
import pl.matmar.matipolit.lo1plus.domain.PlansLegend
import pl.matmar.matipolit.lo1plus.domain.PlansPlan
import pl.matmar.matipolit.lo1plus.utils.ApiException
import pl.matmar.matipolit.lo1plus.utils.NoInternetException

class PlansViewModel(mRepository: PlansRepository, mUserRepository: UserRepository) : ViewModel(){

    private val repository = mRepository
    private val userRepository = mUserRepository

    val user = userRepository.user

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _legend = MutableLiveData<PlansLegend>()
    val legend : LiveData<PlansLegend>
        get() = _legend

    private val _currentPlan = MutableLiveData<PlansPlan>()
    val currentPlan : LiveData<PlansPlan>
        get() = _currentPlan

    private val _onStartedEvent = MutableLiveData<Boolean>()
    val onStartedEvent : LiveData<Boolean>
        get() = _onStartedEvent

    private val _onSuccessEvent = MutableLiveData<String?>()
    val onSuccessEvent : LiveData<String?>
        get() = _onSuccessEvent

    private val _onFailureEvent = MutableLiveData<String>()
    val onFailureEvent : LiveData<String>
        get() = _onFailureEvent

    private val _onSelectEvent = MutableLiveData<Boolean>()
    val onSelectEvent : LiveData<Boolean>
        get() = _onSelectEvent

    fun onStartedEventFinished(){
        _onStartedEvent.value = false
    }

    fun onSuccessEventFinished(){
        _onSuccessEvent.value = null
    }

    fun onFailureEventFinished(){
        _onFailureEvent.value = null
    }

    fun onSelectEventFinished(){
        _onSelectEvent.value = false
    }

    fun onSelectEvent(){
        _onSelectEvent.value = true
    }

    fun refreshPlans(){
        _onStartedEvent.value = true
        viewModelScope.launch {
            try {
                repository.refreshPlans()
                _onSuccessEvent.value = "Pomyślnie odświeżono plany"
                return@launch
            }catch (e: ApiException){
                _onFailureEvent.value = e.message
            }catch (e: NoInternetException){
                _onFailureEvent.value = e.message
                return@launch
            }
        }
    }

    fun getPlan(id: String?) {
        if(id!=null) {
            _currentPlan.value = repository.getPlan(id.padStart(3, '0'))
        }else{
            _currentPlan.value = null
        }
    }

    init {
    }
}