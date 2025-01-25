package com.security.app.request

data class MatchingLearningUpdateRequest(
    val incorrectMatchingLearningInfo: List<MatchingLearningInfo>,
    val correctMatchingLearningInfo: List<MatchingLearningInfo>,
)

data class MatchingLearningInfo(
    val itemId: String,
    val learningContentId: String,
    val learningContentType: String,
)