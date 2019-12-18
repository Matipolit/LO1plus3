package pl.matmar.matipolit.lo1plus.ui.home

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.GodzinyLayoutBinding
import pl.matmar.matipolit.lo1plus.utils.GodzinyJSON

class GodzinyCardItem(
    val godzinyJSON: GodzinyJSON,
    val time : String
) : BindableItem<GodzinyLayoutBinding>(){
    override fun getLayout(): Int = R.layout.godziny_layout
    override fun bind(viewBinding: GodzinyLayoutBinding, position: Int) {
        viewBinding.godziny = godzinyJSON
        viewBinding.time = time
    }

    override fun bind(
        viewBinding: GodzinyLayoutBinding,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.bind(viewBinding, position, payloads)
        if (!payloads.isEmpty()){
            viewBinding.time = payloads[0].toString()
        }
    }
}