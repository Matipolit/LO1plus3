package pl.matmar.matipolit.lo1plus.ui.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.matmar.matipolit.lo1plus.data.repositories.AttendanceRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository

class AttendanceViewModelFactory (private val repository: AttendanceRepository, private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AttendanceViewModel(
            repository,
            userRepository
        ) as T
    }
}