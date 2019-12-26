package pl.matmar.matipolit.lo1plus.ui.grades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.home_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.databinding.GradesFragmentBinding
import pl.matmar.matipolit.lo1plus.utils.Coroutines
import pl.matmar.matipolit.lo1plus.utils.asSections
import pl.matmar.matipolit.lo1plus.utils.snackbar
import timber.log.Timber


class GradesFragment : Fragment(), KodeinAware{
    override val kodein by kodein()

    private val factory: GradesViewModelFactory by instance()

    private val viewModel: GradesViewModel by lazy {
        ViewModelProviders.of(this, factory)
            .get(GradesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = GradesFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.user.observe(this, Observer {
            it.userID?.let {
                viewModel.refreshGrades(it, 1)
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
                Timber.d(it.toString())
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
        viewModel.grades.observe(this, Observer {
            it?.let {
                var averageHeaderItem : GradesAverageHeaderItem? = null
                if(it.averageVal != null && it.averageText != null){
                    averageHeaderItem = GradesAverageHeaderItem(it)
                }
                initRecyclerView(averageHeaderItem, it.oceny.asSections())
            }
        })
    }

    private fun initRecyclerView(averageHeaderItem: GradesAverageHeaderItem?, sections: List<Section>){
        //TODO add decoration
        Timber.d("Init recyclerView")
        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            averageHeaderItem?.let {
                add(it)
            }
            addAll(sections)
            spanCount = 4
        }

        recycler_view.apply {
            layoutManager = GridLayoutManager(context, mAdapter.spanCount).apply {
                spanSizeLookup = mAdapter.spanSizeLookup
            }
            adapter = mAdapter

        }
    }
}