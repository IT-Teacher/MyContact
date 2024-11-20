package uz.itteacher.mycontact.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.itteacher.mycontact.model.CallHistory
import uz.itteacher.mycontact.model.ContactUser

@Database(entities = [ContactUser::class, CallHistory::class], version = 1)

abstract class AppDataBase(): RoomDatabase() {

    abstract fun contactUserDAO(): ContactUserDAO

    abstract fun callHistoryDAO(): CallHistoryDAO

    companion object{
        const val DB_NAME = "contact_db"

        private var instance: AppDataBase? = null

        fun getIntance(context: Context): AppDataBase{
            if (instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DB_NAME
                ).allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}