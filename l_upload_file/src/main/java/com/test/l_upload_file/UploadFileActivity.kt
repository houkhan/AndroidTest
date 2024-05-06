package com.test.l_upload_file

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils
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
            btnUploadFileOld.setOnClickListener {
                selectComplainFileOld()
            }
            btnUploadFileNew.setOnClickListener {
                selectComplainFileNew()
            }
        }
    }

    /**
     *  上传文件 旧版API startActivityForResult
     */
    private fun selectComplainFileOld() {
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

    /**
     *  上传文件 旧版API startActivityForResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPLOAD_FILE) {
            selectComplainFileComplete(resultCode, data)
        }
    }

    /**
     *  上传文件 新版API registerForActivityResult
     */
    private var selectComplainFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            selectComplainFileComplete(resultCode, data)
        }
    }

    /**
     *  上传文件 新版API registerForActivityResult
     */
    private fun selectComplainFileNew() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        try {
            selectComplainFileLauncher.launch(Intent.createChooser(intent, "请选择文件"))
        } catch (e: ActivityNotFoundException) {
            ToastUtils.showShort("请安装文件管理器")
        }
    }

    /**
     * 选择文件完成
     */
    private fun selectComplainFileComplete(resultCode: Int, data: Intent?) {
        try {
            if (resultCode != -1) {
                return
            }
            val selectFileUri = data?.data
            if (selectFileUri != null) {
                showFileInfo(selectFileUri)
            }
        } catch (e: Exception) {
            ToastUtils.showShort("该文件无法上传")
            LogUtils.eTag(TAG, "selectComplainFileComplete", e)
        }
    }

    /**
     * 此处展示 文件信息
     * 采用 blankj utilcode
     * UriUtils.uri2File 将 uri 转换为 file
     * UriUtils.uri2FileNoCacheCopy 测试 会出现 null 推荐使用 UriUtils.uri2File
     */
    private fun showFileInfo(selectFileUri: Uri) {
        mBinding.tvUploadFileInfo.append("selectFileUri == $selectFileUri\n")
        try {
            val uri2File = UriUtils.uri2File(selectFileUri)
            mBinding.tvUploadFileInfo.append("uri2File == ${FileUtils.getFileMD5ToString(uri2File)}\n")
            if (ImageUtils.isImage(uri2File)) {
                mBinding.ivUploadFileInfo.load(uri2File)
            }
        } catch (e: IOException) {
            LogUtils.eTag(TAG, "showFileInfo", e)
        }
    }
}