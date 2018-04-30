package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton addFoodtypeBtn;
    FloatingActionButton addFoodBtn ;
    FloatingActionButton addShareBtn;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fm = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initViews();

    }

    @Override
    protected void onResume() {
        addFoodtypeBtn=findViewById(R.id.button_add_new_foodtype);
        addFoodtypeBtn.setIcon(R.drawable.ic_fab_star);
        addFoodtypeBtn.setOnClickListener(this);
        addFoodBtn = findViewById(R.id.button_add_new_food);
        addFoodBtn.setIcon(R.drawable.ic_fab_star);
        addFoodBtn.setOnClickListener(this);
        addShareBtn = findViewById(R.id.button_add_new_share);
        addShareBtn.setIcon(R.drawable.ic_fab_star);
        addShareBtn.setOnClickListener(this);
        super.onResume();
    }

    public void initViews(){
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView((View) object);
            }
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = null;
                switch(position){
                    //主页fragment
                    case 0:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.home_fragment, null, false);
                        HomeContent hc = new HomeContent();
                        ft = fm.beginTransaction();
                        ft.add(R.id.home_fragment,hc);
                        ft.commit();
                        container.addView(view);
                        break;
                    //菜谱fragment
                    case 1:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.cookbook_fragment, null, false);
                        CookbookContent cbc = new CookbookContent();
                        ft = fm.beginTransaction();
                        ft.add(R.id.cookbook_fragment,cbc);
                        ft.commit();
                        container.addView(view);
                        break;
                    //朋友fragment
                    case 2:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.friend_fragment, null, false);
                        FriendContent frc = new FriendContent();
                        ft = fm.beginTransaction();
                        ft.add(R.id.friend_fragment,frc);
                        ft.commit();
                        container.addView(view);
                        break;
                    //我的fragment
                    case 3:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.me_fragment, null, false);
                        MeContent mc = new MeContent();
                        ft = fm.beginTransaction();
                        ft.add(R.id.me_fragment,mc);
                        ft.commit();
                        container.addView(view);
                        break;
                }
                return view;
            }
        });
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

        /**添加五个底部菜单项*/
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.find),
                        Color.parseColor("#BFEFFF"))
                        .selectedIcon(getResources().getDrawable(R.drawable.find_s))
                        .title("发现")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.cookbook),
                        Color.parseColor("#BFEFFF"))
                        .selectedIcon(getResources().getDrawable(R.drawable.cookbook_s))
                        .title("美食")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.friend),
                        Color.parseColor("#BFEFFF"))
                        .selectedIcon(getResources().getDrawable(R.drawable.friend_s))
                        .title("朋友")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.me),
                        Color.parseColor("#BFEFFF"))
                        .selectedIcon(getResources().getDrawable(R.drawable.me_s))
                        .title("我")
                        .build()
        );
        navigationTabBar.setModels(models);
        /**底部切换时的过度时间等设置*/
        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);

        /**暂时先关闭提示功能*/
        navigationTabBar.setIsBadged(false);

        /** 下面两句话先保留，用于底部菜单栏新消息提示用，其中badge为徽章/消息提示*/
        /*navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);*/

        /** page更改时的触发事件，暂时保留*/
        /*navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });*/

        /** 将viewPager和底部菜单栏相连接 */
        navigationTabBar.setViewPager(viewPager, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add_new_share:
                if(commonInfo.loginStatus){
                    Intent newShareIntent;
                    newShareIntent = new Intent(this,NewShareActivity.class);
                    startActivity(newShareIntent);
                }else{
                    toastShow("您还没有登录");
                }
                break;
            case R.id.button_add_new_foodtype:
                if(commonInfo.loginStatus){
                    Intent addFoodTypeIntent;
                    addFoodTypeIntent = new Intent(this,AddNewFoodTypeActivity.class);
                    startActivity(addFoodTypeIntent);
                }else{
                    toastShow("您还没有登录");
                }

                break;
            case R.id.button_add_new_food:
                if(commonInfo.loginStatus){
                    Intent addFoodIntent = new Intent(this,addNewFoodActivity.class);
                    startActivity(addFoodIntent);
                }else{
                    toastShow("您还没有登录");
                }

                break;
        }
    }
    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}

