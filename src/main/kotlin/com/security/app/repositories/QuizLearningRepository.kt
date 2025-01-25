package com.security.app.repositories

import com.security.app.entities.QuizLearning
import com.security.app.model.LearningContentType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface QuizLearningRepository : JpaRepository<QuizLearning, UUID> {
    fun findAllByItemIdInAndLearningContentIdInAndLearningContentTypeIn(
        itemIds: List<UUID>,
        learningContentIds: List<UUID>,
        learningContentTypes: List<LearningContentType>
    ): List<QuizLearning>
}