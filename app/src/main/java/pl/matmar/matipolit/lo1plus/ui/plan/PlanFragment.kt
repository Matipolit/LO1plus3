package pl.matmar.matipolit.lo1plus.ui.plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.jay.widget.StickyHeadersLinearLayoutManager
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.home_fragment.recycler_view
import kotlinx.android.synthetic.main.plan_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.AppInterface
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.PlanFragmentBinding
import pl.matmar.matipolit.lo1plus.domain.asSections
import pl.matmar.matipolit.lo1plus.ui.shared.ui.StickyAdapter
import pl.matmar.matipolit.lo1plus.utils.dpToPx
import pl.matmar.matipolit.lo1plus.utils.isRefreshNeeded
import pl.matmar.matipolit.lo1plus.utils.snackbar
import timber.log.Timber
import java.util.*

class PlanFragment : Fragment(), KodeinAware{
    override val kodein by kodein()

    private val factory: PlanViewModelFactory by instance()

    private val viewModel: PlanViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(PlanViewModel::class.java)
    }

    private var decorationsAdded: Boolean = false

    private val fragmentJob = SupervisorJob()
    private val fragmentScope = CoroutineScope(fragmentJob + Dispatchers.Main)
    private var userID: String? = null
    private var bound: Boolean = false

    lateinit var mInterface: AppInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("OnCreate")

        mInterface = activity as AppInterface

        val binding = PlanFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val swipeContainer = binding.swipeContainer

        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.const_pref_key), Context.MODE_PRIVATE)

        var lastRefresh : Long? = null

        viewModel.user.observe(this, Observer { user ->
            user.userID?.let {
                userID = it
                sharedPref?.let {
                    lastRefresh = sharedPref.getLong(getString(R.string.const_pref_plan_lastrefresh), 0L)
                }
                if(isRefreshNeeded(context, lastRefresh)){
                    viewModel.refreshPlan(it)
                }else{
                    bindUI()
                    bound = true
                }
            }
        })

        viewModel.onSuccessEvent.observe(this, Observer {
            swipeContainer.isRefreshing = false
            it?.let {
                snackBar(binding, it, false)
                sharedPref?.let {
                    with (it.edit()) {
                        putLong(getString(R.string.const_pref_plan_lastrefresh), Date().time)
                        commit()
                    }

                }
                if(!bound){
                    bindUI()
                    bound = true
                }
                //bindUI()
                viewModel.onSuccessEventFinished()
            }
        })

        viewModel.onStartedEvent.observe(this, Observer {
            Timber.d("onStartedEvent")
            if (it == true) {
                swipeContainer.isRefreshing = true
                Timber.d(it.toString())
                //context?.toast("Login started")
                snackBar(binding, "Odświeżam...", false)
                viewModel.onStartedEventFinished()
            }
        })

        viewModel.onFailureEvent.observe(this, Observer {
            swipeContainer.isRefreshing = false
            if (it != null) {
                //context?.toast(it)
                snackBar(binding, it, false)
                //bindUI()
                viewModel.onFailureEventFinished()
                if(!bound){
                    bindUI()
                    bound = true
                }
            }
        })

        swipeContainer.setOnRefreshListener {
            userID?.let {
                viewModel.refreshPlan(it)
            }
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("OnActivityCreated")
    }

    private fun bindUI() {
        viewModel.planWrapper.observe(this, Observer {
            it?.let {
                val sections = it.plan.asSections()
                if(sections.isNotEmpty()){
                    displayRecycler()
                    initRecyclerView(sections)
                }else{
                    displayInfo(R.drawable.ic_home_plan, "Brak lekcji w wybranym tygodniu")
                }
            }?: kotlin.run {
                displayInfo(R.drawable.ic_disconnected, "Plan z wybranego tygodnia nie został pobrany do trybu offline")
            }
            //viewModel.plan.removeObservers(this)
        })
    }

    private fun initRecyclerView(
        sections: List<Section>
    ){
        Timber.d("Init recyclerView")

        val mAdapter = StickyAdapter(context)
        mAdapter.apply {
            for(section in sections){
                add(section)
            }
        }

        recycler_view.apply {
            layoutManager = StickyHeadersLinearLayoutManager<StickyAdapter>(context)
            adapter = mAdapter
            if(!decorationsAdded){
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                decorationsAdded = true
            }
        }
    }

    private fun snackBar(binding: PlanFragmentBinding, message: String, showButton: Boolean){
        binding.coordinator.snackbar(message, showButton)
    }

    private fun displayRecycler(){
        recycler_view.visibility = View.VISIBLE
        info_icon.visibility = View.GONE
        info_text.visibility = View.GONE
        mInterface.setToolbarElevation(0f)
    }

    private fun displayInfo(icon: Int, message: String ) {
        recycler_view.visibility = View.GONE
        info_icon.visibility = View.VISIBLE
        info_icon.setImageResource(icon)
        info_text.visibility = View.VISIBLE
        info_text.text = message
        context?.let {
            mInterface.setToolbarElevation(dpToPx(4f, it))
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("OnAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("OnDetach")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("OnDestroy")
    }
}