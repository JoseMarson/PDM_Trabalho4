// <----- Criado por samuel
package com.eduardoomarson.quizpdm.ui.feature.board

import androidx.lifecycle.ViewModel
import com.eduardoomarson.quizpdm.data.remote.firestore.FirestoreRepository
import com.eduardoomarson.quizpdm.ui.feature.home.components.Model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BoardViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _users = MutableStateFlow<List<UserModel>>(emptyList())
    val users: StateFlow<List<UserModel>> = _users

    init {
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        repository.fetchAllUsersOrderedRank(
            onResult = { entityList ->

                _users.value = entityList.mapIndexed { index, entity ->
                    UserModel(
                        id = index + 1,           // posição no ranking
                        name = entity.name,
                        pic = entity.pic,
                        score = entity.totalScore
                    )
                }

            },
            onError = {
                it.printStackTrace()
            }
        )
    }
}