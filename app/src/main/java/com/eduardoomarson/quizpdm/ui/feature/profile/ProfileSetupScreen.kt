package com.eduardoomarson.quizpdm.ui.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eduardoomarson.quizpdm.R

// Lista dos avatares disponíveis no drawable
val availableAvatars = listOf(
    R.drawable.person1,
    R.drawable.person2,
    R.drawable.person3,
    R.drawable.person4,
    R.drawable.person5,
    R.drawable.person6,
    R.drawable.person7,
    R.drawable.person8,
    R.drawable.person9,
)

@Composable
fun ProfileSetupScreen(
    viewModel: ProfileSetupViewModel = hiltViewModel(),
    onProfileSaved: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onProfileSaved()
    }

    ProfileSetupContent(
        name = uiState.name,
        selectedAvatar = uiState.selectedAvatarRes,
        onNameChange = { viewModel.onNameChange(it) },
        onAvatarSelected = { viewModel.onAvatarSelected(it) },
        onSaveClick = { viewModel.saveProfile() },
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage
    )
}

@Composable
fun ProfileSetupContent(
    name: String,
    selectedAvatar: Int?,
    onNameChange: (String) -> Unit,
    onAvatarSelected: (Int) -> Unit,
    onSaveClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = "Configure seu Perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(32.dp))

            // Preview do avatar selecionado
            if (selectedAvatar != null) {
                Image(
                    painter = painterResource(selectedAvatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, colorResource(R.color.navy_blue), CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, colorResource(R.color.navy_blue), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Avatar", fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Campo nome
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Seu nome / apelido") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Escolha seu avatar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(12.dp))

            // Grid de avatares
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(availableAvatars) { avatarRes ->
                    val isSelected = selectedAvatar == avatarRes
                    Image(
                        painter = painterResource(avatarRes),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .border(
                                width = if (isSelected) 4.dp else 1.dp,
                                color = if (isSelected)
                                    colorResource(R.color.navy_blue)
                                else
                                    colorResource(R.color.grey),
                                shape = CircleShape
                            )
                            .clickable { onAvatarSelected(avatarRes) },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.orange),
                    contentColor = colorResource(R.color.white)
                ),
                enabled = !isLoading && name.isNotBlank() && selectedAvatar != null
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("Salvar e Continuar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSetupContentPreview() {
    ProfileSetupContent(
        name = "Eduardo",
        selectedAvatar = R.drawable.person1,
        onNameChange = {},
        onAvatarSelected = {},
        onSaveClick = {}
    )
}