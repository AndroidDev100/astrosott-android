package com.astro.sott.activities.manageDevice.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.astro.sott.R
import com.astro.sott.activities.loginActivity.AstrLoginViewModel.AstroLoginViewModel
import com.astro.sott.activities.manageDevice.adapter.ManageDeviceAdapter
import com.astro.sott.callBacks.DeviceDeleteCallBack
import com.astro.sott.databinding.ActivityManageDeviceBinding
import com.astro.sott.networking.refreshToken.EvergentRefreshToken
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse
import com.astro.sott.usermanagment.modelClasses.getDevice.AccountDeviceDetailsItem
import com.astro.sott.utils.commonMethods.AppCommonMethods
import com.astro.sott.utils.userInfo.UserInfo

class ManageDeviceActivity : AppCompatActivity(), DeviceDeleteCallBack {
    private var activityManageDeviceBinding: ActivityManageDeviceBinding? = null
    private var astroLoginViewModel: AstroLoginViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityManageDeviceBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_device)
        setToolbar()
        setRecyclerProperties()
        modelCall()
        getDevices()
    }

    private fun setRecyclerProperties() {
        activityManageDeviceBinding?.recyclerview?.layoutManager = LinearLayoutManager(this)

    }

    private fun modelCall() {
        astroLoginViewModel = ViewModelProviders.of(this).get(AstroLoginViewModel::class.java)

    }

    private fun getDevices() {
        activityManageDeviceBinding?.progressBar?.visibility = View.VISIBLE
        astroLoginViewModel?.getDevice(UserInfo.getInstance(this).accessToken)?.observe(this, Observer {
            activityManageDeviceBinding?.progressBar?.visibility = View.GONE
            if (it.isStatus) {
                if (it.getDevicesResponse.getAccountDevicesResponseMessage != null && it.getDevicesResponse.getAccountDevicesResponseMessage?.accountDeviceDetails != null && it.getDevicesResponse.getAccountDevicesResponseMessage?.accountDeviceDetails?.size!! > 0) {
                    setUiComponent(it.getDevicesResponse.getAccountDevicesResponseMessage?.accountDeviceDetails!!)
                }
            } else {
                if (it.errorCode.equals("eV2124", ignoreCase = true) || it.errorCode.equals("111111111", ignoreCase = true)) {
                    EvergentRefreshToken.refreshToken(this, UserInfo.getInstance(this).refreshToken).observe(this, Observer { evergentCommonResponse1: EvergentCommonResponse? ->
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
        activityManageDeviceBinding?.progressBar?.visibility = View.VISIBLE
        astroLoginViewModel?.removeDevice(UserInfo.getInstance(this).accessToken, serialNo)?.observe(this, Observer {
            activityManageDeviceBinding?.progressBar?.visibility = View.GONE
            if (it.isStatus) {
                if (it.removeDeviceResponse.removeDevicesResponseMessage != null) {
                    Toast.makeText(this, it.removeDeviceResponse.removeDevicesResponseMessage?.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun setUiComponent(accountDeviceDetails: List<AccountDeviceDetailsItem?>) {
        var manageDeviceAdapter = ManageDeviceAdapter(accountDeviceDetails, this)
        activityManageDeviceBinding?.recyclerview?.adapter = manageDeviceAdapter
        activityManageDeviceBinding?.logoutDevices?.visibility = View.VISIBLE

    }


    private fun setToolbar() {
        setSupportActionBar(activityManageDeviceBinding?.include?.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.title = "MANAGE DEVICES"

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

    override fun onDelete(serialNo: String?) {
        removeDevices(serialNo)
    }
}