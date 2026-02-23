package com.eduardoomarson.quizpdm.ui.feature.home

sealed interface HomeEvent {
    data object OnSinglePlayerClick : HomeEvent
    data object OnHomeClick : HomeEvent
    data object OnBoardClick : HomeEvent
    data object OnProfileClick : HomeEvent
    data object OnLogoutClick : HomeEvent
}