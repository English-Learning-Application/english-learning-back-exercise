package com.security.app.repositories

import com.security.app.entities.MatchingLearning
import com.security.app.model.LearningContentType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MatchingLearningRepository : JpaRepository<MatchingLearning, UUID> {
    fun findAllByItemIdInAndLearningContentIdInAndLearningContentTypeIn(
        itemIds: List<UUID>,
        learningContentIds: List<UUID>,
        learningContentTypes: List<LearningContentType>
    ): List<MatchingLearning>
}