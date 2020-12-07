package com.dialog.dialoggo.activities.subscription.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.subscription.adapter.BillPaymentMethodAdapter;
import com.dialog.dialoggo.activities.subscription.callback.SubscriptionActivityCallBack;
import com.dialog.dialoggo.activities.subscription.manager.AllChannelManager;
import com.dialog.dialoggo.activities.subscription.model.BillPaymentMethodModel;
import com.dialog.dialoggo.activities.subscription.model.BillPaymentModel;
import com.dialog.dialoggo.activities.subscription.model.PurchaseResponse;
import com.dialog.dialoggo.activities.subscription.viewmodel.BillPaymentViewModel;
import com.dialog.dialoggo.activities.subscription.viewmodel.InvokeModel;
import com.dialog.dialoggo.baseModel.BaseBindingFragment;
import com.dialog.dialoggo.callBacks.commonCallBacks.BillPaymentMethodCallBack;
import com.dialog.dialoggo.databinding.FragmentBillPaymentBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.kaltura.client.types.HouseholdPaymentMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BillPaymentFragment extends BaseBindingFragment<FragmentBillPaymentBinding> implements BillPaymentMethodCallBack, AlertDialogSingleButtonFragment.AlertDialogListener {

    private SubscriptionActivityCallBack mListener;
    private BillPaymentViewModel viewModel;
    private String accountType = "";
    private String accountNumber = "";
    private boolean isValuedMatched = false;
    private String paymentMethodId = "";
    private String productId;
    private String currency;
    private String price;
    private int count = 0;
    private List<BillPaymentMethodModel> billPaymentMethodModelList = new ArrayList<>();
    String startTime = "01/14/2012 09:29:48";
    String endTime = "";
    private boolean isFirstTimePurchaseCall = true;

    public BillPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public FragmentBillPaymentBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentBillPaymentBinding.inflate(inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SubscriptionActivityCallBack) {
            mListener = (SubscriptionActivityCallBack) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SubscriptionActivityCallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.setToolBarTitle(getString(R.string.bill_payments));
        mListener.showToolBar(true);
        callViewModel();
        checknetworkConnectivity();
    }

    private void checknetworkConnectivity() {
        if (getActivity() == null) {
            return;
        }
        if (NetworkConnectivity.isOnline(getActivity())) {
            getBillAccounts();
        } else {
            showDialog(getString(R.string.no_internet_connection));
        }
    }

    private void getBillAccounts() {
        viewModel.getMbbAccountDetails().observe(this, new Observer<List<BillPaymentModel>>() {
            @Override
            public void onChanged(List<BillPaymentModel> billPaymentModels) {
                if (billPaymentModels != null && billPaymentModels.size() > 0) {
                    BillPaymentMethodAdapter billPaymentMethodAdapter = new BillPaymentMethodAdapter(getActivity(), billPaymentModels, (BillPaymentMethodCallBack) BillPaymentFragment.this);
                    getBinding().paymentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    getBinding().paymentRecyclerView.setAdapter(billPaymentMethodAdapter);

                } else {
                    showDialog(getString(R.string.no_data_found));
                }
            }
        });

//        viewModel.getBillPaymentAccounts().observe(this, new Observer<List<BillPaymentModel>>() {
//            @Override
//            public void onChanged(List<BillPaymentModel> billPaymentModels) {
//                if (billPaymentModels != null && billPaymentModels.size() > 0) {
//                    BillPaymentMethodAdapter billPaymentMethodAdapter = new BillPaymentMethodAdapter(getActivity(), billPaymentModels, (BillPaymentMethodCallBack) BillPaymentFragment.this);
//                    getBinding().paymentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                    getBinding().paymentRecyclerView.setAdapter(billPaymentMethodAdapter);
//
//                } else {
//                    showDialog(getString(R.string.no_data_found));
//                }
//            }
//        });
    }

    private void callViewModel() {
        viewModel = ViewModelProviders.of(this).get(BillPaymentViewModel.class);
    }

    private void setClickListeners() {
        getBinding().btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountType != "" && accountNumber != "") {
                    if (NetworkConnectivity.isOnline(getActivity())) {
                        callHouseHoldListApi();
                    }else {
                        showDialog(getString(R.string.no_internet_connection));
                    }
                }
            }
        });
        //  getBinding().btnContinue.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_billPaymentFragment_to_paymentSuccessFragment,null));
    }

    private void callHouseHoldListApi() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.callHouseHoldList().observe(this, new Observer<List<HouseholdPaymentMethod>>() {
            @Override
            public void onChanged(List<HouseholdPaymentMethod> paymentMethodList) {
                if (paymentMethodList != null) {
                    if (paymentMethodList.size() > 0) {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        isValuedMatched = getresultFromHouseHoldMethod(paymentMethodList);
                        if (isValuedMatched) {
                            callPurchaseApi();
                        } else {
                            callPaymentInvokeApi();
                        }
                    } else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        callPaymentInvokeApi();
                    }
                } else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    callPaymentInvokeApi();
                    // showDialog(getString(R.string.sorry_content_cannot_be_played_now_error_xxx));
                }
            }
        });
    }

    private void callPaymentInvokeApi() {
        count++;
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (count<=2) {
            viewModel.callInvokeApi(accountType, accountNumber).observe(this, new Observer<InvokeModel>() {
                @Override
                public void onChanged(InvokeModel invokeModel) {
                    if (invokeModel != null) {
                        if (invokeModel.isStatus()) {
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                            callHouseHoldListApi();
                        } else {
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                            showDialog(invokeModel.getError());
                        }
                    } else {
                        showDialog(getString(R.string.something_went_wrong_try_again));
                    }
                }
            });
        }else {
            count = 0;
            showDialog(getString(R.string.something_went_wrong_try_again));
        }
    }

    private void callPurchaseApi() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);

        long timeDiff = AssetContent.getTimeDifference(startTime, getEndTime());


        if (timeDiff>5) {
            price = AllChannelManager.getInstance().getPrice();
            currency = AllChannelManager.getInstance().getCurrency();
            productId = AllChannelManager.getInstance().getProductId();
            Date systemDate = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            startTime = sdf.format(systemDate);

            viewModel.callPurchaseApi(paymentMethodId, productId, currency, price).observe(this, new Observer<PurchaseResponse>() {
                @Override
                public void onChanged(PurchaseResponse purchaseResponse) {

                    if (purchaseResponse != null) {
                        if (purchaseResponse.isStatus()) {
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                            if (accountType.equalsIgnoreCase(AppLevelConstants.DIALOG_TV)) {
                                AllChannelManager.getInstance().setPaymentType(AppLevelConstants.DIALOG_TV);
                            } else if (accountType.equalsIgnoreCase(AppLevelConstants.MOBILE)) {
                                AllChannelManager.getInstance().setPaymentType(AppLevelConstants.MOBILE);
                            }
                            AllChannelManager.getInstance().setTransactionId(purchaseResponse.getPaymentGatewayReferenceId());
                            Navigation.findNavController(getView()).navigate(R.id.action_billPaymentFragment_to_paymentSuccessFragment, null);

                        } else {
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                            showDialog(purchaseResponse.getMessage());
                        }
                    }
                }
            });
        }else {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
        }
    }

    private String getEndTime() {
        if (isFirstTimePurchaseCall){
             endTime = "01/14/2012 09:29:58";
            isFirstTimePurchaseCall = false;
        }else {
            Date systemDate = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            endTime = sdf.format(systemDate);
        }
        return endTime;
    }

    private boolean getresultFromHouseHoldMethod(List<HouseholdPaymentMethod> paymentMethodList) {
        boolean isExternalIdMatched = false;
        for (int i = 0; i < paymentMethodList.size(); i++) {
            if (paymentMethodList.get(i).getExternalId().equalsIgnoreCase(accountNumber)) {
                isExternalIdMatched = true;
                paymentMethodId = paymentMethodList.get(i).getId().toString();
                break;
            }
        }
        return isExternalIdMatched;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        billPaymentMethodModelList.clear();
    }

//    @Override
//    public void showButton(boolean show) {
//        if(show){
//           getBinding().btnContinue.setBackgroundResource(R.drawable.rounded_red_button);
//            setClickListeners();
//        }
//    }

    @Override
    public void itemclicked(String type, String value) {
        this.accountType = type;
        this.accountNumber = value;
        getBinding().btnContinue.setBackgroundResource(R.drawable.rounded_red_button);
        setClickListeners();
    }

    private void showDialog(String message) {
        FragmentManager fm = getFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }


    @Override
    public void onFinishDialog() {
        if (getActivity() == null) {
            return;
        }
        getActivity().finish();
    }
}
