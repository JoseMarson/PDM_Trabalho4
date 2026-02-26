package com.eduardoomarson.quizpdm.ui.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eduardoomarson.quizpdm.R

@Composable
fun Banner() {
    Image(painter = painterResource(id = R.drawable.banner),
        contentDescription = null,
        modifier = Modifier.padding(all = 24.dp))
}