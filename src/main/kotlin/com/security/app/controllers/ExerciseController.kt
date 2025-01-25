package com.security.app.controllers

import com.security.app.entities.FlashCardLearning
import com.security.app.entities.MatchingLearning
import com.security.app.entities.QuizLearning
import com.security.app.model.FlashCardLearningModel
import com.security.app.model.ListMessage
import com.security.app.model.MatchingLearningModel
import com.security.app.model.QuizLearningModel
import com.security.app.request.FlashCardLearningUpdateRequest
import com.security.app.request.MatchingLearningUpdateRequest
import com.security.app.request.QuestionLearningUpdateRequest
import com.security.app.services.FlashCardLearningService
import com.security.app.services.MatchingLearningService
import com.security.app.services.PronunciationLearningService
import com.security.app.services.QuizLearningService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
            val list = flashCardLearningService.updateFlashCardLearning(tokenString, request.learnedFlashCardLearningInfo, request.skippedFlashCardLearningInfo)
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
            val list = matchingLearningService.updateMatchingLearning(tokenString, request.correctMatchingLearningInfo, request.incorrectMatchingLearningInfo)
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
            val list = quizLearningService.updateQuizLearning(tokenString, request.correctQuestionLearningInfo, request.incorrectQuestionLearningInfo)
                ?: return ResponseEntity.badRequest().body(ListMessage.BadRequest("Content not found"))

            val modelList = list.map { QuizLearningModel.fromEntity(it) }
            return ResponseEntity.ok(ListMessage.Success("Quiz learning updated", modelList))
        }catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Content not found"))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(ListMessage.BadRequest(e.message ?: "Error occurred"))
        }
    }
}