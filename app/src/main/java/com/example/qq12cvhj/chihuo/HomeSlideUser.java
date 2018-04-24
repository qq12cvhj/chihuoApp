package com.example.qq12cvhj.chihuo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qq12cvhj on 2018/4/23.
 */

public class HomeSlideUser extends Fragment {
    private List<HotUser> hotUserList;
    private Gson gson;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.home_slide_user,container,false);
        return view;
    }

    @Override
    public void onResume() {
        gson = new Gson();
        hotUserList = getHotUserList();
        HotUserAdapter hotUserAdapter = new HotUserAdapter(getContext(),R.layout.list_hot_user_item,hotUserList);
        ListView listView = (ListView) getActivity().findViewById(R.id.hotUserList);
        listView.setAdapter(hotUserAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotUser hotUser = hotUserList.get(position);
                Intent userHomeIntnet = new Intent(getActivity(),UserHomeActivity.class);
                userHomeIntnet.putExtra("trUserId",hotUser.userid);
                userHomeIntnet.putExtra("trUserNickName",hotUser.nickname);
                toastShow("前往用户 "+hotUser.nickname+" 的主页");
                startActivity(userHomeIntnet);
            }
        });
        super.onResume();

    }

    class HotUser{
        int userid;
        String nickname;
        List<String> imgList;
    }
    class HotUserAdapter extends ArrayAdapter<HotUser>{
        private int resourceId;
        public HotUserAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<HotUser> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            HotUser hotUser = getItem(position);
            TextView hotuserNickname = (TextView)view.findViewById(R.id.hotuserNickname);
            ImageView hotUserimg_1 = (ImageView)view.findViewById(R.id.hotUserimg_1);
            ImageView hotUserimg_2 = (ImageView)view.findViewById(R.id.hotUserimg_2);
            ImageView hotUserimg_3 = (ImageView)view.findViewById(R.id.hotUserimg_3);
            hotuserNickname.setText(hotUser.nickname);
            switch(hotUser.imgList.size()){
                case 0:
                    break;
                case 1:
                    hotUserimg_1.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(hotUser.imgList.get(0))
                            .resize(200,200)
                            .config(Bitmap.Config.RGB_565)
                            .into(hotUserimg_1);
                    break;
                case 2:
                    hotUserimg_1.setVisibility(View.VISIBLE);
                    hotUserimg_2.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(hotUser.imgList.get(0))
                            .resize(200,200)
                            .config(Bitmap.Config.RGB_565)
                            .into(hotUserimg_1);
                    Picasso.get()
                            .load(hotUser.imgList.get(1))
                            .resize(200,200)
                            .config(Bitmap.Config.RGB_565)
                            .into(hotUserimg_2);
                    break;
                case 3:
                    hotUserimg_1.setVisibility(View.VISIBLE);
                    hotUserimg_2.setVisibility(View.VISIBLE);
                    hotUserimg_3.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(hotUser.imgList.get(0))
                            .resize(200,200)
                            .config(Bitmap.Config.RGB_565)
                            .into(hotUserimg_1);
                    Picasso.get()
                            .load(hotUser.imgList.get(1))
                            .resize(200,200)
                            .config(Bitmap.Config.RGB_565)
                            .into(hotUserimg_2);
                    Picasso.get()
                            .load(hotUser.imgList.get(2))
                            .resize(200,200)
                            .config(Bitmap.Config.RGB_565)
                            .into(hotUserimg_3);
                    break;
            }
            return view;
        }
    }
    //从网络端获取热门用户列表
    private List<HotUser> getHotUserList(){
        List<HotUser> userList;
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(commonInfo.httpUrl("getHotUserList"))
                    .build();
            Response response = client.newCall(request).execute();
            String resonseData = response.body().string();
            Log.d("responseData",resonseData);
            userList = gson.fromJson(resonseData,new TypeToken<List<HotUser>>(){}.getType());
        }catch (Exception e){
            userList = new ArrayList<>();
        }
        return userList;
    }
    private void toastShow(String str){
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }

}
