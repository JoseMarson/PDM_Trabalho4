package com.example.myquiz.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

import com.google.firebase.firestore.FirebaseFirestore
import com.example.myquiz.data.model.User

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun login( email: String , password: String,  onSuccess: () -> Unit,  onError: (String) -> Unit)
    {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError("Email ou senha inválidos.")
            }
    }

    fun register(
        nome: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->

                val uid = result.user?.uid ?: ""

                 db.collection("usuarios")
                    .get()
                    .addOnSuccessListener { snapshot ->

                        val ultimaPosicao = snapshot.size() + 1

                        val user = User(
                            uid = uid,
                            nome = nome,
                            pontos = 0,
                            posicao = ultimaPosicao ,
                            quizFeitos = 0
                        )

                        db.collection("usuarios")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                    }
            }
            .addOnFailureListener {
                onError("Erro ao cadastrar")
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserLogged(): Boolean {
        return auth.currentUser != null
    }



    // Retorno dados banco aqui  --------------------



    // ----------------------------------------

    fun listenUserData(
        uid: String,
        onChange: (User?) -> Unit
    ) {
        db.collection("usuarios")
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onChange(null)
                    return@addSnapshotListener
                }

                val user = snapshot?.toObject(User::class.java)
                onChange(user)
            }
    }

}