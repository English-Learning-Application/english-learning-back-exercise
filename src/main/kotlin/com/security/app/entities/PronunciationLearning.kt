package com.security.app.entities

import com.security.app.model.LearningContentType
import jakarta.persistence.*
import lombok.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EntityListeners(AuditingEntityListener::class)
class PronunciationLearning {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var pronunciationLearningId: UUID

    @Column(nullable = false)
    var learningContentId: UUID = UUID.randomUUID()

    @Column(nullable = false)
    var itemId : UUID = UUID.randomUUID()

    @Column(nullable = false)
    var learningContentType: LearningContentType = LearningContentType.WORD

    @OneToMany(mappedBy = "pronunciationLearning", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var pronunciationLearningContents: List<PronunciationAssessment> = mutableListOf()

    @Column(nullable = true)
    var userId: UUID? = null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}