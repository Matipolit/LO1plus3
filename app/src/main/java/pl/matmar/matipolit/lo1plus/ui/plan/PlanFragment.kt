package pl.matmar.matipolit.lo1plus.ui.plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.PlanFragmentBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("OnCreate")

        val binding = PlanFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.const_pref_key), Context.MODE_PRIVATE)

        var lastRefresh : Long? = null

        viewModel.user.observe(this, Observer { user ->
            user.userID?.let {
                sharedPref?.let {
                    lastRefresh = sharedPref.getLong(getString(R.string.const_pref_plan_lastrefresh), 0L)
                }
                if(isRefreshNeeded(context, lastRefresh)){
                    viewModel.refreshPlan(it)
                }
            }
        })

        viewModel.onSuccessEvent.observe(this, Observer {
            it?.let {
                binding.root.snackbar(it, showButton = false)
                sharedPref?.let {
                    with (it.edit()) {
                        putLong(getString(R.string.const_pref_plan_lastrefresh), Date().time)
                        commit()
                    }

                }
                viewModel.onSuccessEventFinished()
            }
        })

        viewModel.onStartedEvent.observe(this, Observer {
            Timber.d("onStartedEvent")
            if (it == true) {
                Timber.d(it.toString())
                //context?.toast("Login started")
                binding.root.snackbar("Odświeżam...", showButton = false)
                viewModel.onStartedEventFinished()
            }
        })

        viewModel.onFailureEvent.observe(this, Observer {
            if (it != null) {
                //context?.toast(it)
                binding.recyclerView.snackbar(it, showButton = false)

                viewModel.onFailureEventFinished()
            }
        })

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentScope.launch {
            bindUI()
        }
        Timber.d("OnActivityCreated")
    }

    private fun bindUI() {
        viewModel.plan.observe(this, Observer {
            it?.let {

            }
        })
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