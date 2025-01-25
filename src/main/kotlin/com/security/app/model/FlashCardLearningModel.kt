package com.security.app.model

import com.security.app.entities.FlashCardLearning
import java.util.*

class FlashCardLearningModel(
    var flashCardLearningId: UUID? = null,
    var learningContentId: UUID = UUID.randomUUID(),
    var itemId: UUID = UUID.randomUUID(),
    var learningContentType: LearningContentType = LearningContentType.WORD,
    var numberOfLearned: Int = 0,
    var numberOfSkipped: Int = 0,
) {
    companion object {
        fun fromEntity(entity: FlashCardLearning): FlashCardLearningModel {
            return FlashCardLearningModel(
                flashCardLearningId = entity.flashCardLearningId,
                learningContentId = entity.learningContentId,
                itemId = entity.itemId,
                learningContentType = entity.learningContentType,
                numberOfLearned = entity.numberOfLearned,
                numberOfSkipped = entity.numberOfSkipped,
            )
        }
    }
}