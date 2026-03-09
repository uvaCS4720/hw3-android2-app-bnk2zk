package edu.nd.pmcburne.hwapp.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import edu.nd.pmcburne.hwapp.one.data.AppDatabase
import edu.nd.pmcburne.hwapp.one.network.GameRepository
import edu.nd.pmcburne.hwapp.one.ui.ScoreboardScreen
import edu.nd.pmcburne.hwapp.one.ui.ScoreboardViewModel
import edu.nd.pmcburne.hwapp.one.ui.theme.HWStarterRepoTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ScoreboardViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = GameRepository(gameDao = database.basketballGameDao())
        ScoreboardViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HWStarterRepoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScoreboardScreen(viewModel = viewModel)
                }
            }
        }
    }
}
