package pl.matmar.matipolit.lo1plus.ui.grades

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.GradesAverageHeaderLayoutBinding
import pl.matmar.matipolit.lo1plus.domain.Grades

class GradesAverageHeaderItem (
    val grades : Grades
) : BindableItem<GradesAverageHeaderLayoutBinding>(){
    override fun getLayout(): Int  = R.layout.grades_average_header_layout

    override fun bind(viewBinding: GradesAverageHeaderLayoutBinding, position: Int) {
        viewBinding.grades = grades
    }

}