package edu.nd.pmcburne.hwapp.one.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface BasketballGameDao {
    @Query("SELECT * FROM games WHERE date = :date AND gender = :gender")
    suspend fun getGames(date: String, gender: String): List<BasketballGame>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<BasketballGame>)

    @Query("DELETE FROM games WHERE date = :date AND gender = :gender")
    suspend fun deleteGamesByDateAndGender(date: String, gender: String)

    @Transaction
    suspend fun refreshGames(date: String, gender: String, games: List<BasketballGame>) {
        deleteGamesByDateAndGender(date, gender)
        insertAll(games)
    }
}
