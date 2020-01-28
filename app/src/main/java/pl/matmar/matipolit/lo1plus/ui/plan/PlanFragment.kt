package pl.matmar.matipolit.lo1plus.ui.plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.PlanFragmentBinding
import pl.matmar.matipolit.lo1plus.domain.asSections
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("OnCreate")

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
                }
            }
        })

        viewModel.onSuccessEvent.observe(this, Observer {
            swipeContainer.isRefreshing = false
            it?.let {
                binding.root.snackbar(it, showButton = false)
                sharedPref?.let {
                    with (it.edit()) {
                        putLong(getString(R.string.const_pref_plan_lastrefresh), Date().time)
                        commit()
                    }

                }
                bindUI()
                viewModel.onSuccessEventFinished()
            }
        })

        viewModel.onStartedEvent.observe(this, Observer {
            swipeContainer.isRefreshing = true
            Timber.d("onStartedEvent")
            if (it == true) {
                Timber.d(it.toString())
                //context?.toast("Login started")
                binding.root.snackbar("Odświeżam...", showButton = false)
                viewModel.onStartedEventFinished()
            }
        })

        viewModel.onFailureEvent.observe(this, Observer {
            swipeContainer.isRefreshing = false
            if (it != null) {
                //context?.toast(it)
                binding.recyclerView.snackbar(it, showButton = false)
                bindUI()
                viewModel.onFailureEventFinished()
            }
        })

        swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener() {
            userID?.let {
                viewModel.refreshPlan(it)
            }
        })

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("OnActivityCreated")
    }

    private fun bindUI() {
        viewModel.plan.observe(this, Observer {
            it?.let {
                initRecyclerView(it.planLekcji.asSections())
            }
            viewModel.plan.removeObservers(this)
        })
    }

    private fun initRecyclerView(
        sections: List<Section>
    ){
        Timber.d("Init recyclerView")

        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            for(section in sections){
                add(section)
            }
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
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