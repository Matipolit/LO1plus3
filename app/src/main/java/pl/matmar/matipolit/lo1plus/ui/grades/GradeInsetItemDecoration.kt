package pl.matmar.matipolit.lo1plus.ui.grades

import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import pl.matmar.matipolit.lo1plus.ui.shared.decoration.InsetItemDecoration
import pl.matmar.matipolit.lo1plus.utils.INSET
import pl.matmar.matipolit.lo1plus.utils.INSET_TYPE_KEY

internal open class GradeInsetItemDecoration(@ColorInt backgroundColor: Int, @Dimension padding: Int) :
    InsetItemDecoration(backgroundColor, padding, INSET_TYPE_KEY, INSET)