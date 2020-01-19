package pl.matmar.matipolit.lo1plus.ui.grades.overview

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.GradesFragmentBinding
import pl.matmar.matipolit.lo1plus.domain.Grade
import pl.matmar.matipolit.lo1plus.ui.grades.GradeHeaderItemDecoration
import pl.matmar.matipolit.lo1plus.ui.grades.GradeInsetItemDecoration
import pl.matmar.matipolit.lo1plus.ui.grades.GradesAverageHeaderItemDecoration
import pl.matmar.matipolit.lo1plus.utils.*
import timber.log.Timber
import java.util.*


class GradesFragment : Fragment(), KodeinAware{
    override val kodein by kodein()

    private val factory: GradesViewModelFactory by instance()

    private val viewModel: GradesViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(GradesViewModel::class.java)
    }

    private var decorationsAdded: Boolean = false
    private var scroll: Parcelable? = null

    private val fragmentJob = SupervisorJob()
    private val fragmentScope = CoroutineScope(fragmentJob + Dispatchers.Main)

    private var userID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("OnCreate")
        val binding = GradesFragmentBinding.inflate(inflater)
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
                    lastRefresh = sharedPref.getLong(getString(R.string.const_pref_grades_lastrefresh), 0L)
                }
                if(isRefreshNeeded(context, lastRefresh)){
                    viewModel.refreshGrades(userID!!, 1)
                }
            }
        })

        viewModel.onSuccessEvent.observe(this, Observer {
            swipeContainer.isRefreshing = false
            it?.let {
                binding.root.snackbar(it, showButton = false)
                sharedPref?.let {
                    with (it.edit()) {
                        putLong(getString(R.string.const_pref_grades_lastrefresh), Date().time)
                        commit()
                    }

                }
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

                viewModel.onFailureEventFinished()
            }
        })

        swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener(){
            userID?.let {
                viewModel.refreshGrades(it, 1)
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

    private suspend fun bindUI() = Coroutines.main {
        viewModel.grades.observe(this, Observer {
            it?.let {
                    var averageHeaderItem: GradesAverageHeaderItem? = null
                    if (it.averageVal != null && it.averageText != null) {
                        averageHeaderItem =
                            GradesAverageHeaderItem(
                                it
                            )
                    }
                    initRecyclerView(
                        averageHeaderItem,
                        it.oceny.asSections(GradeItem.OnClickListener { grade, subjectName ->
                            navigateToDetail(grade, subjectName)
                            decorationsAdded = false
                            scroll = recycler_view.layoutManager?.onSaveInstanceState()
                            Timber.d("Scroll: $scroll")
                        })
                    )
                }
        })
    }

    private fun navigateToDetail(grade: Grade, subjectName: String){
        this.findNavController().navigate(GradesFragmentDirections.actionGradesFragmentToGradeDetailFragment(grade, subjectName))
    }

    private fun initRecyclerView(averageHeaderItem: GradesAverageHeaderItem?, sections: List<Section>){
        //TODO dynamically estimate span count according to screen size
        Timber.d("Init recyclerView")
        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            spanCount = GRADES_SPAN
            averageHeaderItem?.let {
                add(it)
            }
            addAll(sections)
        }

        val bgColor = resources.getColor(R.color.colorTransparent)
        val padding = resources.getDimensionPixelSize(R.dimen.small_margin)

        recycler_view.apply {
            layoutManager = GridLayoutManager(context, mAdapter.spanCount).apply {
                spanSizeLookup = mAdapter.spanSizeLookup
                if(!decorationsAdded){
                    addItemDecoration(
                        GradesAverageHeaderItemDecoration(
                            bgColor,
                            padding
                        )
                    )
                    addItemDecoration(
                        GradeHeaderItemDecoration(
                            bgColor,
                            padding
                        )
                    )
                    addItemDecoration(
                        GradeInsetItemDecoration(
                            bgColor,
                            padding
                        )
                    )
                    decorationsAdded = true
                }
            }
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