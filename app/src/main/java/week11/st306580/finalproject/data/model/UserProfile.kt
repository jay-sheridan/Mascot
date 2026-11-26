package week11.st306580.finalproject.data.model

data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val dailyStudyGoalMinutes: Int = 60,
    val preferredDifficulty: String = "Beginner",
    val selectedSubjects: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)