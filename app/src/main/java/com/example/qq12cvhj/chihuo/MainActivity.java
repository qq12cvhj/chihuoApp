package com.example.qq12cvhj.chihuo;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    public void initViews(){
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 5;
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
                        container.addView(view);
                        break;
                    //菜谱fragment
                    case 1:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.cookbook_fragment, null, false);
                        container.addView(view);
                        break;
                    //朋友fragment
                    case 2:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.friend_fragment, null, false);
                        container.addView(view);
                        break;
                    //我的fragment
                    case 3:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.me_fragment, null, false);
                        container.addView(view);
                        break;
                    //设置fragment
                    case 4:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.setting_fragment, null, false);
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
                        getResources().getDrawable(R.mipmap.ic_launcher),
                        Color.parseColor("#11EEEE"))
                        .selectedIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                        .title("发现")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.ic_launcher),
                        Color.parseColor("#FDF111"))
                        .selectedIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                        .title("美食")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.ic_launcher),
                        Color.parseColor("#9AFF9A"))
                        .selectedIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                        .title("朋友")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.ic_launcher),
                        Color.parseColor("#00F5FF"))
                        .selectedIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                        .title("自己")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.ic_launcher),
                        Color.parseColor("#9400D3"))
                        .selectedIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                        .title("设置")
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
}

