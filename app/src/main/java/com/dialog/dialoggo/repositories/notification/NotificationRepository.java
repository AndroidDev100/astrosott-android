package com.dialog.dialoggo.repositories.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.dialog.dialoggo.callBacks.kalturaCallBacks.NotificationCallback;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.kaltura.client.types.InboxMessage;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.List;

public class NotificationRepository {
    private static NotificationRepository notificationRepository;

    private NotificationRepository() {

    }

    public static NotificationRepository getInstance() {
        if (notificationRepository == null) {
            notificationRepository = new NotificationRepository();
        }

        return notificationRepository;
    }

    public LiveData<List<InboxMessage>> getNotification(Context context) {
        final MutableLiveData<List<InboxMessage>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.getNotification(new NotificationCallback() {
            @Override
            public void getnotification(Boolean status, Response<ListResponse<InboxMessage>> result) {
                if (status) {
                    PrintLogging.printLog(this.getClass(), "", "NotificationLivedata 1-->>" + result.results.getObjects());

                    connection.postValue(result.results.getObjects());
                } else {
                    connection.postValue(null);
                }
            }
        });
        return connection;
    }

    public LiveData<Boolean> getStatus(String id, String status, Context context) {
        final MutableLiveData<Boolean> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.getNotificationStatus(id, status, result -> {
            if (result.isSuccess()) {
                connection.postValue(true);
            }
        });
        return connection;
    }
}
