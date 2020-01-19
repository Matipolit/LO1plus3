package pl.matmar.matipolit.lo1plus.ui.home

import android.text.format.DateUtils
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pl.matmar.matipolit.lo1plus.data.repositories.HomeRepository
import pl.matmar.matipolit.lo1plus.data.repositories.UserRepository
import pl.matmar.matipolit.lo1plus.utils.*
import timber.log.Timber
import java.util.*


class HomeViewModel(mHomeRepository: HomeRepository, mUserRepository: UserRepository) : ViewModel(){
    private val repository = mHomeRepository
    private val userRepository = mUserRepository

    val user = userRepository.user
    val home = repository.home
    var timerStarted = false

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit  var timer : Timer
    //TODO add a way to cancel and start timer

    //for the godziny card

    companion object {
        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L
    }


    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time).split(":")[0] + " min" + DateUtils.formatElapsedTime(time).split(":")[1] + " s"
    }

    private val _titleText = MutableLiveData<String>()
    val titleText: LiveData<String>
        get() = _titleText


    init {
        Timber.d("Init")
        //testTimer()
    }

    private val _onStartedEvent = MutableLiveData<Boolean>()
    val onStartedEvent : LiveData<Boolean>
        get() = _onStartedEvent

    private val _onSuccessEvent = MutableLiveData<String?>()
    val onSuccessEvent : LiveData<String?>
        get() = _onSuccessEvent

    private val _onFailureEvent = MutableLiveData<String>()
    val onFailureEvent : LiveData<String>
        get() = _onFailureEvent

    fun onStartedEventFinished(){
        _onStartedEvent.value = false
    }

    fun onSuccessEventFinished(){
        _onSuccessEvent.value = null
    }

    fun onFailureEventFinished(){
        _onFailureEvent.value = null
    }

    fun refreshHome(userId: String){
        _onStartedEvent.value = true
        viewModelScope.launch {
            try {
                repository.refreshHome(DEFAULT_CARD_LIST, userId)
                _onSuccessEvent.value = "Pomyślnie odświeżono home"
                return@launch
            }catch (e: ApiException){
                _onFailureEvent.value = e.message
                return@launch
            }catch (e: NoInternetException){
                _onFailureEvent.value = e.message
                return@launch
            }
        }
    }

    private val _timerData = MutableLiveData<GodzinyView>()
    val timerData : LiveData<GodzinyView>
        get() = _timerData

    fun startGodziny(godzinyJSON: GodzinyJSON){
        timerStarted = true
        timer = Timer()
        kotlin.run {
            timer.schedule(object: TimerTask(){
                override fun run() {
                    Timber.d("timer tick")
                    var aktualnaLekcja : Godzina?= null
                    var przerwatime : Float? = null
                    var nastepnaLekcja : Godzina? = null
                    val godzinyObj = godzinyJSON.godzinyObject
                    Timber.d("$godzinyJSON")
                    val godzinyList = godzinyObj.godzinyGodziny
                    val dzwonekDelay = godzinyObj.dzwonekDelay
                    val jutro = godzinyObj.jutro
                    var date = godzinyObj.date
                    val currentDate = godzinyObj.dzwonekDelay?.let { System.currentTimeMillis().toDateWithDelay(it) }?: Date()
                    var jutroDate = jutro?.date
                    var i = 0

                    val godzinyView = GodzinyView("Godziny", null, null, null, null, null, null, null, null, null)

                    date = TodayDateAtMidnight()

                    if(jutroDate == null){
                        jutroDate = date.tommorow()
                    }


                    if(currentDate.after(date)&& currentDate.before(jutroDate)){
                        if (godzinyList != null) {
                            //Timber.d("Godziny in viewModel: $godzinyObj")
                            if(godzinyList.isEmpty()){
                                godzinyView.TitleText = "Brak lekcji dzisiaj"
                                godzinyView.FirstMediumText = "Kolejne lekcje - ${jutro?.lessonName} ${jutro?.name?:"jutro"} o ${godzinyJSON.jutroTime}"
                            }else if(godzinyList.last().endTime.before(currentDate)) {
                                if(jutro?.lessonName!= null && jutro.name != null){
                                    godzinyView.TitleText = "Koniec lekcji na dzisiaj"
                                    godzinyView.FirstMediumText = "Kolejne lekcje - ${jutro.lessonName} ${jutro.name} o ${godzinyJSON.jutroTime}"
                                }else{
                                    godzinyView.Visibility = View.GONE
                                }
                            }
                            else if(godzinyList.first().startTime.after(currentDate)){

                                val firstLekcjaStart = godzinyList.first().startTime.time

                                if(firstLekcjaStart - currentDate.time > 900000){
                                    godzinyView.TitleText = "Dzisiaj zaczynasz o"
                                    godzinyView.TimerText = godzinyList.first().startTime.asFormattedHourString()
                                    godzinyView.TimerSubText = godzinyList.first().name
                                }else if(firstLekcjaStart- currentDate.time > 0){
                                    godzinyView.TitleText = "Dzisiaj zaczynasz za"
                                    godzinyView.TimerText = (firstLekcjaStart - currentDate.time).asFormattedTime()
                                    godzinyView.TimerSubText = godzinyList.first().name
                                }
                            }
                            else{
                                for (godzina in godzinyList) {
                                    if(godzinyList.size >= i+2){
                                        nastepnaLekcja = godzinyList[i+1]
                                    }
                                    if (godzina.startTime.before(currentDate) && godzina.endTime.after(currentDate)) {
                                        aktualnaLekcja = godzina
                                        godzinyView.TitleText = "Do końca lekcji"
                                        godzinyView.TimerText = (godzina.endTime.time - currentDate.time).asFormattedTime()
                                        godzinyView.ProgressBar = (100-(godzina.endTime.time - currentDate.time)/ LESSON_TIME_MILIS*100L).toInt()
                                        Timber.d("Miliseconds left: ${(godzina.endTime.time - currentDate.time)/ LESSON_TIME_MILIS}")
                                        if (nastepnaLekcja != null) {
                                            godzinyView.SecondMediumText = "Kolejna lekcja"
                                            godzinyView.SecondSmallText1 = nastepnaLekcja.asLessonTime()
                                            godzinyView.SecondSmallText2 = nastepnaLekcja.name
                                        }
                                        break
                                    } else if (nastepnaLekcja != null){
                                        if(godzina.endTime.before(currentDate) && nastepnaLekcja.startTime.after(currentDate)){
                                            przerwatime = (nastepnaLekcja.startTime.time - godzina.endTime.time).toFloat()
                                            godzinyView.TitleText = "Kolejna lekcja"
                                            godzinyView.FirstMediumText = nastepnaLekcja.asLessonTime()
                                            godzinyView.FirstSmallText = nastepnaLekcja.name
                                            godzinyView.TimeHeaderText = "Do końca przerwy"
                                            val timeLeft = nastepnaLekcja.startTime.time - currentDate.time
                                            godzinyView.TimerText = timeLeft.asFormattedTime()
                                            godzinyView.ProgressBar = (100-(timeLeft/przerwatime*100F)).toInt()
                                            break
                                        }
                                    }
                                    i++ } } }
                    }else{
                        if(currentDate.after(jutroDate)){
                            if(jutro!=null){
                                if(jutro.lessonTime != null){
                                    if(jutro.lessonTime.time - currentDate.time > 900000){
                                        godzinyView.TitleText = "Dzisiaj zaczynasz o"
                                        godzinyView.TimerText = jutro.lessonTime.asFormattedHourString()
                                    }else if(jutro.lessonTime.time - currentDate.time > 0){
                                        godzinyView.TitleText = "Dzisiaj zaczynasz za"
                                        godzinyView.TimerText = (jutro.lessonTime.time - currentDate.time).asFormattedTime()
                                    }else{
                                        godzinyView.Visibility = View.GONE
                                    }

                                }
                            }
                        }
                    }
                    _timerData.postValue(godzinyView)
                    Timber.d(godzinyView.toString())
                }
            },0, 200)
        }
    }

    fun cancelTimer(){
        timer.cancel()
    }


    override fun onCleared() {
        super.onCleared()
        if(timerStarted){
            timer.cancel()
            timerStarted = false
        }
    }
}