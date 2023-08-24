package com.ekochkov.burnthiscalories.util

object Constants {
    const val PROFILE_KEY = "Profile"
    const val BUNDLE_KEY = "Bundle"
    const val BURN_LIST_KEY = "BurnList"
    const val BURN_EVENT_KEY = "burnEvent"
    const val BURN_EVENT_ID_KEY = "burnEventId"

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
    const val PROFILE_IS_NOT_FILLED_TEXT = "Профиль не заполнен"

    const val BURNLIST_IS_NOT_FILLED_TEXT = "Список продуктов отсутствует"

    const val BURN_EVENT_IS_RUNNING = "сжигание калорий запущено"

    const val BURN_EVENT_SERVICE_CHANNEL_ID = "burnEventService"
    const val BURN_EVENT_SERVICE_CHANNEL_NAME = "burnEventService"
}