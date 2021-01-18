package com.astro.sott.activities.forgotPassword.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.astro.sott.R
import com.astro.sott.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private var activityForgotPasswordBinding: ActivityForgotPasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityForgotPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
    }
}