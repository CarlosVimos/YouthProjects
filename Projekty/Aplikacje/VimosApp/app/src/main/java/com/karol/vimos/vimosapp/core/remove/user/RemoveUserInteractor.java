package com.karol.vimos.vimosapp.core.remove.user;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karol.vimos.vimosapp.utils.Constants;
import com.karol.vimos.vimosapp.utils.ToastUtil;

/**
 * Created by Vimos on 17/02/17.
 */

public class RemoveUserInteractor implements RemoveUserContract.Interactor {

    private RemoveUserContract.OnRemoveUserListener mOnRemoveUserListener;
    FirebaseUser firebaseUser1;

    public RemoveUserInteractor(RemoveUserContract.OnRemoveUserListener onRemoveUserListener) {
        this.mOnRemoveUserListener = onRemoveUserListener;
    }

    @Override
    public void performRemoveUser(final Activity activity, FirebaseUser firebaseUser) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        final String userId = firebaseUser.getUid();

//        setDeletedToUserDatabase(userId);

        firebaseUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            ToastUtil.shortToast(activity, "User Deleted Successfully");
                            mOnRemoveUserListener.onSuccess("Deleted.");
                        } else {
                            mOnRemoveUserListener.onFailure(task.getException().getMessage());
                            ToastUtil.shortToast(activity, "User cannot be deleted");
                        }

                    }
                });

    }

    // in: /user/uid/account: deleted.
    private void setDeletedToUserDatabase(String userId) {


        userId = firebaseUser1.getUid();

        DatabaseReference ref_accountDeleted;
        ref_accountDeleted = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS).child(userId).child(Constants.ARG_ACCOUNT);
        ref_accountDeleted.setValue("deleted");


    }
}
