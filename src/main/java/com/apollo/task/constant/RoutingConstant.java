package com.apollo.task.constant;

public abstract class RoutingConstant {

    public static final String TASK_PATH = "/task";
    public static final String USER_PATH = "/user";
    public static final String TASK_ID = "taskId";
    public static final String TASK_ID_PATH = "/{" + TASK_ID + "}";
    public static final String OWNER_ID = "ownerId";
    public static final String OWNER_ID_PATH = "/{" + OWNER_ID + "}";
    public static final String USER_ID = "userId";
    public static final String USER_ID_PATH = "/{" + USER_ID + "}";
    public static final String TASK_TYPE = "taskType";
    public static final String TASK_TYPE_PATH = "/{" + TASK_TYPE + "}";
    public static final String TASK_STATUS = "taskStatus";
    public static final String TASK_STATUS_PATH = "/{" + TASK_STATUS + "}";
    public static final String GROUP_NAME = "groupName";
    public static final String GROUP_NAME_PATH = "/{" + GROUP_NAME + "}";
    public static final String TASK_USER_PATH = TASK_PATH + USER_PATH + USER_ID_PATH;
}
