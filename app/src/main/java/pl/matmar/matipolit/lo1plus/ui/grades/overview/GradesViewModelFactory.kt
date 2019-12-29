package pl.matmar.matipolit.lo1plus.ui.grades.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.matmar.matipolit.lo1plus.data.repositories.GradesRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class GradesViewModelFactory (private val repository: GradesRepository, private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GradesViewModel(
            repository,
            userRepository
        ) as T
    }
}