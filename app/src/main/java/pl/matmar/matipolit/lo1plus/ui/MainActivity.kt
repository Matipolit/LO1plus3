package pl.matmar.matipolit.lo1plus.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val topLevelDestinations = setOf(R.id.homeFragment,
        R.id.gradesFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()

    }

    private fun setupNavigation() {
        val appBarConfiguration = getAppBarConfiguration(topLevelDestinations)

        val navController = findNavController(R.id.navHost_fragment)
        setSupportActionBar(binding.mainToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener{controller, destination, arguments ->
            when(destination.id){
                R.id.authFragment -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    binding.mainToolbar.visibility = View.GONE
                    //actionBar?.setDisplayHomeAsUpEnabled(false)
                    //actionBar?.setDisplayShowHomeEnabled(false)
                    /*    actionBar.setHomeButtonEnabled(false); // disable the button
                    actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
                    actionBar.setDisplayShowHomeEnabled(false);*/
                }
                else -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    binding.mainToolbar.visibility = View.VISIBLE
                }
            }
        }

        binding.navView.setupWithNavController(navController)
    }

    fun getAppBarConfiguration(topLevelDestinations : Set<Int>) : AppBarConfiguration{
        return AppBarConfiguration.Builder(topLevelDestinations)
            .setDrawerLayout(binding.drawerLayout)
            .build()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.navHost_fragment),
            getAppBarConfiguration(topLevelDestinations))
    }
}
