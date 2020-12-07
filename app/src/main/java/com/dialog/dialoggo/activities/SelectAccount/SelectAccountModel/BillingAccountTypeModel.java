package com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel;

import java.util.List;

public class BillingAccountTypeModel {
    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public List<DetailListItem> getDetailListItems() {
        return detailListItems;
    }

    public void setDetailListItems(List<DetailListItem> detailListItems) {
        this.detailListItems = detailListItems;
    }

    private String viewType;
    private List<DetailListItem> detailListItems;
}
