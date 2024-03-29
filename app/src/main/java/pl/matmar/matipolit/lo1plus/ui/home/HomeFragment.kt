package pl.matmar.matipolit.lo1plus.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.home_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.HomeFragmentBinding
import pl.matmar.matipolit.lo1plus.ui.SharedViewModel
import pl.matmar.matipolit.lo1plus.utils.*
import timber.log.Timber
import java.util.*


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

    private var decorationsAdded: Boolean = false
    private var godzinyRemoved: Boolean = false
    private var userID: String? = null
    private var bound: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("OnCreateView")
        val binding = HomeFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val swipeContainer = binding.swipeContainer

        val user = sharedViewModel.user

        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.const_pref_key), Context.MODE_PRIVATE)

        var lastRefresh : Long? = null

        viewModel.user.observe(this, Observer { user1 ->
            user1?.let { nonNullUser ->
                userID = nonNullUser.userID
                sharedPref?.let {
                    lastRefresh = sharedPref.getLong(getString(R.string.const_pref_home_lastrefresh), 0L)

                }
                if(isRefreshNeeded(context, lastRefresh)){
                    viewModel.refreshHome(userID!!)
                }else{
                    bindUI()
                    bound = true
                }
            }
        })


        viewModel.onSuccessEvent.observe(this, Observer { succesEventString ->
            swipeContainer.isRefreshing = false
            succesEventString?.let { successEventString ->
                binding.root.snackbar(successEventString, showButton = false)
                sharedPref?.let {
                    with (it.edit()) {
                        putLong(getString(R.string.const_pref_home_lastrefresh), Date().time)
                        commit()
                    }

                }
                if(!bound){
                    bindUI()
                    bound = true
                }
                viewModel.onSuccessEventFinished()
            }
        })

        viewModel.onStartedEvent.observe(this, Observer {
            Timber.d("onStartedEvent")
            if (it == true) {
                swipeContainer.isRefreshing = true
                //context?.toast("Login started")
                binding.root.snackbar("Odświeżam...", showButton = false)
                viewModel.onStartedEventFinished()
            }
        })

        viewModel.onFailureEvent.observe(this, Observer {
            swipeContainer.isRefreshing = false
            if (it != null) {
                //context?.toast(it)
                binding.root.snackbar(it, showButton = false)
                if(!bound){
                    bindUI()
                    bound = true
                }
                viewModel.onFailureEventFinished()
            }
        })

        swipeContainer.setOnRefreshListener {
            userID?.let {
                viewModel.refreshHome(it)
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("OnActivityCreated")
        /*if(bind){
            Timber.d("Bind!")
            bindUI()
        }*/
    }


    private fun bindUI() = Coroutines.main {
        viewModel.home.observe(this, Observer {
            it?.let {
                if(it.isNotEmpty()){
                    Timber.d("initializing recyclerview")
                    val homeCards = it
                    initRecyclerView(homeCards.asHomeCardItem())
                    //viewModel.home.removeObservers(this)
                }
            }
        })
    }

    private fun initRecyclerView(
        homeCardItems: List<HomeCardItem>
    ) {
        var godzinyCardItem: GodzinyCardItem? = null
        var homeSection = Section()

        for(item in homeCardItems){
            if(item.homeCard.title == "Godziny"){
                val godziny = item.homeCard.content.asGodzinyJSON()
                if(viewModel.timerStarted){
                    viewModel.cancelTimer()
                }
                viewModel.startGodziny(godziny)
                godzinyCardItem = GodzinyCardItem()
                homeSection.add(godzinyCardItem)
            }else{
                homeSection.add(item)
            }
        }

        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            add(homeSection)
            //add(0, godzinyCardItem)
        }
        val bgColor = resources.getColor(R.color.colorTransparent)
        val padding = resources.getDimensionPixelSize(R.dimen.small_margin)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context).apply {
                if(!decorationsAdded){
                    addItemDecoration(decoration)
                    decorationsAdded = true
                }
            }
            adapter = mAdapter
        }
        viewModel.timerData.observe(this, Observer {godzinyView ->
            godzinyCardItem?.let {
                if(godzinyView.Visibility == View.VISIBLE){
                    if(godzinyRemoved){
                        homeSection.add(0, godzinyCardItem)
                        godzinyRemoved = false
                    }
                    it.notifyChanged(godzinyView)
                }else{
                    if(!godzinyRemoved){
                        homeSection.remove(godzinyCardItem)
                        godzinyRemoved = true
                    }
                }
            }

        })

        }

    override fun onDestroyView() {
        super.onDestroyView()
        if(viewModel.timerStarted){
            viewModel.cancelTimer()
            viewModel.timerStarted = false
        }
    }
}