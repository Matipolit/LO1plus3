package pl.matmar.matipolit.lo1plus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()

    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.navHost_fragment)
        setSupportActionBar(binding.mainToolbar)
        setupActionBarWithNavController(navController, binding.drawerLayout)

        binding.navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.navHost_fragment),
            binding.drawerLayout
        )
    }
}
