package com.security.app.model

data class ProgressModel(
    val flashCardProgress: List<FlashCardLearningModel>,
    val quizProgress: List<QuizLearningModel>,
    val pronunciationProgress: List<PronunciationLearningModel>,
    val matchingProgress: List<MatchingLearningModel>
)