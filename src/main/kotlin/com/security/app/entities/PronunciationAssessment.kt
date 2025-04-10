package com.security.app.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EntityListeners(AuditingEntityListener::class)
class PronunciationAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var pronunciationAssessmentId: UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pronunciationLearningId")
    @JsonIgnore
    lateinit var pronunciationLearning: PronunciationLearning

    @Column(nullable = true, columnDefinition = "TEXT")
    var pronunciationWord: String = ""

    @Column(nullable = true)
    var score: Int = 0

    @Column(nullable = true, columnDefinition = "TEXT")
    var pronunciationAccentPrediction: String = ""

    @Column(nullable = true, columnDefinition = "TEXT")
    var scoreCertificateEstimation: String = ""

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}