package com.astro.sott.activities.language.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.astro.sott.R
import com.astro.sott.databinding.ActivityChangeLanguageBinding
import com.astro.sott.utils.commonMethods.AppCommonMethods
import com.astro.sott.utils.helpers.ToastHandler
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey

class ChangeLanguageActivity : AppCompatActivity() {

    private var activityChangeLanguageBinding: ActivityChangeLanguageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangeLanguageBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_language)
        setToolBar()
        setUI()
        setCLicks()
    }

    private fun setCLicks() {
        activityChangeLanguageBinding!!.englishLay.setOnClickListener(View.OnClickListener {
            changeLanguage()
        })

        activityChangeLanguageBinding!!.malayLay.setOnClickListener(View.OnClickListener {
            changeLanguage()
        })
    }

    private fun setUI() {
        val selectedLanguage = KsPreferenceKey(this).appLangName
        if (selectedLanguage.equals("en", ignoreCase = true) || selectedLanguage.equals("", ignoreCase = true)) {
            activityChangeLanguageBinding!!.secondTitleText.text = "English"
            activityChangeLanguageBinding!!.malaySecondTitleText.text = "Malay"
            activityChangeLanguageBinding!!.englishTick.visibility = View.VISIBLE


        } else {
            supportActionBar!!.title = "Tukar bahasa"
            activityChangeLanguageBinding!!.malayTick.visibility = View.VISIBLE
            activityChangeLanguageBinding!!.secondTitleText.text = "Bahasa Inggeris"
            activityChangeLanguageBinding!!.malaySecondTitleText.text = "Bahasa Melayu"
        }
    }

    private fun setToolBar() {
        setSupportActionBar(activityChangeLanguageBinding?.include?.toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.title = resources.getString(R.string.change_language)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeLanguage() {
        try {
            val selectedLanguage = KsPreferenceKey(this).appLangName
            if (selectedLanguage.equals("en", ignoreCase = true) || selectedLanguage.equals("", ignoreCase = true)) {
                AppCommonMethods.updateLanguage("ms", this)
                activityChangeLanguageBinding!!.englishTick.visibility = View.GONE
                activityChangeLanguageBinding!!.malayTick.visibility = View.VISIBLE
                activityChangeLanguageBinding!!.secondTitleText.text = "Bahasa Inggeris"
                supportActionBar!!.title = "Tukar bahasa"
                activityChangeLanguageBinding!!.malaySecondTitleText.text = "Bahasa Melayu"

                ToastHandler.show("Language changed to Malay", application.applicationContext)
            } else {
                activityChangeLanguageBinding!!.englishTick.visibility = View.VISIBLE
                activityChangeLanguageBinding!!.malayTick.visibility = View.GONE
                activityChangeLanguageBinding!!.secondTitleText.text = "English"
                supportActionBar!!.title = resources.getString(R.string.change_language)
                activityChangeLanguageBinding!!.malaySecondTitleText.text = "Malay"

                AppCommonMethods.updateLanguage("en", this)
                ToastHandler.show("Language changed to English", application.applicationContext)
            }
            // new ActivityLauncher(AppSettingsActivity.this).homeScreen(AppSettingsActivity.this, HomeActivity.class);

            //  finish();
        } catch (exc: Exception) {
        }
    }

}