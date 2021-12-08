package com.astro.sott.activities.manageDevice.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.astro.sott.R
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel
import com.astro.sott.activities.manageDevice.adapter.ManageDeviceAdapter
import com.astro.sott.baseModel.BaseActivity
import com.astro.sott.callBacks.DeviceDeleteCallBack
import com.astro.sott.databinding.ActivityManageDeviceBinding
import com.astro.sott.fragments.dialog.AlertDialogFragment
import com.astro.sott.networking.refreshToken.EvergentRefreshToken
import com.astro.sott.thirdParty.fcm.FirebaseEventManager
import com.astro.sott.usermanagment.modelClasses.getDevice.AccountDeviceDetailsItem
import com.astro.sott.utils.commonMethods.AppCommonMethods
import com.astro.sott.utils.userInfo.UserInfo
import java.util.*
import kotlin.collections.ArrayList

class ManageDeviceActivity : BaseActivity(), DeviceDeleteCallBack,
    AlertDialogFragment.AlertDialogListener {
    private var activityManageDeviceBinding: ActivityManageDeviceBinding? = null
    private var astroLoginViewModel: AstroLoginViewModel? = null
    private var serialList: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityManageDeviceBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_manage_device)
        AppCommonMethods.setProgressBar(activityManageDeviceBinding?.progressLay?.progressHeart)
        setToolbar()
        FirebaseEventManager.getFirebaseInstance(this)
            .trackScreenName(FirebaseEventManager.MANAGE_DEVICES)

        setRecyclerProperties()
        modelCall()
        setClicks()
        getDevices()
    }

    private fun setClicks() {
        activityManageDeviceBinding?.logoutDevices?.setOnClickListener {
            showAlertDialog(
                resources.getString(R.string.all_logout),
                resources.getString(R.string.device_logout)
            )
        }
    }

    private fun setToolbar() {
        activityManageDeviceBinding?.include?.backButton?.setOnClickListener {
            onBackPressed()
        }
        activityManageDeviceBinding?.include?.title?.text = "Manage Devices"
    }

    private fun setRecyclerProperties() {
        activityManageDeviceBinding?.recyclerview?.layoutManager = LinearLayoutManager(this)

    }

    private fun modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel::class.java)

    }

    private fun getDevices() {
        activityManageDeviceBinding?.progressLay?.progressHeart?.visibility = View.VISIBLE
        astroLoginViewModel?.getDevice(UserInfo.getInstance(this).accessToken)
            ?.observe(this, Observer {
                activityManageDeviceBinding?.progressLay?.progressHeart?.visibility = View.GONE
                if (it.isStatus) {
                    if (it.getDevicesResponse.getAccountDevicesResponseMessage != null && it.getDevicesResponse.getAccountDevicesResponseMessage?.accountDeviceDetails != null && it.getDevicesResponse.getAccountDevicesResponseMessage?.accountDeviceDetails?.size!! > 0) {
                        setUiComponent(
                            AppCommonMethods.checkCurrentDevice(
                                it.getDevicesResponse.getAccountDevicesResponseMessage?.accountDeviceDetails!!,
                                this
                            )
                        )
                    }
                } else {
                    if (it.errorCode.equals(
                            "eV2124",
                            ignoreCase = true
                        ) || it.errorCode.equals("111111111", ignoreCase = true)
                    ) {
                        EvergentRefreshToken.refreshToken(
                            this,
                            UserInfo.getInstance(this).refreshToken
                        ).observe(this, Observer { evergentCommonResponse1 ->
                            if (it.isStatus()) {
                                getDevices()
                            } else {
                                AppCommonMethods.removeUserPrerences(this)
                                onBackPressed()
                            }
                        })
                    } else {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }


    private fun removeDevicesAll() {
        activityManageDeviceBinding?.progressLay?.progressHeart?.visibility = View.VISIBLE
        astroLoginViewModel?.removeDevice(UserInfo.getInstance(this).accessToken, serialList)
            ?.observe(this, Observer {
                activityManageDeviceBinding?.progressLay?.progressHeart?.visibility = View.GONE
                if (it.isStatus) {
                    if (it.removeDeviceResponse.removeDevicesResponseMessage != null) {
                        Toast.makeText(
                            this,
                            it.removeDeviceResponse.removeDevicesResponseMessage?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        getDevices()
                    }
                } else {
                    if (it.errorCode.equals(
                            "eV2124",
                            ignoreCase = true
                        ) || it.errorCode.equals("111111111", ignoreCase = true)
                    ) {
                        EvergentRefreshToken.refreshToken(
                            this,
                            UserInfo.getInstance(this).refreshToken
                        ).observe(this, Observer { evergentCommonResponse1 ->
                            if (it.isStatus()) {
                                getDevices()
                            } else {
                                AppCommonMethods.removeUserPrerences(this)
                                onBackPressed()
                            }
                        })
                    } else {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun removeDevices(serialNo: String?) {
        serialList = ArrayList<String>()
        activityManageDeviceBinding?.progressLay?.progressHeart?.visibility = View.VISIBLE
        serialList?.add(serialNo!!)
        astroLoginViewModel?.removeDevice(UserInfo.getInstance(this).accessToken, serialList)
            ?.observe(this, Observer {
                activityManageDeviceBinding?.progressLay?.progressHeart?.visibility = View.GONE
                if (it.isStatus) {
                    if (it.removeDeviceResponse.removeDevicesResponseMessage != null) {
                        Toast.makeText(
                            this,
                            it.removeDeviceResponse.removeDevicesResponseMessage?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        getDevices()
                    }
                } else {
                    if (it.errorCode.equals(
                            "eV2124",
                            ignoreCase = true
                        ) || it.errorCode.equals("111111111", ignoreCase = true)
                    ) {
                        EvergentRefreshToken.refreshToken(
                            this,
                            UserInfo.getInstance(this).refreshToken
                        ).observe(this, Observer { evergentCommonResponse1 ->
                            if (it.isStatus()) {
                                getDevices()
                            } else {
                                AppCommonMethods.removeUserPrerences(this)
                                onBackPressed()
                            }
                        })
                    } else {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun setUiComponent(accountDeviceDetails: List<AccountDeviceDetailsItem?>) {
        serialList = ArrayList<String>()
        for (deviceDetails in accountDeviceDetails) {
            if (!deviceDetails?.serialNo.equals(
                    AppCommonMethods.getDeviceId(getContentResolver()),
                    true
                )
            ) {
                serialList?.add(deviceDetails?.serialNo!!)
            }
        }
        var manageDeviceAdapter = ManageDeviceAdapter(accountDeviceDetails, this)
        activityManageDeviceBinding?.recyclerview?.adapter = manageDeviceAdapter
        if (accountDeviceDetails.size > 1) {
            activityManageDeviceBinding?.logoutDevices?.visibility = View.VISIBLE
        } else {
            activityManageDeviceBinding?.logoutDevices?.visibility = View.GONE
        }

    }

    private fun showAlertDialog(title: String, msg: String) {
        val fm: FragmentManager = supportFragmentManager
        val alertDialog = AlertDialogFragment.newInstance(
            title,
            msg,
            resources.getString(R.string.ok),
            resources.getString(R.string.cancel)
        )
        alertDialog.setAlertDialogCallBack(this)
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDelete(serialNo: String?) {
        removeDevices(serialNo)
    }

    override fun onFinishDialog() {
        removeDevicesAll()
    }
}