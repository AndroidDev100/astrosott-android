package com.dialog.dialoggo.activities.notification.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.repositories.notification.NotificationRepository;
import com.kaltura.client.types.InboxMessage;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {

    public NotificationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<InboxMessage>> getNotification() {
        return NotificationRepository.getInstance().getNotification(getApplication().getApplicationContext());

    }

    public LiveData<Boolean> updatestatus(String id, String status) {
        return NotificationRepository.getInstance().getStatus(id, status, getApplication().getApplicationContext());
    }
}
