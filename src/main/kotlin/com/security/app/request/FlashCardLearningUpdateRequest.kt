package com.security.app.request

data class FlashCardLearningUpdateRequest(
    val skippedFlashCardLearningInfo: List<FlashCardLearningInfo>,
    val learnedFlashCardLearningInfo: List<FlashCardLearningInfo>,
    val courseId: String
)

data class FlashCardLearningInfo(
    val itemId: String,
    val learningContentId: String,
    val learningContentType: String,
)