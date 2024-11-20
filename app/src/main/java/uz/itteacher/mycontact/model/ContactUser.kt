package uz.itteacher.mycontact.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "user_name")
    val userName: String,
    val phone: String,
    val email: String,
    @ColumnInfo(name = "image_id_res")
    val imageIdRes: String? = null
)
