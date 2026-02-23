package com.example.myquiz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
 import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face5
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myquiz.R
import com.example.myquiz.navigation.BottomBar
import com.example.myquiz.ui.components.InfoCard
import com.example.myquiz.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    onLogout: () -> Unit
) {

    LaunchedEffect(Unit) {
        authViewModel.startListeningUser()
    }

    val user = authViewModel.user




    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())

        ) {
            Column {

                //  HEADER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                )
                {

                    // Bloco roxo ocupando toda largura
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color(0xFF12042A))
                    )

                    // Avatar centralizado metade dentro
                    Surface(
                        shape = CircleShape,
//                        color = MaterialTheme.colorScheme.primary,
                        color = Color(0xFF12042A),
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.BottomCenter)
                            .offset(y = 40.dp) // metade do tamanho (160 / 2)
                    )
                    {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PermIdentity,
                                contentDescription = "Usuário",
                                modifier = Modifier.fillMaxSize(0.6f),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(25.dp)) // espaço após avatar

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(40.dp))


                    Text(
                        text = "Olá, ${user?.nome ?: "Anônimo"} 👋",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(40.dp))
//                    text = "Olá, ${user_geral_dados?.pontos ?: "Anônimo"} 👋", // estado

                    InfoCard(
                        title = "Pontuação atual",
                        value = "${user?.pontos ?: 0}   ",
                        iconRes = R.drawable.feitos
                    )


                    Spacer(modifier = Modifier.height(24.dp))

                    InfoCard(
                        title = "Quantidades Quiz finalizados",
                        value = "${user?.quizFeitos?: 0}  ", // quizFeitos
                        iconRes = R.drawable.point
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    InfoCard(
                        title = "Minha posição geral",
                        value = "${user?.posicao?: 0}º  ",
                        iconRes = R.drawable.podio
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }
    }
}