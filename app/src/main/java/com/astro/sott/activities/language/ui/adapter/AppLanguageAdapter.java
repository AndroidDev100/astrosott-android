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
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppLanguageAdapter extends BaseExpandableListAdapter {

    private Context context;
//    private List<String> expandableListTitle;
private ArrayList<AudioLanguages> audioLanguageList;

//    private HashMap<String, List<String>> expandableListDetail;

    public AppLanguageAdapter(Context context) {
        this.context = context;
//        this.expandableListTitle = expandableListTitle;
//        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return expandedListPosition;
//        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
//                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
       // final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_app_language, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        //expandedListTextView.setText(expandedListText);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.tick);
        RelativeLayout relativeLayout=(RelativeLayout)convertView.findViewById(R.id.rl1);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getVisibility() == View.VISIBLE){
                    imageView.setVisibility(View.GONE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }

        });
        notifyDataSetChanged();
//        if (expandedListTextView.getText().toString().equalsIgnoreCase("Watch Movie")) {
//            imageView.setVisibility(View.VISIBLE);
//        } else {
//            imageView.setVisibility(View.GONE);
//
//        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 3;
    }

    @Override
    public Object getGroup(int listPosition) {
        return listPosition;
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        ImageView listTitleTextView = (ImageView) convertView
                .findViewById(R.id.arrow);
        TextView textView =(TextView) convertView.findViewById(R.id.expandedListItem);

        if (isExpanded) {
            listTitleTextView.setImageResource(R.drawable.arrow_up);
        } else {
            listTitleTextView.setImageResource(R.drawable.ic_arrow_down_24px);
        }

       // listTitleTextView.setText(listTitle);
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