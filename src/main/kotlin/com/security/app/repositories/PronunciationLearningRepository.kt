package com.security.app.repositories

import com.security.app.entities.PronunciationLearning
import com.security.app.model.LearningContentType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PronunciationLearningRepository : JpaRepository<PronunciationLearning, UUID> {
    fun findAllByItemIdInAndLearningContentIdInAndLearningContentTypeInAndUserIdAndCourseId(
        itemIds: List<UUID>,
        learningContentIds: List<UUID>,
        learningContentTypes: List<LearningContentType>,
        userId: UUID,
        courseId: UUID
    ): List<PronunciationLearning>

    fun findAllByUserIdAndCourseIdIsIn(
        userId: UUID,
        courseIds: List<UUID>
    ): List<PronunciationLearning>

    fun findAllByUserId(userId: UUID): List<PronunciationLearning>
}