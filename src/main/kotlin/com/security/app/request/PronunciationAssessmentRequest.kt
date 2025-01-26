package com.security.app.request

data class PronunciationAssessmentRequest(
    val base64Audio: String,
    val audioFormat: String,
    val originalText: String,
)
