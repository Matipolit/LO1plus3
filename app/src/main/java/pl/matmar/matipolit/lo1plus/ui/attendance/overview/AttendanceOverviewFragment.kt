package pl.matmar.matipolit.lo1plus.ui.attendance.overview

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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
import pl.matmar.matipolit.lo1plus.databinding.AttendanceOverviewFragmentBinding
import pl.matmar.matipolit.lo1plus.domain.Attendance
import pl.matmar.matipolit.lo1plus.domain.asSections
import pl.matmar.matipolit.lo1plus.ui.shared.ui.StickyAdapter
import pl.matmar.matipolit.lo1plus.utils.dpToPx
import pl.matmar.matipolit.lo1plus.utils.isRefreshNeeded
import pl.matmar.matipolit.lo1plus.utils.setMenuIconTint
import pl.matmar.matipolit.lo1plus.utils.snackbar
import timber.log.Timber
import java.util.*


class AttendanceOverviewFragment : Fragment(), KodeinAware{
    override val kodein by kodein()


    private val factory: AttendanceOverviewViewModelFactory by instance()

    private val viewModel: AttendanceOverviewViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(AttendanceOverviewViewModel::class.java)
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

        //TODO: add menu with navigation to summary fragment

        Timber.d("OnCreate")

        mInterface = activity as AppInterface

        setHasOptionsMenu(true)


        val binding = AttendanceOverviewFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val swipeContainer = binding.swipeContainer

        val snackMargin = binding.bottomButtonsConstraint.height

        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.const_pref_key), Context.MODE_PRIVATE)

        var lastRefresh : Long? = null

        viewModel.user.observe(this, Observer { user ->
            user.userID?.let {
                userID = it
                sharedPref?.let {
                    lastRefresh = sharedPref.getLong(getString(R.string.const_pref_att_lastrefresh), 0L)
                }
                if(isRefreshNeeded(context, lastRefresh)){
                    viewModel.refreshAtt(it)
                }else{
                    bindUI()
                    bound = true
                }
            }
        })

        viewModel.onSuccessEvent.observe(this, Observer {
            swipeContainer.isRefreshing = false
            it?.let {
                snackBar(binding, it, false, snackMargin)
                sharedPref?.let {
                    with (it.edit()) {
                        putLong(getString(R.string.const_pref_att_lastrefresh), Date().time)
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
                snackBar(binding, "Odświeżam...", false, snackMargin)
                viewModel.onStartedEventFinished()
            }
        })

        viewModel.onFailureEvent.observe(this, Observer {
            swipeContainer.isRefreshing = false
            if (it != null) {
                //context?.toast(it)
                snackBar(binding, it, false, snackMargin)
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
                viewModel.refreshAtt(it)
            }
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("OnActivityCreated")
    }

    private fun bindUI() {
        viewModel.attWrapper.observe(this, Observer {
            it?.let {
                val sections = it.attendance.asSections()
                if(sections.isNotEmpty()){
                    displayRecycler()
                    initRecyclerView(it.attendance.asSections())
                }else{
                    displayInfo(R.drawable.ic_menu_attendance, "Brak lekcji w wybranym tygodniu")
                }
            }?: kotlin.run {
                displayInfo(R.drawable.ic_disconnected, "Frekwencja z wybranego tygodnia nie została pobrana do trybu offline")
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

    private fun snackBar(binding: AttendanceOverviewFragmentBinding, message: String, showButton: Boolean, margin: Int? = null){
        binding.root.snackbar(message, showButton, bottomMargin = margin)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.attendance_overview_menu, menu)

        context?.let {
            setMenuIconTint(it, menu, R.id.summary_item, R.color.colorAdaptableHigh)

        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.summary_item -> {
                navigateToSummary(viewModel.attWrapper.value?.attendance)
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToSummary(attendance: Attendance?){
        attendance?.let {
            this.findNavController().navigate(AttendanceOverviewFragmentDirections.actionAttendanceFragmentToAttendanceSummaryFragment(it))
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