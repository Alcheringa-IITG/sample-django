package com.example.android.myapplogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.android.myapplogin.model.FbLoginModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FbLogin extends Fragment {
    CallbackManager callbackManager ;
//    TextView txtEmail, txtBirthday, txtFriends;
    ProgressDialog mDialog;
//    ImageView imgAvatar;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_fb_login, container, false);

        callbackManager= CallbackManager.Factory.create();

//        txtBirthday = rootView.findViewById(R.id.txtBirthday);
//        txtEmail = rootView.findViewById(R.id.txtEmail);
//        txtFriends = rootView.findViewById(R.id.txtFriends);
//
//        imgAvatar= rootView.findViewById(R.id.avatar);

        final LoginButton loginButton= rootView.findViewById(R.id.loginbutton);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mDialog = new ProgressDialog(getActivity());
                mDialog.setMessage("Retrieving Data...");
                mDialog.show();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
//                        getData(object);
                        try {
                            save(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,birthday,friends");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

//        if(AccessToken.getCurrentAccessToken()!=null)
//            txtEmail.setText(AccessToken.getCurrentAccessToken().getUserId());

        return rootView;
    }

    public void replaceFragment(Fragment someFragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

//    private void getData(JSONObject object) {
//        try{
//            URL profil_picture = new URL("https://graph.facebook.com/"+ object.getString("id")+ "/picture?width=250&height=250");
//            Picasso.with(getActivity()).load(profil_picture.toString()).into(imgAvatar);
//
//            txtEmail.setText(object.getString("email"));
//            txtBirthday.setText(object.getString("birthday"));
//            txtFriends.setText("Friends: " + object.getJSONObject("friends").getJSONObject("summary").getString("total_count"));
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void save(JSONObject object) throws JSONException {
        FbLoginModel FbModelModel = new FbLoginModel(object.getString("email"), AccessToken.getCurrentAccessToken().getUserId());

        if ( InternetUtil.isInternetOnline(getActivity()) ){
            Log.d("hello", "wassup");
            AddUserServer(FbModelModel);
        }
    }

    private void AddUserServer(FbLoginModel fbModel) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.FB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostApi postApi= retrofit.create(PostApi.class);
        Call<FbLoginModel> call = postApi.addUser(fbModel);

        call.enqueue(new Callback<FbLoginModel>() {
            @Override
            public void onResponse(Call<FbLoginModel> call, Response<FbLoginModel> response) {

                if(response.isSuccessful()){
                    if (response.body() != null) {
                        Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_LONG).show();

                        Fragment fragment = null;
                        fragment = new Home();
                        replaceFragment(fragment);
                    }
                }else {
                    Log.d("fail", "fail");
                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<FbLoginModel> call, Throwable t) {
                Log.d("fail", "fail");
                Toast.makeText(getActivity(), "Login Failed: Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
