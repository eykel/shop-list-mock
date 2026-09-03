package com.eykel.shoplistmock.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface UiState
interface UiAction
interface UiEffect

/**
 * Base of every screen ViewModel: state as a [StateFlow], one-shot events as a [Channel].
 *
 * Each screen owns three declarations (`XState`, `XAction`, `XEffect`) in its `XContract.kt`
 * and a ViewModel that implements [onAction].
 */
abstract class MviViewModel<S : UiState, A : UiAction, E : UiEffect>(
    initialState: S
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    protected val currentState: S get() = _state.value

    protected fun setState(reducer: S.() -> S) {
        _state.update(reducer)
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch { _effect.send(effect) }
    }

    abstract fun onAction(action: A)
}
