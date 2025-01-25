package com.security.app.services

import com.security.app.entities.FlashCardLearning
import com.security.app.model.LearningContentType
import com.security.app.repositories.FlashCardLearningRepository
import com.security.app.request.FlashCardLearningInfo
import com.security.app.utils.toUUID
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FlashCardLearningService (
    private val flashCardLearningRepository: FlashCardLearningRepository,
    private val learningContentService: LearningContentService
){
    @Transactional
    fun updateFlashCardLearning(
        tokenString: String,
        learnedFlashcardList: List<FlashCardLearningInfo>,
        skippedFlashcardList: List<FlashCardLearningInfo>
    ) : List<FlashCardLearning>? {
        val extractedLearningContentIds = (learnedFlashcardList + skippedFlashcardList).map { it.learningContentId }.distinct()
        val learningContentResponse =
            learningContentService.getLearningContentByIds(extractedLearningContentIds, tokenString)

        if(extractedLearningContentIds.size != learningContentResponse?.size) {
            return null
        }


        fun createFlashCardLearningEntities(
            flashcardList: List<FlashCardLearningInfo>,
            numberOfLearned: Int? = null,
            numberOfSkipped: Int? = null
        ): List<FlashCardLearning> {
            return flashcardList.map { flashcard ->
                FlashCardLearning().apply {
                    learningContentId = flashcard.learningContentId.toUUID()
                    itemId = flashcard.itemId.toUUID()
                    learningContentType = LearningContentType.fromString(flashcard.learningContentType)
                    numberOfLearned?.let { this.numberOfLearned = it }
                    numberOfSkipped?.let { this.numberOfSkipped = it }
                }
            }
        }

        val learnedFlashCardEntities : List<FlashCardLearning> = createFlashCardLearningEntities(learnedFlashcardList, numberOfLearned = 1)
        val skippedFlashCardEntities : List<FlashCardLearning> = createFlashCardLearningEntities(skippedFlashcardList, numberOfSkipped = 1)

        val flashCardLearningEntities = learnedFlashCardEntities + skippedFlashCardEntities
        val itemIds = flashCardLearningEntities.map { it.itemId }
        val learningContentIds = flashCardLearningEntities.map { it.learningContentId }
        val learningContentTypes = flashCardLearningEntities.map { it.learningContentType }

        val existingEntitiesMap = flashCardLearningRepository.findAllByItemIdInAndLearningContentIdInAndLearningContentTypeIn(
            itemIds, learningContentIds, learningContentTypes
        ).associateBy { Triple(it.itemId, it.learningContentId, it.learningContentType) }

        val updatedFlashCardEntities = flashCardLearningEntities.map {
            val key = Triple(it.itemId, it.learningContentId, it.learningContentType)
            val existedFlashCardLearning = existingEntitiesMap[key]

            if (existedFlashCardLearning == null) {
                it
            } else {
                existedFlashCardLearning.numberOfLearned += it.numberOfLearned
                existedFlashCardLearning.numberOfSkipped += it.numberOfSkipped
                existedFlashCardLearning
            }
        }

        return flashCardLearningRepository.saveAll(updatedFlashCardEntities)
    }
}