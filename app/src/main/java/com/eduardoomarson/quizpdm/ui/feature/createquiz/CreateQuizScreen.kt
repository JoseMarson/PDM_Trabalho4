package com.eduardoomarson.quizpdm.ui.feature.createquiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eduardoomarson.quizpdm.R

@Composable
fun CreateQuizScreen(
    onQuizSaved: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: CreateQuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onQuizSaved()
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    CreateQuizContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onEvent = { viewModel.onEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizContent(
    uiState: CreateQuizUiState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClick: () -> Unit = {},
    onEvent: (CreateQuizEvent) -> Unit = {}
) {
    // Controla o dialog de adicionar questão
    var showAddQuestionDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Criar Quiz", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Título ──
            item {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { onEvent(CreateQuizEvent.OnTitleChange(it)) },
                    label = { Text("Título do Quiz") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !uiState.isLoading
                )
            }

            // ── Descrição ──
            item {
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { onEvent(CreateQuizEvent.OnDescriptionChange(it)) },
                    label = { Text("Descrição (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    enabled = !uiState.isLoading
                )
            }

            // ── Categoria ──
            item {
                Text("Categoria", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                availableCategories.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { category ->
                            val isSelected = uiState.selectedCategory == category
                            FilterChip(
                                selected = isSelected,
                                onClick = { onEvent(CreateQuizEvent.OnCategoryChange(category)) },
                                label = { Text(category, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colorResource(R.color.navy_blue),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // ── Dificuldade ──
            item {
                Text("Dificuldade", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    availableDifficulties.forEach { difficulty ->
                        val isSelected = uiState.difficulty == difficulty
                        FilterChip(
                            selected = isSelected,
                            onClick = { onEvent(CreateQuizEvent.OnDifficultyChange(difficulty)) },
                            label = { Text(difficulty) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colorResource(R.color.orange),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // ── Questões ──
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Questões (${uiState.questions.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showAddQuestionDialog = true }) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Adicionar questão",
                            tint = colorResource(R.color.navy_blue)
                        )
                    }
                }
            }

            // Lista de questões adicionadas
            itemsIndexed(uiState.questions) { index, question ->
                QuestionSummaryCard(
                    index = index + 1,
                    question = question,
                    onRemove = { onEvent(CreateQuizEvent.OnRemoveQuestion(index)) }
                )
            }

            // ── Botão Salvar ──
            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { onEvent(CreateQuizEvent.OnSaveQuiz) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.orange)
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Salvar Quiz", color = Color.White)
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }

    // Dialog para adicionar questão
    if (showAddQuestionDialog) {
        AddQuestionDialog(
            onDismiss = { showAddQuestionDialog = false },
            onConfirm = { question ->
                onEvent(CreateQuizEvent.OnAddQuestion(question))
                showAddQuestionDialog = false
            }
        )
    }
}

// Card resumo de questão adicionada
@Composable
fun QuestionSummaryCard(
    index: Int,
    question: QuestionInput,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$index. ${question.question}",
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            maxLines = 2
        )
        IconButton(onClick = onRemove) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Remover",
                tint = colorResource(R.color.orange)
            )
        }
    }
}

// Dialog para criar uma questão
@Composable
fun AddQuestionDialog(
    onDismiss: () -> Unit,
    onConfirm: (QuestionInput) -> Unit
) {
    var question by remember { mutableStateOf("") }
    var answer1 by remember { mutableStateOf("") }
    var answer2 by remember { mutableStateOf("") }
    var answer3 by remember { mutableStateOf("") }
    var answer4 by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf("a") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Questão", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Pergunta") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                OutlinedTextField(
                    value = answer1,
                    onValueChange = { answer1 = it },
                    label = { Text("Resposta A") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = answer2,
                    onValueChange = { answer2 = it },
                    label = { Text("Resposta B") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = answer3,
                    onValueChange = { answer3 = it },
                    label = { Text("Resposta C") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = answer4,
                    onValueChange = { answer4 = it },
                    label = { Text("Resposta D") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Resposta correta:", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("a", "b", "c", "d").forEach { letter ->
                        FilterChip(
                            selected = correctAnswer == letter,
                            onClick = { correctAnswer = letter },
                            label = { Text(letter.uppercase()) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colorResource(R.color.navy_blue),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (question.isNotBlank() && answer1.isNotBlank() &&
                        answer2.isNotBlank() && answer3.isNotBlank() && answer4.isNotBlank()
                    ) {
                        onConfirm(
                            QuestionInput(
                                question = question,
                                answer1 = answer1,
                                answer2 = answer2,
                                answer3 = answer3,
                                answer4 = answer4,
                                correctAnswer = correctAnswer
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.orange)
                )
            ) {
                Text("Adicionar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CreateQuizScreenPreview() {
    CreateQuizContent(
        uiState = CreateQuizUiState(
            title = "Quiz de Ciências",
            selectedCategory = "Ciências",
            difficulty = "medium"
        )
    )
}