package com.apollo.task.constant;

public interface RoutingConstant {

    String TASK_PATH = "/task";
    String USER_PATH = "/user";
    String TASK_ID = "taskId";
    String TASK_ID_PATH = "/{" + TASK_ID + "}";
    String OWNER_ID = "ownerId";
    String OWNER_ID_PATH = "/{" + OWNER_ID + "}";
    String USER_ID = "userId";
    String USER_ID_PATH = "/{" + USER_ID + "}";
    String TASK_TYPE = "taskType";
    String TASK_TYPE_PATH = "/{" + TASK_TYPE + "}";
    String TASK_STATUS = "taskStatus";
    String TASK_STATUS_PATH = "/{" + TASK_STATUS + "}";
    String GROUP_NAME = "groupName";
    String GROUP_NAME_PATH = "/{" + GROUP_NAME + "}";
    String TASK_USER_PATH = TASK_PATH + USER_PATH + USER_ID_PATH;
}
