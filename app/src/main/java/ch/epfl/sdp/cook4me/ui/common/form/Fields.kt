package ch.epfl.sdp.cook4me.ui.common.form

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.epfl.sdp.cook4me.R

/**
 * A simple component that displays a question and a text field
 * @param question the question to be displayed
 * @param label the label of the text field
 * @param onTextChanged the function to be called when the text field is changed
 */
@Composable
fun InputField(
    value: String,
    @StringRes question: Int,
    modifier: Modifier = Modifier,
    @StringRes label: Int? = null,
    isError: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(question), style = MaterialTheme.typography.h6.copy(fontSize = 18.sp))
        OutlinedTextField(
            value = value,
            onValueChange =
            onValueChange,
            isError = isError,
            label = { label?.let { Text(stringResource(id = it)) } },
            modifier = modifier.fillMaxWidth().semantics { if (isError) stateDescription = "Error" }
        )
    }
}

@Composable
fun EmailField(value: String, isError: Boolean, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier
            .testTag(stringResource(R.string.TAG_EMAIL_FIELD)).semantics { if (isError) stateDescription = "Error" },
        value = value,
        onValueChange = { onNewValue(it) },
        isError = isError,
        placeholder = { Text("Email") },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
    )
}

@Composable
fun UserField(value: String, isError: Boolean, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier
            .testTag(stringResource(R.string.TAG_USER_FIELD)).semantics { if (isError) stateDescription = "Error" },
        value = value,
        onValueChange = { onNewValue(it) },
        isError = isError,
        placeholder = { Text("Username") },
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Username") }
    )
}

@Composable
fun ProfileInfosField(
    icon: ImageVector,
    preview: String,
    value: String,
    isError: Boolean,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier
            .testTag(preview).semantics { if (isError) stateDescription = "Error" },
        value = value,
        onValueChange = { onNewValue(it) },
        isError = false,
        placeholder = { Text(preview) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = "Username") }
    )
}

@Composable
fun BiosField(
    icon: ImageVector,
    preview: String,
    value: String,
    isError: Boolean,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = false,
        modifier = modifier
            .fillMaxHeight()
            .testTag(preview).semantics { if (isError) stateDescription = "Error" },
        value = value,
        onValueChange = { onNewValue(it) },
        isError = false,
        placeholder = { Text(preview) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = "Username") }
    )
}

@Composable
fun PasswordField(value: String, isError: Boolean, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier
            .testTag(stringResource(R.string.TAG_PASSWORD_FIELD)).semantics { if (isError) stateDescription = "Error" },
        value = value,
        onValueChange = { onNewValue(it) },
        isError = isError,
        placeholder = { Text(text = "Password") },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun FieldTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.h6.copy(fontSize = 18.sp)
    )
}