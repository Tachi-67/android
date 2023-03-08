package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

@Composable
fun TupCreationScreen(
    modifier: Modifier = Modifier,
    onClickAddImage: () -> Unit,
    onClickTakePhoto: () -> Unit,
    tupCreationViewModel: TupCreationViewModel = viewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize(),

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = MaterialTheme.colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text="Header", style = MaterialTheme.typography.h5, color = Color.White)
        }
        TupperwareForm(
            modifier = Modifier
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            ButtonRow(
                modifier = Modifier.fillMaxSize(),
                onCancelPressed = { /*TODO*/ },
                onDonePressed = {},
            )
        }

    }

}

@Composable
fun TupperwareForm(
    modifier: Modifier = Modifier,
) {
    var nameTextField by remember {
        mutableStateOf("")
    }

    var descTextField by remember {
        mutableStateOf("")
    }

    Box(
        modifier =  modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            FieldText("Add Images")

            Spacer(modifier = Modifier.size(10.dp))

            ImageSelector(
                modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onClickAddImage = { /*TODO*/ },
                onClickTakePhoto = { /*TODO*/ },
                onClickImage = { /*TODO*/ }
            )
            Spacer(modifier = Modifier.size(10.dp))
            Cook4MeDivider()
            Spacer(modifier = Modifier.size(5.dp))
            FieldText("Tupperware Name")
            Spacer(modifier = Modifier.size(5.dp))
            TextField(
                modifier = Modifier
                    .height(50.dp)

                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.caption,
                value = nameTextField, onValueChange = { nameTextField = it },
                shape = RoundedCornerShape(30.dp),
                colors = textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
            )
            Spacer(modifier = Modifier.size(10.dp))
            Cook4MeDivider()
            Spacer(modifier = Modifier.size(2.dp))
            FieldText("Description")
            Spacer(modifier = Modifier.size(5.dp))
            TextField(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.caption,
                value = descTextField, onValueChange = { descTextField = it },
                shape = RoundedCornerShape(30.dp),
                colors = textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
            )
            FieldText("Tags")
            Spacer(modifier = Modifier)
        }

    }
}

@Composable
private fun FieldText(text: String = "") {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h6
    )
}


@Preview("default", showBackground = true)
@Composable
fun TupCreationScreenPreview() {
    Cook4meTheme {
        TupCreationScreen(onClickTakePhoto = {}, onClickAddImage = {})
    }
}