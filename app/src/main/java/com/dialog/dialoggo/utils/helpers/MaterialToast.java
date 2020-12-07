package com.dialog.dialoggo.utils.helpers;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import androidx.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dialog.dialoggo.R;

/**
 * Created by AnkurYadav on 11/18/2016.
 */

public class MaterialToast {

    private static MaterialToast materialToast;

    private MaterialToast() {

    }

    public static MaterialToast getInstance() {
        if (materialToast == null) {
            materialToast = new MaterialToast();
        }
        return materialToast;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void show(String message, Drawable drawable, int color, int gravity, Context activity) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View viewHolder = View.inflate(activity, R.layout.material_toast, null);

        LinearLayout outerLayout = viewHolder.findViewById(R.id.outerLayout);
        outerLayout.setBackground(drawable);

        TextView toastTitle = viewHolder.findViewById(R.id.toastTitle);
        toastTitle.setText(message);

        //CustomeText.setFontRegular(toastTitle);

        Toast toastObject = new Toast(activity);
        toastObject.setDuration(Toast.LENGTH_SHORT);
        toastObject.setGravity(Gravity.BOTTOM, 0, 140);
        toastObject.setView(viewHolder);
        toastObject.show();

    }
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void display(String message, Drawable drawable, int color, int gravity,Context activity) {
        LayoutInflater layoutInflater = (LayoutInflater)activity.getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View viewHolder = View.inflate(activity, R.layout.material_toast, null);

        LinearLayout outerLayout= viewHolder.findViewById(R.id.outerLayout);
        outerLayout.setBackground(drawable);

        TextView toastTitle= viewHolder.findViewById(R.id.toastTitle);
        toastTitle.setText(message);
        //CustomeText.setFontRegular(toastTitle);

//        Toast toastObject = new Toast(activity);
//        toastObject.setDuration(40000);
//        toastObject.setGravity(Gravity.BOTTOM,0,140);
//        toastObject.setView(viewHolder);
//        toastObject.show();

        int duration = Toast.LENGTH_LONG;
        final Toast toast = Toast.makeText(activity.getApplicationContext(), message, duration);
        toast.show();
        new CountDownTimer(5000, 1000)
        {
            public void onTick(long millisUntilFinished) {
                if (toast.getView().getWindowToken() != null)
                    toast.show();
                else
                    cancel();
            }
            public void onFinish() {
                if (toast.getView().getWindowToken() !=null)
                    toast.show();
                else
                    cancel();
            }
        }.start();

    }

}
