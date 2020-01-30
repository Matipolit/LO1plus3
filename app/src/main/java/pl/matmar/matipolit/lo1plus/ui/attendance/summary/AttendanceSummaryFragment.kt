package pl.matmar.matipolit.lo1plus.ui.attendance.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.matmar.matipolit.lo1plus.databinding.AttendanceSummaryFragmentBinding

class AttendanceSummaryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = AttendanceSummaryFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val attendance = AttendanceSummaryFragmentArgs.fromBundle(arguments!!).attendance

        binding.attendance = attendance

        return binding.root
    }
}