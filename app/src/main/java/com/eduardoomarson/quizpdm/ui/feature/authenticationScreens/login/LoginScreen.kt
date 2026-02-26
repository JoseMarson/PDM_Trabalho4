package com.eduardoomarson.quizpdm.ui.feature.authenticationScreens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduardoomarson.quizpdm.R
import com.eduardoomarson.quizpdm.authentication.AuthState
import com.eduardoomarson.quizpdm.authentication.AuthViewModel
import com.eduardoomarson.quizpdm.ui.theme.QuizPDMTheme


@Composable
fun LoginScreen(
    navigateToListScreen: () -> Unit,
    navigateToSignUpScreen : () -> Unit,
    navigateToForgotPasswordScreen : () -> Unit,
    authViewModel: AuthViewModel
){
    var email by remember { mutableStateOf(value = "") }
    var password by remember { mutableStateOf(value = "") }
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

    LoginContent(
        email = email,
        password = password,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLoginClick = {
            authViewModel.login(email, password)
        },
        onSignUpClick = navigateToSignUpScreen,
        onForgotPasswordClick = navigateToForgotPasswordScreen,
        onGoogleSignInClick = { authViewModel.signInWithGoogle(context) },
        isLoading = authState.value is AuthState.Loading,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun LoginContent(email: String,
                 password: String,
                 onEmailChange: (String) -> Unit,
                 onPasswordChange: (String) -> Unit,
                 onLoginClick: () -> Unit,
                 onSignUpClick: () -> Unit,
                 onForgotPasswordClick : () -> Unit,
                 onGoogleSignInClick: () -> Unit = {},
                 isLoading: Boolean = false,
                 snackbarHostState: SnackbarHostState
                 ){ Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) }) {paddingValues ->
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
        Text("Quizz App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.navy_blue))

        Spacer(modifier = Modifier.height(32.dp))

        Text("Login",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.navy_blue)
            )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            value = email,
            onValueChange = onEmailChange,
            enabled = !isLoading,
            label = {Text("E-mail")},
            placeholder = {
                Text(text = "teste@email.com")
            })

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            value = password,
            onValueChange = onPasswordChange,
            enabled = !isLoading,
            label = {Text("Senha")},
            placeholder = {
                Text(text = "Mínimo 6 caracteres. Ex: 12345@")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )




        TextButton(
            onClick = onForgotPasswordClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(R.color.navy_blue),
                containerColor = colorResource(R.color.white)
            ),
            enabled = !isLoading
        ) {
            Text(text = "Esqueci minha senha")
        }

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.orange),
                contentColor = colorResource(R.color.white)
            ),
            enabled = !isLoading
        ) {
            // Sugestão do Claude de usar circularProgressInidicator
            if(isLoading){
                CircularProgressIndicator()
            } else {
                Text(text = "Entrar")
            }
        }

        Text(
            text = "ou",
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))

        //Adição de botão com login com Conta Google
        Button(
            onClick = onGoogleSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.white),
                contentColor = colorResource(R.color.navy_blue)
            ),
            border = BorderStroke(1.dp, colorResource(R.color.navy_blue)),
            enabled = !isLoading
        ) {
            Icon(
                painter = painterResource(R.drawable.google_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Entrar com Google")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSignUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.orange),
                contentColor = colorResource(R.color.white)
            ),
            enabled = !isLoading
        ) {
            Text(text = "Não tem uma conta? Cadastre-se aqui!")
        }
    }
}
}

@Preview
@Composable
private fun LoginContentPreview() {
    QuizPDMTheme {
        LoginContent(
            email = "",
            password = "",
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onSignUpClick = {},
            onForgotPasswordClick = {},
            isLoading = false,
            snackbarHostState = SnackbarHostState(),
        )
    }
}