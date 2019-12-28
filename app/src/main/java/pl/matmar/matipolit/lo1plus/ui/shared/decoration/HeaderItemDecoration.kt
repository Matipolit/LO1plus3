package pl.matmar.matipolit.lo1plus.ui.shared.decoration

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

open class HeaderItemDecoration(@ColorInt background: Int, private val sidePaddingPixels: Int, @param:LayoutRes private val headerViewType: Int) :
    ItemDecoration() {
    private val paint: Paint
    fun isHeader(child: View?, parent: RecyclerView): Boolean {
        val viewType = parent.layoutManager!!.getItemViewType(child!!)
        return viewType == headerViewType
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (!isHeader(view, parent)) return
        outRect.left = sidePaddingPixels
        outRect.right = sidePaddingPixels
        outRect.top = sidePaddingPixels
        outRect.bottom = sidePaddingPixels
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (!isHeader(child, parent)) continue
            val lm = parent.layoutManager
            val top = lm!!.getDecoratedTop(child) + child.translationY
            var bottom = lm.getDecoratedBottom(child) + child.translationY
            if (i == parent.childCount - 1) { // Draw to bottom if last item
                bottom = Math.max(parent.height.toFloat(), bottom)
            }
            val right = lm.getDecoratedRight(child) + child.translationX
            val left = lm.getDecoratedLeft(child) + child.translationX
            c.drawRect(left, top, right, bottom, paint)
        }
    }

    init {
        paint = Paint()
        paint.color = background
    }
}