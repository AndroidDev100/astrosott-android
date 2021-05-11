package com.astro.sott.fragments.subscription.adapter

import android.R.color
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.astro.sott.R


class BulletsAdapter(context: Context, bullets: ArrayList<String>, private val currentColor: Int) : ArrayAdapter<String>(context, 0, bullets) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        var convertView: View? = convertView
        val bulletDescription: String? = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.bullet_list_item, parent, false)
        }
        // Lookup view for data population
        val bullet = convertView?.findViewById(R.id.bullet) as TextView
        val bulletDetails = convertView.findViewById(R.id.bullet_detail) as TextView
        // Populate the data into the template view using the data object
        for (drawable in bullet.compoundDrawablesRelative) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(currentColor, PorterDuff.Mode.SRC_IN)
//                drawable.setTint(context.resources.getColor(R.color.yellow_orange))
//                DrawableCompat.setTint(DrawableCompat.wrap(drawable).mutate(),
//                        context.resources.getColor(R.color.yellow_orange))
            }
        }

        bulletDetails.text = bulletDescription
        // Return the completed view to render on screen
        return convertView
    }

}