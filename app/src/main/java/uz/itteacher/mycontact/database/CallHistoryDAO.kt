package uz.itteacher.mycontact.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.itteacher.mycontact.model.CallHistory

@Dao
interface CallHistoryDAO {

    @Query("SELECT * FROM CallHistory where user_contact_id = :id")
    fun getCallHistoryById(id: Int): Flow<List<CallHistory>>

    @Query("SELECT * FROM CallHistory GROUP BY user_contact_id ORDER BY id DESC LIMIT 5")
    fun getRecentCalls(): List<CallHistory>

    @Insert
    fun insertCallHistory(callHistory: CallHistory)

    @Delete
    fun deleteCallHistory(callHistory: CallHistory)

}