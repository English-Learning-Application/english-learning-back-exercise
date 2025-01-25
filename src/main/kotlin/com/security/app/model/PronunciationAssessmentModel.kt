package com.security.app.model

import com.security.app.entities.PronunciationAssessment
import java.util.*

class PronunciationAssessmentModel(
    var pronunciationAssessmentId: UUID? = null,
    var pronunciationLearningId: UUID = UUID.randomUUID(),
    var pronunciationWord: String = "",
    var score: Int = 0,
    var pronunciationAccentPrediction: String = "",
    var scoreCertificateEstimation: String = ""
) {
    companion object {
        fun fromEntity(entity: PronunciationAssessment): PronunciationAssessmentModel {
            return PronunciationAssessmentModel(
                pronunciationAssessmentId = entity.pronunciationAssessmentId,
                pronunciationLearningId = entity.pronunciationLearning.pronunciationLearningId, // assuming pronunciationLearningId is the foreign key
                pronunciationWord = entity.pronunciationWord,
                score = entity.score,
                pronunciationAccentPrediction = entity.pronunciationAccentPrediction,
                scoreCertificateEstimation = entity.scoreCertificateEstimation
            )
        }
    }
}
