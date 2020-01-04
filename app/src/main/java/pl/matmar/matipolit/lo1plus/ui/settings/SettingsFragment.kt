package pl.matmar.matipolit.lo1plus.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pl.matmar.matipolit.lo1plus.R


class SettingsFragment : PreferenceFragmentCompat(), KodeinAware {

    override val kodein by kodein()

    private val factory: SettingsViewModelFactory by instance()

    private val viewModel: SettingsViewModel by lazy {
        //val activity = requireNotNull(this.activity){}
        ViewModelProviders.of(this, factory)
            .get(SettingsViewModel::class.java)
    }

    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navController = this.findNavController()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        viewModel.navigateToLoginEvent.observe(this, Observer {
            if(it){
                navController.navigate(R.id.action_settingsFragment_to_authFragment)
                viewModel.onNavigateToLoginEventFinished()
            }
        })

        val logoutPreference: Preference? = findPreference("logout")
        logoutPreference?.setOnPreferenceClickListener {
            kotlin.run {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                viewModel.removeUser()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Czy na pewno chcesz się wylogować?")
                    .setPositiveButton("Tak", dialogClickListener)
                    .setNegativeButton("Nie", dialogClickListener)
                    .show()
            }
            true
        }

    }


}