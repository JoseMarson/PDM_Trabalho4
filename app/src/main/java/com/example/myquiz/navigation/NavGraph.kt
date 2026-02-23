package com.example.myquiz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myquiz.ui.screens.HomeScreen
import com.example.myquiz.ui.screens.ListQuizScreen
import com.example.myquiz.ui.screens.LoginScreen
import com.example.myquiz.ui.screens.RankListScreen
import com.example.myquiz.ui.screens.SignUpScreen
import com.example.myquiz.ui.screens.NewQuizScreen

import com.example.myquiz.viewmodel.AuthViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // usado em tela login
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // usado em tela cadastro
        composable("signup") {
            SignUpScreen(
                authViewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // usado em tela home/perfil
        composable("home") {
            HomeScreen(
                navController = navController,
                onLogout = {
                // authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // usado em tela quiz
        composable( "list_quiz"){
            ListQuizScreen(
                navController = navController
            )
        }

        // usado em tela listar rank
        composable( "list_rank"){
            RankListScreen(
                navController = navController
            )
        }

        // usado para deslogar e voltar a tela principal e deslogar
        composable("logout") {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = {
                    authViewModel.logout()
                    navController.navigate("signup")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
//
        composable("newQuiz") {
            NewQuizScreen(navController)
        }


    }
}