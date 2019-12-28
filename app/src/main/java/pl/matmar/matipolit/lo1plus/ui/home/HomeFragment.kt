package pl.matmar.matipolit.lo1plus.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.home_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.databinding.HomeFragmentBinding
import pl.matmar.matipolit.lo1plus.ui.SharedViewModel
import pl.matmar.matipolit.lo1plus.utils.Coroutines
import pl.matmar.matipolit.lo1plus.utils.asGodzinyJSON
import pl.matmar.matipolit.lo1plus.utils.asHomeCardItem
import pl.matmar.matipolit.lo1plus.utils.snackbar
import timber.log.Timber


class HomeFragment : Fragment(), KodeinAware {
    override val kodein by kodein()

    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProviders.of(this)
            .get(SharedViewModel::class.java)
    }

    private val factory: HomeViewModelFactory by instance()

    private val viewModel: HomeViewModel by lazy {
        //val activity = requireNotNull(this.activity){}
        ViewModelProviders.of(this, factory)
            .get(HomeViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val user = sharedViewModel.user
        Timber.d(user.value?.email)

        viewModel.home.observe(this, Observer {
            Timber.d("Home changed")
        })

        viewModel.user.observe(this, Observer {
            Timber.d("user changed")
            it?.let {
                viewModel.refreshHome(it.userID!!)
            }
        })


        viewModel.onSuccessEvent.observe(this, Observer {
            it?.let {
                binding.root.snackbar(it, showButton = false)
                viewModel.onSuccessEventFinished()
            }
        })

        viewModel.onStartedEvent.observe(this, Observer {
            Timber.d("onStartedEvent")
            if (it == true) {
                //context?.toast("Login started")
                binding.root.snackbar("Odświeżam...", showButton = false)
                viewModel.onStartedEventFinished()
            }
        })

        viewModel.onFailureEvent.observe(this, Observer {
            if (it != null) {
                //context?.toast(it)
                binding.root.snackbar(it, showButton = false)
                viewModel.onFailureEventFinished()
            }
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()
    }


    private fun bindUI() = Coroutines.main {
        viewModel.home.observe(this, Observer {
            it?.let {
                val homeCards = it
                initRecyclerView(homeCards.asHomeCardItem())

            }
        })
    }

    private fun initRecyclerView(
        homeCardItems: List<HomeCardItem>
    ) {
        var godzinyCardItem: GodzinyCardItem? = null
        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            for(item in homeCardItems){
                if(item.homeCard.title == "Godziny"){
                    Timber.d("Godziny card in initRecyclerView: ${item.homeCard.content}")
                    val godziny = item.homeCard.content.asGodzinyJSON()
                    if(viewModel.timerStarted){
                        viewModel.cancelTimer()
                    }
                    viewModel.startGodziny(godziny)
                    godzinyCardItem = GodzinyCardItem()
                    add(godzinyCardItem!!)
                }else{
                    add(item)
                }
            }
            //add(0, godzinyCardItem)
        }
            recycler_view.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }
        viewModel.timerData.observe(this, Observer {
            val godzinyView = it
            godzinyCardItem?.let {
                it.notifyChanged(godzinyView)
            }

        })

        }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancelTimer()
    }
}