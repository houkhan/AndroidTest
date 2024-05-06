package com.test.l_upload_file

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import com.test.l_upload_file.databinding.ActivityUploadFileBinding
import java.io.IOException


/**
 * @Author : hss
 * @Date : 2024/5/6 - 11:29
 * @Description : 文件上传测试
 */
class UploadFileActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_UPLOAD_FILE = 10001
        private const val TAG = "UploadFileActivity"
        fun start(context: Context) {
            context.startActivity(Intent(context, UploadFileActivity::class.java))
        }
    }

    private val mBinding: ActivityUploadFileBinding by lazy { ActivityUploadFileBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setOnClick()
    }

    private fun setOnClick() {
        mBinding.apply {
            btnUploadFile.setOnClickListener {
                selectComplainFile()
            }
        }
    }

    /**
     * 选择文件
     */
    private fun selectComplainFile() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        try {
            startActivityForResult(
                Intent.createChooser(intent, "请选择文件"),
                REQUEST_CODE_UPLOAD_FILE
            )
        } catch (e: ActivityNotFoundException) {
            ToastUtils.showShort("请安装文件管理器")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPLOAD_FILE) {
            selectComplainFileComplete(resultCode, data)
        }
    }

    private fun selectComplainFileComplete(resultCode: Int, data: Intent?) {
        try {
            if (resultCode != -1) {
                return
            }
            val selectFileUri = data?.data
            if (selectFileUri != null) {
                printFileInfo(selectFileUri)
            }
        } catch (e: Exception) {
            ToastUtils.showShort("该文件无法上传")
            LogUtils.eTag(TAG, "selectComplainFileComplete", e)
        }
    }

    private fun printFileInfo(selectFileUri: Uri) {
        mBinding.tvUploadFileInfo.append("selectFileUri == $selectFileUri\n")
        try {
            val uri2File = UriUtils.uri2File(selectFileUri)
            mBinding.tvUploadFileInfo.append("uri2File == ${FileUtils.getFileMD5ToString(uri2File)}\n")
        } catch (e: IOException) {
            LogUtils.eTag(TAG, "printFileInfo", e)
        }
    }
}