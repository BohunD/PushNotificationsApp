package com.apps.pushnotificationsapp.presentation.newReminderScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.apps.pushnotificationsapp.presentation.newReminderScreen.ui.NewReminderScreen

const val NEW_REMINDER_ROUTE = "new_reminder/{id}"
const val ID = "id"

fun NavController.navigateToNewReminder(id: Int?) = navigate("new_reminder/$id")

fun NavGraphBuilder.newReminderScreen(onCloseScreen: () -> Unit) {
    composable(
        route = NEW_REMINDER_ROUTE,
        arguments = listOf(navArgument(ID) { type = NavType.IntType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt(ID) ?: -1
        NewReminderScreen(id) {
            onCloseScreen()
        }
    }
}