package com.astro.sott.activities.signUp.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.astro.sott.R
import com.astro.sott.activities.forgotPassword.ui.ForgotPasswordActivity
import com.astro.sott.activities.home.HomeActivity
import com.astro.sott.activities.isThatYou.IsThatYouActivity
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel
import com.astro.sott.activities.loginActivity.ui.AccountBlockedDialog
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity
import com.astro.sott.activities.verification.VerificationActivity
import com.astro.sott.baseModel.BaseActivity
import com.astro.sott.databinding.ActivitySinUpBinding
import com.astro.sott.fragments.dialog.AlreadyUserFragment
import com.astro.sott.networking.refreshToken.EvergentRefreshToken
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager
import com.astro.sott.thirdParty.fcm.FirebaseEventManager
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse
import com.astro.sott.usermanagment.modelClasses.getContact.SocialLoginTypesItem
import com.astro.sott.utils.commonMethods.AppCommonMethods
import com.astro.sott.utils.helpers.ActivityLauncher
import com.astro.sott.utils.helpers.AppLevelConstants
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey
import com.astro.sott.utils.userInfo.UserInfo
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
import java.util.*

class SignUpActivity : BaseActivity(), AccountBlockedDialog.EditDialogListener {
    private var socialLoginTypesItem: List<SocialLoginTypesItem>? = null
    private var astroLoginViewModel: AstroLoginViewModel? = null
    private var activitySinUpBinding: ActivitySinUpBinding? = null
    private var callbackManager: CallbackManager? = null
    private var passwordVisibility = false
    private var name: String = ""
    private val EMAIL = "email, public_profile"

