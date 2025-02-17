package com.security.app.repositories

import com.security.app.entities.QuizLearning
import com.security.app.model.LearningContentType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface QuizLearningRepository : JpaRepository<QuizLearning, UUID> {
    fun findAllByItemIdInAndLearningContentIdInAndLearningContentTypeInAndUserIdAndCourseId(
        itemIds: List<UUID>,
        learningContentIds: List<UUID>,
        learningContentTypes: List<LearningContentType>,
        userId: UUID,
        courseId: UUID
    ): List<QuizLearning>

    fun findAllByUserIdAndCourseIdIsIn(
        userId: UUID,
        courseIds: List<UUID>
    ): List<QuizLearning>

    fun findAllByUserId(userId: UUID): List<QuizLearning>
}