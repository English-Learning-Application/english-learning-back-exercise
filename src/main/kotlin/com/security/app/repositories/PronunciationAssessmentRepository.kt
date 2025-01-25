package com.security.app.repositories

import com.security.app.entities.PronunciationAssessment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PronunciationAssessmentRepository : JpaRepository<PronunciationAssessment, UUID> {
}