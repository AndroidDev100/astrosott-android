package com.dialog.dialoggo.activities.videoQuality.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.player.adapter.TrackItem;

import java.util.ArrayList;

public class VideoQualityViewModel extends AndroidViewModel {
    public VideoQualityViewModel(@NonNull Application application) {
        super(application);
    }

    public ArrayList<TrackItem> getQualityList() {
        ArrayList<TrackItem> trackItems = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                trackItems.add(new TrackItem("Auto", "",getApplication().getResources().getString(R.string.auto_description)));
            } else if (i == 1) {
                trackItems.add(new TrackItem("Low", "",getApplication().getResources().getString(R.string.low_description)));
            } else if (i == 2) {
                trackItems.add(new TrackItem("Medium", "",getApplication().getResources().getString(R.string.medium_description)));
            } else {
                trackItems.add(new TrackItem("High", "",getApplication().getResources().getString(R.string.high_description)));
            }

        }
        return trackItems;
    }
}