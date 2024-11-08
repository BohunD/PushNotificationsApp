package com.apps.pushnotificationsapp.presentation.mainScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apps.pushnotificationsapp.presentation.mainScreen.ui.MainScreen

const val MAIN_SCREEN_ROUTE = "main_screen"

fun NavController.navigateMainScreen() = navigate(MAIN_SCREEN_ROUTE)

fun NavGraphBuilder.mainScreen(onNewReminderClicked: ()->Unit, onEditReminderClicked: (Int)->Unit) {
    composable(
        route = MAIN_SCREEN_ROUTE,
    ) {
        MainScreen(
            onNewReminderClicked,
            onEditReminderClicked
        )
    }
}