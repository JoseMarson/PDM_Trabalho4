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
import com.eduardoomarson.quizpdm.ui.feature.profile.ProfileSetupScreen

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
object ProfileSetupRoute
@Serializable
object BoardRoute
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
                navigateToProfileScreen = {
                    navController.navigate(ProfileSetupRoute) {
                        popUpTo(SignupRoute) { inclusive = true }
                    }
                },
                navigateToLoginScreen = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }

        composable<ProfileSetupRoute> {
            ProfileSetupScreen(
                onProfileSaved = {
                    navController.navigate(HomeRoute) {
                        popUpTo(ProfileSetupRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                onSinglePlayerClick = {
                    // navController.navigate(QuizRoute) TODO
                },
                onHomeClick = {
                    navController.navigate(HomeRoute)
                },
                onBoardClick = {
                    // navController.navigate(BoardRoute) TODO
                },
                onProfileClick = {
                    navController.navigate(ProfileSetupRoute)
                },
                onLogout = {
                    authViewModel.signout()
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

        /* Exemplo no aplicativo anterior usar como referencia para criar HomeRoute - TODO

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