package pl.matmar.matipolit.lo1plus.ui.attendance.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.matmar.matipolit.lo1plus.data.repositories.AttendanceRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository

class AttendanceOverviewViewModelFactory (private val repository: AttendanceRepository, private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AttendanceOverviewViewModel(
            repository,
            userRepository
        ) as T
    }
}