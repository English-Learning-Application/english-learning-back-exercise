package com.security.app.model

enum class LearningContentType(val value: String) {
    WORD("WORD"),
    EXPRESSION("EXPRESSION"),
    IDIOM("IDIOM"),
    SENTENCE("SENTENCE"),
    PHRASAL_VERB("PHRASAL_VERB"),
    TENSE("TENSE"),
    PHONETICS("PHONETICS");

    companion object {
        fun fromString(value: String): LearningContentType {
            return when (value) {
                "WORD" -> WORD
                "EXPRESSION" -> EXPRESSION
                "IDIOM" -> IDIOM
                "SENTENCE" -> SENTENCE
                "PHRASAL_VERB" -> PHRASAL_VERB
                "TENSE" -> TENSE
                "PHONETICS" -> PHONETICS
                else -> throw IllegalArgumentException("Learning content type not found")
            }
        }
    }
}

enum class AchievementProgressType(val value: String) {
    ADD("ADD"),
    UPDATE("UPDATE");

    companion object {
        fun fromValue(value: String): AchievementProgressType {
            return when (value) {
                "ADD" -> ADD
                "UPDATE" -> UPDATE
                else -> throw IllegalArgumentException()
            }
        }
    }
}