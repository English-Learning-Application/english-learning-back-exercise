package com.security.app.services

import com.security.app.model.Message
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class AchievementUpdateService(
    private val webClient: WebClient
) {
    private final val ACHIEVEMENT_SERVICE_URL = System.getenv("ACHIEVEMENT_SERVICE_URL")

    fun updateAchievements(
        userId: String,
        type: String,
        value: Int,
        updateType: String
    ) {
        webClient.post()
            .uri("$ACHIEVEMENT_SERVICE_URL/update")
            .bodyValue(
                mapOf(
                    "userId" to userId,
                    "type" to type,
                    "value" to value,
                    "updateType" to updateType
                )
            )
            .retrieve()
            .bodyToMono(Message.Success::class.java)
            .subscribe()
    }
}