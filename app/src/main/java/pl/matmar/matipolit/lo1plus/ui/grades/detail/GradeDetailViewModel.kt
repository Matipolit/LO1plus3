package pl.matmar.matipolit.lo1plus.ui.grades.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.matmar.matipolit.lo1plus.domain.Grade

class GradeDetailViewModel(grade: Grade) : ViewModel() {

    private val _selectedGrade = MutableLiveData<Grade>()
    val selectedGrade: LiveData<Grade>
        get() = _selectedGrade

    init {
        _selectedGrade.value = grade
    }
}