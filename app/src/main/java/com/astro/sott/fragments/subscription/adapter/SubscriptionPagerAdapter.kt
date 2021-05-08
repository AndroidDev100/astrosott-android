package com.astro.sott.fragments.subscription.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.astro.sott.R
import com.astro.sott.databinding.FragmentPackageBinding
import com.astro.sott.modelClasses.InApp.PackDetail
import com.astro.sott.utils.helpers.carousel.SliderPotrait


class SubscriptionPagerAdapter(private var context: Context, private val packagesList: List<PackDetail>, private val productList: ArrayList<String>, private val onPackageChooseClickListener: OnPackageChooseClickListener) : PagerAdapter() {

    override fun isViewFromObject(view: View, mObject: Any): Boolean {
        return view == mObject

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val bannerBinding: FragmentPackageBinding = DataBindingUtil.bind<FragmentPackageBinding>(inflater.inflate(R.layout.fragment_package, container, false))!! //HomeViewPagerBannerBinding.bind(view);
        bannerBinding.executePendingBindings()
        val packageModel = packagesList[position].productsResponseMessageItem
        val skuModel = packagesList[position].skuDetails
        if (packageModel.isFreemium != null && !packageModel.isFreemium!!) {
            bannerBinding.text.visibility = View.INVISIBLE
            bannerBinding.text.setPadding(0, 0, 0, 0)
        } else {
            bannerBinding.text.visibility = View.VISIBLE
            bannerBinding.text.setPadding(0, SliderPotrait.dp2px(context, 6f), 0, SliderPotrait.dp2px(context, 6f))
        }
        bannerBinding.packagePriceOld.visibility = View.GONE
        bannerBinding.offer.visibility = View.GONE
//        if (position == 1) {
//            bannerBinding.text.visibility = View.VISIBLE
//            bannerBinding.text.setPadding(0, SliderPotrait.dp2px(context, 6f), 0, SliderPotrait.dp2px(context, 6f))
//            bannerBinding.packagePriceOld.visibility = View.VISIBLE
//            bannerBinding.packagePriceOld.paintFlags = bannerBinding.packagePriceOld.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            bannerBinding.offer.visibility = View.VISIBLE
//        }
        val rainbow = context.resources.getIntArray(R.array.packages_colors)
        val currentColor = rainbow[position % rainbow.size]

        var backgroundDrawable: Drawable = bannerBinding.mainBackground.background
        backgroundDrawable = DrawableCompat.wrap(backgroundDrawable)
        DrawableCompat.setTint(backgroundDrawable, currentColor)
        bannerBinding.mainBackground.background = backgroundDrawable
        bannerBinding.packageTitle.text = packageModel.displayName
        bannerBinding.packageTitle.setTextColor(currentColor)
        if (packageModel.duration == 1)
            bannerBinding.packagePrice.text = skuModel.price + "/" + packageModel.period?.replace("(s)", "")
        else
            bannerBinding.packagePrice.text = skuModel.price + "/" + packageModel.duration + " " + packageModel.period?.replace("(s)", "")

        bannerBinding.packageDescription.text = skuModel.description

        var buttonDrawable: Drawable = bannerBinding.btnChooseMe.background
        buttonDrawable = DrawableCompat.wrap(buttonDrawable)
        DrawableCompat.setTint(buttonDrawable, currentColor)
        bannerBinding.btnChooseMe.background = buttonDrawable
        bannerBinding.masktop.setColorFilter(currentColor)
        bannerBinding.maskbottom.setColorFilter(currentColor)

        val my_array = ArrayList<String>()
        my_array.add("First access to live events, sooka exclusives and originals")
        my_array.add("Enjoy it all with zero ads")
        if (packageModel.skuORQuickCode != null && productList.isNotEmpty()) {
            if (checkActiveOrNot(packageModel.skuORQuickCode!!)) {
                bannerBinding.btnChooseMe.text = "Current Plan"
            } else {
                bannerBinding.btnChooseMe.text = "Choose Me"
            }
        } else {
            bannerBinding.btnChooseMe.text = "Choose Me"
        }

        val adapter: ArrayAdapter<String> = BulletsAdapter(context, my_array, currentColor)
        bannerBinding.bulletsList.adapter = adapter
        bannerBinding.btnChooseMe.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onPackageChooseClickListener.onPackageClicked(position, packagesList[position])
            }

        })

        container.addView(bannerBinding.root)
        return bannerBinding.root
    }

    private fun checkActiveOrNot(skuORQuickCode: String): Boolean {
        var matched = false
        for (productId in productList) {
            matched = productId.equals(skuORQuickCode, true)
        }
        return matched
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int {
        return packagesList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return packagesList[position].productsResponseMessageItem.displayName
    }

    interface OnPackageChooseClickListener {
        fun onPackageClicked(position: Int, packDetails: PackDetail)
    }
}