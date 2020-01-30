package pl.matmar.matipolit.lo1plus.ui.grades.overview

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.GradeHeaderLayoutBinding
import pl.matmar.matipolit.lo1plus.domain.Subject
import timber.log.Timber

class GradeHeaderItem (
    val subject: Subject
) : BindableItem<GradeHeaderLayoutBinding>(){
    override fun getLayout(): Int  = R.layout.grade_header_layout

    override fun bind(viewBinding: GradeHeaderLayoutBinding, position: Int) {
        viewBinding.subject = subject
    }

    init {
        Timber.d("${subject.sredniaFloat}")
    }

}