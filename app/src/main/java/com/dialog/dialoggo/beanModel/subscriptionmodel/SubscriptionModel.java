package com.dialog.dialoggo.beanModel.subscriptionmodel;

import com.kaltura.client.types.Subscription;

import java.io.Serializable;

public class SubscriptionModel extends Subscription implements Serializable {

    private boolean isSelected;
    private Subscription subscription;
    private Long nextrenewalDate;
    private boolean isRenewableForPurchase;

    public boolean isRenewableForPurchase() {
        return isRenewableForPurchase;
    }

    public void setRenewableForPurchase(boolean renewableForPurchase) {
        isRenewableForPurchase = renewableForPurchase;
    }

    public Long getNextrenewalDate() {
        return nextrenewalDate;
    }

    public void setNextrenewalDate(Long nextrenewalDate) {
        this.nextrenewalDate = nextrenewalDate;
    }

    public SubscriptionModel() {
    }

    public SubscriptionModel(boolean isSelected, Subscription subscription, Long renewalDate, boolean isRenewableForPurchase) {
        this.isSelected = isSelected;
        this.subscription = subscription;
        this.nextrenewalDate = renewalDate;
        this.isRenewableForPurchase = isRenewableForPurchase;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
