package com.test.androidtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.androidtest.databinding.ActivityTestMainBinding

/**
 * @Author : hss
 * @Date : 2024/5/6 - 10:59
 * @Description : 程序主页
 */
class TestMainActivity : AppCompatActivity() {

    private val mBinding: ActivityTestMainBinding by lazy { ActivityTestMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        mBinding.apply {
            btnTestUploadFile.setOnClickListener {

            }
        }
    }
}