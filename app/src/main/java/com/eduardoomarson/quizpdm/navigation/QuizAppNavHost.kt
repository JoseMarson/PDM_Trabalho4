package com.eduardoomarson.quizpdm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.eduardoomarson.quizpdm.ui.feature.authenticationScreens.login.LoginScreen
import com.eduardoomarson.quizpdm.ui.feature.authenticationScreens.signup.SignupScreen
import com.eduardoomarson.quizpdm.ui.feature.authenticationScreens.forgotpassword.ForgotPasswordScreen
import com.eduardoomarson.quizpdm.authentication.AuthState
import com.eduardoomarson.quizpdm.authentication.AuthViewModel
import com.eduardoomarson.quizpdm.ui.feature.board.BoardScreen
import com.eduardoomarson.quizpdm.ui.feature.createquiz.CreateQuizScreen
import com.eduardoomarson.quizpdm.ui.feature.home.HomeScreen
import com.eduardoomarson.quizpdm.ui.feature.home.components.Model.UserModel
import com.eduardoomarson.quizpdm.ui.feature.profile.ProfileSetupScreen
import com.eduardoomarson.quizpdm.ui.feature.quiz.QuizScreen
import com.eduardoomarson.quizpdm.ui.feature.quizlist.QuizListScreen


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
object CreateQuizRoute
@Serializable
object QuizRandomRoute
@Serializable
object ProfileSetupRoute
@Serializable
object BoardRoute
@Serializable
object HistoryRoute
@Serializable
data class QuizListRoute(val category: String)
@Serializable
data class QuizByIdRoute(val quizId: String)

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

        composable<HistoryRoute> {
            // HistoryScreen — implementar depois - TODO
        }

        composable<CreateQuizRoute> {
            CreateQuizScreen(
                onQuizSaved = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<QuizRandomRoute> {
            QuizScreen(
                category = null,
                onBackClick = { navController.popBackStack() },
                onFinish = {
                    navController.navigate(HomeRoute) {
                        popUpTo(HomeRoute) { inclusive = false }
                    }
                }
            )
        }

        composable<QuizListRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<QuizListRoute>()
            QuizListScreen(
                category = route.category,
                onQuizClick = { quizId ->
                    navController.navigate(QuizByIdRoute(quizId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<QuizByIdRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<QuizByIdRoute>()
            QuizScreen(
                quizId = route.quizId,
                onBackClick = { navController.popBackStack() },
                onFinish = { navController.navigate(HomeRoute) {
                    popUpTo(HomeRoute) { inclusive = false } // Sugestão Claude: Para limpar a pilha
                }
                }
            )
        }

        //====================================================    Editado por samuel 160 e 167
        composable<BoardRoute> {
            BoardScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        //====================================================

        composable<HomeRoute> {
            HomeScreen(
                onSinglePlayerClick = {
                    navController.navigate(QuizRandomRoute)
                },
                onCategoryClick = { category ->
                    navController.navigate(QuizListRoute(category))
                },
                onHomeClick = {
                    navController.navigate(HomeRoute)
                },
                onBoardClick = {  // <----- Editado por samuel
                     navController.navigate(BoardRoute)  // <----- Editado por samuel
                }, // <----- Editado por samuel
                onHistoryClick = {
                    //navController.navigate(HistoryRoute) TODO
                },
                onProfileClick = {
                    navController.navigate(ProfileSetupRoute)
                },
                onCreateQuizClick = { navController.navigate(CreateQuizRoute) },
                onNeedsProfileSetup = {
                    navController.navigate(ProfileSetupRoute) {
                        popUpTo(HomeRoute) { inclusive = false }
                    }
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
