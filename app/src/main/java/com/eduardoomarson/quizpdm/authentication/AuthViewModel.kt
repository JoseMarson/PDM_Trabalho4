package com.eduardoomarson.quizpdm.authentication

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardoomarson.quizpdm.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.eduardoomarson.quizpdm.authentication.AuthState


class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthState()
    }

    fun checkAuthState() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        val validationErrorLogin = validateLoginInput(email, password)
        if (validationErrorLogin != null) {
            _authState.value = AuthState.Error(validationErrorLogin)
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Algo deu errado...")
                }
            }
    }

    fun signup(email: String, password: String, confirmPassword: String) {

        val validationError = validateSignupInput(email, password, confirmPassword)
        if (validationError != null) {
            _authState.value = AuthState.Error(validationError)
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Algo deu errado...")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    /* Sugestão CLAUDE:
    PROMPT: "Considerando o que já tenho proposto como AuthViewModel, e o objeto PasswordResetSend
     criado no AuthState como adicionar funcionalidade de recuperar senha?"
     Início da Sugestão: */
    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _authState.value = AuthState.Error("E-mail não pode estar vazio")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = AuthState.Error("E-mail inválido")
            return
        }

        _authState.value = AuthState.Loading
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.PasswordResetSend
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Erro ao enviar e-mail")
                }
            }
    }
    /* Fim Sugestão CLAUDE */

    private fun validateSignupInput(
        email: String,
        password: String,
        confirmPassword: String
    ): String? {
        return when {
            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ->
                "Todos os campos são obrigatórios."

            password.length < 6 ->
                "A senha deve ter pelo menos 6 caracteres."

            password != confirmPassword ->
                "As senhas não coincidem."
            /* Sugestão CLAUDE
            PROMPT: "Estou escrevendo uma função no AuthViewModel para realizar validação dos campos de
                     cadastro do aplicativo TodoList a fim de reduzir boilerplate, teria alguma sugestão de
                     melhoria?"
             Início Sugestão:
             */
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "E-mail inválido"
            /*Fim sugestão CLAUDE*/
            else -> null
        }
    }

    private fun validateLoginInput(email: String, password: String): String? {
        return when {
            email.isEmpty() || password.isEmpty() ->
                "Todos os campos são obrigatórios."

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "E-mail inválido"

            else -> null
        }
    }

    /* Sugestão Gemini
    PROMPT: Inicialmente estava usando Toast, mas agora resolvi aplicar Snackbar.
    Analisando as telas de Login, Signup, AuthViewModel, há alguma alteração que deve ser feita?
     */
    fun resetAuthState() {
        if (_authState.value !is AuthState.Unauthenticated) {
            _authState.value = AuthState.Unauthenticated
        }
    }
    /*Fim Sugestão Gemini */

    // Funções para logar e cadastrar com conta Google

    fun signInWithGoogle(context: Context) {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        _authState.value = AuthState.Loading

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(context, request)
                handleGoogleSignIn(result)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.message ?: "Erro ao entrar com Google"
                )
            }
        }
    }

    private fun handleGoogleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(
                googleIdTokenCredential.idToken, null
            )
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.Error(
                            task.exception?.message ?: "Erro ao autenticar"
                        )
                    }
                }
        } else {
            _authState.value = AuthState.Error("Credencial inválida")
        }
    }
}