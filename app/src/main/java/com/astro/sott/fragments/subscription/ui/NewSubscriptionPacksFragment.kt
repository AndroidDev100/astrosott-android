package com.astro.sott.fragments.subscription.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.billingclient.api.SkuDetails
import com.astro.sott.R
import com.astro.sott.activities.home.HomeActivity
import com.astro.sott.activities.search.ui.ActivitySearch
import com.astro.sott.activities.signUp.ui.SignUpActivity
import com.astro.sott.activities.subscriptionActivity.ui.ProfileSubscriptionActivity
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity
import com.astro.sott.activities.webview.ui.WebViewActivity
import com.astro.sott.baseModel.BaseBindingFragment
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack
import com.astro.sott.databinding.FragmentNewSubscriptionPacksBinding
import com.astro.sott.fragments.dialog.CommonDialogFragment
import com.astro.sott.fragments.subscription.adapter.SubscriptionPagerAdapter
import com.astro.sott.fragments.subscription.adapter.SubscriptionRecyclerViewAdapter
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel
import com.astro.sott.modelClasses.InApp.PackDetail
import com.astro.sott.networking.refreshToken.EvergentRefreshToken
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem
import com.astro.sott.utils.PacksDateLayer
import com.astro.sott.utils.billing.SKUsListListener
import com.astro.sott.utils.commonMethods.AppCommonMethods
import com.astro.sott.utils.helpers.ActivityLauncher
import com.astro.sott.utils.helpers.AppLevelConstants
import com.astro.sott.utils.helpers.ToastHandler
import com.astro.sott.utils.helpers.carousel.SliderPotrait
import com.astro.sott.utils.userInfo.UserInfo
import kotlinx.android.synthetic.main.app_toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewSubscriptionPacksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewSubscriptionPacksFragment : BaseBindingFragment<FragmentNewSubscriptionPacksBinding>(),
    View.OnClickListener, SubscriptionPagerAdapter.OnPackageChooseClickListener,
    CommonDialogFragment.EditDialogListener {
    private var productList = ArrayList<String>()
    private var pendingList = ArrayList<String>()
    private lateinit var cardClickedCallback: CardCLickedCallBack
    private var indicatorWidth: Int = 0
    private lateinit var subscriptionViewModel: SubscriptionViewModel
    private var subscriptionIds: Array<String>? = null
    private lateinit var packDetailList: ArrayList<PackDetail>
    private var skuDetails: SkuDetails? = null
    private var accountServiceMessage = ArrayList<AccountServiceMessageItem>()
    private var from = "Profile"


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
        if (requireArguments().getSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY) != null)
            subscriptionIds =
                requireArguments().getSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY) as Array<String>
        binding.toolbar.search_icon.setOnClickListener {
            ActivityLauncher(requireActivity()).searchActivity(requireActivity(), ActivitySearch::class.java)
        }
        if (requireArguments().getString("from", "") != null)
            from = requireArguments().getString("from", "")

        setClicks()
        getActiveSubscription()
    }

    private fun setClicks() {
        binding.terms.setOnClickListener {
            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            intent.putExtra(AppLevelConstants.WEBVIEW, AppLevelConstants.TNC)
            startActivity(intent)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cardClickedCallback = context as CardCLickedCallBack
    }

    private fun getActiveSubscription() {
        activePlan = AccountServiceMessageItem()
        binding.progressLay?.progressHeart?.visibility=View.VISIBLE
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel::class.java)
        subscriptionViewModel.getActiveSubscription(UserInfo.getInstance(activity).accessToken, "")
            .observe(
                this,
                Observer { evergentCommonResponse: EvergentCommonResponse<GetActiveResponse> ->
                    if (evergentCommonResponse.isStatus) {
                        if (evergentCommonResponse.response.getActiveSubscriptionsResponseMessage != null && evergentCommonResponse.response.getActiveSubscriptionsResponseMessage!!.accountServiceMessage != null && evergentCommonResponse.response.getActiveSubscriptionsResponseMessage!!.accountServiceMessage!!.size > 0) {
                            getListofActivePacks(evergentCommonResponse.response.getActiveSubscriptionsResponseMessage!!.accountServiceMessage)
                            getProducts()
                        } else {
                            getProducts()
                        }
                    } else {
                        if (evergentCommonResponse.errorCode.equals(
                                "eV2124",
                                ignoreCase = true
                            ) || evergentCommonResponse.errorCode == "111111111"
                        ) {
                            EvergentRefreshToken.refreshToken(
                                activity,
                                UserInfo.getInstance(activity).refreshToken
                            ).observe(
                                this,
                                Observer { evergentCommonResponse1: EvergentCommonResponse<*>? ->
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

    private var activePlan: AccountServiceMessageItem? = null
    private var isPendingPlan = false
    private fun getListofActivePacks(accountServiceMessage: List<AccountServiceMessageItem?>?) {
        productList = java.util.ArrayList<String>()
        isPendingPlan = false
        for (accountServiceMessageItem in accountServiceMessage!!) {
            if (!accountServiceMessageItem!!.isFreemium!! && accountServiceMessageItem.status.equals(
                    "ACTIVE",
                    true
                )
            ) {
                if (accountServiceMessageItem?.serviceID != null) {
                    activePlan = accountServiceMessageItem
                }
            }

        }
        for (accountServiceMessageItem in accountServiceMessage!!) {
            if (!accountServiceMessageItem!!.isFreemium!!) {
                if (accountServiceMessageItem.status.equals(
                        "PENDING ACTIVE",
                        true
                    ) || accountServiceMessageItem.status.equals(
                        "PENDING",
                        true
                    )
                ) {
                    if (accountServiceMessageItem.serviceType.equals("SVOD", ignoreCase = true)) {
                        isPendingPlan = true
                    }
                }
            }

        }


    }

    private fun getProductsForLogout() {
        subscriptionViewModel.product.observe(
            this,
            androidx.lifecycle.Observer { evergentCommonResponse: EvergentCommonResponse<*> ->
                binding.progressLay?.progressHeart?.visibility = View.GONE
                if (evergentCommonResponse.isStatus) {
                    if (evergentCommonResponse.getProductResponse != null && evergentCommonResponse.getProductResponse.getProductsResponseMessage != null && evergentCommonResponse.getProductResponse.getProductsResponseMessage!!.productsResponseMessage != null && evergentCommonResponse.getProductResponse.getProductsResponseMessage!!.productsResponseMessage!!.size > 0) {
                        productListItem =
                            evergentCommonResponse.getProductResponse.getProductsResponseMessage!!.productsResponseMessage
                        Collections.sort(
                            productListItem,
                            object : Comparator<ProductsResponseMessageItem?> {
                                override fun compare(
                                    o1: ProductsResponseMessageItem?,
                                    o2: ProductsResponseMessageItem?
                                ): Int {
                                    return o1?.displayOrder?.compareTo(o2?.displayOrder!!)!!
                                }
                            })
                        checkIfDetailAvailableOnPlaystore(productListItem)
                    }
                } else {
                    if (evergentCommonResponse.errorCode.equals(
                            "eV2124",
                            ignoreCase = true
                        ) || evergentCommonResponse.errorCode.equals("111111111", ignoreCase = true)
                    ) {
                        activity?.let {
                            EvergentRefreshToken.refreshToken(
                                activity,
                                UserInfo.getInstance(activity).refreshToken
                            ).observe(it, Observer { evergentCommonResponse1 ->
                                if (evergentCommonResponse.isStatus()) {
                                    getProductsForLogout()
                                } else {
                                    AppCommonMethods.removeUserPrerences(activity)
                                    activity?.onBackPressed()
                                }
                            })
                        }
                    } else {

                        ToastHandler.show(
                            evergentCommonResponse.errorMessage,
                            requireActivity()
                        )
                    }
                }
            })
    }

    var productListItem: List<ProductsResponseMessageItem?>? = null
    private fun getProducts() {
        if (!UserInfo.getInstance(activity).isActive) {
            getProductsForLogout()
        } else {
            if (from.equals("Content Detail Page", true)) {
                if (PacksDateLayer.getInstance().packDetailList != null) {
                    packDetailList = PacksDateLayer.getInstance().packDetailList;
                    binding.progressLay?.progressHeart?.visibility = View.GONE
                    loadDataFromModel(PacksDateLayer.getInstance().packDetailList)
                } else {
                    getProductsForLogout()
                }
            } else {
                getProductsForLogout()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (activity != null && activity is HomeActivity) {
            (activity as HomeActivity?)!!.stopProcessor();
        }
    }

    private fun checkIfDetailAvailableOnPlaystore(productsResponseMessage: List<ProductsResponseMessageItem?>?) {
        try {
            // packDetailList = java.util.ArrayList()
            val subSkuList = /*ArrayList<String>();*/
                AppCommonMethods.getSubscriptionSKUs(productsResponseMessage, activity)
            val productsSkuList =
                ArrayList<String>();//AppCommonMethods.getProductSKUs(productsResponseMessage, activity)
            if (activity is ProfileSubscriptionActivity) {

                (activity as ProfileSubscriptionActivity?)!!.onListOfSKUs(
                    subSkuList,
                    productsSkuList,
                    SKUsListListener {
                        packDetailList = ArrayList<PackDetail>()
                        if (productsResponseMessage != null) {
                            for (responseMessageItem in productsResponseMessage) {
                                if (responseMessageItem?.appChannels != null && responseMessageItem?.appChannels!![0] != null && responseMessageItem?.appChannels!![0]!!.appChannel != null && responseMessageItem?.appChannels!![0]!!.appChannel.equals(
                                        "Google Wallet",
                                        ignoreCase = true
                                    ) && responseMessageItem?.appChannels!![0]!!.appID != null
                                ) {
                                    Log.w("avname", requireActivity().javaClass.getName() + "")
                                    if (activity is ProfileSubscriptionActivity) {
                                        skuDetails =
                                            (activity as ProfileSubscriptionActivity?)!!.getSubscriptionDetail(
                                                responseMessageItem?.appChannels!![0]!!.appID
                                            )
                                    } else if (activity is SubscriptionDetailActivity) {
                                        if (responseMessageItem.serviceType.equals(
                                                "ppv",
                                                ignoreCase = true
                                            )
                                        ) {
                                            skuDetails =
                                                (activity as SubscriptionDetailActivity?)!!.getPurchaseDetail(
                                                    responseMessageItem?.appChannels!![0]!!.appID
                                                )
                                        } else {
                                            skuDetails =
                                                (activity as SubscriptionDetailActivity?)!!.getSubscriptionDetail(
                                                    responseMessageItem?.appChannels!![0]!!.appID
                                                )
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
                (activity as SubscriptionDetailActivity?)!!.onListOfSKUs(
                    subSkuList,
                    productsSkuList,
                    SKUsListListener {
                        packDetailList = ArrayList<PackDetail>()
                        if (productsResponseMessage != null) {
                            for (responseMessageItem in productsResponseMessage) {
                                if (responseMessageItem?.appChannels != null && responseMessageItem?.appChannels!![0] != null && responseMessageItem?.appChannels!![0]!!.appChannel != null && responseMessageItem?.appChannels!![0]!!.appChannel.equals(
                                        "Google Wallet",
                                        ignoreCase = true
                                    ) && responseMessageItem?.appChannels!![0]!!.appID != null
                                ) {
                                    Log.w("avname", requireActivity().javaClass.getName() + "")
                                    if (activity is HomeActivity) {
                                        skuDetails =
                                            (activity as HomeActivity?)!!.getSubscriptionDetail(
                                                responseMessageItem?.appChannels!![0]!!.appID
                                            )
                                    } else if (activity is SubscriptionDetailActivity) {
                                        if (responseMessageItem.serviceType.equals(
                                                "ppv",
                                                ignoreCase = true
                                            )
                                        ) {
                                            skuDetails =
                                                (activity as SubscriptionDetailActivity?)!!.getPurchaseDetail(
                                                    responseMessageItem?.appChannels!![0]!!.appID
                                                )
                                        } else {
                                            skuDetails =
                                                (activity as SubscriptionDetailActivity?)!!.getSubscriptionDetail(
                                                    responseMessageItem?.appChannels!![0]!!.appID
                                                )
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
        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /* if(activity!=null)
             activity?.onBackPressed()*/
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
        binding.packagesRecyclerView?.setLayoutManager(
            LinearLayoutManager(
                activity,
                RecyclerView.HORIZONTAL,
                false
            )
        )
        binding.packagesRecyclerView?.adapter = SubscriptionRecyclerViewAdapter(
            requireActivity(),
            (packagesList as java.util.ArrayList<PackDetail>),
            productList,
            this
        )
    }

    private fun setViewPager(packagesList: List<PackDetail>) {
        binding.viewPager?.adapter =
            SubscriptionPagerAdapter(requireActivity(), packagesList, activePlan!!, this)
        binding.viewPager?.setPadding(
            SliderPotrait.dp2px(requireActivity(), 32f),
            0,
            SliderPotrait.dp2px(requireActivity(), 32f),
            0
        );
        binding.viewPager?.clipChildren = false
        binding.viewPager?.clipToPadding = false
        binding.viewPager?.pageMargin = SliderPotrait.dp2px(requireActivity(), 16f);
        val rainbow = resources.getIntArray(R.array.packages_colors)
        binding.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                val currentColor = rainbow[position % rainbow.size]
                binding.tabs?.setSelectedTabIndicatorColor(currentColor)
                binding.tabs?.setTabTextColors(resources.getColor(R.color.gray), currentColor)
            }
        })
        binding.tabs?.setupWithViewPager(binding.viewPager);

        binding.tabs?.setSelectedTabIndicatorColor(resources.getColor(R.color.yellow_orange))
        binding.tabs?.setTabTextColors(
            resources.getColor(R.color.gray),
            resources.getColor(R.color.yellow_orange)
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCommonMethods.setProgressBar(binding?.progressLay?.progressHeart)
        binding?.toolbar?.search_icon?.visibility=View.GONE
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

            }
        }
    }

    override fun onPackageClicked(
        position: Int,
        packDetails: PackDetail,
        activePlan: String?,
        planName: String?,
        price: Long?
    ) {
        if (UserInfo.getInstance(context).isActive) {
            if (isPendingPlan) {

                //  PrintLogging.printLog("PurchaseActivity", "Received a pending purchase of SKU: " + purchase.getSku());
                // handle pending purchases, e.g. confirm with users about the pending
                // purchases, prompt them to complete it, etc.


                commonDialog(
                    resources.getString(R.string.payment_progress),
                    resources.getString(R.string.payment_progress_desc),
                    resources.getString(R.string.ok)
                )

            } else {
                cardClickedCallback.onCardClicked(
                    packDetails.skuDetails?.sku,
                    packDetailList[position].productsResponseMessageItem.serviceType,
                    activePlan,
                    planName,
                    price
                )
            }
        } else {
            ActivityLauncher(requireActivity()).signupActivity(
                requireActivity(),
                SignUpActivity::class.java,
                CleverTapManager.SUBSCRIPTION_PAGE
            )
        }
    }

    private fun commonDialog(tiltle: String, description: String, actionBtn: String) {
        val fm: FragmentManager = activity?.supportFragmentManager!!
        val commonDialogFragment =
            CommonDialogFragment.newInstance(tiltle, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT)
    }

    override fun onActionBtnClicked() {

    }


}