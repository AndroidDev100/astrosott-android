package com.dialog.dialoggo.baseModel;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {

    @Nullable
    protected BaseBindingActivity getBaseActivity() {
        return (BaseBindingActivity) getActivity();
    }

    public enum FragmentTransactionType {
        ADD, REPLACE, ADD_TO_BACK_STACK_AND_ADD, ADD_TO_BACK_STACK_AND_REPLACE, POP_BACK_STACK_AND_REPLACE, CLEAR_BACK_STACK_AND_REPLACE
    }

}
