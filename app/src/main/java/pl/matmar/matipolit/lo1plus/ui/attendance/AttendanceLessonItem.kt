package pl.matmar.matipolit.lo1plus.ui.attendance

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.AttendanceLessonItemBinding
import pl.matmar.matipolit.lo1plus.domain.AttLesson
import pl.matmar.matipolit.lo1plus.ui.shared.ui.StickyItem

class AttendanceLessonItem (
    val lesson: AttLesson,
    val hours: String
) : BindableItem<AttendanceLessonItemBinding>(), StickyItem {
    override fun getLayout(): Int = R.layout.attendance_lesson_item

    override fun bind(viewBinding: AttendanceLessonItemBinding, position: Int) {
        viewBinding.lesson = lesson
        viewBinding.hours = hours
    }

    override fun isSticky(): Boolean = false
}