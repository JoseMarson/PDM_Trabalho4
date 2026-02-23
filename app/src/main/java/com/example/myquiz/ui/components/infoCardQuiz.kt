package com.example.myquiz.ui.components



import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Games
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InfoCardQuiz(
    onClick: () -> Unit,
    textoTitulo: String,
    textoDescricao: String
)
{
    Spacer(modifier = Modifier.height(10.dp))

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDE7F6)
        ),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Games,
                contentDescription = "Quiz",
                tint = Color(0xFF311B92),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {

                Text(
                    text = textoTitulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF311B92)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = textoDescricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5E35B1)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(5.dp))

}