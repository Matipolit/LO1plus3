package pl.matmar.matipolit.lo1plus.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository


@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory (private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(userRepository) as T
    }
}