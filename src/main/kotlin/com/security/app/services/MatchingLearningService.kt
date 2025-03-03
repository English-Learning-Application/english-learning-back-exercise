package com.security.app.services

import com.security.app.entities.MatchingLearning
import com.security.app.entities.PronunciationLearning
import com.security.app.model.AchievementProgressType
import com.security.app.model.LearningContentType
import com.security.app.repositories.MatchingLearningRepository
import com.security.app.request.MatchingLearningInfo
import com.security.app.utils.JwtTokenUtils
import com.security.app.utils.toUUID
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MatchingLearningService(
    private val matchingLearningRepository: MatchingLearningRepository,
    private val learningContentService: LearningContentService,
    private val jwtTokenUtils: JwtTokenUtils,
    private val achievementUpdateService: AchievementUpdateService
) {
    @Transactional
    fun updateMatchingLearning(
        tokenString: String,
        correctMatchingInfoList : List<MatchingLearningInfo>,
        incorrectMatchingInfoList: List<MatchingLearningInfo>,
        courseId: String
    ) : List<MatchingLearning>? {
        val extractedLearningContentIds = (correctMatchingInfoList + incorrectMatchingInfoList).map { it.learningContentId }.distinct()
        val learningContentResponse =
            learningContentService.getLearningContentByIds(extractedLearningContentIds, tokenString)

        if(extractedLearningContentIds.size != learningContentResponse?.size) {
            return null
        }

        val userId = jwtTokenUtils.getUserId(tokenString) ?: return null

        fun createMatchingLearningEntities(
            matchingLearningInfoList: List<MatchingLearningInfo>,
            numberOfCorrect: Int? = null,
            numberOfIncorrect: Int? = null
        ): List<MatchingLearning> {
            return matchingLearningInfoList.map { matchingInfo ->
                MatchingLearning().apply{
                    learningContentId = matchingInfo.learningContentId.toUUID()
                    itemId = matchingInfo.itemId.toUUID()
                    learningContentType = LearningContentType.fromString(matchingInfo.learningContentType)
                    numberOfCorrect?.let { this.numberOfCorrect = it }
                    numberOfIncorrect?.let { this.numberOfIncorrect = it}
                    this.courseId = courseId.toUUID()
                }
            }
        }

        val correctMatchingEntities : List<MatchingLearning> = createMatchingLearningEntities(correctMatchingInfoList, numberOfCorrect = 1)
        val incorrectMatchingEntities : List<MatchingLearning> = createMatchingLearningEntities(incorrectMatchingInfoList, numberOfIncorrect = 1)

        val matchingLearningEntities = correctMatchingEntities + incorrectMatchingEntities
        val itemIds = matchingLearningEntities.map { it.itemId }
        val learningContentIds = matchingLearningEntities.map { it.learningContentId }
        val learningContentTypes = matchingLearningEntities.map { it.learningContentType }

        val existingEntitiesMap = matchingLearningRepository.findAllByItemIdInAndLearningContentIdInAndLearningContentTypeInAndUserIdAndCourseId(
            itemIds, learningContentIds, learningContentTypes, userId.toUUID(), courseId.toUUID()
        ).associateBy { Triple(it.itemId, it.learningContentId, it.learningContentType) }

        val updatedMatchingEntities = matchingLearningEntities.map {
            val key = Triple(it.itemId, it.learningContentId, it.learningContentType)
            val existedMatchingEntity = existingEntitiesMap[key]

            if (existedMatchingEntity == null) {
                it.userId = userId.toUUID()
                it
            } else {
                existedMatchingEntity.numberOfCorrect += it.numberOfCorrect
                existedMatchingEntity.numberOfIncorrect += it.numberOfIncorrect
                existedMatchingEntity
            }
        }

        achievementUpdateService.updateAchievements(userId, "MATCHING", correctMatchingInfoList.size, AchievementProgressType.UPDATE.value)

        return matchingLearningRepository.saveAll(updatedMatchingEntities)
    }

    fun getProgress(userId: String, courseIds: List<String>): List<MatchingLearning> {
        return matchingLearningRepository.findAllByUserIdAndCourseIdIsIn(userId.toUUID(), courseIds.map { it.toUUID() })
    }

    fun getProgressOfUser(userId: String): List<MatchingLearning> {
        return matchingLearningRepository.findAllByUserId(userId.toUUID())
    }
}