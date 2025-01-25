package com.security.app.model

import com.security.app.entities.QuizLearning
import java.util.*

class QuizLearningModel(
    var quizLearningId: UUID? = null,
    var learningContentId: UUID = UUID.randomUUID(),
    var itemId: UUID = UUID.randomUUID(),
    var learningContentType: LearningContentType = LearningContentType.WORD,
    var numberOfCorrect: Int = 0,
    var numberOfIncorrect: Int = 0
) {
    companion object {
        fun fromEntity(entity: QuizLearning): QuizLearningModel {
            return QuizLearningModel(
                quizLearningId = entity.quizLearningId,
                learningContentId = entity.learningContentId,
                itemId = entity.itemId,
                learningContentType = entity.learningContentType,
                numberOfCorrect = entity.numberOfCorrect,
                numberOfIncorrect = entity.numberOfIncorrect
            )
        }
    }
}
