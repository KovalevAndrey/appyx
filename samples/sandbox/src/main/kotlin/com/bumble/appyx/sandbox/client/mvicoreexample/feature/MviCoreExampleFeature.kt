package com.bumble.appyx.sandbox.client.mvicoreexample.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.Effect
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.News
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.State
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.Wish
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.Wish.ChangeState
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.Wish.ChildInput
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.Wish.Finish
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.Wish.LoadData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class MviCoreExampleFeature(initialStateName: String) :
    ActorReducerFeature<Wish, Effect, State, News>(
        initialState = State.InitialState(initialStateName),
        actor = ActorImpl,
        reducer = ReducerImpl,
        newsPublisher = NewsPublisherImpl
    ) {

    sealed class State {
        data class InitialState(val stateName: String) : State()
        data object Loading : State()
        data class Loaded(val stateName: String) : State()
        data object Finished : State()
    }

    sealed class Wish {
        data class ChildInput(val data: String) : Wish()
        data object LoadData : Wish()
        data object Finish : Wish()
        data class ChangeState(val stateName: String) : Wish()
    }

    sealed class Effect {
        data class ChangeState(val stateName: String) : Effect()
        data class ChildInput(val data: String) : Effect()
        data object Loading : Effect()
        data object Finished : Effect()
        data object DataLoaded : Effect()
    }

    sealed class News {
        data object Finished : News()
        data class StateUpdated(val message: String) : News()
    }

    private object ActorImpl : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> =
            when (wish) {
                is ChangeState -> Effect.ChangeState(wish.stateName).toObservable()
                is Finish -> Effect.Finished.toObservable()
                is ChildInput -> Effect.ChildInput(wish.data).toObservable()
                is LoadData -> Observable.timer(2, TimeUnit.SECONDS)
                    .map<Effect> { Effect.DataLoaded }
                    .startWithItem(Effect.Loading)
                    .observeOn(AndroidSchedulers.mainThread())
            }
    }

    private object ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State =
            when (effect) {
                is Effect.ChangeState -> State.Loaded(effect.stateName)
                is Effect.Finished -> State.Finished
                is Effect.ChildInput -> State.Loaded(effect.data)
                is Effect.DataLoaded -> State.Loaded("Loaded")
                is Effect.Loading -> State.Loading
            }
    }

    private object NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(action: Wish, effect: Effect, state: State): News? =
            when (effect) {
                is Effect.ChangeState -> News.StateUpdated(effect.stateName)
                is Effect.Finished -> News.Finished
                else -> null
            }
    }
}


fun <T : Any> T?.toObservable(): Observable<T> =
    if (this == null) Observable.empty() else Observable.just(this)
