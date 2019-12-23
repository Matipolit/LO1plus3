package pl.matmar.matipolit.lo1plus.ui.grades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.databinding.GradesFragmentBinding


class GradesFragment : Fragment(), KodeinAware{
    override val kodein by kodein()

    private val factory: GradesViewModelFactory by instance()

    private val viewModel: GradesViewModel by lazy {
        ViewModelProviders.of(this)
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

        return binding.root
    }
}