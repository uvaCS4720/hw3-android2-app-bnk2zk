package edu.nd.pmcburne.hwapp.one.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Domain model and Room entity for a basketball game.
 */
@Entity(tableName = "games")
data class BasketballGame(
    @PrimaryKey val id: String,
    val date: String, // yyyy/mm/dd
    val gender: String, // "men" or "women"
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val status: String, // e.g., "upcoming", "live", "final"
    val startTime: String,
    val period: String,
    val clock: String,
    val winner: String
)
