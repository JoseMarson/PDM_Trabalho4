package com.example.myquiz.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(
    navController: NavHostController
) {

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {

        // perfil ----------------------/
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "perfil"
                )
            },
            label = { Text("Meu perfil") }
        )

        // list quiz ----------------------/
        NavigationBarItem(
            selected = currentRoute == "list_quiz",
            onClick = {
                navController.navigate("list_quiz") {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = "Quiz"
                )
            },
            label = { Text("Quiz") }
        )


        // Rank ----------------------/
        NavigationBarItem(
            selected = currentRoute == "list_rank",
            onClick = {
                navController.navigate("list_rank") {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = "Rank"
                )
            },
            label = { Text("Rank") }
        )

        // Logout ----------------------/
        NavigationBarItem(
            selected = currentRoute == "logout",
            onClick = {
                navController.navigate("logout") {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Sair"
                )
            },
            label = { Text("Sair") }
        )

    }
}