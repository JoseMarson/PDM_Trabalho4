package com.eduardoomarson.quizpdm.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduardoomarson.quizpdm.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon

@Composable
@Preview
fun CategoryGrid(
    quizzesByCategory: Map<String, Int> = emptyMap(),
    onCategoryClick: (String) -> Unit = {}
){
    /*  LLM: CLAUDE
        PROMPT: Considerando o código do componente CategoryGrid,
               eu gostaria de usar outros icones para as categorias de quiz
               que coloquei. A principio tentei usar imagens png mas não ficou bom
     */

    // INICIO Sugestão CLAUDE
    val categoryIcons = mapOf(
        "Ciências"   to Icons.Filled.Science,
        "História"   to Icons.AutoMirrored.Filled.MenuBook,
        "Esportes"   to Icons.Filled.SportsSoccer,
        "Geral"      to Icons.Filled.Apps,
        "Tecnologia" to Icons.Filled.Computer,
        "Direito"    to Icons.Filled.Gavel,
        "Inglês"     to Icons.Filled.Language,
        "Japonês"    to Icons.Filled.Translate,
    )

    val categories = categoryIcons.keys.toList()
    // FIM Sugestão CLAUDE

    Column {
        categories.chunked(2).forEach { rowCategories ->   // Sugestão ClaUDE: 2 por linha
            Row(modifier = Modifier.fillMaxWidth()) {
                rowCategories.forEach { category ->
                    CategoryCard(
                        title = category,
                        icon = categoryIcons[category] ?: Icons.Filled.Quiz,
                        quizCount = quizzesByCategory[category] ?: 0,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onCategoryClick(category) }
                    )
                }
                // SUGESTÃO CLAUDE: Se a linha tiver só 1 item, preenche o espaço vazio
                if (rowCategories.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    quizCount: Int = 0,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    Row(
        modifier = modifier
            .height(55.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(R.color.white))
            .clickable { onClick() }
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // SUGESTÃO CLAUDE
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = colorResource(R.color.navy_blue)
        )
        // FIM SUGESTÃO CLAUDE
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            if (quizCount > 0) {
                Text(text = "$quizCount quizzes", fontSize = 11.sp)
            }
        }
    }
}