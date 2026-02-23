package com.example.myquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myquiz.navigation.AppNavGraph
import com.example.myquiz.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()

            Surface(
                color = MaterialTheme.colorScheme.background
            ) {

                AppNavGraph(
                    navController = navController,
                    authViewModel = authViewModel
                )

            }
        }
    }
}