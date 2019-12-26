package pl.matmar.matipolit.lo1plus.ui.grades

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.GradeHeaderLayoutBinding
import pl.matmar.matipolit.lo1plus.domain.Subject

class GradeHeaderItem (
    val subject: Subject
) : BindableItem<GradeHeaderLayoutBinding>(){
    override fun getLayout(): Int  = R.layout.grade_header_layout

    override fun bind(viewBinding: GradeHeaderLayoutBinding, position: Int) {
        viewBinding.subject = subject
    }

}