package ch.epfl.sdp.cook4me.ui.challenge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.common.button.CreateNewItemButton

@Composable
fun ChallengeFeedScreen(
    onCreateNewChallengeClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(LocalContext.current.getString(R.string.Challenge_Feed_TAG)),
    ) {
        CreateNewItemButton(itemType = "Challenge", onClick = onCreateNewChallengeClick)
    }
}
