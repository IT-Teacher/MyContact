package uz.itteacher.mycontact.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import uz.itteacher.mycontact.model.ContactUser

@Dao
interface ContactUserDAO {
    @Query("SELECT * FROM ContactUser ORDER BY user_name ASC")
    fun getAll(): Flow<List<ContactUser>>

    @Query("SELECT * FROM ContactUser WHERE id = :id")
    fun getById(id: Int): ContactUser


    @Query("SELECT * FROM ContactUser WHERE user_name LIKE :name")
    fun getByName(name: String): List<ContactUser>

    @Insert
    fun insertContactUser(contactUser: ContactUser)

    @Delete
    fun deleteContactUser(contactUser: ContactUser)

    @Update
    fun updateContactUser(contactUser: ContactUser)


}