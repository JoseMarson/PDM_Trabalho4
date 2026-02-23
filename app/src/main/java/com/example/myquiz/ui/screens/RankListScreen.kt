package com.example.myquiz.ui.screens


import android.R.attr.onClick
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myquiz.R
import com.example.myquiz.navigation.BottomBar
import com.example.myquiz.ui.components.AddQuizCard
import com.example.myquiz.ui.components.InfoCard
import com.example.myquiz.ui.components.RankingCard

@Composable
fun RankListScreen (
    navController : NavHostController
) {

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
                text = "Podio geral",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(25.dp))
            InfoCard(
                title = "Minha posição geral",
                value = "label teste   ",
                iconRes = R.drawable.podio
            )
            Spacer(modifier = Modifier.height(25.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF00030C))
            )

            Spacer(modifier = Modifier.height(20.dp))

            RankingCard(
                posicao = 10,
                nome = "Samuel"
            )
            RankingCard(
                posicao = 10,
                nome = "Samuel"
            )
            RankingCard(
                posicao = 10,
                nome = "Samuel"
            )
            RankingCard(
                posicao = 10,
                nome = "Samuel"
            )
            RankingCard(
                posicao = 10,
                nome = "Samuel"
            )
            RankingCard(
                posicao = 10,
                nome = "Samuel"
            )
            RankingCard(
                posicao = 10,
                nome = "Samuel"
            )
        }

    }


}