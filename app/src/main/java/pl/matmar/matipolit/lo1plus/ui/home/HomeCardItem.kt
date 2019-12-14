package pl.matmar.matipolit.lo1plus.ui.home

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.CardLayoutBinding
import pl.matmar.matipolit.lo1plus.domain.HomeCard

class HomeCardItem(
    private val homeCard: HomeCard
) : BindableItem<CardLayoutBinding>(){
    override fun getLayout(): Int = R.layout.card_layout

    override fun bind(viewBinding: CardLayoutBinding, position: Int) {
        viewBinding.card = homeCard
        //TODO add color field to HomeCard
    }

}