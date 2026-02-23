package com.example.myquiz.viewmodel


import androidx.lifecycle.ViewModel
//import com.example.todoapp.model.TodoItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
//import java.util.*

//class TodoQuiz : ViewModel() {
//    private val firestore = FirebaseFirestore.getInstance()
//    private val auth = FirebaseAuth.getInstance()
//
//    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
//    val todos: StateFlow<List<TodoItem>> = _todos.asStateFlow()
//
//    init {
//        // Adiciona um listener que busca as tarefas sempre que o usuário loga ou desloga.
//        auth.addAuthStateListener { firebaseAuth ->
//            fetchTodos(firebaseAuth.currentUser?.uid)
//        }
//    }
//
//    private fun fetchTodos(userId: String?) {
//        if (userId.isNullOrBlank()) {
//            _todos.value = emptyList() // Limpa a lista se não há usuário
//            return
//        }
//
//        firestore.collection("users").document(userId).collection("todos")
//            .addSnapshotListener { snapshot, e ->
//                if (e != null) {
//                    _todos.value = emptyList()
//                    return@addSnapshotListener
//                }
//
//                if (snapshot != null) {
//                    // Converte os documentos diretamente para uma lista de TodoItem
//                    // O campo 'id' será preenchido automaticamente pela anotação @DocumentId
//                    _todos.value = snapshot.toObjects()
//                }
//            }
//    }
//
//    private fun now(): String =
//        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
//
//    fun addTodo(title: String, description: String) {
//        val userId = auth.currentUser?.uid ?: return
//
//        val newTodo = TodoItem(
//            // Não precisamos mais definir o ID aqui, o Firestore faz isso
//            title = title,
//            description = description,
//            done = false,
//            date = now()
//        )
//
//        // O método .add() cria um documento com um ID único automaticamente
//        firestore.collection("users").document(userId).collection("todos")
//            .add(newTodo)
//    }

//    fun deleteTodo(todo: TodoItem) {
//        val userId = auth.currentUser?.uid ?: return
//        // Usamos o ID do documento, que agora está no nosso objeto TodoItem
//        firestore.collection("users").document(userId).collection("todos")
//            .document(todo.id)
//            .delete()
//    }
//
//    fun toggle(todo: TodoItem) {
//        val userId = auth.currentUser?.uid ?: return
//        // Usamos o ID do documento para atualizar apenas o campo 'done'
//        firestore.collection("users").document(userId).collection("todos")
//            .document(todo.id)
//            .update("done", !todo.done)
//    }
//}
