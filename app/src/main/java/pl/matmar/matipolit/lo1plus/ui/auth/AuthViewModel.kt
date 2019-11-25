package pl.matmar.matipolit.lo1plus.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository
import timber.log.Timber

class AuthViewModel : ViewModel(){
    //For coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val _email = MutableLiveData<String>()
    val email : LiveData<String>
        get() = _email

    val _password = MutableLiveData<String>()
    val password : LiveData<String>
        get() = _password

    //Events

    private val _onStartedEvent = MutableLiveData<Boolean>()
    val onStartedEvent : LiveData<Boolean>
        get() = _onStartedEvent

    private val _onSuccessEvent = MutableLiveData<String>()
    val onSuccessEvent : LiveData<String>
        get() = _onSuccessEvent

    private val _onFailureEvent = MutableLiveData<Boolean>()
    val onFailureEvent : LiveData<Boolean>
        get() = _onFailureEvent

    //Event functions

    fun onStartedEventFinished(){
        _onStartedEvent.value = false
    }

    fun onSuccessEventFinished(){
        _onSuccessEvent.value = null
    }

    fun onFailureEventFinished(){
        _onFailureEvent.value = false
    }

    fun onLoginButtonClick(){
        Timber.d("Login click")
        _onStartedEvent.value = true
        if (email.value.isNullOrEmpty() || password.value.isNullOrEmpty()){
            //TODO: implement error message
            _onFailureEvent.value = true

            return
        }

        viewModelScope.launch {
            val loginResponse = UserRepository().userLogin(email.value!!, password.value!!)
            _onSuccessEvent.value = loginResponse.value
        }


    }
}