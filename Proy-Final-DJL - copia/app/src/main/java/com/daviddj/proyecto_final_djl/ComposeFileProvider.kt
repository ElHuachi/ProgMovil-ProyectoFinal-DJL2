package com.daviddj.proyecto_final_djl

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class ComposeFileProvider : FileProvider(
    R.xml.file_paths
){
    companion object {
        fun getImageUri(context: Context): Uri {
            // 1
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            // 2
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory
            )
            // 3
            val authority = "com.daviddj.proyecto_final-djl.fileprovider"
            // 4
            return getUriForFile(
                context,
                authority,
                file,
            )
        }

        fun getVideoUri(context: Context): Uri {
            // 1
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            // 2
            val file = File.createTempFile(
                "selected_image_",
                ".mp4",
                directory
            )
            // 3
            val authority = "com.daviddj.proyecto_final-djl.fileprovider"
            // 4
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}