package com.bumble.appyx.sandbox.client.mvicoreexample

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.sandbox.client.mvicoreexample.MviCoreExampleNode.Routing
import com.bumble.appyx.sandbox.client.mvicoreexample.MviCoreExampleViewImpl.Event
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.News
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.State
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.MviCoreExampleFeature.Wish
import com.bumble.appyx.sandbox.client.mvicoreexample.feature.ViewModel
import com.bumble.appyx.sandbox.stub.FeatureStub
import com.bumble.appyx.testing.unit.common.helper.parentNodeTestHelper
import com.bumble.appyx.sandbox.stub.NodeViewStub
import com.bumble.appyx.testing.junit4.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MviCoreExampleNodeTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private val view = object : NodeViewStub<Event, ViewModel, Routing>(), MviCoreExampleView {}

    private val backStack = BackStack<Routing>(
        initialElement = Routing.Child1,
        savedStateMap = null
    )

    private val feature = FeatureStub<Wish, State, News>(
        initialState = State.InitialState("Test Initial State")
    )

    private val interactor = MviCoreExampleInteractor(
        view = view,
        feature = feature,
        backStack = backStack
    )

    private lateinit var node: MviCoreExampleNode

    @Before
    fun setUp() {
        node = MviCoreExampleNode(
            view = view,
            buildContext = BuildContext.root(savedStateMap = null),
            plugins = listOf(interactor),
            backStack = backStack
        )
    }

    @Test
    fun `given node is created then first child is attached`() {
        node.parentNodeTestHelper().also {
            it.assertChildHasLifecycle(
                routing = Routing.Child1,
                state = Lifecycle.State.CREATED
            )
        }
    }

    @Test
    fun `given node is create when navigating to child 2 then child 2 is attached`() {
        val testHelper = node.parentNodeTestHelper()
        testHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {
            view.eventsRelay.accept(Event.SwitchChildClicked)

            testHelper.assertChildHasLifecycle(
                routing = Routing.Child2,
                state = Lifecycle.State.STARTED
            )
        }
    }
}
