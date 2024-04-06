package com.qnenet.qne.objects.classes;

/**
 * Represents single user answer.
 *
 * @author Paul Parlett
 *
 */
public class QAnswer extends QAbstractEntity {

    private int questionId;
    private String value;

    /**
     * Default constructor for UserAnswer
     */
    public QAnswer() {
        super();
    }

    /**
     * Constructor for UserAnswer
     *
     * @param questionId the question id
     * @param value the value
     */
    public QAnswer(int questionId, String value) {
        super();
        this.questionId = questionId;
        this.value = value;
    }

    /**
     * Constructor for UserAnswer
     *
     * @param questionId the question id
     */
    public QAnswer(int questionId) {
        this(questionId, null);
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserAnswer [questionId=");
        builder.append(questionId);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }

}
