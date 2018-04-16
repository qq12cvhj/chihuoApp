package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShareListActivity extends AppCompatActivity {
    private List<ShareInfo> shareInfoList = new ArrayList<>();
    private Gson gson = new Gson();
    int trUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //保证可以在主线程中使用okhttp
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_list);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        trUserId = intent.getIntExtra("userId",-1);
    }

    @Override
    protected void onResume() {
        switch(trUserId){
            case -1:
                toastShow("获取用户参数错误，请重试");
                finish();
                break;
            default:
                /**
                 * 这中间填写由上一页传过来的用户参数，并通过getShareinfoList 取得适配列表
                 * */
                shareInfoList = getShareInfoList(trUserId);
                ShareInfoAdapter shareInfoAdapter = new ShareInfoAdapter
                        (this,R.layout.list_share_item,shareInfoList);
                final ListView shareListView = (ListView) findViewById(R.id.share_list);
                shareListView.setAdapter(shareInfoAdapter);
                shareListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ShareInfo shareInfo = shareInfoList.get(position);
                        Intent shareDetailIntent = new Intent(ShareListActivity.this,ShareDetailActivity.class);
                        shareDetailIntent.putExtra("trShareId",shareInfo.shareId);
                        startActivity(shareDetailIntent);
                        toastShow("你点击了第"+shareInfo.shareId+"个分享");
                    }
                });
                break;
        }
        super.onResume();
    }

    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
    private List<ShareInfo> getShareInfoList(int authorId){
        List<ShareInfo> sharelist = new ArrayList<>();
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(commonInfo.httpUrl("getShareInfoList"+authorId))
                    .build();
            Response response = client.newCall(request).execute();
            String resonseData = response.body().string();
            Log.d("responseData",resonseData);
            sharelist = gson.fromJson(resonseData,new TypeToken<List<ShareInfo>>(){}.getType());
            return sharelist;
        }catch(Exception e){
            e.printStackTrace();
            return sharelist;
        }
    }
}
