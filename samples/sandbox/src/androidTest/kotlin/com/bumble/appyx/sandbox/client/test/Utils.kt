package com.bumble.appyx.sandbox.client.test

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.observers.TestObserver
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan

fun <T : Any> ObservableSource<out T>.wrapToObservable(): Observable<T> = Observable.wrap(cast())

inline fun <reified T> Any?.cast(): T = this as T

fun <T : Any> TestObserver<T>.assertLastValueEqual(value: T) {
    assertThat(this.values().size, greaterThan(0))
    this.assertValueAt(values().size - 1, value)
}
