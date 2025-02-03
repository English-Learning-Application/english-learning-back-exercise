package com.security.app.model

import com.security.app.entities.PronunciationLearning
import java.util.*

class PronunciationLearningModel(
    var pronunciationLearningId: UUID? = null,
    var learningContentId: UUID = UUID.randomUUID(),
    var itemId: UUID = UUID.randomUUID(),
    var courseId: UUID = UUID.randomUUID(),
    var learningContentType: LearningContentType = LearningContentType.WORD,
    var pronunciationLearningContents: List<PronunciationAssessmentModel> = mutableListOf()
) {
    companion object {
        fun fromEntity(entity: PronunciationLearning): PronunciationLearningModel {
            return PronunciationLearningModel(
                pronunciationLearningId = entity.pronunciationLearningId,
                learningContentId = entity.learningContentId,
                itemId = entity.itemId,
                courseId = entity.courseId ?: UUID.randomUUID(),
                learningContentType = entity.learningContentType,
                pronunciationLearningContents = entity.pronunciationLearningContents.map { PronunciationAssessmentModel.fromEntity(it) }
            )
        }
    }
}
