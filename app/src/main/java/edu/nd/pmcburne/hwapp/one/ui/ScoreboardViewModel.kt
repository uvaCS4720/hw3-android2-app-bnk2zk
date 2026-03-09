package edu.nd.pmcburne.hwapp.one.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hwapp.one.data.BasketballGame
import edu.nd.pmcburne.hwapp.one.network.GameRepository
import edu.nd.pmcburne.hwapp.one.network.RepositoryResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * UI State for the Scoreboard screen.
 */
data class ScoreboardUiState(
    val games: List<BasketballGame> = emptyList(),
    val isLoading: Boolean = false,
    val gender: String = "men",
    val selectedDate: LocalDate = LocalDate.now(),
    val errorMessage: String? = null,
    val isOffline: Boolean = false
)

class ScoreboardViewModel(private val repository: GameRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ScoreboardUiState())
    val uiState: StateFlow<ScoreboardUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun setGender(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
        refresh()
    }

    fun setDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        refresh()
    }

    fun refresh() {
        val currentState = _uiState.value
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
            
            val year = currentState.selectedDate.year.toString()
            val month = currentState.selectedDate.monthValue.toString().padStart(2, '0')
            val day = currentState.selectedDate.dayOfMonth.toString().padStart(2, '0')
            
            val result = repository.getGames(currentState.gender, year, month, day)
            
            _uiState.value = when (result) {
                is RepositoryResult.Success -> {
                    currentState.copy(
                        games = result.games,
                        isLoading = false,
                        isOffline = result.isOffline
                    )
                }
                is RepositoryResult.Error -> {
                    currentState.copy(
                        games = result.cachedGames,
                        isLoading = false,
                        errorMessage = result.message,
                        isOffline = true
                    )
                }
            }
        }
    }

    class Factory(private val repository: GameRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScoreboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ScoreboardViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
