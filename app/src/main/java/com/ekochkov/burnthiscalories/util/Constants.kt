package com.ekochkov.burnthiscalories.util

object Constants {
    const val PROFILE_KEY = "Profile"
    const val BUNDLE_KEY = "Bundle"
    const val BURN_LIST_KEY = "BurnList"

    const val BURN_EVENT_STATUS_DONE = 0
    const val BURN_EVENT_STATUS_IN_PROGRESS = 1

    const val BURN_EVENT_STATUS_DONE_TEXT = "выполнено"
    const val BURN_EVENT_STATUS_IN_PROGRESS_TEXT = "в процессе"
    const val BURN_EVENT_STATUS_NOT_FOUND_TEXT = "не найден"

    const val FIELDS_NOT_FILLED = "поля не заполнены"

    const val PRODUCT_ADDED = "продукт добавлен"
    const val PRODUCT_ADDED_ERROR = "произошла ошибка"
    const val PRODUCT_UPDATED = "продукт обновлен"
    const val PRODUCT_UPDATED_ERROR = "произошла ошибка"
    const val PRODUCT_DELETED = "продукт удален"
    const val DB_RESULT_ERROR = "произошла ошибка"
    const val PRODUCT_WITH_NAME_ALREADY_EXIST = "такой продукт уже существует"

    const val NO_PRODUCT_ID = -1

    const val EXCEPTION_MESSAGE_ARGUMENT_IS_NULL = "Required argument is null"
    const val EXCEPTION_MESSAGE_UNKNOWN_VIEW_MODEL = "Unknown view model class"

    const val PROFILE_IS_NOT_FILLED = 0
    const val PROFILE_IS_FILLED = 1
}