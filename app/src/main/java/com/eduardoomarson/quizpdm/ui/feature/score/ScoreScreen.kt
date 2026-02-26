package com.eduardoomarson.quizpdm.ui.feature.score

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduardoomarson.quizpdm.R

@Composable
fun ScoreScreen(
    score: Int,
    totalQuestions: Int = 0,
    correctAnswers: Int = 0,
    timeSeconds: Long = 0L,
    maxScore: Int = 0,
    onBackToMain:()->Unit){

    val percentage = if (totalQuestions > 0)
        (correctAnswers * 100) / totalQuestions else 0

    val minutes = timeSeconds / 60
    val seconds = timeSeconds % 60

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.grey)),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.trophy),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "SUA PONTUAÇÃO:",
                color = colorResource(R.color.navy_blue),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = score.toString(),
                color= colorResource(R.color.navy_blue),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatRow("✅ Acertos", "$correctAnswers / $totalQuestions")
                StatRow("📊 Percentual", "$percentage%")
                StatRow("⏱ Tempo", "%02d:%02d".format(minutes, seconds))
                StatRow("🏆 Pontuação máxima", maxScore.toString())
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onBackToMain,
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.orange)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Voltar Página Inicial", color = Color.White)
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = colorResource(R.color.navy_blue))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun ScoreScreenPreview(){
    ScoreScreen(score = 100, onBackToMain = {})
}