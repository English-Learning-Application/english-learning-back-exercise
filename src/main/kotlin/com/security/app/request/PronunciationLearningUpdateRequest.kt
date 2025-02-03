package com.security.app.request

data class PronunciationLearningUpdateRequest(
    val pronunciationLearningUpdateInfo: List<PronunciationLearningUpdateInfo>,
    val courseId: String
)

data class PronunciationLearningUpdateInfo(
    val learningContentId: String,
    val itemId: String,
    val learningContentType: String,
    val pronunciationAssessmentInfo: List<PronunciationAssessmentLearningInfo>,
)

data class PronunciationAssessmentLearningInfo(
    val pronunciationWord: String,
    val score: Int,
    val pronunciationAccentPrediction: String,
    val scoreCertificateEstimation: String,
)