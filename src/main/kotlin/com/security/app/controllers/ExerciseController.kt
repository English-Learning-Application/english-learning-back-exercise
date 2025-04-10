package com.security.app.controllers

import com.security.app.entities.FlashCardLearning
import com.security.app.entities.MatchingLearning
import com.security.app.entities.PronunciationLearning
import com.security.app.entities.QuizLearning
import com.security.app.model.*
import com.security.app.request.*
import com.security.app.response.PronunciationAssessmentApiResponse
import com.security.app.services.FlashCardLearningService
import com.security.app.services.MatchingLearningService
import com.security.app.services.PronunciationLearningService
import com.security.app.services.QuizLearningService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/exercises")
class ExerciseController(
    private val flashCardLearningService: FlashCardLearningService,
    private val quizLearningService: QuizLearningService,
    private val pronunciationLearningService: PronunciationLearningService,
    private val matchingLearningService: MatchingLearningService
) {
    @PostMapping("/flashcard")
    fun updateFlashCardLearning(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: FlashCardLearningUpdateRequest
    ) :ResponseEntity<ListMessage<FlashCardLearningModel>> {
        try{
            val tokenString = httpServletRequest.getHeader("Authorization").removePrefix("Bearer ")
            val list = flashCardLearningService.updateFlashCardLearning(
                tokenString,
                request.learnedFlashCardLearningInfo,
                request.skippedFlashCardLearningInfo,
                request.courseId
            )
                ?: return ResponseEntity.badRequest().body(ListMessage.BadRequest("Content not found"))

            val modelList = list.map { FlashCardLearningModel.fromEntity(it) }
            return ResponseEntity.ok(ListMessage.Success("Flashcard learning updated", modelList))
        }catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Content not found"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Error occurred"))
        }
    }

    @PostMapping("/matching")
    fun updateMatchingLearning(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: MatchingLearningUpdateRequest
    ) :ResponseEntity<ListMessage<MatchingLearningModel>> {
        try{
            val tokenString = httpServletRequest.getHeader("Authorization").removePrefix("Bearer ")
            val list = matchingLearningService.updateMatchingLearning(
                tokenString,
                request.correctMatchingLearningInfo,
                request.incorrectMatchingLearningInfo,
                request.courseId
            )
                ?: return ResponseEntity.badRequest().body(ListMessage.BadRequest("Content not found"))

            val modelList = list.map { MatchingLearningModel.fromEntity(it) }
            return ResponseEntity.ok(ListMessage.Success("Matching learning updated", modelList))
        }catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Content not found"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Error occurred"))
        }
    }

    @PostMapping("/quiz")
    fun updateQuizLearning(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: QuestionLearningUpdateRequest
    ) :ResponseEntity<ListMessage<QuizLearningModel>> {
        try{
            val tokenString = httpServletRequest.getHeader("Authorization").removePrefix("Bearer ")
            val list = quizLearningService.updateQuizLearning(
                tokenString,
                request.correctQuestionLearningInfo,
                request.incorrectQuestionLearningInfo,
                request.courseId
            )
                ?: return ResponseEntity.badRequest().body(ListMessage.BadRequest("Content not found"))

            val modelList = list.map { QuizLearningModel.fromEntity(it) }
            return ResponseEntity.ok(ListMessage.Success("Quiz learning updated", modelList))
        }catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Content not found"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Error occurred"))
        }
    }

    @PostMapping("/pronunciation/assessment")
    fun pronunciationAssessment(
        @RequestBody request: PronunciationAssessmentRequest
    ) :ResponseEntity<Message<PronunciationAssessmentApiResponse>> {
        try{
            val assessment = pronunciationLearningService.getPronunciationAssessment(
                base64EncodedAudio = request.base64Audio,
                audioFormat = request.audioFormat,
                originalText = request.originalText,
            ) ?: return ResponseEntity.badRequest().body(Message.BadRequest("Content not found"))

            return ResponseEntity.ok(Message.Success("Pronunciation assessment", assessment))
        }catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(Message.BadRequest(e.message ?: "Content not found"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Message.BadRequest(e.message ?: "Error occurred"))
        }
    }

    @PostMapping("/pronunciation")
    fun updatePronunciationLearning(
        httpServletRequest: HttpServletRequest,
        @RequestBody request: PronunciationLearningUpdateRequest
    ) :ResponseEntity<ListMessage<PronunciationLearningModel>> {
        try{
            val tokenString = httpServletRequest.getHeader("Authorization").removePrefix("Bearer ")
            val list = pronunciationLearningService.updatePronunciationLearning(tokenString, request.pronunciationLearningUpdateInfo, request.courseId)
                ?: return ResponseEntity.badRequest().body(ListMessage.BadRequest("Content not found"))

            val modelList = list.map { PronunciationLearningModel.fromEntity(it) }
            return ResponseEntity.ok(ListMessage.Success("Pronunciation learning updated", modelList))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Content not found"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Error occurred"))
        }
    }

    @GetMapping("/progress")
    fun getProgress(
        httpServletRequest: HttpServletRequest,
        @RequestParam("courseIds") courseIds: List<String>
    ) :ResponseEntity<Message<ProgressModel>> {
        val auth = SecurityContextHolder.getContext().authentication
        val userId = auth.name
        try{
            if(courseIds.isEmpty()) {
                val allFlashcardProgress = flashCardLearningService.getProgressOfUser(userId)
                val allQuizProgress = quizLearningService.getProgressOfUser(userId)
                val allPronunciationProgress = pronunciationLearningService.getProgressOfUser(userId)
                val allMatchingProgress = matchingLearningService.getProgressOfUser(userId)

                val progressModel = ProgressModel(
                    flashCardProgress = allFlashcardProgress.map { FlashCardLearningModel.fromEntity(it) },
                    quizProgress = allQuizProgress.map { QuizLearningModel.fromEntity(it) },
                    pronunciationProgress = allPronunciationProgress.map { PronunciationLearningModel.fromEntity(it) },
                    matchingProgress = allMatchingProgress.map { MatchingLearningModel.fromEntity(it) }
                )

                return ResponseEntity.ok(Message.Success("Progress found", progressModel))
            }

            val flashCardProgress = flashCardLearningService.getProgress(userId, courseIds)
            val quizProgress = quizLearningService.getProgress(userId, courseIds)
            val pronunciationProgress = pronunciationLearningService.getProgress(userId, courseIds)
            val matchingProgress = matchingLearningService.getProgress(userId, courseIds)

            val progressModel = ProgressModel(
                flashCardProgress = flashCardProgress.map { FlashCardLearningModel.fromEntity(it) },
                quizProgress = quizProgress.map { QuizLearningModel.fromEntity(it) },
                pronunciationProgress = pronunciationProgress.map { PronunciationLearningModel.fromEntity(it) },
                matchingProgress = matchingProgress.map { MatchingLearningModel.fromEntity(it) }
            )

            return ResponseEntity.ok(Message.Success("Progress found", progressModel))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(Message.BadRequest(e.message ?: "Content not found"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Message.BadRequest(e.message ?: "Error occurred"))
        }
    }
}