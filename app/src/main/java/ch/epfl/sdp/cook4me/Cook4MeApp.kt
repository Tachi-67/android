package ch.epfl.sdp.cook4me

import EditProfileScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.epfl.sdp.cook4me.ui.CreateEvent
import ch.epfl.sdp.cook4me.ui.OverviewScreen
import ch.epfl.sdp.cook4me.ui.login.LoginScreen
import ch.epfl.sdp.cook4me.ui.map.GoogleMapView
import ch.epfl.sdp.cook4me.ui.map.dummyMarkers
import ch.epfl.sdp.cook4me.ui.profile.ProfileScreen
import ch.epfl.sdp.cook4me.ui.tupperwareform.TupCreationScreenWithState
import ch.epfl.sdp.cook4me.ui.tupperwareform.TupCreationViewModel

/**
 * enum values that represent the screens in the app
 */
private enum class Screen {
    Login,
    CreateTupperwareScreen,
    OverviewScreen,
    ProfileScreen,
    EditProfileScreen,
    Map,
    CreateEventScreen
}

@Composable
fun Cook4MeApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Login.name) {
        composable(route = Screen.Login.name) {
            LoginScreen(
                onSuccessfulLogin = { navController.navigate(Screen.OverviewScreen.name) }
            )
        }
        composable(route = Screen.OverviewScreen.name) {
            OverviewScreen(
                onMapClick = { navController.navigate(Screen.Map.name) },
                onProfileClick = { navController.navigate(Screen.ProfileScreen.name) },
                onEditProfileClick = { navController.navigate(Screen.EditProfileScreen.name) },
                onAddTupperwareClick = { navController.navigate(Screen.CreateTupperwareScreen.name) },
                onAddEventClick = { navController.navigate(Screen.CreateEventScreen.name) },
            )
        }
        composable(route = Screen.Map.name) {
            GoogleMapView(modifier = Modifier.fillMaxSize(), markers = dummyMarkers)
        }
        composable(route = Screen.ProfileScreen.name) {
            ProfileScreen()
        }
        composable(route = Screen.EditProfileScreen.name) {
            EditProfileScreen()
        }
        composable(route = Screen.CreateTupperwareScreen.name) {
            TupCreationScreenWithState(TupCreationViewModel())
        }
        composable(route = Screen.CreateEventScreen.name) {
            CreateEvent()
        }
    }
}
