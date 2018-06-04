package com.example.qq12cvhj.chihuo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.caimuhao.rxpicker.RxPicker;
import com.caimuhao.rxpicker.bean.ImageItem;
import com.caimuhao.rxpicker.utils.RxPickerImageLoader;
import com.jacksonandroidnetworking.JacksonParserFactory;
import java.io.File;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddNewFoodTypeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView foodTypeInput,descInput;
    private Button addFoodTypeBtn,selCoverBtn;
    private ImageView showCoverImg;
    private TextView coverPath;
    SweetAlertDialog pDialog;
    public class GlideImageLoader implements RxPickerImageLoader {
        @Override public void display(ImageView imageView, String path, int width, int height) {
            Glide.with(imageView.getContext())
                    .load(path)
                    .error(R.drawable.ic_preview_image)
                    .centerCrop()
                    .override(width, height)
                    .into(imageView);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_type);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //初始化图片选择器
        RxPicker.init(new GlideImageLoader());
        //初始化网络工具
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        foodTypeInput = findViewById(R.id.foodTypeInput);
        descInput = findViewById(R.id.descInput);
        addFoodTypeBtn = findViewById(R.id.addFoodTypeBtn);
        addFoodTypeBtn.setOnClickListener(this);
        selCoverBtn = findViewById(R.id.selCoverBtn);
        selCoverBtn.setOnClickListener(this);
        coverPath = findViewById(R.id.coverPath);
        showCoverImg = findViewById(R.id.showCoverImg);
        showCoverImg.setTag(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addFoodTypeBtn:
                if(showCoverImg.getTag().equals(false)){
                    toastShow("请先上传封面");
                }else{
                    addNewFoodType();
                }

                break;
            case R.id.selCoverBtn:
                selCoverImg();
                break;
        }
    }
    private void selCoverImg(){
        RxPicker.of().start(this).subscribe(new Consumer<List<ImageItem>>() {
            @Override public void accept(@NonNull List<ImageItem> imageItems) throws Exception {
                ImageItem imageItem;
                imageItem = imageItems.get(0);
                Toast.makeText(getApplicationContext(), imageItem.getPath(), Toast.LENGTH_SHORT).show();
                File file = new File(imageItem.getPath());
                if (file.exists()) {
                    AndroidNetworking.upload(commonInfo.httpUrl("uploadCoverImg"))
                            .addMultipartFile("coverImg",file)
                            .setPriority(Priority.HIGH)
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long bytesUploaded, long totalBytes) {
                                    // do anything with progress
                                }
                            }).getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("sssss",response);
                                    coverPath.setText(response);
                                    showCoverImg.setTag(true);
                                }
                                @Override
                                public void onError(ANError anError) {
                                }
                            });
                    Bitmap bm = BitmapFactory.decodeFile(imageItem.getPath());
                    showCoverImg.setImageBitmap(bm);
                }
            }
        });
    }
    private void addNewFoodType(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                  .setTitleText("上传中，请稍后");
        if(foodTypeInput.getText().toString().trim().equals("")
                || descInput.getText().toString().trim().equals("") ){
            toastShow("名称和简介不能为空哦！");
        }else{
            pDialog.show();
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    add_send();
                }
            },1000);
        }
    }

    private void add_send(){
        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody
                    .Builder()
                    .add("foodTypeInput",foodTypeInput.getText().toString())
                    .add("foodDescInput",descInput.getText().toString())
                    .add("foodCoverPath",coverPath.getText().toString())
                    .build();
            Request request = new Request
                    .Builder()
                    .url(commonInfo.httpUrl("addNewFoodType"))
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            int responseData = Integer.parseInt(response.body().string());
            switch(responseData){
                case -1:
                    pDialog.setTitleText("创建失败，请重试!")
                            .setConfirmText("好的")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    break;

                case 0:
                    pDialog.setTitleText("创建成功!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                }
                            })
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    break;
            }

        }catch (Exception e){
            e.printStackTrace();
            pDialog.setTitleText("网络超时，请重试")
                    .setConfirmText("好的")
                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        }
    }

    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
