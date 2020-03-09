package pl.matmar.matipolit.lo1plus.ui.plans.selectPlan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SelectPlanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = pl.matmar.matipolit.lo1plus.databinding.SelectPlanFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this



        return binding.root
    }
}