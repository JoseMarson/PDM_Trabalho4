package com.eduardoomarson.quizpdm.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
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
    onBoardClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        onSinglePlayerClick = onSinglePlayerClick,
        onBoardClick = onBoardClick
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onSinglePlayerClick: () -> Unit = {},
    onBoardClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
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
            GameMadeButtons(onSinglePlayerClick)
            Spacer(modifier = Modifier.height(24.dp))
            CategoryHeader()
            CategoryGrid()
        }
        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onItemSelected = { itemId ->
                if (itemId == R.id.board) onBoardClick()
            }
        )
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
        )
    )
}