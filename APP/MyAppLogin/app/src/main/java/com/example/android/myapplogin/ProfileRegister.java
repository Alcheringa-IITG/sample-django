package com.example.android.myapplogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.myapplogin.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileRegister extends Fragment implements View.OnClickListener {


    private EditText Edreg_username;
    private EditText Edreg_password;
    private EditText Edreg_email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_register, container, false);

        Button regBtn = rootView.findViewById(R.id.registration_button);
        Button logBtn = rootView.findViewById(R.id.to_login_button);

        Edreg_username = rootView.findViewById(R.id.reg_username);
        Edreg_password = rootView.findViewById(R.id.reg_password);
        Edreg_email = rootView.findViewById(R.id.reg_email);

        regBtn.setOnClickListener(this);
        logBtn.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registration_button:
                RegButtonClick();
                break;
//            case R.id.to_login_button:
//                LogButtonClick();
//                break;
        }
    }



    public void replaceFragment(Fragment someFragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    public void RegButtonClick()
    {


        String str_reg_username = Edreg_username.getText().toString();
        String str_reg_password = Edreg_password.getText().toString();
        String str_reg_email = Edreg_email.getText().toString();


        User userModel = new User(
                1,
                str_reg_email,
                str_reg_username,
                str_reg_password,
                "86b277b1abd6d9d0e97780efd9376454d683eb77"
        );



        if (!IsEmptyEditTextLogin()){

            if ( InternetUtil.isInternetOnline(getActivity()) ){
                RegisterInServer(userModel);
            }

        }


    }


//    public void LogButtonClick()
//    {
//
//        Fragment fragment = null;
//        fragment = new ProfileLogin();
//        replaceFragment(fragment);
//
//
//    }


    public void RegisterInServer(User userModel) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostApi postApi= retrofit.create(PostApi.class);
        Call<User> call = postApi.registrationUser(userModel);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()){
                    if (response.body() != null) {

                        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefLoginEdit = preferences.edit();
                        prefLoginEdit.putBoolean("registration",true);
                        prefLoginEdit.commit();


//                        Fragment fragment = new ProfileLogin();
//                        replaceFragment(fragment);

                    }
                }else {
                    Log.d("fail", "fail");
                }

            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("fail", "fail");
            }
        });

    }



    private Boolean IsEmptyEditTextLogin(){


        if(Edreg_password.getText().toString().isEmpty() || Edreg_username.getText().toString().isEmpty()|| Edreg_email.getText().toString().isEmpty()){

            Toast toast = Toast.makeText(getActivity(),"Empty EditText", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();


            return true;
        }else{
            return false;
        }

    }



}