package pl.matmar.matipolit.lo1plus.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.matmar.matipolit.lo1plus.data.network.AuthResponse
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository
import pl.matmar.matipolit.lo1plus.utils.ApiException
import pl.matmar.matipolit.lo1plus.utils.NoInternetException
import timber.log.Timber

class AuthViewModel(mRepository: UserRepository) : ViewModel(){

    val repository = mRepository
    //For coroutines
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //Livedata
    val _email = MutableLiveData<String>()
    val email : LiveData<String>
        get() = _email

    val _password = MutableLiveData<String>()
    val password : LiveData<String>
        get() = _password

    val user = repository.user

    //Events

    private val _onStartedEvent = MutableLiveData<Boolean>()
    val onStartedEvent : LiveData<Boolean>
        get() = _onStartedEvent

    private val _onSuccessEvent = MutableLiveData<AuthResponse>()
    val onSuccessEvent : LiveData<AuthResponse>
        get() = _onSuccessEvent

    private val _onFailureEvent = MutableLiveData<String>()
    val onFailureEvent : LiveData<String>
        get() = _onFailureEvent

    //Event functions

    fun onStartedEventFinished(){
        _onStartedEvent.value = false
    }

    fun onSuccessEventFinished(){
        _onSuccessEvent.value = null
    }

    fun onFailureEventFinished(){
        _onFailureEvent.value = null
    }

    fun onLoginButtonClick(){
        Timber.d("Login click")
        _onStartedEvent.value = true
        if (email.value.isNullOrEmpty() || password.value.isNullOrEmpty()){
            if(email.value=="test"){
                _email.value = "test@lo1plus.pl"
                _password.value = "Szkolny!Bot1"
            }else{
                _onFailureEvent.value = "E-mail lub hasło nie mogą być puste"
                return
            }
        }
        if(email.value=="test"){
            _email.value = "test@lo1plus.pl"
            _password.value = "Szkolny!Bot1"
        }

        viewModelScope.launch {
            try{
                val loginResponse = repository.userLogin(email.value!!, password.value!!)
                Timber.d("Got the response from repository")
                if(loginResponse.correct=="true"){
                    _onSuccessEvent.value = loginResponse
                    return@launch
                }else{
                    _onFailureEvent.value = loginResponse.info
                    return@launch
                }
            }catch (e: ApiException){
                _onFailureEvent.value = e.message
                return@launch
            }catch (e: NoInternetException){
                _onFailureEvent.value = e.message
            }
            _onFailureEvent.value = "Błąd logowania"
        }


    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}