package com.bumble.appyx.sandbox.stub

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.ParentNodeView
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer

open class NodeViewStub<Event : Any, ViewModel : Any, NavTarget : Any>(
    val eventsRelay: PublishRelay<Event> = PublishRelay.create(),
    val viewModelRelay: PublishRelay<ViewModel> = PublishRelay.create(),
    private val disposable: Disposable = Disposable.empty()
) : ParentNodeView<NavTarget>,
    ObservableSource<Event> by eventsRelay,
    Consumer<ViewModel> by viewModelRelay,
    Disposable by disposable {

    @Composable
    override fun ParentNode<NavTarget>.NodeView(modifier: Modifier) = Unit
}
