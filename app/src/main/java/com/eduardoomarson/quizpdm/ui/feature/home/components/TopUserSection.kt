package com.eduardoomarson.quizpdm.ui.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.eduardoomarson.quizpdm.R

@Composable
fun picNameToResId(picName: String): Int? {
    val context = LocalContext.current
    if (picName.isEmpty()) return null
    val resId = context.resources.getIdentifier(
        picName, "drawable", context.packageName
    )
    return if (resId != 0) resId else null
}

@Composable
@Preview
fun TopUserSection(
    userName: String = "Jogador",
    userScore: Int = 0,
    userPic: String = ""
){
    val avatarResId = picNameToResId(userPic)

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        ) {
        if (avatarResId != null) {
            Image(
                painter = painterResource(avatarResId),
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = null,
                modifier = Modifier.size(55.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        Text(text="Hi,$userName!",
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),
            )
        Row(
            modifier = Modifier
                .height(40.dp)
                .background(color= colorResource(id = R.color.navy_blue),
                    shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Image(painter = painterResource(id= R.drawable.garnet),
                contentDescription = null,
                modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = userScore.toString(),
                color = colorResource(R.color.white))
            Spacer(modifier = Modifier.width(8.dp))
            Image(painter = painterResource(R.drawable.plus),
                contentDescription = null,
                modifier = Modifier.size(20.dp))
        }
    }

}