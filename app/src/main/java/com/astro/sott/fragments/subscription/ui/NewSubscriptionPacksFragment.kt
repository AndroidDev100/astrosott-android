package com.astro.sott.fragments.subscription.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.billingclient.api.SkuDetails
import com.astro.sott.R
import com.astro.sott.activities.home.HomeActivity
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity
import com.astro.sott.activities.search.ui.ActivitySearch
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity
import com.astro.sott.activities.webview.ui.WebViewActivity
import com.astro.sott.baseModel.BaseBindingFragment
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack
import com.astro.sott.databinding.FragmentNewSubscriptionPacksBinding
import com.astro.sott.fragments.subscription.adapter.SubscriptionPagerAdapter
import com.astro.sott.fragments.subscription.adapter.SubscriptionRecyclerViewAdapter
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel
import com.astro.sott.modelClasses.InApp.PackDetail
import com.astro.sott.networking.refreshToken.EvergentRefreshToken
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem
import com.astro.sott.usermanagment.modelClasses.lastSubscription.LastSubscriptionResponse
import com.astro.sott.utils.billing.SKUsListListener
import com.astro.sott.utils.billing.SKUsListListener
import com.astro.sott.utils.commonMethods.AppCommonMethods
import com.astro.sott.utils.helpers.ActivityLauncher
import com.astro.sott.utils.helpers.AppLevelConstants
import com.astro.sott.utils.helpers.carousel.SliderPotrait
import com.astro.sott.utils.userInfo.UserInfo
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.app_toolbar.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewSubscriptionPacksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewSubscriptionPacksFragment : BaseBindingFragment<FragmentNewSubscriptionPacksBinding>(), View.OnClickListener, SubscriptionPagerAdapter.OnPackageChooseClickListener {
    private var productList = ArrayList<String>()
    private var pendingList = ArrayList<String>()
    private lateinit var cardClickedCallback: CardCLickedCallBack
    private var indicatorWidth: Int = 0
    private lateinit var subscriptionViewModel: SubscriptionViewModel
    private var subscriptionIds: Array<String>? = null
    private lateinit var packDetailList: ArrayList<PackDetail>
    private var skuDetails: SkuDetails? = null
    private var accountServiceMessage = ArrayList<AccountServiceMessageItem>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel::class.java)
        if (arguments!!.getSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY) != null)
            subscriptionIds = arguments!!.getSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY) as Array<String>
        binding.toolbar.search_icon.setOnClickListener {
            ActivityLauncher(activity!!).searchActivity(activity!!, ActivitySearch::class.java)
        }
        getActiveSubscription()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cardClickedCallback = context as CardCLickedCallBack
    }

    private fun getActiveSubscription() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel::class.java)
        subscriptionViewModel.getActiveSubscription(UserInfo.getInstance(activity).accessToken, "").observe(this, Observer { evergentCommonResponse: EvergentCommonResponse<GetActiveResponse> ->
            if (evergentCommonResponse.isStatus) {
                if (evergentCommonResponse.response.getActiveSubscriptionsResponseMessage != null && evergentCommonResponse.response.getActiveSubscriptionsResponseMessage!!.accountServiceMessage != null && evergentCommonResponse.response.getActiveSubscriptionsResponseMessage!!.accountServiceMessage!!.size > 0) {
                    getListofActivePacks(evergentCommonResponse.response.getActiveSubscriptionsResponseMessage!!.accountServiceMessage)
                    getProducts()
                } else {
                    getProducts()
                }
            } else {
                if (evergentCommonResponse.errorCode.equals("eV2124", ignoreCase = true) || evergentCommonResponse.errorCode == "111111111") {
                    EvergentRefreshToken.refreshToken(activity, UserInfo.getInstance(activity).refreshToken).observe(this, Observer { evergentCommonResponse1: EvergentCommonResponse<*>? ->
                        if (evergentCommonResponse.isStatus) {
                            getActiveSubscription()
                        } else {
                            AppCommonMethods.removeUserPrerences(activity)
                            getProducts()
                        }
                    })
                } else {
                    getProducts()
                }
            }
        })
    }


    private fun getListofActivePacks(accountServiceMessage: List<AccountServiceMessageItem?>?) {
        productList = java.util.ArrayList<String>()
        for (accountServiceMessageItem in accountServiceMessage!!) {
            if (accountServiceMessageItem!!.status.equals("ACTIVE", ignoreCase = true) && !accountServiceMessageItem.isFreemium!!) {
                if (accountServiceMessageItem.serviceID != null) {
                    productList.add(accountServiceMessageItem.serviceID!!)
                }
            } else if (accountServiceMessageItem!!.status.equals("PENDING ACTIVE", ignoreCase = true) && !accountServiceMessageItem.isFreemium!!) {
                if (accountServiceMessageItem.serviceID != null) {
                    pendingList.add(accountServiceMessageItem.serviceID!!)
                }
            }
        }
    }

    private fun getProductsForLogout() {
        subscriptionViewModel.product.observe(this, androidx.lifecycle.Observer { evergentCommonResponse: EvergentCommonResponse<*> ->
            binding.includeProgressbar.progressBar.visibility = View.GONE
            if (evergentCommonResponse.isStatus) {
                if (evergentCommonResponse.getProductResponse != null && evergentCommonResponse.getProductResponse.getProductsResponseMessage != null && evergentCommonResponse.getProductResponse.getProductsResponseMessage!!.productsResponseMessage != null && evergentCommonResponse.getProductResponse.getProductsResponseMessage!!.productsResponseMessage!!.size > 0) {
                    checkIfDetailAvailableOnPlaystore(evergentCommonResponse.getProductResponse.getProductsResponseMessage!!.productsResponseMessage)
                }
            } else {
            }
        })
    }

    private fun getProducts() {
        if (!UserInfo.getInstance(activity).isActive) {
            getProductsForLogout()
        } else {
            if (subscriptionIds != null) {
                val jsonArray = JsonArray()
                for (id in subscriptionIds!!) {
                    jsonArray.add(id)
                }
                subscriptionViewModel.getProductForLogin(UserInfo.getInstance(activity).accessToken, jsonArray).observe(this, androidx.lifecycle.Observer { evergentCommonResponse: EvergentCommonResponse<*> ->
                    binding.includeProgressbar.progressBar.setVisibility(View.GONE)
                    if (evergentCommonResponse.isStatus) {
                        if (evergentCommonResponse.getProductResponse != null && evergentCommonResponse.getProductResponse.getProductsResponseMessage != null && evergentCommonResponse.getProductResponse.getProductsResponseMessage!!.productsResponseMessage != null && evergentCommonResponse.getProductResponse.getProductsResponseMessage!!.productsResponseMessage!!.size > 0) {
                            checkIfDetailAvailableOnPlaystore(evergentCommonResponse.getProductResponse.getProductsResponseMessage!!.productsResponseMessage)
                        }
                    } else {
                        if (evergentCommonResponse.errorCode.equals("eV2124", ignoreCase = true) || evergentCommonResponse.errorCode == "111111111") {
                            EvergentRefreshToken.refreshToken(activity, UserInfo.getInstance(activity).refreshToken).observe(this, androidx.lifecycle.Observer { evergentCommonResponse1: EvergentCommonResponse<*>? ->
                                if (evergentCommonResponse.isStatus) {
                                    getProducts()
                                } else {
                                    AppCommonMethods.removeUserPrerences(activity)
                                }
                            })
                        }
                    }
                })
            } else {
                getProductsForLogout()
            }
        }
    }

    private fun checkIfDetailAvailableOnPlaystore(productsResponseMessage: List<ProductsResponseMessageItem?>?) {
        // packDetailList = java.util.ArrayList()
        val subSkuList = /*ArrayList<String>();*/AppCommonMethods.getSubscriptionSKUs(productsResponseMessage, activity)
        val productsSkuList = ArrayList<String>();//AppCommonMethods.getProductSKUs(productsResponseMessage, activity)
        if (activity is HomeActivity) {
            (activity as HomeActivity?)!!.onListOfSKUs(subSkuList, productsSkuList, SKUsListListener {
                packDetailList = ArrayList<PackDetail>()
                if (productsResponseMessage != null) {
                    for (responseMessageItem in productsResponseMessage) {
                        if (responseMessageItem?.appChannels != null && responseMessageItem?.appChannels!![0] != null && responseMessageItem?.appChannels!![0]!!.appChannel != null && responseMessageItem?.appChannels!![0]!!.appChannel.equals("Google Wallet", ignoreCase = true) && responseMessageItem?.appChannels!![0]!!.appID != null) {
                            Log.w("avname", activity!!.javaClass.getName() + "")
                            if (activity is HomeActivity) {
                                skuDetails = (activity as HomeActivity?)!!.getSubscriptionDetail(responseMessageItem?.appChannels!![0]!!.appID)
                            } else if (activity is SubscriptionDetailActivity) {
                                if (responseMessageItem.serviceType.equals("ppv", ignoreCase = true)) {
                                    skuDetails = (activity as SubscriptionDetailActivity?)!!.getPurchaseDetail(responseMessageItem?.appChannels!![0]!!.appID)
                                } else {
                                    skuDetails = (activity as SubscriptionDetailActivity?)!!.getSubscriptionDetail(responseMessageItem?.appChannels!![0]!!.appID)
                                }
                            }
                            if (skuDetails != null) {
                                val packDetail = PackDetail()
                                packDetail.skuDetails = skuDetails
                                packDetail.productsResponseMessageItem = responseMessageItem
                                //  Log.w("priceValues", skuDetails!!.introductoryPrice + "<--->"+skuDetails!!.price+"<----->")
                                packDetailList.add(packDetail)
                            }
                        }
                    }
                    if (packDetailList.size > 0) loadDataFromModel(packDetailList)
                }
            })
        } else if (activity is SubscriptionDetailActivity) {
            (activity as SubscriptionDetailActivity?)!!.onListOfSKUs(subSkuList, productsSkuList, SKUsListListener {
                packDetailList = ArrayList<PackDetail>()
                if (productsResponseMessage != null) {
                    for (responseMessageItem in productsResponseMessage) {
                        if (responseMessageItem?.appChannels != null && responseMessageItem?.appChannels!![0] != null && responseMessageItem?.appChannels!![0]!!.appChannel != null && responseMessageItem?.appChannels!![0]!!.appChannel.equals("Google Wallet", ignoreCase = true) && responseMessageItem?.appChannels!![0]!!.appID != null) {
                            Log.w("avname", activity!!.javaClass.getName() + "")
                            if (activity is HomeActivity) {
                                skuDetails = (activity as HomeActivity?)!!.getSubscriptionDetail(responseMessageItem?.appChannels!![0]!!.appID)
                            } else if (activity is SubscriptionDetailActivity) {
                                if (responseMessageItem.serviceType.equals("ppv", ignoreCase = true)) {
                                    skuDetails = (activity as SubscriptionDetailActivity?)!!.getPurchaseDetail(responseMessageItem?.appChannels!![0]!!.appID)
                                } else {
                                    skuDetails = (activity as SubscriptionDetailActivity?)!!.getSubscriptionDetail(responseMessageItem?.appChannels!![0]!!.appID)
                                }
                            }
                            if (skuDetails != null) {
                                val packDetail = PackDetail()
                                packDetail.skuDetails = skuDetails
                                packDetail.productsResponseMessageItem = responseMessageItem
                                //  Log.w("priceValues", skuDetails!!.introductoryPrice + "<--->"+skuDetails!!.price+"<----->")
                                packDetailList.add(packDetail)
                            }
                        }
                    }
                    if (packDetailList.size > 0) loadDataFromModel(packDetailList)
                }
            })
        }

    }

    private fun loadDataFromModel(productsResponseMessage: List<PackDetail>) {
        if (resources.getBoolean(R.bool.isTablet)) {
            setRecyclerview(productsResponseMessage)
        } else {
            setViewPager(productsResponseMessage);
        }
    }

    private fun setRecyclerview(packagesList: List<PackDetail>) {
        binding.packagesRecyclerView?.setHasFixedSize(true)
        binding.packagesRecyclerView?.setItemViewCacheSize(20)
        binding.packagesRecyclerView?.setLayoutManager(LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false))
        binding.packagesRecyclerView?.adapter = SubscriptionRecyclerViewAdapter(activity!!, (packagesList as java.util.ArrayList<PackDetail>), productList, this)
    }

    private fun setViewPager(packagesList: List<PackDetail>) {
        binding.viewPager?.adapter = SubscriptionPagerAdapter(activity!!, packagesList, productList, pendingList, this)
        binding.viewPager?.setPadding(SliderPotrait.dp2px(activity!!, 32f), 0, SliderPotrait.dp2px(activity!!, 32f), 0);
        binding.viewPager?.clipChildren = false
        binding.viewPager?.clipToPadding = false
        binding.viewPager?.pageMargin = SliderPotrait.dp2px(activity!!, 16f);
        val rainbow = resources.getIntArray(R.array.packages_colors)
        binding.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val currentColor = rainbow[position % rainbow.size]
                binding.tabs?.setSelectedTabIndicatorColor(currentColor)
                binding.tabs?.setTabTextColors(resources.getColor(R.color.gray), currentColor)
            }
        })
        binding.tabs?.setupWithViewPager(binding.viewPager);
        binding.terms.setOnClickListener(this)
        binding.tabs?.setSelectedTabIndicatorColor(resources.getColor(R.color.yellow_orange))
        binding.tabs?.setTabTextColors(resources.getColor(R.color.gray), resources.getColor(R.color.yellow_orange))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    // tab titles
    override fun inflateBindingLayout(inflater: LayoutInflater): FragmentNewSubscriptionPacksBinding? {
        return FragmentNewSubscriptionPacksBinding.inflate(inflater)
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
                NewSubscriptionPacksFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.terms -> {
                val intent = Intent(activity!!, WebViewActivity::class.java)
                intent.putExtra(AppLevelConstants.WEBVIEW, AppLevelConstants.TNC)
                startActivity(intent)
            }
        }
    }

    override fun onPackageClicked(position: Int, packDetails: PackDetail, activePlan: String?) {
        if (UserInfo.getInstance(context).isActive) {
            cardClickedCallback.onCardClicked(packDetails.skuDetails?.sku, packDetailList[position].productsResponseMessageItem.serviceType, activePlan)
        } else {
            ActivityLauncher(activity!!).astrLoginActivity(activity!!, AstrLoginActivity::class.java, "")
        }
    }
}