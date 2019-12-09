package pl.matmar.matipolit.lo1plus.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.matmar.matipolit.lo1plus.data.database.User

class SharedViewModel () : ViewModel(){
    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    fun setUser(muser: User){
        _user.value = muser
    }
}