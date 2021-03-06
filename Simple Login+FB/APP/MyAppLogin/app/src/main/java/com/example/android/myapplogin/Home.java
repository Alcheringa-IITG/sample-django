package com.example.android.myapplogin;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myapplogin.model.PostModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Home extends Fragment {

    private ArrayList<Integer> pkPost = new ArrayList<>();
    private ArrayList<String> namePost = new ArrayList<>();
    private RecyclerView recyclerView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.post_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_category_list);

        if ( InternetUtil.isInternetOnline(getActivity()) ){
            ClearList();
            showAllPosts();
        }


        getActivity().setTitle("All Posts");

        return rootView;


    }

    private void showAllPosts() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostApi postApi= retrofit.create(PostApi.class);
        Call<List<PostModel>> call = postApi.getListPost();

        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {


                if(response.isSuccessful()){

                    if (response.body() != null) {
                        List<PostModel> postList = response.body();

                        for(PostModel h:postList){

                            Integer cat_id = h.getId();
                            pkPost.add(cat_id);

                            String cat_name = h.getTitle();
                            namePost.add(cat_name);


                        }

                        initRecyclerView();

                    }

                }else {
                    Log.d("fail", "fail");
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d("fail", t.getMessage() == null ? "" : t.getMessage());
            }

        });

    }


    private void initRecyclerView(){
        Log.d("Home", "initRecyclerView: init recyclerview.");
        RecyclerHomeList adapter = new RecyclerHomeList(getActivity(), pkPost, namePost);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    public void ClearList()
    {
        pkPost.clear();
        namePost.clear();

        RecyclerHomeList adapter = new RecyclerHomeList(getActivity(), pkPost,  namePost);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }





    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }


}