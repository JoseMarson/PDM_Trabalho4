package com.eduardoomarson.quizpdm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.eduardoomarson.quizpdm.ui.feature.authenticationScreens.login.LoginScreen
import com.eduardoomarson.quizpdm.ui.feature.authenticationScreens.signup.SignupScreen
import com.eduardoomarson.quizpdm.ui.feature.authenticationScreens.forgotpassword.ForgotPasswordScreen
import com.eduardoomarson.quizpdm.authentication.AuthState
import com.eduardoomarson.quizpdm.authentication.AuthViewModel
import com.eduardoomarson.quizpdm.ui.feature.home.HomeScreen

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute
@Serializable
object SignupRoute
@Serializable
object ForgotPasswordRoute
@Serializable
object HomeRoute
@Serializable
data class AddEditRoute(val id: Long?= null)

@Composable
fun QuizAppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState = authViewModel.authState.observeAsState()

    val startDestination = if (authState.value is AuthState.Authenticated) {
        HomeRoute
    } else {
        LoginRoute
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable<LoginRoute> {
            LoginScreen(
                navigateToListScreen = {
                    navController.navigate(HomeRoute) {
                        popUpTo(LoginRoute) { inclusive = true } // Sugestão CLaude
                    }
                },
                navigateToSignUpScreen = {
                    navController.navigate(SignupRoute)
                },
                navigateToForgotPasswordScreen = {
                    navController.navigate(ForgotPasswordRoute)
                },
                authViewModel = authViewModel
            )
        }

        composable<ForgotPasswordRoute> {
            ForgotPasswordScreen(
                navigateBack = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }

        composable<SignupRoute> {
            SignupScreen(
                navigateToListScreen = {
                    navController.navigate(HomeRoute) {
                        popUpTo(SignupRoute) { inclusive = true }
                    }
                },
                navigateToLoginScreen = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                onSinglePlayerClick = {
                    // navController.navigate(QuizRoute) TODO
                },
                onBoardClick = {
                    // navController.navigate(BoardRoute) TODO
                }
            )
        }
    }
}


        /* Exemplo no aplicativo anterior usar como referencia para criar HomeRoute - TODO
        composable<HomeRoute>{
            HomeScreen(
                navigateToAddEditScreen = { id ->
                    navController.navigate(AddEditRoute(id = id))
                },
                navigateToLoginScreen = {
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true } // Sugestão Claude
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable<AddEditRoute>{ backStackEntry ->
            val addEditRoute = backStackEntry.toRoute<AddEditRoute>()
            AddEditScreen(
                id = addEditRoute.id,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

 */