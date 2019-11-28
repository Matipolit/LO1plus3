package pl.matmar.matipolit.lo1plus.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import pl.matmar.matipolit.lo1plus.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by lazy {
        //val activity = requireNotNull(this.activity){}
        ViewModelProviders.of(this)
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

        return binding.root
    }
}