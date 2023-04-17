package ch.epfl.sdp.cook4me.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
class ComposePermissionStatusProvider(
    private val permissions: List<String>
) : PermissionStatusProvider {
    @Composable
    private fun getPermissionsState(): MultiplePermissionsState =
        rememberMultiplePermissionsState(permissions)

    @Composable
    override fun allPermissionsGranted() = getPermissionsState().allPermissionsGranted

    @Composable
    override fun shouldShowRationale() = getPermissionsState().shouldShowRationale

    @Composable
    override fun getRevokedPermissions() = getPermissionsState().revokedPermissions.map { it.permission }.toList()

    @Composable
    override fun requestAllPermissions() {
        val permissionsState = getPermissionsState()
        LaunchedEffect(permissionsState) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
}
