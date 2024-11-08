package com.apps.pushnotificationsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.apps.pushnotificationsapp.presentation.mainScreen.MAIN_SCREEN_ROUTE
import com.apps.pushnotificationsapp.presentation.mainScreen.mainScreen
import com.apps.pushnotificationsapp.presentation.newReminderScreen.navigateToNewReminder
import com.apps.pushnotificationsapp.presentation.newReminderScreen.newReminderScreen


@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = MAIN_SCREEN_ROUTE,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        mainScreen(
            onNewReminderClicked = {navController.navigateToNewReminder(-1)},
            onEditReminderClicked = { navController.navigateToNewReminder(it) })

        newReminderScreen { navController.popBackStack() }
    }
}