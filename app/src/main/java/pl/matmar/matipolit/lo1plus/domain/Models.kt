package pl.matmar.matipolit.lo1plus.domain

import pl.matmar.matipolit.lo1plus.utils.toFormattedSpannable

data class HomeCard(
    val title: String,
    val content: String,
    val color: Int,
    val icon: Int
){
    val formattedContent = content.toFormattedSpannable()
}