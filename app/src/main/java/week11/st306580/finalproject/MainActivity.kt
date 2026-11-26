package week11.st306580.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st306580.finalproject.ui.auth.AuthScreen
import week11.st306580.finalproject.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val authViewModel: AuthViewModel = viewModel()

            AuthScreen(
                viewModel = authViewModel
            )
        }
    }
}