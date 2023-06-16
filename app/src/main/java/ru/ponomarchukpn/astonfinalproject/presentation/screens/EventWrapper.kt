package ru.ponomarchukpn.astonfinalproject.presentation.screens

class EventWrapper<out T>(private val content: T) {
    //TODO наверное не нужен, лив датой же не пользуюсь

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}
