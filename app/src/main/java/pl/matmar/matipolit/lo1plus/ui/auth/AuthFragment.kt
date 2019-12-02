package pl.matmar.matipolit.lo1plus.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.AuthFragmentBinding
import pl.matmar.matipolit.lo1plus.utils.hide
import pl.matmar.matipolit.lo1plus.utils.show
import pl.matmar.matipolit.lo1plus.utils.snackbar
import timber.log.Timber


class AuthFragment : Fragment(), KodeinAware {

    lateinit var navController: NavController

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private val viewModel: AuthViewModel by lazy {
        val activity = requireNotNull(this.activity){}
        ViewModelProviders.of(this, factory)
            .get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AuthFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        navController = this.findNavController()




        //views

        val progressSpinner = binding.progressSpinner

        binding.agreementText.setOnClickListener(View.OnClickListener {
            privacyTextClick()
        })

        //observers

        viewModel.user.observe(this, Observer {
            Timber.d("user changed")
            it?.let {
                navController.navigate(R.id.action_authFragment_to_homeFragment)

            }
        })

        viewModel.onStartedEvent.observe(this, Observer {
            Timber.d("onStartedEvent")
            if(it) {
                progressSpinner.show()
                //context?.toast("Login started")
                binding.root.snackbar("Loguję...", showButton = false)
                viewModel.onStartedEventFinished()
            }
        })

        viewModel.onSuccessEvent.observe(this, Observer {
            if(it!=null) {
                progressSpinner.hide()
                //context?.toast(it.correct)
                it.userID?.let { it1 -> binding.root.snackbar(it1, showButton = false) }
                viewModel.onSuccessEventFinished()
            }
        })

        viewModel.onFailureEvent.observe(this, Observer {
            if(it!=null) {
                progressSpinner.hide()
                //context?.toast(it)
                binding.root.snackbar(it, showButton = false)

                viewModel.onFailureEventFinished()
            }
        })

        return binding.root
    }
    fun privacyTextClick(){
        Timber.d("PrivacyTextClick")
        navController.navigate(R.id.privacyPolicy_endpoint)
    }
}