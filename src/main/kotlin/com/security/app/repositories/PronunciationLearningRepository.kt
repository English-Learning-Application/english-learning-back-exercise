package com.security.app.repositories

import com.security.app.entities.PronunciationLearning
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PronunciationLearningRepository : JpaRepository<PronunciationLearning, UUID> {
}