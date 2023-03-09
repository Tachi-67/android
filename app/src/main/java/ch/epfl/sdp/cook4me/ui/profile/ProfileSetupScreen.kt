package ch.epfl.sdp.cook4me.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.theme.Cook4meTheme
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

@Preview(showBackground = true)
@Composable
fun ProfileSetupScreen() {
    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    var username by rememberSaveable { mutableStateOf("User 42") }
    var favDish by rememberSaveable { mutableStateOf("Pizza") }
    var allergies by rememberSaveable { mutableStateOf("No Allergies") }
    var bio by rememberSaveable { mutableStateOf("Hi there I'm a chef") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Cancel", modifier = Modifier.clickable {
                notification.value = "Cancelled"
            })
            Text(text = "Save", modifier = Modifier.clickable {
                notification.value = "Saved"
            })
        }
        ProfileSetupImage()

        //Textfield for the username
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Username", modifier = Modifier.width(100.dp))
            TextField(
                value = username,
                onValueChange = { username = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent, textColor = Color.Black
                )
            )
        }
        //Textfield for the Favorite dish
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Favorite dish", modifier = Modifier.width(100.dp))
            TextField(
                value = favDish,
                onValueChange = { favDish = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent, textColor = Color.Black
                )
            )
        }

        //Textfield for the Allergies
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Allergies", modifier = Modifier.width(100.dp))
            TextField(
                value = allergies,
                onValueChange = { allergies = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent, textColor = Color.Black
                )
            )
        }

        //Textfield for the bio
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Bio", modifier = Modifier
                    .width(100.dp)
                    .padding(top = 8.dp)
            )
            TextField(
                value = bio,
                onValueChange = { bio = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent, textColor = Color.Black
                ),
                singleLine = false,
                modifier = Modifier.height(150.dp)
            )
        }
    }
}

@Composable
fun ProfileSetupImage() {
    //draws the image of the profile
    val imageURI = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        if (imageURI.value.isEmpty()) R.drawable.ic_user
        else imageURI.value
    )

    /**
     *Remembers and launches on recomposition
     *takes a contract and a on result function
     *contract = the action we want to take & Input/Output of the action
     *onResult = lambda that receives the result
     * launches an activity to get the image
     * the url received we places in imageURI.value
     * the painter will then get updated with the new value
     */
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageURI.value = it.toString() }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") }, //starts the launcher and accept all type of images
                contentScale = ContentScale.Crop //crops the image into the available space
            )
        }
        Text(text = "Change profile picture")
    }
}


@Preview
@Composable
fun DefaultPreview() {
    Cook4meTheme {
        ProfileScreen()
    }
}

