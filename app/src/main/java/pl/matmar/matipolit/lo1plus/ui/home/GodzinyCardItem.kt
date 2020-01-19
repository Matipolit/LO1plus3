package pl.matmar.matipolit.lo1plus.ui.home

import com.xwray.groupie.databinding.BindableItem
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.GodzinyLayoutBinding
import pl.matmar.matipolit.lo1plus.utils.GodzinyView

class GodzinyCardItem(
    initialGodzinyView: GodzinyView?
) : BindableItem<GodzinyLayoutBinding>(){
    override fun getLayout(): Int = R.layout.godziny_layout
    override fun bind(viewBinding: GodzinyLayoutBinding, position: Int) {
    }

    val mInitialGodzinyView = initialGodzinyView

    override fun bind(
        viewBinding: GodzinyLayoutBinding,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.bind(viewBinding, position, payloads)
        if (payloads.isNotEmpty()){
            if(payloads[0] is GodzinyView){
                val godzinyView = payloads[0] as GodzinyView
                viewBinding.godzinyView = godzinyView
                viewBinding.root.visibility = godzinyView.Visibility
            }
        }
        mInitialGodzinyView?.let {
            viewBinding.godzinyView = mInitialGodzinyView
            viewBinding.root.visibility = mInitialGodzinyView.Visibility
        }
    }
}