package ch.epfl.sdp.cook4me.ui.eventform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.application.AccountService
import ch.epfl.sdp.cook4me.application.EventFormService
import kotlinx.coroutines.runBlocking

/**
 * A component screen that displays details of an event
 * @param event the event to be displayed
 */
@Composable
fun DetailedEventScreen(
    eventService: EventFormService = EventFormService(),
    accountService: AccountService = AccountService()
) {
    /*
    * 2023/04/06 14:48
    * Working on displaying actual event from firestore.
    * For now, I use the event id as the email of the current user.(See CreateEventScreen.kt)
    * This should be changed as discussed in CreateEventScreen.kt
    * Ask Daniel!!!
    *
    * TODO:
    *  - In an event, store the creator id
    *  - For a user, query all events the users owns
    *  - Resolve errors in this screen
    *  - Test CreateEventScreen, because I throw NullPointerException when I try to get the current user email.
    *  - Test DetailedEventScreen, because I throw NullPointerException when I try to get the current user email.
    *  - There is a bug in CreateEventScreen, in the input field of event description, maybe debug it.
    *  - In EventFormService, I think we should change the return type of getById() to Event? instead of Event, or see what happens if
    *    no event is found. (ask dayan or daniel perhaps?)
    *  - Test EventFormService for the new method getById()
    * */
    var event: Event?
    runBlocking {
        try{
            val userEmail = accountService.getCurrentUserEmail()
            if(userEmail != null)
                event = eventService.getById(userEmail)
        } catch (e: NullPointerException){
            throw e
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(MaterialTheme.colors.background)
    ) {
        SectionWithTitle(title = stringResource(R.string.event_name), content = event.name)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(title = stringResource(R.string.event_description), content = event.description)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(title = stringResource(R.string.event_location), content = event.location)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(
            title = stringResource(R.string.event_max_participants),
            content = event.maxParticipants.toString()
        )
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(title = stringResource(R.string.event_creator), content = event.creator)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(
            title = stringResource(R.string.event_participants),
            content = event.participants.joinToString(separator = ", ")
        )
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        val accessibility = if (event.isPrivate) "Only Subscribers" else "Everyone"
        SectionWithTitle(title = stringResource(R.string.event_who_can_see_event), content = accessibility)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)

        SectionWithTitle(title = stringResource(R.string.event_time), content = event.eventDate)
        Divider(color = MaterialTheme.colors.secondary, thickness = 1.dp)
    }
}

@Composable
fun SectionWithTitle(title: String, content: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primary
        )

        Text(
            text = content,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
    }
}
