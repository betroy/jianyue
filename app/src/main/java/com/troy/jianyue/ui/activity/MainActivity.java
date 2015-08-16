package com.troy.jianyue.ui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.baidu.cyberplayer.utils.T;
import com.troy.jianyue.R;
import com.troy.jianyue.adapter.DrawerLayoutAdapter;
import com.troy.jianyue.bean.MenuItem;
import com.troy.jianyue.service.DownLoadService;
import com.troy.jianyue.ui.fragment.PicturePopularFragment;
import com.troy.jianyue.ui.fragment.VideoPopularFragment;
import com.troy.jianyue.ui.fragment.WeiXinPopularFragment;
import com.troy.jianyue.util.CommonUtil;
import com.troy.jianyue.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/4/30.
 */
public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ListView mListView;
    private Resources mResources;
    private DrawerLayoutAdapter mAdapter;
    private long mDownBackTime = 0L;
    private static final long PRESS_BACK_INTERVAL = 2000L;
    private boolean mDrawerOpened = false;
    private FragmentManager mFragmentManager;
    private WeiXinPopularFragment mWeiXinPopularFragment;
    private VideoPopularFragment mVideoPopularFragment;
    private PicturePopularFragment mPicturePopularFragment;
    private int mCurrentItemId = 0;
    private CharSequence[] mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mToolbar.setTitle(mTitles[mCurrentItemId]);
    }

    @Override
    protected void initViews() {
        mResources = getResources();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mListView = (ListView) findViewById(R.id.listview);
        //设置ToolBar
        setupToolBar();
        initFragment();
        addListener();
    }

    //填充DrawerLayout Menus中ListView的数据
    @Override
    protected void initData() {
        List<MenuItem> menus = new ArrayList<MenuItem>();
        CharSequence[] titles = mResources.getStringArray(R.array.item_title);
        Drawable[] drawables = new Drawable[]{mResources.getDrawable(R.mipmap.drawer_text), mResources.getDrawable(R.mipmap.drawer_image), mResources.getDrawable(R.mipmap.drawer_film), mResources.getDrawable(R.mipmap.drawer_settings)};
        Class<? extends Fragment>[] fragments = new Class[]{WeiXinPopularFragment.class, PicturePopularFragment.class, VideoPopularFragment.class, null};
        MenuItem.Type[] types = new MenuItem.Type[]{
                MenuItem.Type.WEIXINPOPULAR_FRAGMENT, MenuItem.Type.PICTUREPOPULAR_FRAGMENT, MenuItem.Type.VIDEOPOPULAR_FRAGMENT, MenuItem.Type.SETTING_ACTIVITY
        };
        for (int i = 0; i < titles.length; i++) {
            MenuItem menuItem = new MenuItem(titles[i], drawables[i], fragments[i], types[i]);
            menus.add(menuItem);
        }
        mTitles = titles;
        mAdapter = new DrawerLayoutAdapter(this, menus);
        mListView.setAdapter(mAdapter);
        mListView.setItemChecked(0, true);
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerOpened = true;
                mToolbar.setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerOpened = false;
                if (mCurrentItemId < 3)
                    mToolbar.setTitle(mTitles[mCurrentItemId]);
            }
        };
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mWeiXinPopularFragment = new WeiXinPopularFragment();
        mVideoPopularFragment = new VideoPopularFragment();
        mPicturePopularFragment = new PicturePopularFragment();
        switchFragment(mWeiXinPopularFragment);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    private void addListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerLayout.closeDrawers();
                MenuItem.Type type = ((MenuItem) mAdapter.getItem(position)).mType;
                if (type == MenuItem.Type.SETTING_ACTIVITY) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                } else {
                    mCurrentItemId = position;
                    Class<? extends Fragment> classFrament =
                            ((MenuItem) mAdapter.getItem(position)).mFragment;
                    try {
                        switchFragment((Fragment) Class.forName(classFrament.getName()).newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        android.view.MenuItem menuItem = menu.findItem(R.id.action_settings);
        Drawable d = getResources().getDrawable(R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
        menuItem.setIcon(d);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerOpened) {
            mDrawerLayout.closeDrawers();
            return;
        }
        if (System.currentTimeMillis() - mDownBackTime > PRESS_BACK_INTERVAL) {
            mDownBackTime = System.currentTimeMillis();
            ToastUtil.show("再按一次返回键退出应用");
        } else {
            finish();
            super.onBackPressed();
        }
    }
}
