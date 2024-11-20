package uz.itteacher.mycontact

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun saveImageToFile(context: Context, imageUri: String): String? {
    try {
        val inputStream = context.contentResolver.openInputStream(Uri.parse(imageUri))
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val file = File(context.filesDir, "contact_images")
        if (!file.exists()) {
            file.mkdir()  // Create directory if it doesn't exist
        }
        val imageFile = File(file, "${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return imageFile.absolutePath // Return the file path
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}
