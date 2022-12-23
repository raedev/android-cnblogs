package com.cnblogs.app.home

import android.os.Bundle
import android.os.CountDownTimer
import com.cnblogs.app.basic.BaseViewBindingActivity
import com.cnblogs.app.home.databinding.ActivityLauncherBinding
import com.magical.ui.UiCompat
import com.magical.ui.utils.StatusBarUtils
import java.util.*

/**
 * 启动页
 * @author RAE
 * @date 2022/10/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class LauncherActivity : BaseViewBindingActivity<ActivityLauncherBinding>() {

    /** 倒计时 */
    private val countDownTimer = object : CountDownTimer(3000, 1000) {

        override fun onTick(millisUntilFinished: Long) = Unit

        override fun onFinish() = finish()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setStatusBarLightMode(this)
        // 非栈顶时候退出
        if (!isTaskRoot) return finish()
        setContentView(ActivityLauncherBinding.inflate(layoutInflater))
        binding.tvCopyright.let {
            val text = it.text.toString()
            it.text = text.replace("@year", "©2017-${Calendar.getInstance().get(Calendar.YEAR)}")
        }

        UiCompat.displayImage(binding.imgCover, "http://cdn.mayizhuanlan.com/photo/2022cunjiejpg")
    }

    override fun onResume() {
        super.onResume()
        countDownTimer.start()
    }

    override fun onPause() {
        super.onPause()
        countDownTimer.cancel()
    }


}