package com.security.app.services

import com.nimbusds.jose.shaded.gson.Gson
import com.security.app.model.LearningContentModel
import com.security.app.model.ListMessage
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class LearningContentService(
    private val webClient: WebClient
) {
    private final val COURSE_SERVICE_URL = System.getenv("COURSE_SERVICE_URL")
    fun getLearningContentByIds(learningContentId: List<String>, tokenString: String): List<LearningContentModel>? {
        println("$COURSE_SERVICE_URL/learning-content?contentIds=${learningContentId.joinToString(",")}")
        return webClient.get()
            .uri("$COURSE_SERVICE_URL/learning-content?contentIds=${learningContentId.joinToString(",")}")
            .headers{
                headers ->
                headers.set("Authorization", "Bearer $tokenString")
            }
            .retrieve()
            .bodyToMono(ListMessage.Success::class.java)
            .block()
            ?.let {
                val gson = Gson()
                val listJson = gson.toJson(it.results)
                val learningContentList = gson.fromJson(listJson, Array<LearningContentModel>::class.java).toList()
                return learningContentList
            }
    }
}