package com.apps.pushnotificationsapp.presentation.mainScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val MAIN_SCREEN_ROUTE = "main_screen"

fun NavController.navigateMainScreen() = navigate(MAIN_SCREEN_ROUTE)

fun NavGraphBuilder.mainScreen(onNewReminderClicked: ()->Unit) {
    composable(
        route = MAIN_SCREEN_ROUTE,
    ) {
        MainScreen {
            onNewReminderClicked()
        }
    }
}