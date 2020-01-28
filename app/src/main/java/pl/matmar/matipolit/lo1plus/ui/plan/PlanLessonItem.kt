package pl.matmar.matipolit.lo1plus.ui.plan

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.PlanLessonItemBinding
import pl.matmar.matipolit.lo1plus.domain.Lekcja

class PlanLessonItem(
    val lesson: Lekcja,
    val hours: String
) : BindableItem<PlanLessonItemBinding>() {
    override fun getLayout(): Int = R.layout.plan_lesson_item

    override fun bind(viewBinding: PlanLessonItemBinding, position: Int) {
        viewBinding.lesson = lesson
        viewBinding.hours = hours
    }
}