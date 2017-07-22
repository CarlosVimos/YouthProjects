package com.karol.vimos.vimosapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karol.vimos.vimosapp.ui.activities.PreferenceRedChatUserActivity;
import com.karol.vimos.vimosapp.R;
import com.karol.vimos.vimosapp.core.detection.DetectionContract;
import com.karol.vimos.vimosapp.core.detection.DetectionPresenter;
import com.karol.vimos.vimosapp.core.lector.LectorContract;
import com.karol.vimos.vimosapp.core.lector.LectorPresenter;
import com.karol.vimos.vimosapp.core.logout.LogoutContract;
import com.karol.vimos.vimosapp.core.logout.LogoutPresenter;
import com.karol.vimos.vimosapp.core.settings.get.radius.GetRadiusContract;
import com.karol.vimos.vimosapp.core.settings.get.radius.GetRadiusPresenter;
import com.karol.vimos.vimosapp.core.settings.set.radius.SetRadiusContract;
import com.karol.vimos.vimosapp.core.settings.set.radius.SetRadiusPresenter;
import com.karol.vimos.vimosapp.models.User;
import com.karol.vimos.vimosapp.ui.activities.ChangeEmailActivity;
import com.karol.vimos.vimosapp.ui.activities.ChangePasswordActivity;
import com.karol.vimos.vimosapp.ui.activities.LoginActivity;
import com.karol.vimos.vimosapp.ui.activities.RemoveUserActivity;
import com.karol.vimos.vimosapp.utils.Constants;
import com.karol.vimos.vimosapp.utils.SharedPrefUtil;

