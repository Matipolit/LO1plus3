package pl.matmar.matipolit.lo1plus.ui.plan

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.PlanLessonItemBinding
import pl.matmar.matipolit.lo1plus.domain.PlanLesson
import pl.matmar.matipolit.lo1plus.ui.shared.ui.StickyItem

class PlanLessonItem(
    val lesson: PlanLesson,
    val hours: String
) : BindableItem<PlanLessonItemBinding>(), StickyItem {
    override fun getLayout(): Int = R.layout.plan_lesson_item

    override fun bind(viewBinding: PlanLessonItemBinding, position: Int) {
        viewBinding.lesson = lesson
        viewBinding.hours = hours
    }

    override fun isSticky(): Boolean = false
}