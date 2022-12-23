package com.cnblogs.app.basic

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

/**
 * Cnblogs BaseActivity
 * @author RAE
 * @date 2022/10/17
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val context: Context
        get() = this

}