package com.eduardoomarson.quizpdm.ui.feature.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduardoomarson.quizpdm.R

@Composable
fun QuizErrorScreen(
    message: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "😕",
                fontSize = 48.sp
            )
            Text(
                text = message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.navy_blue)
            )
            Text(
                text = "Tente criar um quiz nessa categoria primeiro!",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.orange)
                )
            ) {
                Text("Voltar", color = Color.White)
            }
        }
    }
}