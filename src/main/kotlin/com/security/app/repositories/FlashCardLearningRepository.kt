package com.security.app.repositories

import com.security.app.entities.FlashCardLearning
import com.security.app.model.LearningContentType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FlashCardLearningRepository : JpaRepository<FlashCardLearning, UUID> {
    fun findAllByItemIdAndLearningContentIdAndLearningContentType(
        itemId: UUID,
        learningContentId: UUID,
        learningContentType: LearningContentType
    ) : FlashCardLearning?

    fun findAllByItemIdInAndLearningContentIdInAndLearningContentTypeInAndUserId(
        itemIds: List<UUID>,
        learningContentIds: List<UUID>,
        learningContentTypes: List<LearningContentType>,
        userId: UUID
    ): List<FlashCardLearning>

    fun findAllByUserId(userId: UUID): List<FlashCardLearning>
}