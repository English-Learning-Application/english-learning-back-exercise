package com.security.app.services

import com.security.app.entities.QuizLearning
import com.security.app.model.AchievementProgressType
import com.security.app.model.LearningContentType
import com.security.app.repositories.QuizLearningRepository
import com.security.app.request.QuestionLearningInfo
import com.security.app.utils.JwtTokenUtils
import com.security.app.utils.toUUID
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class QuizLearningService(
    private val quizLearningRepository: QuizLearningRepository,
    private val learningContentService: LearningContentService,
    private val jwtTokenUtils: JwtTokenUtils,
    private val achievementUpdateService: AchievementUpdateService
) {
    @Transactional
    fun updateQuizLearning(
        tokenString: String,
        correctQuizInfoList : List<QuestionLearningInfo>,
        incorrectQuizInfoList: List<QuestionLearningInfo>,
        courseId: String
    ) : List<QuizLearning>? {
        val extractedLearningContentIds = (correctQuizInfoList + incorrectQuizInfoList).map { it.learningContentId }.distinct()
        val learningContentResponse =
            learningContentService.getLearningContentByIds(extractedLearningContentIds, tokenString)

        if(extractedLearningContentIds.size != learningContentResponse?.size) {
            return null
        }

        val userId = jwtTokenUtils.getUserId(tokenString) ?: return null

        fun createQuizLearningEntities(
            quizLearningInfoList: List<QuestionLearningInfo>,
            numberOfCorrect: Int? = null,
            numberOfIncorrect: Int? = null
        ): List<QuizLearning> {
            return quizLearningInfoList.map { quizInfo ->
                QuizLearning().apply {
                    learningContentId = quizInfo.learningContentId.toUUID()
                    itemId = quizInfo.itemId.toUUID()
                    learningContentType = LearningContentType.fromString(quizInfo.learningContentType)
                    numberOfCorrect?.let { this.numberOfCorrect = it }
                    numberOfIncorrect?.let { this.numberOfIncorrect = it}
                    this.courseId = courseId.toUUID()
                }
            }
        }

        val correctQuizEntities : List<QuizLearning> = createQuizLearningEntities(correctQuizInfoList, numberOfCorrect = 1)
        val incorrectQuizEntities : List<QuizLearning> = createQuizLearningEntities(incorrectQuizInfoList, numberOfIncorrect = 1)

        val quizLearningEntities = correctQuizEntities + incorrectQuizEntities
        val itemIds = quizLearningEntities.map { it.itemId }
        val learningContentIds = quizLearningEntities.map { it.learningContentId }
        val learningContentTypes = quizLearningEntities.map { it.learningContentType }

        val existingEntitiesMap = quizLearningRepository.findAllByItemIdInAndLearningContentIdInAndLearningContentTypeInAndUserIdAndCourseId(
            itemIds, learningContentIds, learningContentTypes, userId.toUUID(), courseId.toUUID()
        ).associateBy { Triple(it.itemId, it.learningContentId, it.learningContentType) }

        val updatedQuizEntities = quizLearningEntities.map {
            val key = Triple(it.itemId, it.learningContentId, it.learningContentType)
            val existedQuizEntity = existingEntitiesMap[key]

            if (existedQuizEntity == null) {
                it.userId = userId.toUUID()
                it
            } else {
                existedQuizEntity.numberOfCorrect += it.numberOfCorrect
                existedQuizEntity.numberOfIncorrect += it.numberOfIncorrect
                existedQuizEntity
            }
        }

        val separatedCourseIds = updatedQuizEntities.map { it.courseId }.distinct()
        achievementUpdateService.updateAchievements(userId, "QUIZ", separatedCourseIds.size, AchievementProgressType.ADD.value)

        return quizLearningRepository.saveAll(updatedQuizEntities)
    }

    fun getProgress(userId: String, courseIds: List<String>): List<QuizLearning> {
        return quizLearningRepository.findAllByUserIdAndCourseIdIsIn(userId.toUUID(), courseIds.map { it.toUUID() })
    }

    fun getProgressOfUser(userId: String): List<QuizLearning> {
        return quizLearningRepository.findAllByUserId(userId.toUUID())
    }
}