package com.example.myquiz.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myquiz.data.model.Quiz
import com.example.myquiz.navigation.BottomBar
import com.example.myquiz.ui.components.AddQuizCard
import com.example.myquiz.ui.components.InfoCardQuiz
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun ListQuizScreen(
    navController: NavHostController
) {

    val db = FirebaseFirestore.getInstance()

    var listaQuizzes by remember {
        mutableStateOf<List<Quiz>>(emptyList())
    }

     LaunchedEffect(Unit) {
        db.collection("quizzes")
            .orderBy("criadoEm") // ← nome correto do campo
            .get()
            .addOnSuccessListener { result ->
                listaQuizzes = result.documents.mapNotNull { doc ->
                    doc.toObject(Quiz::class.java)?.copy(id = doc.id)
                }
            }
    }

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Jogos disponíveis",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(25.dp))

            AddQuizCard(
                onClick = {
                    navController.navigate("newQuiz")
                },
                textoDescricao = "Criar Novo Quiz"
            )

            Spacer(modifier = Modifier.height(25.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF00030C))
            )

            Spacer(modifier = Modifier.height(20.dp))

            //  AQUI É O QUE VOCÊ QUERIA

            if (listaQuizzes.isEmpty()) {
                Text(
                    text = "Nenhum quiz disponível",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {

                listaQuizzes.forEach { quiz ->

                    InfoCardQuiz(
                        onClick = {
                            // depois você decide o que fazer aqui
                        },
                        quiz.nome,
                        "ID : ${quiz.criadorId}"
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}