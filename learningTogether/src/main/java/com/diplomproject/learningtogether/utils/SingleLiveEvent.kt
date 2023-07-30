package com.diplomproject.learningtogether.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * Наблюдаемая программа lifecycle-aware, которая отправляет только новые обновления после подписки,
 * используется для таких событий, как навигация и сообщения в закусочной.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 * Это позволяет избежать распространенной проблемы с событиями: при изменении конфигурации
 * (например, при вращении) может быть выдано обновление, если наблюдатель активен. Эти текущие
 * данные вызывают наблюдаемый объект только в том случае, если есть явный вызов set Value()
 * или call().
 *
 * Note that only one observer is going to be notified of changes.
 * Обратите внимание, что только один наблюдатель будет уведомлен об изменениях.
 *
 * этот класс расширяет возможности observe. с помощью observe мы подписываемся на события (класс CoursesFragment)
 */

private const val TAG = "SingleLiveEvent"

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val mPending =
        AtomicBoolean(false)//AtomicBoolean - это для того чтобы переменная была потоко безопасной

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        if (hasActiveObservers()) {
            Log.w(
                TAG,
                "Multiple observers registered but only one will be notified of changes."
            )
            Log.w(
                TAG,
                "Зарегистрировано несколько наблюдателей, но только один будет уведомлен об изменениях."
            )
        }
        // Observe the internal MutableLiveData (Наблюдайте за внутренними изменяемыми оперативными данными)
        super.observe(owner) { t: T? ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     * Используется для случаев, когда T равно Void, чтобы сделать вызовы более чистыми.
     */
    @MainThread
    fun call() {
        value = null
    }
}