/**
 * Created by Vimos on 15/02/17.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, SetRadiusContract.View, GetRadiusContract.View, LogoutContract.View,
        DetectionContract.View, LectorContract.View {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    SharedPrefUtil sharedPrefUtil;
    int radius;
    private ProgressDialog mProgressDialog;

    private SetRadiusPresenter mSetRadiusPresenter;

    private LogoutPresenter mLogoutPresenter;

    private DetectionPresenter mDetectionPresenter;
    private LectorPresenter mLectorPresenter;

    private ImageView ivRed, ivGreen, ivOrange;
    private String redOn, redOff, greenOn, greenOff, orangeOn, orangeOff, meters, en, pl, userProfession, userName, userNick;

    private TextView mTVseekBarValue, mTVredSwitchText, mTVgreenSwitchText, mTVorangeSwitchText, tvUserName, tvUserNick, tvUserProfession;
    private EditText mETradius;
    private SeekBar mSeekBarSetRadius;
    private int minSeekBarRadiusValue = 100;

    private GetRadiusPresenter mGetRadiusPresenter;
    private Button toRemoveUserActivity, toChangePasswordActivity, toChangeEmailActivity, btnLogout, btnSetHobby;

    int red_detection = 1;
    int green_detection = 1;
    int orange_detection = 1;

    Spinner spinnerLector;

    String lectorFromDB;

    ArrayAdapter<CharSequence> adapterLector;

    int r_d, g_d, o_d;






    private CheckBox redCheck, greenCheck, orangeCheck;

    private Switch redSwitch, greenSwitch, orangeSwitch;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        init();

        mSeekBarSetRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // it causes that values in seekbar change for every 10 points
                progress = progress / 10;
                progress = progress * 10;


                if (progress < minSeekBarRadiusValue) {

                    progress = minSeekBarRadiusValue;
                    seekBar.setProgress(progress);
                } else {

                    seekBar.setProgress(progress);

                }

                mSetRadiusPresenter.setRadius(getContext(), firebaseUser, progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.btnRemoveUserId:
                goToRemoveUserActivity();
                break;

            case R.id.btnGoToChangePasswordId:
                goToChangePasswordActivity();
                break;

            case R.id.btnGoToChangeEmailId:
                goToChangeEmailActivity();
                break;

            case R.id.btnLogoutId:
                onLogout();
                break;

            case R.id.btnSetHobbyId:
                goToSetHobbyActivity();
                break;


        }


    }

    private void bindViews(View view) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
       // mETradius = (EditText) view.findViewById(R.id.)

        setString();

        ivRed = (ImageView) view.findViewById(R.id.iv_settings_switch_red_id);
        ivGreen = (ImageView) view.findViewById(R.id.iv_settings_switch_green_id);
        ivOrange = (ImageView) view.findViewById(R.id.iv_settings_switch_orange_id);


        mTVseekBarValue = (TextView) view.findViewById(R.id.tvSeekbarValueId);
        mTVredSwitchText = (TextView) view.findViewById(R.id.tv_switch_red_text_id);
        mTVgreenSwitchText = (TextView) view.findViewById(R.id.tv_switch_green_text_id);
        mTVorangeSwitchText = (TextView) view.findViewById(R.id.tv_switch_orange_text_id);

        tvUserName = (TextView) view.findViewById(R.id.tv_settings_user_name_id);
        tvUserNick = (TextView) view.findViewById(R.id.tv_settings_user_nick_id);
        tvUserProfession = (TextView) view.findViewById(R.id.tv_settings_user_profession_id);

        toRemoveUserActivity = (Button) view.findViewById(R.id.btnRemoveUserId);
        toChangePasswordActivity = (Button) view.findViewById(R.id.btnGoToChangePasswordId);
        toChangeEmailActivity = (Button) view.findViewById(R.id.btnGoToChangeEmailId);
        btnLogout = (Button) view.findViewById(R.id.btnLogoutId);
        btnSetHobby = (Button) view.findViewById(R.id.btnSetHobbyId);

        redSwitch = (Switch) view.findViewById(R.id.switchRed);
        greenSwitch = (Switch) view.findViewById(R.id.switchGreen);
        orangeSwitch = (Switch) view.findViewById(R.id.switchOrange);

        spinnerLector = (Spinner) view.findViewById(R.id.spinnerLectorId);

     //   Toast.makeText(getContext(), String.valueOf(r_d), Toast.LENGTH_SHORT).show();

        final String uid = firebaseUser.getUid();
        setRedSwitch();
        setGreenSwitch();
        setOrangeSwitch();


        adapterLector = ArrayAdapter.createFromResource(getContext(), R.array.lector_names, android.R.layout.simple_spinner_item);
        adapterLector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLector.setAdapter(adapterLector);

        getLectorFromDatabase(); // set spinner from database -> lector // which is chose
        getUserInformationFromDB(); // set user: name, nick, profession

        spinnerLector.setSelection(0, false); // it causes that after reopen an app, the is showing correct lector from db


        spinnerLector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (String.valueOf(parent.getItemAtPosition(position)).equals("Polski")) {

                    Toast.makeText(getContext(), "pl", Toast.LENGTH_SHORT).show();
                    mLectorPresenter.Lector(getContext(), firebaseUser, "pl");
                  //  sharedPrefUtil.saveInt(pl, 5);

                } else if (String.valueOf(parent.getItemAtPosition(position)).equals("Angielski")) {
                    Toast.makeText(getContext(), "en", Toast.LENGTH_SHORT).show();
                    mLectorPresenter.Lector(getContext(), firebaseUser, "en");
                   // sharedPrefUtil.saveInt(en, 6);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mSeekBarSetRadius = (SeekBar) view.findViewById(R.id.seekBarRadiusId);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);


    }

    private void init() {

        toRemoveUserActivity.setOnClickListener(this);
        toChangePasswordActivity.setOnClickListener(this);
        toChangeEmailActivity.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnSetHobby.setOnClickListener(this);

        //spinnerLector.setOnItemClickListener(this);

        //redSwitch.setOnClickListener(this);


        mSeekBarSetRadius.setMax(1000);
        mGetRadiusPresenter = new GetRadiusPresenter(this);
        getRadiusFromDB();
        getDetectionFromDatabase();
       // getLectorFromDatabase();
       // mTVseekBarValue.setText(String.valueOf(radius));
        mSetRadiusPresenter = new SetRadiusPresenter(this);
        mLogoutPresenter = new LogoutPresenter(this);
        mDetectionPresenter = new DetectionPresenter(this);
        mLectorPresenter = new LectorPresenter(this);

    }

    private void getLectorFromDatabase() {

        String userId = firebaseUser.getUid();

        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);
                String lec = user.getLector();
                lectorFromDB = lec;


                if (lectorFromDB.equals("pl")) {

                    spinnerLector.setSelection(0);

                } else if (lectorFromDB.equals("en")) {

                    spinnerLector.setSelection(1);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }

    private void getRadiusFromDB() {


        String userId = firebaseUser.getUid();

        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);
                int r = user.getRadius();
                radius = r;
                mTVseekBarValue.setText(String.valueOf(radius) + " " + meters);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        //    mTVseekBarValue.setText(String.valueOf(radius));


    }

    private void getUserInformationFromDB() {

        String userId = firebaseUser.getUid();

        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);
               // String strProfession = user.getProfession();
//                profession = strProfession;
                userProfession = user.getProfession();
                userName = user.getName();
                userNick = user.getNick();

                if (userProfession != null) {

                    tvUserProfession.setText(userProfession);
                }

                if (userName != null) {

                    tvUserName.setText(userName);
                }

                if (userNick != null) {

                    tvUserNick.setText(userNick);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }

    private void getDetectionFromDatabase() {

        String userId = firebaseUser.getUid();

        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(userId).child(Constants.ARG_DETECTION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);



                int red = user.getRed_detection();
                int green = user.getGreen_detection();
                int orange = user.getOrange_detection();

                if (red == 1) {

                    redSwitch.setChecked(true);
                    mTVredSwitchText.setText(redOn);
                    ivRed.setImageResource(R.drawable.ic_green_mark_on);

                } else {
                    mTVredSwitchText.setText(redOff);
                    ivRed.setImageResource(R.drawable.ic_mark_off_red);
                }

                if (green == 1) {

                    greenSwitch.setChecked(true);
                    mTVgreenSwitchText.setText(greenOn);
                    ivGreen.setImageResource(R.drawable.ic_green_mark_on);

                } else {
                    mTVgreenSwitchText.setText(greenOff);
                    ivGreen.setImageResource(R.drawable.ic_mark_off_red);
                }

                if (orange == 1) {

                    orangeSwitch.setChecked(true);
                    mTVorangeSwitchText.setText(orangeOn);
                    ivOrange.setImageResource(R.drawable.ic_green_mark_on);

                } else {
                    mTVorangeSwitchText.setText(orangeOff);
                    ivOrange.setImageResource(R.drawable.ic_mark_off_red);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }






    private void goToRemoveUserActivity() {

        startActivity(new Intent(getActivity(), RemoveUserActivity.class));

    }

    private void onLogout() {

        mLogoutPresenter.logout();

    }



    private void goToSetHobbyActivity() {

        startActivity(new Intent(getActivity(), PreferenceRedChatUserActivity.class));

    }

    private void goToChangePasswordActivity() {
        startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
    }

    private void goToChangeEmailActivity() {
        startActivity(new Intent(getActivity(), ChangeEmailActivity.class));
    }

    private void setRedSwitch() {

        redSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    red_detection = 1;
                    mDetectionPresenter.RedDetection(getContext(), firebaseUser, red_detection);

                    //Toast.makeText(getContext(), "RED SWITCH ON", Toast.LENGTH_SHORT).show();
                    mTVredSwitchText.setText(redOn);
                    ivRed.setImageResource(R.drawable.ic_green_mark_on);

                } else {

                    //   Toast.makeText(getContext(), "RED SWITCH OFF", Toast.LENGTH_SHORT).show();
                    red_detection = 0;
                    mDetectionPresenter.RedDetection(getContext(), firebaseUser, red_detection);

                    mTVredSwitchText.setText(redOff);
                    ivRed.setImageResource(R.drawable.ic_mark_off_red);

                }


            }
        });

    }



    private void setGreenSwitch() {

        greenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    green_detection = 1;
                    mDetectionPresenter.GreenDetection(getContext(), firebaseUser, green_detection);

                    mTVgreenSwitchText.setText(greenOn);
                    ivGreen.setImageResource(R.drawable.ic_green_mark_on);


                } else {

                    green_detection = 0;
                    mDetectionPresenter.GreenDetection(getContext(), firebaseUser, green_detection);

                    mTVgreenSwitchText.setText(greenOff);
                    ivGreen.setImageResource(R.drawable.ic_mark_off_red);

                }

            }
        });

    }

    private void setOrangeSwitch() {

        orangeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    orange_detection = 1;
                    mDetectionPresenter.OrangeDetection(getContext(), firebaseUser, orange_detection);

                    mTVorangeSwitchText.setText(orangeOn);
                    ivOrange.setImageResource(R.drawable.ic_green_mark_on);

                } else {

                    orange_detection = 0;
                    mDetectionPresenter.OrangeDetection(getContext(), firebaseUser, orange_detection);

                    mTVorangeSwitchText.setText(orangeOff);
                    ivOrange.setImageResource(R.drawable.ic_mark_off_red);

                }

            }
        });


    }


    @Override
    public void onSetRadiusSuccess(String message) {

    }

    @Override
    public void onSetRadiusFailure(String message) {

    }

    @Override
    public void onGetRadiusSuccess(String message) {

    }

    @Override
    public void onGetRadiusFailure(String message) {
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLogoutSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        LoginActivity.startIntent(getContext(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onLogoutFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDetectionSuccess(String message) {
//        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetectionFailure(String message) {
       // Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onLectorSuccess(String message) {

    }

    @Override
    public void onLectorFailure(String message) {

    }

    private void setString() {

        redOn = getString(R.string.red_on);
        redOff = getString(R.string.red_off);
        greenOn = getString(R.string.green_on);
        greenOff = getString(R.string.green_off);
        orangeOn = getString(R.string.orange_on);
        orangeOff = getString(R.string.orange_off);

        meters = getString(R.string.tv_meters);

        en = "en";
        pl = "pl";

    }
}
