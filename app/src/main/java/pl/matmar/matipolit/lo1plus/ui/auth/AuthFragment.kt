package pl.matmar.matipolit.lo1plus.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import pl.matmar.matipolit.lo1plus.databinding.AuthFragmentBinding
import pl.matmar.matipolit.lo1plus.utils.hide
import pl.matmar.matipolit.lo1plus.utils.show
import pl.matmar.matipolit.lo1plus.utils.toast
import timber.log.Timber

class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by lazy {
        ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AuthFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //views

        val progressSpinner = binding.progressSpinner

        //observers

        viewModel.onStartedEvent.observe(this, Observer {
            Timber.d("onStartedEvent")
            if(it) {
                progressSpinner.show()
                context?.toast("Login started")
                viewModel.onStartedEventFinished()
            }
        })

        viewModel.onSuccessEvent.observe(this, Observer {
            if(it!=null) {
                progressSpinner.hide()
                context?.toast(it.correct.toString())
                viewModel.onSuccessEventFinished()
            }
        })

        viewModel.onFailureEvent.observe(this, Observer {
            if(it) {
                progressSpinner.hide()
                context?.toast("Login failure")
                viewModel.onFailureEventFinished()
            }
        })

        return binding.root
    }
}