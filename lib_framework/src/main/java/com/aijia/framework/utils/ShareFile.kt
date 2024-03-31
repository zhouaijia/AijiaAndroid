package com.aijia.framework.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException

class ShareFile @JvmOverloads constructor(
    val activity: Activity,
    val filePath: String,
    val type: String = "SYSTEM",
    val title: String? = "发送到"){

    private var fileUri: Uri = getFileUrl(activity, filePath)

    fun shareBySystem(){
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addCategory("android.intent.category.DEFAULT")
        if (type == "QQ") {
            val comp = ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")
            intent.component = comp
        } else if (type == "WX") {
            val comp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")
            intent.component = comp
        }
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_STREAM, fileUri)
        activity.startActivity(Intent.createChooser(intent, title))
    }


    companion object{
        private val TAG = "ShareFile"

        fun getFileUrl(context: Context, path: String): Uri{
            val file = File(path)
            if(!file.isFile)throw FileNotFoundException("Share File not Exists!")
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                val authority = context.packageName + ".provider"
                return FileProvider.getUriForFile(context, authority, file)
            }
            return Uri.fromFile(file)
        }
    }
}
