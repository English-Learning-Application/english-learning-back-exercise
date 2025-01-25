package com.security.app.request

data class QuestionLearningUpdateRequest (
    val incorrectQuestionLearningInfo: List<QuestionLearningInfo>,
    val correctQuestionLearningInfo: List<QuestionLearningInfo>,
)

data class QuestionLearningInfo(
    val itemId: String,
    val learningContentId: String,
    val learningContentType: String,
)