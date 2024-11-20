package uz.itteacher.mycontact.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.itteacher.mycontact.model.CallHistory

@Dao
interface CallHistoryDAO {

    @Query("SELECT * FROM CallHistory")
    fun getAll(): List<CallHistory>

    @Insert
    fun insertCallHistory(callHistory: CallHistory)

    @Delete
    fun deleteCallHistory(callHistory: CallHistory)

}