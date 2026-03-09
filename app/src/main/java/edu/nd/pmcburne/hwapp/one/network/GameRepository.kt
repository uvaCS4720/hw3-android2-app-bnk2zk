package edu.nd.pmcburne.hwapp.one.network

import edu.nd.pmcburne.hwapp.one.data.BasketballGame
import edu.nd.pmcburne.hwapp.one.data.BasketballGameDao

/**
 * Result wrapper to communicate the data and its source (online vs offline).
 */
sealed class RepositoryResult {
    data class Success(val games: List<BasketballGame>, val isOffline: Boolean) : RepositoryResult()
    data class Error(val message: String, val cachedGames: List<BasketballGame>) : RepositoryResult()
}

/**
 * Repository for handling game data from network and local cache.
 */
class GameRepository(
    private val apiService: NcaaApiService = NcaaApi.retrofitService,
    private val gameDao: BasketballGameDao
) {

    /**
     * Fetches scores for a given date and gender.
     * Tries network first, then falls back to local database.
     */
    suspend fun getGames(gender: String, year: String, month: String, day: String): RepositoryResult {
        val dateString = "$year/$month/$day"
        
        return try {
            val response = apiService.getScoreboard(gender, year, month, day)
            val networkGames = response.games?.mapNotNull { entry ->
                val info = entry.game ?: return@mapNotNull null
                
                BasketballGame(
                    id = info.gameID ?: return@mapNotNull null,
                    date = dateString,
                    gender = gender,
                    homeTeam = info.home?.names?.fff ?: info.home?.names?.short ?: "Home Team",
                    awayTeam = info.away?.names?.fff ?: info.away?.names?.short ?: "Away Team",
                    homeScore = info.home?.score?.toIntOrNull() ?: 0,
                    awayScore = info.away?.score?.toIntOrNull() ?: 0,
                    status = info.gameState ?: "Upcoming",
                    startTime = info.startTime ?: "TBD",
                    period = info.currentPeriod ?: "0",
                    clock = info.contestClock ?: "0:00",
                    winner = when {
                        info.home?.winner == true -> "home"
                        info.away?.winner == true -> "away"
                        else -> "none"
                    }
                )
            } ?: emptyList()

            // Update cache
            gameDao.refreshGames(dateString, gender, networkGames)
            
            RepositoryResult.Success(networkGames, isOffline = false)
        } catch (e: Exception) {
            // Network failed, fall back to cache
            val cachedGames = gameDao.getGames(dateString, gender)
            if (cachedGames.isNotEmpty()) {
                RepositoryResult.Success(cachedGames, isOffline = true)
            } else {
                RepositoryResult.Error(e.message ?: "Unknown error", emptyList())
            }
        }
    }
}
