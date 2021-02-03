package com.astro.sott.activities.signUp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
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
import com.astro.sott.databinding.ActivitySinUpBinding
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse
import com.astro.sott.utils.helpers.CustomTextWatcher
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
        setWatcher()
    }

    private fun setWatcher() {
        activitySinUpBinding?.mobileEmailEdt?.addTextChangedListener(CustomTextWatcher(this, object : TextWatcherCallBack {
            override fun afterTextChanged(editable: Editable?) {
            }

            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                activitySinUpBinding?.errorEmail?.visibility = View.GONE
                activitySinUpBinding?.errorEmail?.text = ""
            }

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
            }

        }))

        activitySinUpBinding?.passwordEdt?.addTextChangedListener(CustomTextWatcher(this, object : TextWatcherCallBack {
            override fun afterTextChanged(editable: Editable?) {
            }

            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                activitySinUpBinding?.errorPasssword?.visibility = View.GONE
                activitySinUpBinding?.errorPasssword?.text = ""
            }

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
            }

        }))
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
                    if (email_mobile?.length == 10 || email_mobile?.length == 11) {
                        checkPassword("mobile", email_mobile, password)
                    } else {
                        activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
                        activitySinUpBinding?.errorEmail?.text = getString(R.string.mobile_error)
                    }
                } else if (emailPattern.containsMatchIn(email_mobile)) {
                    checkPassword("email", email_mobile, password)
                } else {

                    var numeric = true
                    try {
                        val num = parseDouble(email_mobile?.first().toString())
                    } catch (e: NumberFormatException) {
                        numeric = false
                    }
                    if (numeric) {
                        activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
                        activitySinUpBinding?.errorEmail?.text = getString(R.string.mobile_error)

                    } else {
                        activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
                        activitySinUpBinding?.errorEmail?.text = getString(R.string.email_error)
                    }
                }

            } else {
                activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
                activitySinUpBinding?.errorEmail?.text = getString(R.string.email_mobile_error)

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

    private fun checkPassword(type: String, emailMobile: String, password: String) {
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,16}$")
        var password = activitySinUpBinding?.passwordEdt?.text.toString();
        if (!password.equals("", true)) {
            if (passwordPattern.containsMatchIn(password)) {

                searchAccountv2(type, emailMobile, password)
                //createUser(type, emailMobile, password)
            } else {
                activitySinUpBinding?.errorPasssword?.visibility = View.VISIBLE
                activitySinUpBinding?.errorPasssword?.text = getString(R.string.password_error)

            }
        } else {
            activitySinUpBinding?.errorPasssword?.visibility = View.VISIBLE
            activitySinUpBinding?.errorPasssword?.text = getString(R.string.password_error)

        }
    }

    private fun searchAccountv2(type: String, emailMobile: String, password: String) {
        activitySinUpBinding?.progressBar?.visibility = View.VISIBLE

        astroLoginViewModel!!.searchAccountV2(type, emailMobile).observe(this, Observer { evergentCommonResponse: EvergentCommonResponse ->
            if (evergentCommonResponse.isStatus) {
                activitySinUpBinding?.progressBar?.visibility = View.GONE
                // Toast.makeText(this, evergentCommonResponse.searchAccountv2Response.searchAccountV2ResponseMessage!!.message, Toast.LENGTH_SHORT).show()
            } else {
                if (evergentCommonResponse.errorMessage.equals("No Accounts Found", true) || evergentCommonResponse.errorMessage.equals("No account found with the given details", true)) {
                    createOtp(type, emailMobile, password)

                } else {
                    activitySinUpBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(this, evergentCommonResponse.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun createOtp(type: String, emailMobile: String, password: String) {
        astroLoginViewModel!!.createOtp(type, emailMobile).observe(this, Observer { evergentCommonResponse: EvergentCommonResponse ->
            activitySinUpBinding?.progressBar?.visibility = View.GONE

            if (evergentCommonResponse.isStatus) {
                Toast.makeText(this, "Verification code had be sent to $emailMobile", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VerificationActivity::class.java)
                intent.putExtra("type", type)
                intent.putExtra("emailMobile", emailMobile)
                intent.putExtra("password", password)
                intent.putExtra("from", "signUp")
                startActivity(intent)
            } else {
                Toast.makeText(this, evergentCommonResponse.errorMessage, Toast.LENGTH_SHORT).show()
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