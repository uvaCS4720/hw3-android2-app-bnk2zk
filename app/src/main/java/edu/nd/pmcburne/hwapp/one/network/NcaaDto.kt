package edu.nd.pmcburne.hwapp.one.network

import kotlinx.serialization.Serializable

@Serializable
data class ScoreboardDto(
    val games: List<GameEntryDto>? = null
)

@Serializable
data class GameEntryDto(
    val game: GameInfoDto? = null
)

@Serializable
data class GameInfoDto(
    val gameID: String? = null,
    val title: String? = null,
    val home: TeamDto? = null,
    val away: TeamDto? = null,
    val gameState: String? = null,
    val startTime: String? = null,
    val currentPeriod: String? = null,
    val contestClock: String? = null,
    val winner: String? = null
)

@Serializable
data class TeamDto(
    val names: TeamNamesDto? = null,
    val score: String? = null,
    val winner: Boolean? = null
)

@Serializable
data class TeamNamesDto(
    val short: String? = null,
    val fff: String? = null // Often the full name
)