    private val passwordPattern = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=\\S+$).{8,16}$")
    private val mobilePattern = Regex("^[0-9]*$")
    private val emailPattern = Regex(
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    )
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activitySinUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sin_up)
        modelCall()
        if (intent.getStringExtra(AppLevelConstants.FROM_KEY) != null)
            from = intent.getStringExtra(AppLevelConstants.FROM_KEY)!!
        CleverTapManager.getInstance().loginOrigin = from
        FirebaseEventManager.getFirebaseInstance(this).trackScreenName(FirebaseEventManager.SIGN_UP)
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
            if (account.id != null && !account.id.equals(
                    "",
                    ignoreCase = true
                ) && account.email != null && !account.email.equals("", ignoreCase = true)
            ) {
                name = account.displayName!!
                login("Google", account.email!!, account.id!!)
            } else {
                if (this != null)
                    Toast.makeText(
                        this,
                        resources.getString(R.string.email_unavailable),
                        Toast.LENGTH_SHORT
                    ).show()
            }

            // Signed in successfully, show authenticated UI.
            //  updateUI(account);
        } catch (e: ApiException) {
            activitySinUpBinding?.progressBar?.visibility = View.GONE
            Toast.makeText(
                this,
                resources.getString(R.string.email_unavailable),
                Toast.LENGTH_SHORT
            ).show()
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // updateUI(null);
        }
    }

    private fun modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel::class.java)
    }

    fun setClicks() {

        activitySinUpBinding?.terms?.setOnClickListener {
            ActivityLauncher(this).termAndCondition(this)
        }
        activitySinUpBinding?.privacy?.setOnClickListener {
            ActivityLauncher(this).privacy(this)
        }
        activitySinUpBinding?.google?.setOnClickListener {
            mGoogleSignInClient!!.signOut()
            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, 4001)
        }

        activitySinUpBinding?.passwordEdt?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                var password = activitySinUpBinding?.passwordEdt?.text.toString()
                if (!password.equals("", true)) {
                    if (passwordPattern.containsMatchIn(password)) {
                        activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.heather))
                        activitySinUpBinding?.errorPasssword?.text =
                            getString(R.string.password_rules)
                    } else {
                        activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.red_live))
                        activitySinUpBinding?.errorEmail?.text =
                            resources.getString(R.string.email_suggestion)
                        activitySinUpBinding?.errorPasssword?.text =
                            getString(R.string.password_error)
                    }
                } else {
                    activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.red_live))
                    activitySinUpBinding?.errorEmail?.text =
                        resources.getString(R.string.email_suggestion)
                    activitySinUpBinding?.errorPasssword?.text =
                        getString(R.string.field_cannot_empty)
                }
            }
            false
        })
        activitySinUpBinding?.fb?.setOnClickListener(View.OnClickListener {
            activitySinUpBinding?.loginButton?.performClick()
        })
        activitySinUpBinding?.loginBtn?.setOnClickListener(View.OnClickListener {
            ActivityLauncher(this).astrLoginActivity(this, AstrLoginActivity::class.java, from)
        })
        activitySinUpBinding?.backIcon?.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        activitySinUpBinding?.nextBtn?.setOnClickListener {
            var password = activitySinUpBinding?.passwordEdt?.text.toString()
            var email_mobile = activitySinUpBinding?.mobileEmailEdt?.text.toString()
            if (!email_mobile.equals("", true)) {
                /*if (mobilePattern.containsMatchIn(email_mobile)) {
                    if (email_mobile?.length == 10 || email_mobile?.length == 11) {

                        checkPassword("mobile", email_mobile, password)
                    } else {
                        activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
                        activitySinUpBinding?.errorEmail?.setTextColor(resources.getColor(R.color.red_live))
                        activitySinUpBinding?.errorEmail?.text =
                            getString(R.string.email_mobile_error)
                        checkPasswordValidation(password)

                    }
                } else*/ if (true) {
                    checkPassword("email", email_mobile, password)
                } else {

                    /*  var numeric = true
                      try {
                          val num = parseDouble(email_mobile?.first().toString())
                      } catch (e: NumberFormatException) {
                          numeric = false
                      }
                      if (numeric) {
                          activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
                          activitySinUpBinding?.errorEmail?.setTextColor(resources.getColor(R.color.red_live))
                          activitySinUpBinding?.errorEmail?.text =
                              getString(R.string.email_mobile_error)
                          checkPasswordValidation(password)
                      } else {*/
                    activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
                    activitySinUpBinding?.errorEmail?.setTextColor(resources.getColor(R.color.red_live))
                    activitySinUpBinding?.errorEmail?.text =
                        getString(R.string.email_suggestion)
                    checkPasswordValidation(password)
                    /* }*/
                }

            } else {
                activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
                activitySinUpBinding?.errorEmail?.setTextColor(resources.getColor(R.color.red_live))
                activitySinUpBinding?.errorEmail?.text = getString(R.string.field_cannot_empty)
                checkPasswordValidation(password)

            }

        }
        activitySinUpBinding?.mobileEmailEdt?.setOnFocusChangeListener { v, hasFocus ->
            activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
            activitySinUpBinding?.errorPasssword?.visibility = View.VISIBLE
        }
        activitySinUpBinding?.passwordEdt?.setOnFocusChangeListener { v, hasFocus ->
            var email_mobile = activitySinUpBinding?.mobileEmailEdt?.text.toString()
            activitySinUpBinding?.errorEmail?.visibility = View.VISIBLE
            activitySinUpBinding?.errorPasssword?.visibility = View.VISIBLE
            if (!email_mobile.equals("", true)) {
                if (mobilePattern.containsMatchIn(email_mobile) || emailPattern.containsMatchIn(
                        email_mobile
                    )
                ) {
                    activitySinUpBinding?.errorEmail?.setTextColor(resources.getColor(R.color.heather))
                    activitySinUpBinding?.errorEmail?.text =
                        resources.getString(R.string.email_suggestion)
                } else {
                    activitySinUpBinding?.errorEmail?.setTextColor(resources.getColor(R.color.red_live))
                    activitySinUpBinding?.errorEmail?.text =
                        resources.getString(R.string.email_suggestion)
                }
            } else {
                activitySinUpBinding?.errorEmail?.setTextColor(resources.getColor(R.color.red_live))
                activitySinUpBinding?.errorEmail?.text =
                    resources.getString(R.string.field_cannot_empty)

            }
        }
        activitySinUpBinding?.eyeIcon?.setOnClickListener(View.OnClickListener {
            if (passwordVisibility) {
                activitySinUpBinding?.eyeIcon?.setBackgroundResource(R.drawable.ic_outline_visibility_off_light)
                activitySinUpBinding?.passwordEdt?.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
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

    private fun checkPasswordValidation(password: String) {
        if (!password.equals("", true)) {
            if (passwordPattern.containsMatchIn(password)) {
                activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.warm_grey))
                activitySinUpBinding?.errorPasssword?.text = getString(R.string.password_error)
            } else {
                activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.red_live))
                activitySinUpBinding?.errorPasssword?.visibility = View.VISIBLE
                activitySinUpBinding?.errorPasssword?.text = getString(R.string.password_error)

            }
        } else {
            activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.red_live))
            activitySinUpBinding?.errorPasssword?.visibility = View.VISIBLE
            activitySinUpBinding?.errorPasssword?.text = getString(R.string.field_cannot_empty)
        }
    }

    private fun setFb() {
        callbackManager = CallbackManager.Factory.create()
        activitySinUpBinding?.loginButton?.setReadPermissions(Arrays.asList(EMAIL))

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    // App code
                    Log.w("fb_login", loginResult.toString() + "")
                    val graphRequest =
                        GraphRequest.newMeRequest(loginResult.accessToken) { `object`: JSONObject?, response: GraphResponse? ->
                            if (`object` != null) {
                                if (`object`.has("email")) {
                                    try {
                                        val fbName = `object`.getString("name")
                                        val email = `object`.getString("email")
                                        val id = `object`.getString("id")
                                        if (email != null && !email.equals(
                                                "",
                                                ignoreCase = true
                                            ) && id != null && !id.equals("", ignoreCase = true)
                                        ) {
                                            name = fbName
                                            login("Facebook", email, id + "")
                                        } else {
                                            Toast.makeText(
                                                this@SignUpActivity,
                                                resources.getString(R.string.email_unavailable),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        resources.getString(R.string.email_unavailable),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    LoginManager.getInstance().logOut()
                                }
                            }
                        }
                    val parameters = Bundle()
                    parameters.putString(
                        "fields",
                        "id,name,email"
                    )
                    graphRequest.parameters = parameters
                    graphRequest.executeAsync()
                }


                override fun onCancel() {
                    activitySinUpBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.email_unavailable),
                        Toast.LENGTH_SHORT
                    ).show()
                }


                // activitySinUpBinding?.loginButton?.loginBehavior = LoginBehavior.WEB_ONLY
                override fun onError(exception: FacebookException) {
                    activitySinUpBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.email_unavailable),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        // activitySinUpBinding?.loginButton?.loginBehavior = LoginBehavior.WEB_ONLY
    }

    private fun checkPassword(type: String, emailMobile: String, password: String) {
        activitySinUpBinding?.errorEmail?.setTextColor(resources.getColor(R.color.heather))
        activitySinUpBinding?.errorEmail?.text = getString(R.string.email_suggestion)
        var password = activitySinUpBinding?.passwordEdt?.text.toString();
        if (!password.equals("", true)) {
            if (passwordPattern.containsMatchIn(password)) {
                /*  if (activitySinUpBinding?.checkbox!!.isChecked) {*/
                activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.warm_grey))
                activitySinUpBinding?.errorPasssword?.text = getString(R.string.password_error)
                activitySinUpBinding?.errorCheckbox?.visibility = View.GONE
                searchAccountv2(type, emailMobile, password)

                /*  } else {
                      activitySinUpBinding?.errorCheckbox?.visibility = View.VISIBLE
                      activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.warm_grey))
                  }*/
                //createUser(type, emailMobile, password)
            } else {
                activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.red_live))
                activitySinUpBinding?.errorPasssword?.visibility = View.VISIBLE
                activitySinUpBinding?.errorPasssword?.text = getString(R.string.password_error)

            }
        } else {
            activitySinUpBinding?.errorPasssword?.setTextColor(resources.getColor(R.color.red_live))
            activitySinUpBinding?.errorPasssword?.visibility = View.VISIBLE
            activitySinUpBinding?.errorPasssword?.text = getString(R.string.field_cannot_empty)

        }
    }

    private fun login(type: String, emailMobile: String, password: String) {
        activitySinUpBinding?.progressBar?.visibility = View.VISIBLE
        val tabletSize = resources.getBoolean(R.bool.isTablet)

        astroLoginViewModel!!.loginUser(type, emailMobile, password, tabletSize).observe(
            this@SignUpActivity,
            Observer { evergentCommonResponse: EvergentCommonResponse<*> ->
                if (evergentCommonResponse.isStatus) {
                    UserInfo.getInstance(this).accessToken =
                        evergentCommonResponse.loginResponse.getOAuthAccessTokenv2ResponseMessage!!.accessToken
                    UserInfo.getInstance(this).refreshToken =
                        evergentCommonResponse.loginResponse.getOAuthAccessTokenv2ResponseMessage!!.refreshToken
                    UserInfo.getInstance(this).externalSessionToken =
                        evergentCommonResponse.loginResponse.getOAuthAccessTokenv2ResponseMessage!!.externalSessionToken
                    KsPreferenceKey.getInstance(this).startSessionKs =
                        evergentCommonResponse.loginResponse.getOAuthAccessTokenv2ResponseMessage!!.externalSessionToken
                    if (type.equals("Facebook", ignoreCase = true) || type.equals(
                            "Google",
                            ignoreCase = true
                        )
                    ) {
                        UserInfo.getInstance(this).isSocialLogin = true
                    }
                    getContact()
                    try {
                        CleverTapManager.getInstance().setSignInEvent(this, from, type)
                    } catch (ex: Exception) {
                    }
                    astroLoginViewModel!!.addToken(UserInfo.getInstance(this).externalSessionToken)
                } else {
                    activitySinUpBinding?.progressBar?.visibility = View.GONE
                    if (type.equals("Facebook", ignoreCase = true) || type.equals(
                            "Google",
                            ignoreCase = true
                        )
                    ) {
                        if (evergentCommonResponse.errorCode.equals("eV2327", ignoreCase = true)) {
                            socialSearchAccountv2(password, type, emailMobile)
                        } else {
                            Toast.makeText(
                                this,
                                evergentCommonResponse.errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        if (evergentCommonResponse.errorCode.equals("eV4492", ignoreCase = true)) {
                            val fm = supportFragmentManager
                            val accountBlockedDialog = AccountBlockedDialog.newInstance(
                                resources.getString(R.string.create_playlist_name_title),
                                ""
                            )
                            accountBlockedDialog.setEditDialogCallBack(this)
                            accountBlockedDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT)
                        } else {
                            Toast.makeText(
                                this,
                                evergentCommonResponse.errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
        if (UserInfo.getInstance(this).isActive)
            onBackPressed()
    }

    private fun getContact() {
        astroLoginViewModel!!.getContact(UserInfo.getInstance(this@SignUpActivity).accessToken)
            .observe(
                this@SignUpActivity,
                Observer { evergentCommonResponse: EvergentCommonResponse<*> ->
                    activitySinUpBinding?.progressBar?.visibility = View.GONE
                    if (evergentCommonResponse.isStatus && evergentCommonResponse.getContactResponse.getContactResponseMessage != null && evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage != null && evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!!.size > 0) {
                        UserInfo.getInstance(this).firstName =
                            evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.firstName
                        UserInfo.getInstance(this).lastName =
                            evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.lastName
                        UserInfo.getInstance(this).email =
                            evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.email
                        if (evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.userName != null && !evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.userName.equals(
                                "",
                                ignoreCase = true
                            )
                        ) {
                            UserInfo.getInstance(this).userName =
                                evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.userName
                        } else if (evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.alternateUserName != null && !evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.alternateUserName.equals(
                                "",
                                ignoreCase = true
                            )
                        ) {
                            UserInfo.getInstance(this).alternateUserName =
                                evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.alternateUserName
                        }
                        if (evergentCommonResponse.getContactResponse.getContactResponseMessage!!.accountRole != null) UserInfo.getInstance(
                            this
                        ).accountRole =
                            evergentCommonResponse.getContactResponse.getContactResponseMessage!!.accountRole
                        if (evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.socialLoginTypes != null && evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.socialLoginTypes!!.size > 0) {
                            socialLoginTypesItem =
                                evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.socialLoginTypes as List<SocialLoginTypesItem>?
                            AppCommonMethods.checkSocailLinking(this, socialLoginTypesItem)
                        }
                        UserInfo.getInstance(this).isPasswordExists =
                            evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.isPasswordExists!!
                        UserInfo.getInstance(this).mobileNumber =
                            evergentCommonResponse.getContactResponse.getContactResponseMessage!!.contactMessage!![0]!!.mobileNumber

                        UserInfo.getInstance(this).cpCustomerId =
                            evergentCommonResponse.getContactResponse.getContactResponseMessage!!.cpCustomerID
                        AppCommonMethods.setCrashlyticsUserId(this)
                        getActiveSubscription()
                    } else {
                        if (evergentCommonResponse.errorCode.equals(
                                "eV2124",
                                ignoreCase = true
                            ) || evergentCommonResponse.errorCode.equals(
                                "111111111",
                                ignoreCase = true
                            )
                        ) {
                            EvergentRefreshToken.refreshToken(
                                this,
                                UserInfo.getInstance(this).refreshToken
                            ).observe(
                                this,
                                Observer { evergentCommonResponse1: EvergentCommonResponse<*>? ->
                                    if (evergentCommonResponse.isStatus) {
                                        getContact()
                                    } else {
                                        AppCommonMethods.removeUserPrerences(this)
                                        onBackPressed()
                                    }
                                })
                        } else {
                            Toast.makeText(
                                this,
                                evergentCommonResponse.errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
    }

    private var displayName = ""
    private var paymentMethod = ""
    private var from = ""
    private fun getActiveSubscription() {
        astroLoginViewModel!!.getActiveSubscription(UserInfo.getInstance(this).accessToken, "")
            .observe(
                this,
                Observer { evergentCommonResponse: EvergentCommonResponse<GetActiveResponse> ->
                    if (evergentCommonResponse.isStatus) {
                        if (evergentCommonResponse.response.getActiveSubscriptionsResponseMessage != null) {
                            if (evergentCommonResponse.response.getActiveSubscriptionsResponseMessage!!.accountServiceMessage != null && evergentCommonResponse.response.getActiveSubscriptionsResponseMessage!!.accountServiceMessage!!.size > 0) {
                                for (accountServiceMessageItem in evergentCommonResponse.response.getActiveSubscriptionsResponseMessage!!.accountServiceMessage!!) {
                                    if (!accountServiceMessageItem?.isFreemium!!) {
                                        if (accountServiceMessageItem.displayName != null)
                                            displayName = accountServiceMessageItem.displayName!!
                                        paymentMethod = accountServiceMessageItem.paymentMethod!!
                                    }
                                }
                                UserInfo.getInstance(this).isMaxis = paymentMethod.equals(
                                    AppLevelConstants.MAXIS_BILLING,
                                    ignoreCase = true
                                )

                                if (!displayName.equals("", ignoreCase = true)) {
                                    UserInfo.getInstance(this).isVip = true
                                    setActive()
                                } else {
                                    UserInfo.getInstance(this).isVip = false
                                    setActive()
                                }
                            } else {
                                UserInfo.getInstance(this).isVip = false
                                setActive()
                            }
                        } else {
                            UserInfo.getInstance(this).isVip = false
                            setActive()
                        }
                    } else {
                        UserInfo.getInstance(this).isVip = false
                        setActive()
                    }
                })
    }

    private fun setActive() {
        UserInfo.getInstance(this).isActive = true
        AppCommonMethods.setCleverTap(this)
        FirebaseEventManager.getFirebaseInstance(this)
            .userLoginEvent(
                UserInfo.getInstance(this).cpCustomerId,
                UserInfo.getInstance(this).accountRole,
                ""
            )

        Toast.makeText(this@SignUpActivity, "User Logged in successfully.", Toast.LENGTH_SHORT)
            .show()

        if (from.equals("Profile", ignoreCase = true)) {
            ActivityLauncher(this).profileScreenRedirection(
                this,
                HomeActivity::class.java
            )
        } else {
            onBackPressed()
        }
        //   ActivityLauncher(this@SignUpActivity).homeScreen(this, HomeActivity::class.java)

    }

    private fun searchAccountv2(type: String, emailMobile: String, password: String) {
        activitySinUpBinding?.progressBar?.visibility = View.VISIBLE

        astroLoginViewModel!!.searchAccountV2(type, emailMobile)
            .observe(this, Observer { evergentCommonResponse ->
                if (evergentCommonResponse.isStatus) {
                    activitySinUpBinding?.progressBar?.visibility = View.GONE
                    val fm: FragmentManager = supportFragmentManager
                    val cancelDialogFragment = AlreadyUserFragment.newInstance("player", "")
                    cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT)
                } else {
                    if (evergentCommonResponse.errorCode.equals("eV2327", true)) {
                        createOtp(type, emailMobile, password)
                    } else {
                        activitySinUpBinding?.progressBar?.visibility = View.GONE
                        Toast.makeText(
                            this,
                            evergentCommonResponse.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun createOtp(type: String, emailMobile: String, password: String) {
        astroLoginViewModel!!.createOtp(type, emailMobile)
            .observe(this, Observer { evergentCommonResponse ->
                activitySinUpBinding?.progressBar?.visibility = View.GONE

                if (evergentCommonResponse.isStatus) {
                    // Toast.makeText(this, "Verification code had be sent to $emailMobile", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, VerificationActivity::class.java)
                    intent.putExtra(AppLevelConstants.TYPE_KEY, type)
                    intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, emailMobile)
                    intent.putExtra(AppLevelConstants.PASSWORD_KEY, password)
                    intent.putExtra(AppLevelConstants.FROM_KEY, "signUp")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, evergentCommonResponse.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun socialSearchAccountv2(password: String, type: String, emailMobile: String) {
        activitySinUpBinding?.progressBar?.visibility = View.VISIBLE
        astroLoginViewModel!!.searchAccountV2("email", emailMobile)
            .observe(this, Observer { evergentCommonResponse: EvergentCommonResponse<*> ->
                activitySinUpBinding?.progressBar?.visibility = View.GONE
                if (evergentCommonResponse.isStatus) {
                    val intent = Intent(this, IsThatYouActivity::class.java)
                    intent.putExtra(AppLevelConstants.TYPE_KEY, type)
                    intent.putExtra(AppLevelConstants.EMAIL_MOBILE_KEY, emailMobile)
                    intent.putExtra(AppLevelConstants.SOCIAL_ID, password)
                    startActivity(intent)
                } else {
                    if (evergentCommonResponse.errorCode.equals("eV2327", ignoreCase = true)) {
                        createUser(password, emailMobile, type)
                    } else {
                        Toast.makeText(
                            this,
                            evergentCommonResponse.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun createUser(password: String, email_mobile: String, type: String) {
        activitySinUpBinding?.progressBar?.visibility = View.VISIBLE
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        astroLoginViewModel!!.createUser(type, email_mobile, password, name, tabletSize)
            .observe(this,
                Observer { evergentCommonResponse: EvergentCommonResponse<*> ->
                    if (evergentCommonResponse.isStatus) {
                        UserInfo.getInstance(this).accessToken =
                            evergentCommonResponse.createUserResponse.createUserResponseMessage!!.accessToken
                        UserInfo.getInstance(this).refreshToken =
                            evergentCommonResponse.createUserResponse.createUserResponseMessage!!.refreshToken
                        UserInfo.getInstance(this).externalSessionToken =
                            evergentCommonResponse.createUserResponse.createUserResponseMessage!!.externalSessionToken
                        KsPreferenceKey.getInstance(this).startSessionKs =
                            evergentCommonResponse.createUserResponse.createUserResponseMessage!!.externalSessionToken
                        astroLoginViewModel!!.addToken(UserInfo.getInstance(this).externalSessionToken)
                        AppCommonMethods.onUserRegister(this)
                        getContact()
                    } else {
                        Toast.makeText(
                            this,
                            evergentCommonResponse.errorMessage,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        activitySinUpBinding?.progressBar?.visibility = View.GONE
                    }
                })
    }

    override fun onFinishEditDialog() {
        ActivityLauncher(this).forgotPasswordActivity(this, ForgotPasswordActivity::class.java)

    }

}