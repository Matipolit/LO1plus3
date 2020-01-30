package pl.matmar.matipolit.lo1plus.ui.shared.ui

import android.content.Context
import android.view.View
import com.jay.widget.StickyHeaders
import com.jay.widget.StickyHeaders.ViewSetup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.utils.dpToPx


internal class StickyAdapter(mContext: Context?) : GroupAdapter<GroupieViewHolder>(), StickyHeaders,
    ViewSetup {
    val context = mContext
    override fun isStickyHeader(position: Int): Boolean {
        val item = getItem(position)
        return item is StickyItem && (item as StickyItem).isSticky()
    }

    override fun setupStickyHeaderView(stickyHeader: View?) {
        context?.let {
            stickyHeader?.elevation = dpToPx(it.resources.getDimension(R.dimen.elevation_card_normal), it)
        }
    }

    override fun teardownStickyHeaderView(stickyHeader: View?) {
        stickyHeader?.elevation = 0f
    }
}