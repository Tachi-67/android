package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.epfl.sdp.cook4me.ComposeFileProvider
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme

@Composable
fun TupCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: TupCreationViewModel = viewModel(),
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
                .weight(1f),
            viewModel = viewModel
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            ButtonRow(
                modifier = Modifier.fillMaxSize(),
                onCancelPressed = {},
                onDonePressed={viewModel.onSubmit()},
            )
        }

    }

}

@Composable
fun TupperwareForm(
    modifier: Modifier = Modifier,
    viewModel: TupCreationViewModel,
) {
    // for now couldn't get rid of it as it is used to get the Uri from the camera
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val titleText by viewModel.titleText
    val descText by viewModel.descText


    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if(uri != null) {
                viewModel.addImage(uri)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            imageUri?.let {
                if(success) {
                    viewModel.addImage(imageUri!!)
                }
            }
        }
    )

    val context = LocalContext.current

    Box(
        modifier =  modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
        ) {
            FieldText("Add Images")

            Spacer(modifier = Modifier.size(10.dp))

            ImageSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onClickAddImage = {
                    imagePicker.launch("image/*")
                },
                onClickTakePhoto = {
                    val uri = ComposeFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                },
                onClickImage = { /*TODO*/ },
                images = viewModel.images
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
                value = titleText, onValueChange = { viewModel.updateTitle(it) },
                shape = RoundedCornerShape(30.dp),
                colors = textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
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
                value = descText, onValueChange = { viewModel.updateDesc(it) },
                shape = RoundedCornerShape(30.dp),
                colors = textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
            )
            Spacer(modifier = Modifier.size(10.dp))
            Cook4MeDivider()
            Spacer(modifier = Modifier.size(2.dp))
            FieldText("Tags")
            Spacer(modifier = Modifier.size(5.dp))
            TextField(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.caption,
                value = viewModel.tags.joinToString(), onValueChange = { viewModel.updateTags(it) },
                shape = RoundedCornerShape(30.dp),
                colors = textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
            )
        }

    }
}

@Composable
private fun FieldText(text: String = "") {
    Text(
        modifier = Modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h6
    )
}


@Preview("default", showBackground = true)
@Composable
fun TupCreationScreenPreview() {
    Cook4meTheme {
        TupCreationScreen()
    }
}