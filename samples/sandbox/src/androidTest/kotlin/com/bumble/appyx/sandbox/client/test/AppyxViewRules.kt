package com.bumble.appyx.sandbox.client.test

import com.bumble.appyx.core.node.NodeView
import com.bumble.appyx.core.node.ViewFactory
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Consumer

fun <ViewModel : Any, Event : Any, View> appyxViewRule(
    viewFactory: ViewFactory<View>,
) where View : NodeView, View : Consumer<in ViewModel>, View : ObservableSource<out Event> =
    AppyxMviViewTestRule(
        modelConsumer = { it },
        eventObservable = { it },
        viewFactory = viewFactory
    )
