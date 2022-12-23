package com.cnblogs.app.basic

import androidx.viewbinding.ViewBinding

/**
 * ViewBinding Activity
 * @author RAE
 * @date 2022/10/18
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class BaseViewBindingActivity<T : ViewBinding> : BaseActivity() {

    protected lateinit var binding: T

    fun setContentView(binding: T) {
        this.binding = binding
        setContentView(binding.root)
    }
}