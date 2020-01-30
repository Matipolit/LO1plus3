package pl.matmar.matipolit.lo1plus.ui.attendance.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.matmar.matipolit.lo1plus.domain.Attendance

class AttendanceSummaryViewModel(attendance: Attendance) {

    private val _attendance = MutableLiveData<Attendance>()
    val attendance: LiveData<Attendance>
        get() = _attendance

    init {
        _attendance.value = attendance
    }

}