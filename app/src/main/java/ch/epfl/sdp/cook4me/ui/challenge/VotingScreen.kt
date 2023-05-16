package ch.epfl.sdp.cook4me.ui.challenge

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.challengeform.Challenge

const val MINSTAR = 1
const val MAXSTAR = 5

@Composable
fun VotingScreen(challenge: Challenge, onVoteChanged: (Challenge) -> Unit) {
    val voteResults = remember { mutableStateMapOf<String, Int>() }

    Column {
        BasicToolbar(stringResource(R.string.voteScreenTitle))

        LazyColumn {
            items(challenge.participants.toList()) { (participant) ->
                ParticipantRow(participant, score = 0) { newScore ->
                    voteResults[participant] = newScore
                }
            }
        }

        Button(
            onClick = {
                val updatedChallenge =
                    challenge.copy(participants = voteResults.mapValues { it.value })
                onVoteChanged(updatedChallenge)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
        ) {
            Text("Vote")
        }
    }
}

@Composable
fun ParticipantRow(participant: String, score: Int, onScoreChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f) // fill the remaining space
                .wrapContentWidth(Alignment.Start)
        ) {
            Text(
                text = participant,
                style = MaterialTheme.typography.h6
            )
        }

        Box(
            modifier = Modifier.wrapContentWidth(Alignment.End)
        ) {
            RatingBar(
                value = score,
                participant = participant
            ) { newScore ->
                onScoreChange(newScore)
            }
        }
    }
}

@Composable
fun RatingBar(participant: String, value: Int, onValueChange: (Int) -> Unit) {
    var selectedValue by remember { mutableStateOf(value) }
    Box {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 1.dp)
        ) {
            for (i in MINSTAR..MAXSTAR) {
                IconButton(
                    onClick = {
                        if (selectedValue == i) {
                            selectedValue = 0
                        } else {
                            selectedValue = i
                        }
                        onValueChange(selectedValue)
                        }
                ) {
                    Icon(
                        painter = painterResource(
                            id =
                            if (i <= selectedValue) {
                                R.drawable.ic_star_filled
                            } else {
                                R.drawable.ic_star_empty
                            }
                        ),
                        contentDescription =
                        if (i <= selectedValue) {
                            "$participant Star $i"
                        } else {
                            "$participant Empty star $i"
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BasicToolbar(title: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = toolbarColor()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color =
    if (darkTheme) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant
