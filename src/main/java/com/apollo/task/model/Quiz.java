package com.apollo.task.model;

import lombok.Data;

import java.util.*;

@Data
public class Quiz {

    private Date quizDueDate;
    private Boolean isActive, isPublic;
    private String quizChapterId, quizCourseId, quizOwnerId, quizDuration;
    private final String quizId = UUID.randomUUID().toString();
    private final Date quizDateOfCreation = Calendar.getInstance().getTime();
    private String quizName = quizId + "-" + quizDateOfCreation;
    private Map<String, String> quizQuestions = new HashMap<>();
    private Map<String, String> quizAnswerOptions = new HashMap<>();
    private Map<String, String> quizAnswer = new HashMap<>();
}
