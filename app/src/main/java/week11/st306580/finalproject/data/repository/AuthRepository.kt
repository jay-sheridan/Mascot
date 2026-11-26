package week11.st306580.finalproject.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import week11.st306580.finalproject.data.model.UserProfile
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Register a new student
    suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): Result<Unit> {

        return try {

            // Create account in Firebase Authentication
            val result = auth.createUserWithEmailAndPassword(
                email,
                password
            ).await()

            val firebaseUser = result.user
                ?: throw Exception("User registration failed")

            // Create student profile
            val userProfile = UserProfile(
                uid = firebaseUser.uid,
                fullName = fullName,
                email = email
            )

            // Save profile in Firestore
            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(userProfile)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // Login existing student
    suspend fun loginUser(
        email: String,
        password: String
    ): Result<Unit> {

        return try {

            auth.signInWithEmailAndPassword(
                email,
                password
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // Logout student
    fun logoutUser() {
        auth.signOut()
    }

    // Get current logged-in student
    fun getCurrentUser() = auth.currentUser

}