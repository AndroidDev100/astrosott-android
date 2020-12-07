package com.dialog.dialoggo.callBacks.kalturaCallBacks;

import com.kaltura.client.types.InboxMessage;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

public interface NotificationCallback {
    void getnotification(Boolean status, Response<ListResponse<InboxMessage>> result);
}
