package com.astro.sott.activities.signUp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.util.Log
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
import com.astro.sott.utils.helpers.AppLevelConstants
import com.astro.sott.utils.helpers.CustomTextWatcher
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.json.JSONException
import org.json.JSONObject
import java.lang.Double.parseDouble

class SignUpActivity : AppCompatActivity() {

    private var astroLoginViewModel: AstroLoginViewModel? = null
    private var activitySinUpBinding: ActivitySinUpBinding? = null
    private var callbackManager: CallbackManager? = null
    private var passwordVisibility = false
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activitySinUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sin_up)
        modelCall()
        setClicks()
        setWatcher()
        updateWithToken(AccessToken.getCurrentAccessToken())
        setGoogleSignIn()
        setFb()
    }

    private fun updateWithToken(currentAccessToken: AccessToken?) {
        if (currentAccessToken != null) {
            LoginManager.getInstance().logOut()
        } else {
        }
    }

    private fun setGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 4001) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            activitySinUpBinding?.progressBar?.visibility = View.VISIBLE
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            activitySinUpBinding?.progressBar?.visibility = View.VISIBLE
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            searchAccountv2("Google", "sunnykadan.1994+6@gmail.com", account.id + "")

            // Signed in successfully, show authenticated UI.
            //  updateUI(account);
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // updateUI(null);
        }
    }

    private fun modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel::class.java)
    }

    fun setClicks() {

        activitySinUpBinding?.google?.setOnClickListener {
            mGoogleSignInClient!!.signOut()
            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, 4001)
        }
        activitySinUpBinding?.fb?.setOnClickListener(View.OnClickListener {

            activitySinUpBinding?.loginButton?.performClick()
        })
        activitySinUpBinding?.loginBtn?.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        activitySinUpBinding?.backIcon?.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
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
                } else if (true) {
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
                activitySinUpBinding?.passwordEdt?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordVisibility = false
                activitySinUpBinding?.passwordEdt?.setSelection(activitySinUpBinding?.passwordEdt?.text?.length!!)
            } else {
                passwordVisibility = true
                activitySinUpBinding?.passwordEdt?.inputType = InputType.TYPE_CLASS_TEXT
                activitySinUpBinding?.eyeIcon?.setBackgroundResource(R.drawable.ic_outline_visibility_light)
                activitySinUpBinding?.passwordEdt?.setSelection(activitySinUpBinding?.passwordEdt?.text?.length!!)

            }
        })

    }

    private fun setFb() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        Log.w("fb_login", loginResult.toString() + "")
                        val graphRequest = GraphRequest.newMeRequest(loginResult.accessToken) { `object`: JSONObject?, response: GraphResponse? ->
                            if (`object` != null) {
                                if (`object`.has("email")) {
                                    try {
                                        val name = `object`.getString("name")
                                        val email = `object`.getString("email")
                                        val id = `object`.getString("id")
                                        searchAccountv2("Facebook", email, id + "")

                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    LoginManager.getInstance().logOut()
                                }
                            }
                        }
                        val parameters = Bundle()
                        parameters.putString("fields",
                                "id,name,email")
                        graphRequest.parameters = parameters
                        graphRequest.executeAsync()
                    }

                    override fun onCancel() {
                        // App code
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                    }
                })
    }

    private fun checkPassword(type: String, emailMobile: String, password: String) {
        val passwordPattern = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=\\S+$).{8,16}$")
        var password = activitySinUpBinding?.passwordEdt?.text.toString();
        if (!password.equals("", true)) {
            if (passwordPattern.containsMatchIn(password)) {
                if (activitySinUpBinding?.checkbox!!.isChecked) {
                    activitySinUpBinding?.errorCheckbox?.visibility = View.GONE
                    searchAccountv2(type, emailMobile, password)

                } else {
                    activitySinUpBinding?.errorCheckbox?.visibility = View.VISIBLE

                }
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

        astroLoginViewModel!!.searchAccountV2(type, emailMobile).observe(this, Observer { evergentCommonResponse ->
            if (evergentCommonResponse.isStatus) {
                activitySinUpBinding?.progressBar?.visibility = View.GONE
                Toast.makeText(this, resources.getString(R.string.already_registered), Toast.LENGTH_SHORT).show()
            } else {
                if (evergentCommonResponse.errorCode.equals("eV2327", true)) {
                    createOtp(type, emailMobile, password)
                } else {
                    activitySinUpBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(this, evergentCommonResponse.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun createOtp(type: String, emailMobile: String, password: String) {
        astroLoginViewModel!!.createOtp(type, emailMobile).observe(this, Observer { evergentCommonResponse ->
            activitySinUpBinding?.progressBar?.visibility = View.GONE

            if (evergentCommonResponse.isStatus) {
                Toast.makeText(this, "Verification code had be sent to $emailMobile", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VerificationActivity::class.java)
                intent.putExtra(AppLevelConstants.TYPE_KEY, type)
                intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, emailMobile)
                intent.putExtra(AppLevelConstants.PASSWORD_KEY, password)
                intent.putExtra(AppLevelConstants.FROM_KEY, "signUp")
                startActivity(intent)
            } else {
                Toast.makeText(this, evergentCommonResponse.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }


}