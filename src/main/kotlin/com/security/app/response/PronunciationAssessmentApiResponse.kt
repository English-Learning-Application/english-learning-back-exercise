package com.security.app.response

data class PronunciationAssessmentApiResponse(
    val words: List<PronunciationWordApiResponse>,
    val score: Int,
    val accent_predictions: AccentPredictionApiResponse,
    val score_estimates: PronunciationScoreEstimationApiResponse,
)

data class PronunciationWordApiResponse(
    val label: String,
    val score: Int,
)

data class AccentPredictionApiResponse(
    val en_US: Int,
    val en_UK: Int,
    val en_AU: Int,
)

data class PronunciationScoreEstimationApiResponse(
    val ielts: String,
    val toefl: String,
    val cefr: String,
    val pte_general: String,
)