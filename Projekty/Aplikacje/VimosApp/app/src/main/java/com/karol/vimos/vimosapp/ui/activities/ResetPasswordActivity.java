package com.karol.vimos.vimosapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.utils.ToastUtil;

public class ResetPasswordActivity extends AppCompatActivity {

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        context.startActivity(intent);
    }

    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String userEmail;
    private EditText mETemail;
    private Button btnSendNewPassword; // as a profession in database
    String strInfo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        strInfo1 = getString(R.string.txt_write_email);

        mETemail = (EditText) findViewById(R.id.et_reset_pass_id);
        btnSendNewPassword = (Button) findViewById(R.id.btn_reset_pass_id);


    }

    // it sends new password to user giving e-mail
    private void sendNewPassowordByEmail() {

        userEmail = mETemail.getText().toString().trim();

        if(TextUtils.isEmpty(userEmail)){

            //Toast.makeText(getContext(),"Please enter email",Toast.LENGTH_LONG).show();
            ToastUtil.shortToast(getApplicationContext(), strInfo1);
            return;
        }
        else {

            firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        ToastUtil.shortToast(getApplicationContext(), "Hasło zostało zresetowanie. Jest na Twojej skrzynce pocztowej.");
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                    }
                    else {

                        ToastUtil.shortToast(getApplicationContext(), "Coś poszło nie tak. Spróbuj jeszcze raz.");

                    }

                }
            });

        }


    }

    public void sendResetPassOnClick(View view) {

        sendNewPassowordByEmail();
        //finish();

    }
}
