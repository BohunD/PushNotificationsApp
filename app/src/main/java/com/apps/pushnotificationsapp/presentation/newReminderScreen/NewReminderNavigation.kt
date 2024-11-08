package com.apps.pushnotificationsapp.presentation.newReminderScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val NEW_REMINDER_ROUTE = "new_reminder"

fun NavController.navigateToNewReminder() = navigate(NEW_REMINDER_ROUTE)

fun NavGraphBuilder.newReminderScreen(onCloseScreen: ()->Unit) {
    composable(
        route = NEW_REMINDER_ROUTE,
    ) {
        NewReminderScreen {
            onCloseScreen()
        }
    }
}