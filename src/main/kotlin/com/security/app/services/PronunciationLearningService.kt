package com.security.app.services

import com.security.app.repositories.PronunciationAssessmentRepository
import com.security.app.repositories.PronunciationLearningRepository
import org.springframework.stereotype.Service

@Service
class PronunciationLearningService(
    private val pronunciationLearningRepository: PronunciationLearningRepository,
    private val pronunciationAssessmentRepository: PronunciationAssessmentRepository
) {
}