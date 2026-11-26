package week11.st306580.finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import week11.st306580.finalproject.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val userName: String = ""
)

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        // Check whether a user is already logged in
        val currentUser = repository.getCurrentUser()

        if (currentUser != null) {
            _uiState.value = AuthUiState(
                isLoggedIn = true,
                userName = currentUser.displayName
                    ?: currentUser.email.orEmpty()
            )
        }
    }

    // Registration
    fun register(
        fullName: String,
        email: String,
        password: String
    ) {

        if (fullName.isBlank() ||
            email.isBlank() ||
            password.isBlank()
        ) {
            _uiState.value = AuthUiState(
                errorMessage = "Please fill in all fields."
            )
            return
        }

        if (password.length < 6) {
            _uiState.value = AuthUiState(
                errorMessage = "Password must contain at least 6 characters."
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = AuthUiState(isLoading = true)

            val result = repository.registerUser(
                fullName.trim(),
                email.trim(),
                password
            )

            result.onSuccess {

                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    successMessage = "Registration successful!",
                    userName = fullName
                )

            }.onFailure { error ->

                _uiState.value = AuthUiState(
                    errorMessage = error.message
                        ?: "Registration failed."
                )
            }
        }
    }

    // Login
    fun login(
        email: String,
        password: String
    ) {

        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState(
                errorMessage = "Please enter email and password."
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = AuthUiState(isLoading = true)

            val result = repository.loginUser(
                email.trim(),
                password
            )

            result.onSuccess {

                val user = repository.getCurrentUser()

                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    successMessage = "Login successful!",
                    userName = user?.email.orEmpty()
                )

            }.onFailure { error ->

                _uiState.value = AuthUiState(
                    errorMessage = error.message
                        ?: "Login failed."
                )
            }
        }
    }

    // Logout
    fun logout() {

        repository.logoutUser()

        _uiState.value = AuthUiState(
            isLoggedIn = false
        )
    }

}