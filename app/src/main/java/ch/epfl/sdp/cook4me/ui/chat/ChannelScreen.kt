package ch.epfl.sdp.cook4me.ui.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ch.epfl.sdp.cook4me.application.AccountService
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme


@Composable
fun ChannelScreen(
    client: ChatClient = provideChatClient(
        apiKey = "w9pumuqjxk3m",
        context = LocalContext.current
    ),
    accountService: AccountService = AccountService(),
    onBackListener: () -> Unit = {},
) {
    // using the current user email as the user id for the stream chat
    // log in with the user.
    // e.g. email is darth.vadar@epfl.ch then id is darth-vadar
    val userEmail = accountService.getCurrentUserEmail()
    val fullName = remember { mutableStateOf("") }
    val user = remember {
        mutableStateOf(User(id = fullName.value))
    }
    userEmail?.let { email ->
        val nameParts = email.split("@")[0].split(".")
        val firstName = nameParts[0].trim()
        val secondName = nameParts[1].trim()
        fullName.value = "$firstName$secondName"
        user.value = User(id = fullName.value)
        val token = client.devToken(user.value.id)
        client.connectUser(user.value, token).enqueue { result ->
            if (result.isSuccess) {
                // Connected
            } else {
                // Handle result.error()
                println(result.error().message)
            }
        }
    }
    val selectedChannelId = remember { mutableStateOf("") }

    ChatTheme {
        ChannelsScreen(
            title = "Channel List of ${fullName.value}",
            isShowingSearch = true,
            onItemClick = { channel ->
                selectedChannelId.value = channel.cid
            },
            onBackPressed = { onBackListener() },
            onHeaderAvatarClick = {
                client.disconnect(true).enqueue()
            },
            onHeaderActionClick = {
                // just creating a channel of 2 ppl, the current user and the sdp2023cook4me user (already manually registered)
                client.createChannel(
                    channelType = "messaging",
                    channelId = "",
                    memberIds = listOf(fullName.value, "danielbucher"),
                    extraData = emptyMap()
                ).enqueue { result ->
                    if (result.isSuccess) {
                        val channel = result.data()
                        selectedChannelId.value = channel.cid
                    } else {
                        // Handle result.error()
                        println("!!!!!!!!!!!!!!!!!!!!!!!!!")
                        println(result.error().message)
                    }
                }
            },
        )
        if (selectedChannelId.value.isNotEmpty()) {
            MessageScreen(channelId = selectedChannelId.value, onBackListener = { selectedChannelId.value = "" })
        }
    }
}