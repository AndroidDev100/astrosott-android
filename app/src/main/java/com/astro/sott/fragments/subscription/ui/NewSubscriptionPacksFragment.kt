package com.astro.sott.fragments.subscription.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
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
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel
import com.astro.sott.modelClasses.InApp.PackDetail
import com.astro.sott.networking.refreshToken.EvergentRefreshToken
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem
import com.astro.sott.utils.commonMethods.AppCommonMethods
import com.astro.sott.utils.helpers.ActivityLauncher
import com.astro.sott.utils.helpers.AppLevelConstants
import com.astro.sott.utils.helpers.carousel.SliderPotrait
import com.astro.sott.utils.userInfo.UserInfo
import com.google.gson.Gson
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
    private lateinit var cardClickedCallback: CardCLickedCallBack
    private var indicatorWidth: Int = 0
    private lateinit var subscriptionViewModel: SubscriptionViewModel
    private var subscriptionIds: Array<String>? = null
    private lateinit var packDetailList: ArrayList<PackDetail>
    private var skuDetails: SkuDetails? = null

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
        getProducts()
        binding.toolbar.search_icon.setOnClickListener {
            ActivityLauncher(activity!!).searchActivity(activity!!, ActivitySearch::class.java)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cardClickedCallback = context as CardCLickedCallBack
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
                        } else {
                        }
                    }
                })
            } else {
                getProductsForLogout()
            }
        }
    }

    private fun checkIfDetailAvailableOnPlaystore(productsResponseMessage: List<ProductsResponseMessageItem?>?) {
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
                        packDetailList.add(packDetail)
                    }
                }
            }
            if (packDetailList.size > 0) loadDataFromModel(packDetailList)
        }
    }

    private fun loadDataFromModel(productsResponseMessage: List<PackDetail>) {
        setViewPager(productsResponseMessage);

    }

    private fun setViewPager(packagesList: List<PackDetail>) {
        val productList = arguments!!.getSerializable("productList") as java.util.ArrayList<String>
        binding.viewPager.adapter = SubscriptionPagerAdapter(activity!!, packagesList, productList, this)
        binding.viewPager.setPadding(SliderPotrait.dp2px(activity!!, 32f), 0, SliderPotrait.dp2px(activity!!, 32f), 0);
        binding.viewPager.clipChildren = false
        binding.viewPager.clipToPadding = false
        binding.viewPager.pageMargin = SliderPotrait.dp2px(activity!!, 16f);
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.tabs.setSelectedTabIndicatorColor(resources.getColor(R.color.yellow_orange))
                        binding.tabs.setTabTextColors(resources.getColor(R.color.gray), resources.getColor(R.color.yellow_orange))
                    }
                    1 -> {
                        binding.tabs.setSelectedTabIndicatorColor(resources.getColor(R.color.red_live))
                        binding.tabs.setTabTextColors(resources.getColor(R.color.gray), resources.getColor(R.color.red_live))
                    }
                    2 -> {
                        binding.tabs.setSelectedTabIndicatorColor(resources.getColor(R.color.green))
                        binding.tabs.setTabTextColors(resources.getColor(R.color.gray), resources.getColor(R.color.green))
                    }
                }

            }

        })
        binding.tabs.setupWithViewPager(binding.viewPager);
        binding.terms.setOnClickListener(this)
        binding.tabs.setSelectedTabIndicatorColor(resources.getColor(R.color.yellow_orange))
        binding.tabs.setTabTextColors(resources.getColor(R.color.gray), resources.getColor(R.color.yellow_orange))

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

    override fun onPackageClicked(position: Int, packDetails: PackDetail) {
        if (UserInfo.getInstance(context).isActive) {
            cardClickedCallback.onCardClicked(packDetailList[position].productsResponseMessageItem.appChannels!![0]!!.appID, packDetailList[position].productsResponseMessageItem.serviceType)
        } else {
            ActivityLauncher(activity!!).astrLoginActivity(activity!!, AstrLoginActivity::class.java, "")
        }
    }
}