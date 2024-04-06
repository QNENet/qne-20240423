package com.qnenet.qne.objects.classes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class QAnswerSet extends QAbstractEntity {

//    private final UUI uuid;
    private final String questionsetName;
    private final List<QAnswer> qAnswers;
    private final Instant instant;
    private final String sessionId;

//    public String getUuid() {
//        return uuid;
//    }

    public String getQuestionsetName() {
        return questionsetName;
    }

    public List<QAnswer> getUserAnswers() {
        return qAnswers;
    }

    public Instant getInstant() {
        return instant;
    }

    public String getSessionId() {
        return sessionId;
    }

    public QAnswerSet(UUID uuid, String questionsetName, List<QAnswer> qAnswers, Instant instant, String sessionId) {
        this.setUUID(uuid);
        this.questionsetName = questionsetName;
        this.qAnswers = qAnswers;
        this.instant = instant;
        this.sessionId = sessionId;
    }
}
