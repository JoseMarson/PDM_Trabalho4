package com.eduardoomarson.quizpdm.ui.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardoomarson.quizpdm.data.local.dao.HistoryDao
import com.eduardoomarson.quizpdm.data.local.entities.HistoryEntity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyDao: HistoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val currentUser = FirebaseAuth.getInstance().currentUser

    init {
        loadHistory()
    }

    private fun loadHistory() {
        val userId = currentUser?.uid ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            historyDao.getHistoryByUser(userId).collect { history ->
                _uiState.update {
                    it.copy(
                        history = history,
                        isLoading = false
                    )
                }
            }
        }
    }
}

data class HistoryUiState(
    val history: List<HistoryEntity> = emptyList(),
    val isLoading: Boolean = false
)