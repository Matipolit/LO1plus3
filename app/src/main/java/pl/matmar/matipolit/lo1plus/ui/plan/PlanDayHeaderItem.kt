package pl.matmar.matipolit.lo1plus.ui.plan

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.PlanDayHeaderBinding
import pl.matmar.matipolit.lo1plus.domain.WeekDay
import pl.matmar.matipolit.lo1plus.ui.shared.ui.StickyItem

class PlanDayHeaderItem(
    val weekDay: WeekDay
) : BindableItem<PlanDayHeaderBinding>(), StickyItem {
    override fun getLayout(): Int = R.layout.plan_day_header

    override fun bind(viewBinding: PlanDayHeaderBinding, position: Int) {
        viewBinding.weekDay = weekDay
    }

    override fun isSticky(): Boolean = true

}