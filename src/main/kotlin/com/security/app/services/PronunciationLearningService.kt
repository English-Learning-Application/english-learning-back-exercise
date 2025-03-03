package com.security.app.services

import com.security.app.entities.PronunciationAssessment
import com.security.app.entities.PronunciationLearning
import com.security.app.model.AchievementProgressType
import com.security.app.model.LearningContentType
import com.security.app.repositories.PronunciationAssessmentRepository
import com.security.app.repositories.PronunciationLearningRepository
import com.security.app.request.PronunciationLearningUpdateInfo
import com.security.app.response.PronunciationAssessmentApiResponse
import com.security.app.utils.JwtTokenUtils
import com.security.app.utils.toUUID
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class PronunciationLearningService(
    private val pronunciationLearningRepository: PronunciationLearningRepository,
    private val pronunciationAssessmentRepository: PronunciationAssessmentRepository,
    private val learningContentService: LearningContentService,
    private val jwtTokenUtils: JwtTokenUtils,
    private val webClient: WebClient,
    private val achievementUpdateService: AchievementUpdateService
) {
    private final val PRONUNCIATION_ASSESSMENT_URL = System.getenv("PRONUNCIATION_ASSESSMENT_URL")
    private final val PRONUNCIATION_ASSESSMENT_API_KEY = System.getenv("PRONUNCIATION_ASSESSMENT_API_KEY")
    private final val PRONUNCIATION_ASSESSMENT_HOST = System.getenv("PRONUNCIATION_ASSESSMENT_API_HOST")
    fun getPronunciationAssessment(base64EncodedAudio: String, audioFormat: String, originalText: String) : PronunciationAssessmentApiResponse? {
        try{
        val response = webClient.post()
            .uri(PRONUNCIATION_ASSESSMENT_URL)
            .headers {
                header ->
                header.set("x-rapidapi-key", PRONUNCIATION_ASSESSMENT_API_KEY)
                header.set("x-rapidapi-host", PRONUNCIATION_ASSESSMENT_HOST)
                header.set("Content-Type", "application/json")
            }
            .bodyValue(mapOf("audio_base64" to base64EncodedAudio, "audio_format" to audioFormat, "text" to originalText))
            .retrieve()
            .bodyToMono(PronunciationAssessmentApiResponse::class.java)
            .block()
            return response
        }
        catch (e: Exception){
            println(e.message)
            return null
        }
    }

    @Transactional
    fun updatePronunciationLearning(
        tokenString: String,
        pronunciationLearningUpdateInfo: List<PronunciationLearningUpdateInfo>,
        courseId: String
    ) : List<PronunciationLearning>? {
        val extractedLearningContentIds = pronunciationLearningUpdateInfo.map { it.learningContentId }.distinct()
        val learningContentResponse =
            learningContentService.getLearningContentByIds(extractedLearningContentIds, tokenString)

        if(extractedLearningContentIds.size != learningContentResponse?.size) {
            return null
        }

        val userId = jwtTokenUtils.getUserId(tokenString) ?: return null

        val pronunciationLearningEntities : List<PronunciationLearning> = pronunciationLearningUpdateInfo.map { updateInfo ->
            val pronunciationLearning = PronunciationLearning().let {
                it.learningContentId = updateInfo.learningContentId.toUUID()
                it.itemId = updateInfo.itemId.toUUID()
                it.userId = userId.toUUID()
                it.learningContentType = LearningContentType.valueOf(updateInfo.learningContentType)
                it.courseId = courseId.toUUID()
                it
            }

            val assessmentInfo = updateInfo.pronunciationAssessmentInfo.map { pronunciationAssessmentLearningInfo ->
                val pronunciationAssessment = PronunciationAssessment().let {
                    it.pronunciationWord = pronunciationAssessmentLearningInfo.pronunciationWord
                    it.score = pronunciationAssessmentLearningInfo.score
                    it.pronunciationLearning = pronunciationLearning
                    it.pronunciationAccentPrediction = pronunciationAssessmentLearningInfo.pronunciationAccentPrediction
                    it.scoreCertificateEstimation = pronunciationAssessmentLearningInfo.scoreCertificateEstimation
                    it
                }
                pronunciationAssessment
            }

            pronunciationLearning.pronunciationLearningContents = assessmentInfo
            pronunciationLearning
        }

        val itemIds = pronunciationLearningEntities.map { it.itemId }
        val learningContentIds = pronunciationLearningEntities.map { it.learningContentId }
        val learningContentTypes = pronunciationLearningEntities.map { it.learningContentType }

        val existingEntitiesMap = pronunciationLearningRepository.findAllByItemIdInAndLearningContentIdInAndLearningContentTypeInAndUserIdAndCourseId(
            itemIds, learningContentIds, learningContentTypes, userId.toUUID(), courseId.toUUID()
        ).associateBy { Triple(it.itemId, it.learningContentId, it.learningContentType) }

        val updatedPronunciationEntities = pronunciationLearningEntities.map {
            val key = Triple(it.itemId, it.learningContentId, it.learningContentType)
            val existedPronunciationLearning = existingEntitiesMap[key]

            if (existedPronunciationLearning == null) {
                it.userId = userId.toUUID()
                it
            } else {
                it.pronunciationLearningContents.forEach { it.pronunciationLearning = existedPronunciationLearning }
                existedPronunciationLearning.pronunciationLearningContents += it.pronunciationLearningContents
                existedPronunciationLearning
            }
        }

        achievementUpdateService.updateAchievements(userId, "PRONUNCIATION", pronunciationLearningUpdateInfo.size, AchievementProgressType.UPDATE.value)

        return pronunciationLearningRepository.saveAll(updatedPronunciationEntities)
    }

    fun getProgress(userId : String, courseIds: List<String>): List<PronunciationLearning> {
        return pronunciationLearningRepository.findAllByUserIdAndCourseIdIsIn(userId.toUUID(), courseIds.map { it.toUUID() })
    }

    fun getProgressOfUser(userId: String): List<PronunciationLearning> {
        return pronunciationLearningRepository.findAllByUserId(userId.toUUID())
    }
}