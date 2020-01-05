package pl.matmar.matipolit.lo1plus.ui.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.matmar.matipolit.lo1plus.data.repositories.PlanRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class PlanViewModelFactory (private val repository: PlanRepository, private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlanViewModel(
            repository,
            userRepository
        ) as T
    }
}