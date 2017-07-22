package com.karol.vimos.vimosapp.core.password;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.utils.ToastUtil;

/**
 * Created by Vimos on 17/02/17.
 */

public class ChangePasswordInteractor implements ChangePasswordContract.Interactor  {

    private ChangePasswordContract.OnChangePasswordListener mOnChangePasswordListener;

    public ChangePasswordInteractor (ChangePasswordContract.OnChangePasswordListener onChangePasswordListener) {
        this.mOnChangePasswordListener = onChangePasswordListener;
    }


    @Override
    public void performChangePassword(final Activity activity, FirebaseUser firebaseUser, final String password) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
       // assert password != null;


        if (!password.equals("")) {

            if (password.length() < 6) {
                ToastUtil.shortToast(activity, "Password is too short.");
            } else {


                firebaseUser.updatePassword(password)
                        .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    ToastUtil.shortToast(activity, "update password...");
                                    mOnChangePasswordListener.onSuccess("Update password completed.");
                                } else {
                                    mOnChangePasswordListener.onFailure("Failed to update passowrd");
                                }


                            }
                        });
            }

        }
            else {

            ToastUtil.shortToast(activity, "Enter a password");
       }



    }

}
