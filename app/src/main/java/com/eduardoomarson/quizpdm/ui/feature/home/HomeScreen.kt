package com.eduardoomarson.quizpdm.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eduardoomarson.quizpdm.ui.feature.home.components.TopUserSection
import com.eduardoomarson.quizpdm.R
import com.eduardoomarson.quizpdm.ui.feature.home.components.BottomNavigationBar
import com.eduardoomarson.quizpdm.ui.feature.home.components.CategoryGrid
import com.eduardoomarson.quizpdm.ui.feature.home.components.CategoryHeader
import com.eduardoomarson.quizpdm.ui.feature.home.components.GameMadeButtons

@Composable
fun HomeScreen(
    onSinglePlayerClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBoardClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNeedsProfileSetup: () -> Unit = {},
    onCreateQuizClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Redireciona usuário novo do Google para configurar perfil

    LaunchedEffect(uiState.needsProfileSetup) {
        if (uiState.needsProfileSetup) onNeedsProfileSetup()
    }

    HomeScreenContent(
        uiState = uiState,
        onSinglePlayerClick = onSinglePlayerClick,
        onCategoryClick = onCategoryClick,
        onHomeClick = onHomeClick,
        onBoardClick = onBoardClick,
        onProfileClick = onProfileClick,
        onCreateQuizClick = onCreateQuizClick,
        onLogout = onLogout,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onSinglePlayerClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBoardClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onCreateQuizClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = colorResource(id = R.color.grey))
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                TopUserSection(
                    userName = uiState.userName,
                    userScore = uiState.userScore,
                    userPic = uiState.userPic
                )
                Spacer(modifier = Modifier.height(16.dp))
                GameMadeButtons(
                    onSinglePlayerClick = onSinglePlayerClick,
                    onCreateQuizClick = onCreateQuizClick)
                Spacer(modifier = Modifier.height(24.dp))
                CategoryHeader()
                CategoryGrid(onCategoryClick = onCategoryClick)
                Spacer(modifier = Modifier.height(80.dp))  // espaço pro BottomNav
            }
            BottomNavigationBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                onItemSelected = { itemId ->
                    when (itemId) {
                        R.id.home -> onHomeClick()
                        R.id.board -> onBoardClick()
                        R.id.profile -> onProfileClick()
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        uiState = HomeUiState(
            userName = "Alex",
            userScore = 2400,
            userPic = ""
        ),
        onCategoryClick = {}
    )
}