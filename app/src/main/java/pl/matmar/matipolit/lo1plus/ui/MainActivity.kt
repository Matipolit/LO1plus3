package pl.matmar.matipolit.lo1plus.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import pl.matmar.matipolit.lo1plus.AppInterface
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity(), AppInterface {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val topLevelDestinations = setOf(
        R.id.authFragment,
        R.id.homeFragment,
        R.id.gradesFragment,
        R.id.planFragment,
        R.id.attendanceFragment,
        R.id.settingsFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()

    }

    private fun setupNavigation() {
        val appBarConfiguration = getAppBarConfiguration(topLevelDestinations)

        navController = findNavController(R.id.navHost_fragment)
        setSupportActionBar(binding.mainToolbar)
        binding.mainToolbar.elevation
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
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

    private fun getAppBarConfiguration(topLevelDestinations : Set<Int>) : AppBarConfiguration{
        return AppBarConfiguration.Builder(topLevelDestinations)
            .setDrawerLayout(binding.drawerLayout)
            .build()
    }

    override fun onSupportNavigateUp(): Boolean {
        return if(drawerLayout != null && topLevelDestinations.contains(navController.currentDestination?.id)){
            Timber.d("Top level")
            drawerLayout.openDrawer(GravityCompat.START)
            true
        }else{
            Timber.d("Lower level")
            NavigationUI.navigateUp(
                Navigation.findNavController(this, R.id.navHost_fragment),
                getAppBarConfiguration(topLevelDestinations))
        }
    }

    override fun onBackPressed() {
        if(topLevelDestinations.contains(navController.currentDestination?.id)){
            ActivityCompat.finishAfterTransition(this)
        }else{
            super.onBackPressed()
        }
    }

    override fun setToolbarElevation(elevation: Float) {
        binding.mainAppbar.elevation = elevation
    }
}
