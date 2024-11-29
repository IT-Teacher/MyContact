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
    fun getAll(): MutableList<ContactUser>

    @Query("SELECT * FROM ContactUser WHERE id = :id")
    fun getById(id: Int): ContactUser


    @Query("SELECT * FROM ContactUser WHERE user_name LIKE :name")
    fun getByName(name: String): List<ContactUser>

    @Insert
    fun insertContactUser(contactUser: ContactUser)

    @Delete
    fun deleteContactUser(contactUser: ContactUser)

    @Query("UPDATE ContactUser SET user_name = :user_name, phone = :phone, image_id_res = :image WHERE id = :id")
    fun updateContactUser(id: Int, user_name: String, phone: String, image: String? = null)

}