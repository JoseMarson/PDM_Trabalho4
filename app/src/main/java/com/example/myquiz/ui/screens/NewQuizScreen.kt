package com.example.myquiz.ui.screens

 import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myquiz.data.model.Question
import com.example.myquiz.data.model.Quiz
import com.example.myquiz.viewmodel.AuthViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.platform.LocalContext
import android.content.Context

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewQuizScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    LaunchedEffect(Unit) {
        authViewModel.startListeningUser()
    }

    val nome = authViewModel.user

    var pergunta by remember { mutableStateOf("") }
    var opcao1 by remember { mutableStateOf("") }
    var opcao2 by remember { mutableStateOf("") }
    var opcao3 by remember { mutableStateOf("") }
    var opcao4 by remember { mutableStateOf("") }
    var nomeQuiz by remember { mutableStateOf("") }

    var respostaCorreta by remember { mutableStateOf("A") }

    val perguntas = remember { mutableStateListOf<Question>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(

                title = { Text("Novo Quiz") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }


    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            // ===== Titulo =====

            item {
                OutlinedTextField(
                    value = nomeQuiz,
                    onValueChange = { nomeQuiz = it },
                    label = { Text("Dê um nome ao Quiz") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))


                Spacer(modifier = Modifier.height(20.dp))
                Divider()
                Spacer(modifier = Modifier.height(20.dp))

            }

            // ===== FORMULÁRIO =====

            item {

                OutlinedTextField(
                    value = pergunta,
                    onValueChange = { pergunta = it },
                    label = { Text("Digite a pergunta") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = opcao1,
                    onValueChange = { opcao1 = it },
                    label = { Text("Escolha A") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = opcao2,
                    onValueChange = { opcao2 = it },
                    label = { Text("Escolha B") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = opcao3,
                    onValueChange = { opcao3 = it },
                    label = { Text("Escolha C") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = opcao4,
                    onValueChange = { opcao4 = it },
                    label = { Text("Escolha D") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Selecione a resposta correta:")

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("A", "B", "C", "D").forEach { letra ->
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            RadioButton(
                                selected = respostaCorreta == letra,
                                onClick = { respostaCorreta = letra }
                            )

                            Text(letra)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (
                            pergunta.isNotBlank() &&
                            opcao1.isNotBlank() &&
                            opcao2.isNotBlank() &&
                            opcao3.isNotBlank() &&
                            opcao4.isNotBlank()
                        ) {

                            perguntas.add(
                                Question(
                                    pergunta,
                                    opcao1,
                                    opcao2,
                                    opcao3,
                                    opcao4,
                                    respostaCorreta,
                                    nomeQuiz
                                )
                            )

                            pergunta = ""
                            opcao1 = ""
                            opcao2 = ""
                            opcao3 = ""
                            opcao4 = ""
                            respostaCorreta = "A"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Adicionar Pergunta")
                }

                Spacer(modifier = Modifier.height(20.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Perguntas adicionadas:",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // ===== LISTA =====

            itemsIndexed(perguntas) { index, item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        // Número da pergunta
                        Text(
                            text = "Pergunta ${index + 1}: ${item.pergunta}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("A) ${item.A}")
                        Text("B) ${item.B}")
                        Text("C) ${item.C}")
                        Text("D) ${item.D}")

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Resposta correta: ${item.respostaCorreta}",
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botão remover
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = { perguntas.removeAt(index) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remover",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }

            //===== SALVAR FORMULARIO  =====
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Divider()
                Spacer(modifier = Modifier.height(20.dp))

                val context = LocalContext.current

                Button(
                    onClick = {


                        if (nomeQuiz.isBlank()) {
                            Toast.makeText(context, "Digite o nome do quiz", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (perguntas.isEmpty()) {
                            Toast.makeText(context, "Adicione pelo menos uma pergunta", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (nomeQuiz.isBlank()) return@Button
                        if (perguntas.isEmpty()) return@Button

                        salvarQuizNoFirestore(
                            context = context,
                            nomeQuiz = nomeQuiz,
                            perguntas = perguntas,
                            onSuccess = {
                                perguntas.clear()
                                nomeQuiz = ""
                                navController.popBackStack()
                            },
                            onError = { erro ->
                                Toast.makeText(context, erro, Toast.LENGTH_SHORT).show()
                            },
//                            nome = nome?.nome.toString()


                        )
                    }
                ){
                    Text("Salvar / Registrar Quiz")
                }


                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

private fun salvarQuizNoFirestore(
    context: Context,
    nomeQuiz: String,
    perguntas: List<Question>,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {

    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (userId == null) {
        onError("Usuário não autenticado")
        return
    }

    val quizRef = db.collection("quizzes").document()

    val quiz = Quiz(
        nome = nomeQuiz,
        criadorId = userId
    )

    quizRef.set(quiz)
        .addOnSuccessListener {

            val batch = db.batch()

            perguntas.forEach { pergunta ->
                val perguntaRef = quizRef
                    .collection("perguntas")
                    .document()

                batch.set(perguntaRef, pergunta)
            }

            batch.commit()
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Quiz salvo com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()

                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onError(e.message ?: "Erro ao salvar perguntas")
                }

        }
        .addOnFailureListener { e ->
            onError(e.message ?: "Erro ao criar quiz")
        }
}


//private fun salvarQuizNoFirestore(
//    context: Context,
//    nomeQuiz: String,
//    perguntas: List<Question>,
//    onSuccess: () -> Unit,
//    onError: (String) -> Unit,
//    nome : String
//
//    ) {
//
//
//    val db = FirebaseFirestore.getInstance()
//    val userId = FirebaseAuth.getInstance().currentUser?.uid
//
//
//    if (userId == null) {
//        onError("Usuário não autenticado")
//        return
//    }
//
//    val quizRef = db.collection("quizzes").document()
//
//    val quiz = Quiz(
//        nome = nomeQuiz,
//        criadorId = nome
//    )
//
//    quizRef.set(quiz)
//        .addOnSuccessListener {
//
//            val batch = db.batch()
//
//            perguntas.forEach { pergunta ->
//
//                val perguntaRef = quizRef
//                    .collection("perguntas")
//                    .document()
//
//                batch.set(perguntaRef, pergunta)
//            }
//
//            batch.commit()
//                .addOnSuccessListener {
//                    Toast.makeText(
//                        context,
//                        "Quiz salvo com sucesso!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                    onSuccess()
//                }
//                .addOnFailureListener { e ->
//                    onError(e.message ?: "Erro ao salvar perguntas")
//                }
//
//        }
//        .addOnFailureListener { e ->
//            onError(e.message ?: "Erro ao criar quiz")
//        }
//}