package pl.matmar.matipolit.lo1plus.ui.grades

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.GradeLayoutBinding
import pl.matmar.matipolit.lo1plus.domain.Grade

class GradeItem (
    val grade: Grade
) : BindableItem<GradeLayoutBinding>(){
    override fun getLayout(): Int  = R.layout.grade_layout

    override fun bind(viewBinding: GradeLayoutBinding, position: Int) {
        viewBinding.grade = grade
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount / 4
}