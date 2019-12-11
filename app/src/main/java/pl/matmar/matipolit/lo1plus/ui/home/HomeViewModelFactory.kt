package pl.matmar.matipolit.lo1plus.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.matmar.matipolit.lo1plus.data.repositories.HomeRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory (private val repository: HomeRepository, private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository, userRepository) as T
    }
}