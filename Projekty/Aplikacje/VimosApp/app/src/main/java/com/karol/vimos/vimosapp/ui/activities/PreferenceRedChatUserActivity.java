package com.karol.vimos.vimosapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.chat_preferences.add.hobby.AddHobbyContract;
import com.karol.vimos.vimosapp.core.chat_preferences.add.hobby.AddHobbyPresenter;
import com.karol.vimos.vimosapp.utils.ToastUtil;

public class PreferenceRedChatUserActivity extends AppCompatActivity implements AddHobbyContract.View {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String profession;
    private EditText mEThobby;
    private Button btnAddHobby; // as a profession in database
    String strAdded1, strAdded2, strProfession;
    FirebaseAnalytics firebaseAnalytics;

    private AddHobbyPresenter mAddHobbyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_red_chat_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setString();

        mAddHobbyPresenter = new AddHobbyPresenter(this);

        mEThobby = (EditText) findViewById(R.id.et_sett_red_chat_hobby_id);
        btnAddHobby = (Button) findViewById(R.id.btn_sett_red_chat_addhobby_id);

    }

    @Override
    public void onAddHobbySuccess(String message) {

        if (profession != null) {
            ToastUtil.shortToast(getApplicationContext(), profession + " " + strAdded1);
        } else {
            ToastUtil.shortToast(getApplicationContext(), strAdded2);
        }
        startActivity(new Intent(PreferenceRedChatUserActivity.this, SettingsActivity.class));

    }

    @Override
    public void onAddHobbyFailure(String message) {

    }

    // in a database it's written as "profession".
    private void onAddHobby() {

        profession = mEThobby.getText().toString().trim();

        if (!profession.equals("")) {

            if (TextUtils.isEmpty(profession)) {

                ToastUtil.shortToast(getApplicationContext(), strProfession);

            }


            mAddHobbyPresenter.addHobby(getApplicationContext(), firebaseUser, profession);
            firebaseAnalytics.setUserProperty("profession", profession);

        } else {
            ToastUtil.shortToast(getApplicationContext(), strProfession);
        }

    }


    public void addHobbyOnClick(View view) {

        onAddHobby();

    }

    private void setString() {

        strAdded1 = getString(R.string.str_added1);
        strAdded2 = getString(R.string.str_added2);
        strProfession = getString(R.string.str_profession);

    }
}
