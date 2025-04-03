package com.bumble.appyx.sandbox.stub

import com.badoo.mvicore.feature.Feature
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer

open class FeatureStub<Wish : Any, State : Any, News : Any>(
    initialState: State,
    val wishesRelay: PublishRelay<Wish> = PublishRelay.create(),
    val statesRelay: BehaviorRelay<State> = BehaviorRelay.createDefault(initialState),
    val newsRelay: PublishRelay<News> = PublishRelay.create(),
    private val disposable: Disposable = Disposable.empty()
) : Feature<Wish, State, News>,
    ObservableSource<State> by statesRelay,
    Consumer<Wish> by wishesRelay,
    Disposable by disposable {

    override val state: State
        get() = statesRelay.value!!

    override val news: ObservableSource<News>
        get() = newsRelay
}
