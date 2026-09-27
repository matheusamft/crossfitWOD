package com.ifsp.crossfitwod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.ifsp.crossfitwod.data.datastore.WodDataStore
import com.ifsp.crossfitwod.data.repository.WodRepository
import com.ifsp.crossfitwod.ui.navigation.AppNavigation
import com.ifsp.crossfitwod.ui.theme.CrossfitWODTheme
import com.ifsp.crossfitwod.viewmodel.WodViewModel
import com.ifsp.crossfitwod.viewmodel.WodViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val dataStore = WodDataStore(applicationContext)
        val repository = WodRepository(dataStore)
        val factory = WodViewModelFactory(repository)
        val viewModel: WodViewModel by viewModels { factory }

        enableEdgeToEdge()
        setContent {
            CrossfitWODTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}