package com.apollo.task.model;

import java.util.*;

public class Quiz {

    private final String quizId = UUID.randomUUID().toString();
    private final Date quizDateOfCreation = Calendar.getInstance().getTime();
    private Date quizDueDate;
    private String quizName = quizId + "-" + quizDateOfCreation;
    private HashMap<String, String > quizQuestions = new HashMap<>();
    private HashMap<String, List> quizAnswerOptions = new HashMap<>();
    private HashMap<String, String> quizAnswer = new HashMap<>();
    private String quizChapterId;
    private String quizCourseId;
    private String quizOwnerId;
    private String quizDuration;
}
