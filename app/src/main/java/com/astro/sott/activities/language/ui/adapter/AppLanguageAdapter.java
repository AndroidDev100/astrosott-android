package com.astro.sott.activities.language.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.callBacks.commonCallBacks.ItemClickListener;
import com.astro.sott.callBacks.commonCallBacks.SettingExpendableItemClick;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppLanguageAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private String appLanguage = "";
    private SettingExpendableItemClick itemClickListener;

    private HashMap<String, List<String>> expandableListDetail;

    public AppLanguageAdapter(List<String> headerList, HashMap<String, List<String>> listHashMap, Context context) {
        this.context = context;
        this.expandableListTitle = headerList;
        this.expandableListDetail = listHashMap;
        itemClickListener = (SettingExpendableItemClick) context;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        //    return expandedListPosition;
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_app_language, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.tick);
        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl1);
        if (listPosition == 0) {
            appLanguage = new KsPreferenceKey(context).getAppLangName();

            if (appLanguage.equalsIgnoreCase("ms")) {
                if (expandedListPosition == 0) {
                    imageView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                }
            } else {
                if (expandedListPosition == 1) {
                    imageView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        } else if (listPosition==1){
            if (expandedListPosition ==  new KsPreferenceKey(context).getAudioLanguageIndex()) {
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }

        }else if (listPosition==2){
            if (expandedListPosition ==  new KsPreferenceKey(context).getSubtitleLanguageIndex()) {
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }
        }else {
            imageView.setVisibility(View.GONE);
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listPosition == 0) {
                   /* setAppLanguage(appLanguage);
                    itemClickListener.onClick(0,1);*/

                }else if (listPosition==1){
                    setAudoLanguage(expandedListPosition);
                    itemClickListener.onClick(expandedListPosition,2);
                }
                else if (listPosition==2){
                    setSubtitleLanguage(expandedListPosition);
                    itemClickListener.onClick(expandedListPosition,3);
                }
                notifyDataSetChanged();

            }

        });
//        if (expandedListTextView.getText().toString().equalsIgnoreCase("Watch Movie")) {
//            imageView.setVisibility(View.VISIBLE);
//        } else {
//            imageView.setVisibility(View.GONE);
//
//        }
        return convertView;
    }

    private void setSubtitleLanguage(int expandedListPosition) {
        new KsPreferenceKey(context).setSubtitleLanguageIndex(expandedListPosition);
    }

    private void setAudoLanguage(int expandedListPosition) {
        new KsPreferenceKey(context).setAudioLanguageIndex(expandedListPosition);
    }

    private void setAppLanguage(String appLanguage) {
        if (appLanguage.equalsIgnoreCase("en")) {
            new KsPreferenceKey(context).setAppLangName("ms");
            AppCommonMethods.updateLanguage("ms", context);
        } else {
            new KsPreferenceKey(context).setAppLangName("en");
            AppCommonMethods.updateLanguage("en", context);
        }

    }

    @Override
    public int getChildrenCount(int listPosition) {
        int pos = expandableListDetail.get(expandableListTitle.get(listPosition)).size();
        return pos;
    }

    @Override
    public Object getGroup(int listPosition) {
        return expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.listTitle);
        ImageView listTitleTextView = (ImageView) convertView
                .findViewById(R.id.arrow);

        if (isExpanded) {
            listTitleTextView.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        } else {
            listTitleTextView.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        }

        textView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}