package pl.matmar.matipolit.lo1plus.ui.grades.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.matmar.matipolit.lo1plus.databinding.GradeDetailFragmentBinding

class GradeDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = GradeDetailFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val grade = GradeDetailFragmentArgs.fromBundle(arguments!!).selectedGrade
        val subjectName = GradeDetailFragmentArgs.fromBundle(arguments!!).selectedSubject

        binding.grade = grade
        binding.subjectName = subjectName
        return binding.root
    }
}