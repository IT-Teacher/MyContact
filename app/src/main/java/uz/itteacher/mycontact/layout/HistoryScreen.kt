package uz.itteacher.mycontact.layout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uz.itteacher.mycontact.database.AppDataBase
import uz.itteacher.mycontact.model.CALLTYPE
import uz.itteacher.mycontact.model.CallHistory


@Composable
fun HistoryScreen(navController: NavHostController, id: String) {
    val context = LocalContext.current
    val appDataBase = AppDataBase.getIntance(context)
    val callHistoryList by appDataBase.callHistoryDAO().getCallHistoryById(id.toInt()).collectAsState(emptyList())

    Column {
        Text(
            text = "Call History",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(16.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(callHistoryList) { callHistory ->
                HistoryItem(callHistory)
            }
        }
    }

    Log.d("TAGATAG", "HistoryScreen: $callHistoryList")
}


@Composable
fun HistoryItem(callHistory: CallHistory) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Type: ${callHistory.callType}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Date: ${callHistory.callDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Default.Phone, // Replace with an appropriate call icon
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }

    Log.d("TAG", "HistoryItem: ${CALLTYPE.INCOMING.name}")
}



