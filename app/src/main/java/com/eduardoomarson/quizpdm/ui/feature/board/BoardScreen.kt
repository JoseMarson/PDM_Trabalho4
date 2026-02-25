// <----- Criado por samuel

package com.eduardoomarson.quizpdm.ui.feature.board
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eduardoomarson.quizpdm.R
import com.eduardoomarson.quizpdm.ui.feature.board.components.LeaderRow
import com.eduardoomarson.quizpdm.ui.feature.board.components.OnBackRow
import com.eduardoomarson.quizpdm.ui.feature.board.components.TopThreeSection

@Composable
fun BoardScreen(
    onBackClick: () -> Unit,
    viewModel: BoardViewModel = viewModel()
) {

    val users by viewModel.users.collectAsState()

     if (users.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.grey)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val topUsers = users.take(3)
    val otherUsers = users.drop(3)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.grey)),
        verticalArrangement = Arrangement.Top,
    ) {

        item {
            OnBackRow(onBack = onBackClick)
        }

        item {
            TopThreeSection(topUsers)
            Spacer(modifier = Modifier.height(16.dp))
        }

        itemsIndexed(otherUsers) { index, user ->
            LeaderRow(
                user = user,
                rank = index + 4
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BoardScreenPreview() {
    BoardScreen(onBackClick = {})
}