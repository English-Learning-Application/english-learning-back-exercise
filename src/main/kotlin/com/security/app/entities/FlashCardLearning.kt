package com.security.app.entities

import com.security.app.model.LearningContentType
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
class FlashCardLearning {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var flashCardLearningId: UUID

    @Column(nullable = false)
    var learningContentId: UUID = UUID.randomUUID()

    @Column(nullable = false)
    var itemId : UUID = UUID.randomUUID()

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var learningContentType: LearningContentType = LearningContentType.WORD

    @Column(nullable = false)
    var numberOfLearned: Int = 0

    @Column(nullable = false)
    var numberOfSkipped: Int = 0

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}