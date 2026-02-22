package com.eduardoomarson.quizpdm.ui.feature.authenticationScreens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduardoomarson.quizpdm.authentication.AuthState
import com.eduardoomarson.quizpdm.authentication.AuthViewModel
import com.eduardoomarson.quizpdm.ui.theme.QuizPDMTheme

@Composable
fun SignupScreen(
    navigateToListScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    authViewModel: AuthViewModel,
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (val state = authState.value) {
            is AuthState.Authenticated -> navigateToListScreen()
            is AuthState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                authViewModel.resetAuthState()
            }
            else -> Unit
        }
    }

    SignupContent(
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onSignupClick = { authViewModel.signup(email, password, confirmPassword) },
        onLoginClick = navigateToLoginScreen,
        isLoading = authState.value is AuthState.Loading,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
fun SignupContent(
    email: String,
    password: String,
    confirmPassword: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignupClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean = false,
    snackbarHostState: SnackbarHostState
){ Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) }
) { paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cadastro", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth(),
            value = email,
            onValueChange = onEmailChange,
            label = {Text("E-mail")},
            placeholder = {
                Text(text = "teste@email.com")
            },
            enabled = !isLoading,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth(),
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Senha") },
            placeholder = {
                Text(text = "Mínimo 6 caracteres. Ex: 12345@")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth(),
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirme a senha") },
            placeholder = {
                Text(text = "Mínimo 6 caracteres. Ex: 12345@")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSignupClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if(isLoading){
                CircularProgressIndicator()
            } else{
                Text(text = "Cadastre-se")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(text = "Já tem uma conta? Faça Login aqui!")
        }
    }
}
}

@Preview
@Composable
private fun SignupContentPreview() {
    QuizPDMTheme {
        SignupContent(
            email = "",
            password = "",
            confirmPassword = "",
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onSignupClick = {},
            onLoginClick = {},
            isLoading = false,
            snackbarHostState = SnackbarHostState(),
        )
    }
}