package com.astro.sott.activities.signUp.ui

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.astro.sott.R
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel
import com.astro.sott.databinding.ActivitySinUpBinding
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse
import java.lang.Double.parseDouble

class SignUpActivity : AppCompatActivity() {

    private var astroLoginViewModel: AstroLoginViewModel? = null

    private var activitySinUpBinding: ActivitySinUpBinding? = null
    private var passwordVisibility = true
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activitySinUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sin_up)
        modelCall()
        setClicks()
    }

    private fun modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel::class.java)
    }

    fun setClicks() {
        activitySinUpBinding?.nextBtn?.setOnClickListener {
            val mobilePattern = Regex("^[0-9]*$")
            val emailPattern = Regex("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
            var email_mobile = activitySinUpBinding?.mobileEmailEdt?.text.toString()
            if (!email_mobile.equals("", true)) {
                var password = activitySinUpBinding?.passwordEdt?.text.toString()
                if (mobilePattern.containsMatchIn(email_mobile)) {
                    if (email_mobile?.length == 10) {
                        createUser("mobile", email_mobile, password)
                    } else {
                        Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show()

                    }
                } else if (emailPattern.containsMatchIn(email_mobile)) {
                    createUser("email", email_mobile, password)
                    Toast.makeText(this, "Email", Toast.LENGTH_SHORT).show()
                } else {

                    var numeric = true
                    try {
                        val num = parseDouble(email_mobile?.first().toString())
                    } catch (e: NumberFormatException) {
                        numeric = false
                    }
                    if (numeric){
                        Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()

                    }
                    }

            }
        }

        activitySinUpBinding?.eyeIcon?.setOnClickListener(View.OnClickListener {
            if (passwordVisibility) {
                activitySinUpBinding?.eyeIcon?.setBackgroundResource(R.drawable.ic_outline_visibility_off_light)
                activitySinUpBinding?.passwordEdt?.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                passwordVisibility = false
                activitySinUpBinding?.passwordEdt?.setSelection(activitySinUpBinding?.passwordEdt?.text?.length!!)
            } else {
                passwordVisibility = true
                activitySinUpBinding?.passwordEdt?.setInputType(InputType.TYPE_CLASS_TEXT)
                activitySinUpBinding?.eyeIcon?.setBackgroundResource(R.drawable.ic_outline_visibility_light)
                activitySinUpBinding?.passwordEdt?.setSelection(activitySinUpBinding?.passwordEdt?.text?.length!!)

            }
        })

    }

    private fun createUser(type: String, email_mobile: String, password: String) {
        astroLoginViewModel!!.createUser(type, email_mobile, password).observe(this, Observer<EvergentCommonResponse> { evergentCommonResponse: EvergentCommonResponse ->
            if (evergentCommonResponse.isStatus) {
                Toast.makeText(this, evergentCommonResponse.createUserResponse.createUserResponseMessage?.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, evergentCommonResponse.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}