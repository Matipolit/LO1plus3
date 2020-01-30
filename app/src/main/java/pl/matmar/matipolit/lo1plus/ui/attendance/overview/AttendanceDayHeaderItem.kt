package pl.matmar.matipolit.lo1plus.ui.attendance.overview

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.AttendanceDayHeaderBinding
import pl.matmar.matipolit.lo1plus.domain.AttDay
import pl.matmar.matipolit.lo1plus.ui.shared.ui.StickyItem

class AttendanceDayHeaderItem (
    val attDay: AttDay
) : BindableItem<AttendanceDayHeaderBinding>(), StickyItem {
    override fun getLayout(): Int = R.layout.attendance_day_header

    override fun bind(viewBinding: AttendanceDayHeaderBinding, position: Int) {
        viewBinding.weekDay = attDay
    }

    override fun isSticky(): Boolean = true

}