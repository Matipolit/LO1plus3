package pl.matmar.matipolit.lo1plus.ui.grades.overview

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.GradeLayoutBinding
import pl.matmar.matipolit.lo1plus.domain.Grade
import pl.matmar.matipolit.lo1plus.utils.INSET
import pl.matmar.matipolit.lo1plus.utils.INSET_TYPE_KEY

class GradeItem (
    val grade: Grade,
    val subjectName: String,
    val onClickListener: OnClickListener,
    val gradesSpan: Int
) : BindableItem<GradeLayoutBinding>(){
    override fun getLayout(): Int  = R.layout.grade_layout

    override fun bind(viewBinding: GradeLayoutBinding, position: Int) {
        viewBinding.grade = grade
        viewBinding.root.setOnClickListener {
            onClickListener.onClick(grade, subjectName)
        }
    }



    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount / gradesSpan

    init {
        extras.put(INSET_TYPE_KEY, INSET)
    }

    class OnClickListener(val clickListener: (grade: Grade, subjectName: String) -> Unit) {
        fun onClick(grade: Grade, subjectName: String) = clickListener(grade, subjectName)
    }
}

