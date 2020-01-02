package pl.matmar.matipolit.lo1plus.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository

class SettingsViewModel(mUserRepository: UserRepository) : ViewModel(){

    private val userRepository = mUserRepository

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateToLoginEvent = MutableLiveData<Boolean>()

    val navigateToLoginEvent: LiveData<Boolean>
        get() = _navigateToLoginEvent


    fun removeUser(){
        viewModelScope.launch {
            userRepository.deleteUser()
            _navigateToLoginEvent.value = true
        }
    }

    fun onNavigateToLoginEventFinished(){
        _navigateToLoginEvent.value = false
    }

}
