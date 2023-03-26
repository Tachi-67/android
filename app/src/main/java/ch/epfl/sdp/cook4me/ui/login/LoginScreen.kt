package ch.epfl.sdp.cook4me.ui.login

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.button.LoadingButton
import ch.epfl.sdp.cook4me.ui.common.form.EmailField
import ch.epfl.sdp.cook4me.ui.common.form.PasswordField
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = LoginViewModel(),
    onSuccessfulLogin: () -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var inProgress by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    BasicToolbar(stringResource(R.string.sign_in_screen_top_bar_message))

    Scaffold(
        scaffoldState = scaffoldState,
        content = { padding ->
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmailField(email, { email = it }, Modifier.fieldModifier())
                PasswordField(password, { password = it }, Modifier.fieldModifier())
                LoadingButton(
                    stringResource(R.string.sign_in_screen_sign_in_button),
                    Modifier.fillMaxWidth().padding(16.dp, 8.dp),
                    inProgress
                ) {
                    scope.launch {
                        if (!viewModel.isEmailValid(email)) {
                            scaffoldState
                                .snackbarHostState
                                .showSnackbar(context.getString(R.string.invalid_email_message))
                        } else if (viewModel.isPasswordBlank(password)) {
                            scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.password_blank))
                        } else {
                            try {
                                inProgress = true
                                viewModel.onSignInClick(email, password)
                                onSuccessfulLogin()
                            } catch (e: FirebaseAuthInvalidUserException) {
                                scaffoldState
                                    .snackbarHostState
                                    .showSnackbar(context.getString(R.string.sign_in_screen_non_exist_user))
                                // println: for logging the exception
                                // otherwise detekt triggers SwallowedException message
                                // refer to: https://detekt.dev/docs/rules/exceptions/
                                // any suggestions apart from println()?
                                println(e)
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                scaffoldState
                                    .snackbarHostState
                                    .showSnackbar(context.getString(R.string.sign_in_screen_wrong_password))
                                inProgress = false
                                println(e)
                            }
                        }
                    }
                }
            }
        }
    )
}

private fun Modifier.fieldModifier(): Modifier =
    this.fillMaxWidth().padding(16.dp, 4.dp)

@Composable
private fun BasicToolbar(title: String) {
    TopAppBar(title = { Text(title) }, backgroundColor = toolbarColor())
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color =
    if (darkTheme) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant
