package pl.matmar.matipolit.lo1plus.ui.home

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.GodzinyLayoutBinding
import pl.matmar.matipolit.lo1plus.utils.GodzinyView

class GodzinyCardItem(
) : BindableItem<GodzinyLayoutBinding>(){
    override fun getLayout(): Int = R.layout.godziny_layout
    override fun bind(viewBinding: GodzinyLayoutBinding, position: Int) {
    }

    override fun bind(
        viewBinding: GodzinyLayoutBinding,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.bind(viewBinding, position, payloads)
        if (!payloads.isEmpty()){
            if(payloads[0] is GodzinyView){
                viewBinding.godzinyView = payloads[0] as GodzinyView
            }
        }
    }
}