package com.astro.sott.fragments.subscription.adapter

import android.app.Activity
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.astro.sott.R
import com.astro.sott.databinding.FragmentPackageBinding
import com.astro.sott.fragments.subscription.adapter.SubscriptionPagerAdapter.OnPackageChooseClickListener
import com.astro.sott.modelClasses.InApp.PackDetail
import com.astro.sott.usermanagment.modelClasses.getProducts.Attribute
import com.astro.sott.utils.helpers.carousel.SliderPotrait
import com.astro.sott.utils.userInfo.UserInfo
import java.util.*

class SubscriptionRecyclerViewAdapter(
    private val activity: Activity,
    private val packagesList: ArrayList<PackDetail>,
    private val productList: ArrayList<String>,
    private val onPackageChooseClickListener: OnPackageChooseClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var activePlan: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: FragmentPackageBinding = DataBindingUtil.bind<FragmentPackageBinding>(
            layoutInflater.inflate(
                R.layout.fragment_package,
                parent,
                false
            )
        )!!
        binding.executePendingBindings()
        return HeaderHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (holder is HeaderHolder) {
                val bannerBinding = holder.packageBinding
                val packageModel = packagesList[position].productsResponseMessageItem
                val skuModel = packagesList[position].skuDetails
                if (packageModel.isFreemium != null && !packageModel.isFreemium!!) {
                    bannerBinding.text.visibility = View.INVISIBLE
                    bannerBinding.text.setPadding(0, 0, 0, 0)
                } else {
                    bannerBinding.text.visibility = View.VISIBLE
                    bannerBinding.text.setPadding(
                        0,
                        SliderPotrait.dp2px(activity, 6f),
                        0,
                        SliderPotrait.dp2px(activity, 6f)
                    )
                }
                if (position == 1) {
                    bannerBinding.btnChooseMe.setTextColor(activity.resources.getColor(R.color.title_color))
                    bannerBinding.text.setTextColor(activity.resources.getColor(R.color.title_color))
                } else {
                    bannerBinding.btnChooseMe.setTextColor(activity.resources.getColor(R.color.black_text_color))
                    bannerBinding.text.setTextColor(activity.resources.getColor(R.color.black_text_color))
                }
                bannerBinding.packagePriceOld.visibility = View.GONE
                val rainbow = activity.resources.getIntArray(R.array.packages_colors)
                val currentColor = rainbow[position % rainbow.size]

                var backgroundDrawable: Drawable = bannerBinding.mainBackground.background
                backgroundDrawable = DrawableCompat.wrap(backgroundDrawable)
                DrawableCompat.setTint(backgroundDrawable, currentColor)
                bannerBinding.mainBackground.background = backgroundDrawable
                bannerBinding.packageTitle.text = packageModel.displayName
                bannerBinding.packageTitle.setTextColor(currentColor)
                Log.w(
                    "priceValues ada",
                    skuModel!!.introductoryPrice + "<--->" + skuModel!!.price + "<----->"
                )
                if (skuModel != null && skuModel.introductoryPricePeriod != null && !skuModel.introductoryPricePeriod.equals(
                        ""
                    )
                ) {
                    if (packageModel.promotions != null && packageModel.promotions?.size!! > 0) {
                        bannerBinding.text.visibility = View.VISIBLE
                        bannerBinding.text.setPadding(
                            0,
                            SliderPotrait.dp2px(activity, 6f),
                            0,
                            SliderPotrait.dp2px(activity, 6f)
                        )
                        bannerBinding.text.text = packageModel.promotions!![0].promoDescrip
                        if (packageModel.promotions!![0].promoCpDescription != null && !packageModel.promotions!![0].promoCpDescription.equals(
                                "",
                                true
                            )
                        ) {
                            bannerBinding.offer.visibility = View.VISIBLE
                            bannerBinding.offer.text =
                                packageModel.promotions!![0].promoCpDescription
                        }
                    }
                    bannerBinding.currency.text = skuModel.priceCurrencyCode
                    bannerBinding.packagePrice.text = skuModel.introductoryPrice + "/"
                    bannerBinding.period.text =
                        packageModel.duration.toString() + packageModel.period
                    bannerBinding.packagePriceOld.text =
                        skuModel.price.toString() + "/" + packageModel.duration + packageModel.period
                    bannerBinding.packagePriceOld.visibility = View.VISIBLE
                    bannerBinding.packagePriceOld.paintFlags =
                        bannerBinding.packagePriceOld.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    bannerBinding.offer.visibility = View.GONE
                    bannerBinding.currency.text = skuModel.priceCurrencyCode
                    bannerBinding.packagePrice.text = skuModel.price + "/"
                    bannerBinding.period.text =
                        packageModel.duration.toString() + packageModel.period
                }
                bannerBinding.packageDescription.text = packageModel.uiDisplayText

                var buttonDrawable: Drawable = bannerBinding.btnChooseMe.background
                buttonDrawable = DrawableCompat.wrap(buttonDrawable)
                DrawableCompat.setTint(buttonDrawable, currentColor)
                bannerBinding.btnChooseMe.background = buttonDrawable
                bannerBinding.masktop.setColorFilter(currentColor)
                bannerBinding.maskbottom.setColorFilter(currentColor)
                var attributeList = packageModel.attributes!!
                Collections.sort(attributeList, object : Comparator<Attribute?> {
                    override fun compare(o1: Attribute?, o2: Attribute?): Int {
                        return o1?.attributeLabel?.compareTo(o2?.attributeLabel!!)!!
                    }
                })
                val my_array = ArrayList<String>()
                if (packageModel.attributes != null) {
                    for (attribute in attributeList)
                        if (!attribute.attributeLabel.equals("ImageURL1", true)) {
                            my_array.add(attribute.attributeValue!!)
                        }
                }
                if (packageModel.skuORQuickCode != null && productList.isNotEmpty()) {
                    if (checkActiveOrNot(packageModel.skuORQuickCode!!)) {
                        bannerBinding.btnChooseMe.background =
                            activity.resources.getDrawable(R.drawable.ic_btn)
                        bannerBinding.btnChooseMe.isEnabled = false
                        activePlan = skuModel.sku
                    } else {
                        bannerBinding.btnChooseMe.text = "Choose Me"
                    }
                } else {
                    bannerBinding.btnChooseMe.text = "Choose Me"
                }

                val adapter: ArrayAdapter<String> = BulletsAdapter(activity, my_array, currentColor)
                bannerBinding.bulletsList.adapter = adapter
                bannerBinding.btnChooseMe.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {


                        onPackageChooseClickListener.onPackageClicked(
                            position,
                            packagesList[position],
                            activePlan,
                            packagesList[position].productsResponseMessageItem.displayName,
                            skuModel.priceAmountMicros
                        )
                    }

                })
            }

        } catch (e: ClassCastException) {
            Log.w("priceValues", e.toString())
            e.printStackTrace()
        }

    }

    private fun checkActiveOrNot(skuORQuickCode: String): Boolean {
        var matched = false
        for (productId in productList) {
            matched = productId.equals(skuORQuickCode, true)
        }
        return matched
    }

    override fun getItemCount(): Int {
        return packagesList.size
    }

    private inner class HeaderHolder(val packageBinding: FragmentPackageBinding) :
        RecyclerView.ViewHolder(packageBinding.root)

}