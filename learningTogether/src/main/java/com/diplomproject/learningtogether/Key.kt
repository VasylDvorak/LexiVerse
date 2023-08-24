package com.diplomproject.learningtogether

object Key {

    //TEG open fragment
    internal const val TEG_TASK_CONTAINER_KEY = "TEG_TASK_CONTROLLER_LAYOUT_KEY"
    internal const val TEG_LEARNING_CONTAINER_KEY = "TEG_TASK_CONTROLLER_LAYOUT_KEY"
    internal const val TEG_COURSES_CONTAINER_KEY = "TEG_TASK_CONTROLLER_LAYOUT_KEY"
    internal const val TEG_SUCCESS_CONTAINER_KEY = "TEG_SUCCESS_CONTAINER_KEY"
    const val TEG_FAVOURITE_CONTAINER_KEY = "TEG_SUCCESS_CONTAINER_KEY"
    internal const val SHOW_ALL_CONTAINER_KEY = "SHOW_ALL_CONTAINER_KEY"
    internal const val TOGETHER_ACTIVITY_REQUEST_CODE = 200


    //
    internal const val THEME_ID_ARGS_KEY = "THEME_ARGS_KEY"
    internal const val LESSON_ID_ARGS_KEY = "LESSON_ID_ARGS_KEY"
    internal const val COURSE_ID_ARGS_KEY = "COURSE_ID_ARGS_KEY"
    internal const val FAVOURITE_ID_ARGS_KEY = "FAVOURITE_ID_ARGS_KEY"
    const val LEARNING_TOGETHER_REQUEST_KOD = "LEARNING_TOGETHER_REQUEST_KOD"
    const val START_FAVORITES_FRAGMENT = "START_FAVORITES_FRAGMENT"

    internal const val DEFAULT_COURSE_ID_KEY = -1L
    internal const val DEFAULT_LESSON_ID_KEY = -1L

    //Image
    internal const val VICTORY_FINISH_IMAGE_KEY =
        "https://avatars.mds.yandex.net/get-altay/2447509/2a0000017406f520b2f2412e0e106be83078/XXL"

    //res
    internal const val ASSETS_LESSONS_DIR_NAME_KEY =
        "lessons"//lessons потому что мы сканируем эту папку

    //ссылка на бд
    internal const val ASSETS_LESSONS_TASK_KEY =
        "lessons_task.json"

    //ссылка на бд
    internal const val DATABASE_URL_KEY =
        "https://learningtogether-f18a6-default-rtdb.europe-west1.firebasedatabase.app/"
//        "https://lessons-147e1-default-rtdb.europe-west1.firebasedatabase.app/"

    //TEG для SingleLiveEvent
    internal const val TAG = "SingleLiveEvent"

    //Уведомления
    internal const val SHOW_NOTICE_TASK_FRAGMENT_KEY = "Вы ошиблись, попробуйте еще раз!!!"
}