package pl.matmar.matipolit.lo1plus.ui.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.matmar.matipolit.lo1plus.data.repositories.PlansRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository

class PlansViewModelFactory (private val repository: PlansRepository, private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlansViewModel(
            repository,
            userRepository
        ) as T
    }
}