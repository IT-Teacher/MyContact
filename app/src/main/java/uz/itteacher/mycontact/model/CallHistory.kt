package uz.itteacher.mycontact.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CallHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "user_contact_id")
    val userContactId: Int,
    @ColumnInfo(name = "call_type")
    val callType: CALLTYPE,
    @ColumnInfo(name = "call_date")
    val callDate: String
)
