package com.astro.sott.activities.forgotPassword.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.astro.sott.R
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel
import com.astro.sott.activities.verification.VerificationActivity
import com.astro.sott.callBacks.TextWatcherCallBack
import com.astro.sott.databinding.ActivityForgotPasswordBinding
import com.astro.sott.utils.helpers.AppLevelConstants
import com.astro.sott.utils.helpers.CustomTextWatcher

class ForgotPasswordActivity : AppCompatActivity() {
    private var activityForgotPasswordBinding: ActivityForgotPasswordBinding? = null
    private var astroLoginViewModel: AstroLoginViewModel? = null
    private var email_mobile: String? = null
    private var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityForgotPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        modelCall()
        setCLicks()
    }

    private fun setCLicks() {
        activityForgotPasswordBinding?.submit?.setOnClickListener {
            if (checkEmailVaildation()) {
                searchAccountv2()
            }
            activityForgotPasswordBinding?.backIcon?.setOnClickListener({
                onBackPressed()
            })
        }

        activityForgotPasswordBinding?.emailMobileEdt?.addTextChangedListener(CustomTextWatcher(this, object : TextWatcherCallBack {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                activityForgotPasswordBinding?.errorEmail?.visibility = View.GONE
            }

            override fun afterTextChanged(editable: Editable) {}
        }))
    }

    private fun modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel::class.java)
    }

    private fun searchAccountv2() {
        activityForgotPasswordBinding?.progressBar?.visibility = View.VISIBLE
        astroLoginViewModel?.searchAccountV2(type, email_mobile)?.observe(this, Observer { evergentCommonResponse ->
            if (evergentCommonResponse.isStatus) {
                createOtp()
            } else {
                activityForgotPasswordBinding?.progressBar?.visibility = View.GONE
                Toast.makeText(this, evergentCommonResponse.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createOtp() {
        astroLoginViewModel!!.createOtp(type, email_mobile).observe(this, Observer { evergentCommonResponse ->
            activityForgotPasswordBinding?.progressBar?.visibility = View.GONE
            if (evergentCommonResponse.isStatus) {
                Toast.makeText(this, "Verification code had be sent to $email_mobile", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VerificationActivity::class.java)
                intent.putExtra(AppLevelConstants.TYPE_KEY, type)
                intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, email_mobile)
                intent.putExtra(AppLevelConstants.PASSWORD_KEY, "password")
                intent.putExtra(AppLevelConstants.FROM_KEY, "signIn")
                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            } else {
                Toast.makeText(this, evergentCommonResponse.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkEmailVaildation(): Boolean {

        val mobilePattern = Regex("^[0-9]*$")
        val emailPattern = Regex("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        email_mobile = activityForgotPasswordBinding?.emailMobileEdt?.text.toString()
        return if (!email_mobile.equals("", ignoreCase = true)) {
            if (mobilePattern.containsMatchIn(email_mobile!!)) {
                if (email_mobile!!.length == 10 || email_mobile!!.length == 11) {
                    type = "mobile"
                    true
                } else {
                    activityForgotPasswordBinding?.errorEmail?.visibility = View.VISIBLE
                    activityForgotPasswordBinding?.errorEmail?.text = resources.getString(R.string.email_mobile_error)
                    false
                }
            } else if (emailPattern.containsMatchIn(email_mobile!!)) {
                type = "email"
                true
            } else {
                activityForgotPasswordBinding?.errorEmail?.visibility = View.VISIBLE
                activityForgotPasswordBinding?.errorEmail?.text = resources.getString(R.string.email_mobile_error)
                false
            }
        } else {
            activityForgotPasswordBinding?.errorEmail?.visibility = View.VISIBLE
            activityForgotPasswordBinding?.errorEmail?.text = resources.getString(R.string.email_mobile_error)
            false
        }
    }
}