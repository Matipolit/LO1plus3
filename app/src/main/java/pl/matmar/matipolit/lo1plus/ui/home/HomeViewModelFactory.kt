package pl.matmar.matipolit.lo1plus.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.matmar.matipolit.lo1plus.data.repositories.HomeRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory (private val repository: HomeRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}