package com.eduardoomarson.quizpdm.ui.feature.authenticationScreens.forgotpassword

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduardoomarson.quizpdm.R
import com.eduardoomarson.quizpdm.authentication.AuthState
import com.eduardoomarson.quizpdm.authentication.AuthViewModel
import com.eduardoomarson.quizpdm.ui.theme.QuizPDMTheme


@Composable
fun ForgotPasswordScreen(
    navigateBack: () -> Unit,
    authViewModel: AuthViewModel,
){
    var email by remember { mutableStateOf(value = "") }
    val snackbarHostState = remember { SnackbarHostState() }
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (val state = authState.value) {
            is AuthState.PasswordResetSend -> {
                Toast.makeText(
                    context,
                    "E-mail de recuperação de senha enviado! Verifique sua caixa de e-mail!",
                    Toast.LENGTH_LONG
                ).show()
                navigateBack()
            }
            is AuthState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                authViewModel.resetAuthState()
            }
            else -> Unit
        }
    }

    ForgotPasswordContent(
        email = email,
        onEmailChange = { email = it },
        onResetClick = {
            authViewModel.resetPassword(email)
        },
        onBackClick = navigateBack,
        isLoading = authState.value is AuthState.Loading,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
fun ForgotPasswordContent(
    email: String,
    onEmailChange : (String) -> Unit,
    onResetClick : () -> Unit,
    onBackClick : () -> Unit,
    isLoading : Boolean = false,
    snackbarHostState: SnackbarHostState,
) {  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) }
) { paddingValues ->
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.quiz_icon),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 32.dp),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(colorResource(R.color.navy_blue))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Recuperar Senha",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.navy_blue)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Digite seu e-mail para receber instruções de recuperação",
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            value = email,
            onValueChange = onEmailChange,
            label = { Text("E-mail") },
            placeholder = { Text("test@gmail.com") },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onResetClick,
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            enabled = !isLoading && email.isNotEmpty()
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Enviar e-mail de recuperação!")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackClick,
            enabled = !isLoading
        ) {
            Text(text = "Voltar para tela de Login")
        }
    }
}
}

@Preview
@Composable
private fun ForgotPasswordContentPreview() {
    QuizPDMTheme {
        ForgotPasswordContent(
            email = "",
            onEmailChange = {},
            onResetClick = {},
            onBackClick = {},
            isLoading = false,
            snackbarHostState = SnackbarHostState(),
        )
    }
}