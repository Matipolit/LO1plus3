package pl.matmar.matipolit.lo1plus.ui.plans

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
import kotlinx.android.synthetic.main.plan_fragment.info_icon
import kotlinx.android.synthetic.main.plan_fragment.info_text
import kotlinx.android.synthetic.main.plans_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.AppInterface
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.PlansFragmentBinding
import pl.matmar.matipolit.lo1plus.domain.PlansPlan
import pl.matmar.matipolit.lo1plus.domain.asSections
import pl.matmar.matipolit.lo1plus.ui.shared.ui.StickyAdapter
import pl.matmar.matipolit.lo1plus.utils.dpToPx
import pl.matmar.matipolit.lo1plus.utils.isRefreshNeeded
import pl.matmar.matipolit.lo1plus.utils.snackbar
import timber.log.Timber

class PlansFragment : Fragment(), KodeinAware{

    override val kodein by kodein()

    private val factory: PlansViewModelFactory by instance()

    private val viewModel: PlansViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(PlansViewModel::class.java)
    }

    private var decorationsAdded: Boolean = false

    private val fragmentJob = SupervisorJob()
    private val fragmentScope = CoroutineScope(fragmentJob + Dispatchers.Main)
    //private var userID: String? = null
    private var bound: Boolean = false

    lateinit var mInterface: AppInterface

    private var currentPlan : PlansPlan? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("OnCreate")

        mInterface = activity as AppInterface

        val binding = PlansFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val swipeContainer = binding.swipeContainer

        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.const_pref_key), Context.MODE_PRIVATE)

        var lastRefresh : Long? = null
        var lastPlanID : String? = null

        sharedPref?.let {
            lastRefresh = sharedPref.getLong(getString(R.string.const_pref_plans_lastrefresh), 0L)
            lastPlanID = sharedPref.getString(getString(R.string.const_pref_plans_lastplanid), null)
        }

        lastPlanID?.let {
            viewModel.getPlan(it)
        }

        if(isRefreshNeeded(context, lastRefresh)){
            viewModel.refreshPlans()
        }else{
            bindUI()
            bound = true
        }



        viewModel.onSuccessEvent.observe(this, Observer {
            swipeContainer.isRefreshing = false
            it?.let {
                snackBar(binding, it, false)
                sharedPref?.let {
                    with (it.edit()) {
                        putLong(getString(pl.matmar.matipolit.lo1plus.R.string.const_pref_plans_lastrefresh), java.util.Date().time)
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
            swipeContainer.isRefreshing = true
            Timber.d("onStartedEvent")
            if (it == true) {
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
            viewModel.refreshPlans()
        }

        return binding.root
    }

    private fun bindUI(){
        viewModel.currentPlan.observe(this, Observer {
            it?.let {
                val sections = it.asSections()
                if(sections.isNotEmpty()){
                    displayRecycler()
                    initRecyclerView(sections)
                }else{
                    displayInfo(R.drawable.ic_home_plan, "Brak lekcji w wybranym planie", false)
                }
            }?:displayInfo(R.drawable.ic_no_plan, "Nie wybrano jeszcze żadnego planu", true)

        })
    }

    private fun initRecyclerView(sections: List<Section>){

        val mAdapter = StickyAdapter(context)
        mAdapter.apply {
            for(section in sections) {
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
            }
        }
    }

    private fun snackBar(binding: PlansFragmentBinding, message: String, showButton: Boolean, margin: Int? = null){
        binding.root.snackbar(message, showButton, bottomMargin = margin)
    }

    private fun displayRecycler(){
        recycler_view.visibility = View.VISIBLE
        info_icon.visibility = View.GONE
        info_text.visibility = View.GONE
        choose_button.visibility = View.GONE
        mInterface.setToolbarElevation(0f)
    }

    private fun displayInfo(icon: Int, message: String, showButton: Boolean ) {
        recycler_view.visibility = View.GONE
        info_icon.visibility = View.VISIBLE
        info_icon.setImageResource(icon)
        info_text.visibility = View.VISIBLE
        info_text.text = message
        if(showButton) {
            choose_button.visibility = View.VISIBLE
            choose_button.text = "Wybierz plan"
        }else{
            choose_button.visibility = View.GONE
        }
        context?.let {
            mInterface.setToolbarElevation(dpToPx(4f, it))
        }
    }
}