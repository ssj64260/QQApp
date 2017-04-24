package com.cxb.qqapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxb.qqapp.R;
import com.cxb.qqapp.model.TabEntity;
import com.cxb.qqapp.service.QQMessageService;
import com.cxb.qqapp.ui.contacts.ContactsFragment;
import com.cxb.qqapp.ui.dynamic.DynamicFragment;
import com.cxb.qqapp.ui.message.MessageFragment;
import com.cxb.qqapp.utils.DisplayUtil;
import com.cxb.qqapp.utils.GlideCircleTransform;
import com.cxb.qqapp.utils.ThreadPoolUtil;
import com.cxb.qqapp.utils.ToastMaster;
import com.cxb.qqapp.widgets.MySlidingMenu;
import com.cxb.qqapp.widgets.QQLevelLayout;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 侧滑栏测试
 */

public class QQMainActivity extends BaseAppCompatActivity {

    private final int[] iconSelect = {
            R.drawable.ic_qq_message_select,
            R.drawable.ic_qq_contacts_select,
            R.drawable.ic_qq_dynamic_select
    };
    private final int[] iconUnselect = {
            R.drawable.ic_qq_message_unselect,
            R.drawable.ic_qq_contacts_unselect,
            R.drawable.ic_qq_dynamic_unselect
    };

    private MySlidingMenu smMain;
    private View leftView;
    private QQLevelLayout qqLevel;
    private ImageView ivBackground;
    private ImageView ivScan;
    private ImageView ivMenuAvatar;
    private TextView tvMenuName;
    private LinearLayout llSign;
    private TextView tvSign;

    private View rightView;
    private CommonTabLayout tabLayout;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private Fragment[] fragmentList;
    private Fragment currentFragment;

    private final int tabCount = 3;

    private Intent mQQMessageIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_main);

        initView();
        initFragment(savedInstanceState);
        setData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadPoolUtil.getInstache().scheduledShutDown(0);
    }

    private void initView() {
        Glide.get(QQMainActivity.this).clearMemory();
        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                Glide.get(QQMainActivity.this).clearDiskCache();
            }
        });

        smMain = (MySlidingMenu) findViewById(R.id.sm_main);

        initLeftView();
        initRightView();
    }

    private void initLeftView() {
        leftView = findViewById(R.id.include_menu);

        qqLevel = (QQLevelLayout) leftView.findViewById(R.id.qql_qq_level);
        ivBackground = (ImageView) leftView.findViewById(R.id.iv_menu_background);
        ivScan = (ImageView) leftView.findViewById(R.id.iv_menu_scan);
        ivMenuAvatar = (ImageView) leftView.findViewById(R.id.iv_menu_avatar);
        tvMenuName = (TextView) leftView.findViewById(R.id.tv_menu_name);
        llSign = (LinearLayout) leftView.findViewById(R.id.ll_menu_sign);
        tvSign = (TextView) leftView.findViewById(R.id.tv_menu_sign);

        ivBackground.setOnClickListener(leftClick);
        ivScan.setOnClickListener(leftClick);
        llSign.setOnClickListener(leftClick);

        tvMenuName.setText("COKU");
        qqLevel.setLevel(255);

        GlideCircleTransform transform = new GlideCircleTransform(this)
                .setBorderThickness(DisplayUtil.dip2px(this, 3))
                .setColor(255, 255, 255, 1);

        Glide.with(this).load(R.drawable.ic_avatar)
                .centerCrop()
                .transform(transform)
                .dontAnimate()
                .into(ivMenuAvatar);
    }

    private void initRightView() {
        rightView = findViewById(R.id.include_content);
        String[] titles = {
                getString(R.string.tab_qq_message),
                getString(R.string.tab_qq_contacts),
                getString(R.string.tab_qq_dynamic)
        };

        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], iconSelect[i], iconUnselect[i]));
        }

        tabLayout = (CommonTabLayout) rightView.findViewById(R.id.com_tablayout);
        tabLayout.setTabData(mTabEntities);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showFragment(position);

                tabLayout.hideMsg(position);
            }

            @Override
            public void onTabReselect(int position) {
                // TODO: 2016/10/9 设置重复点击事件
            }
        });
    }

    private void initFragment(Bundle savedInstanceState) {
        fragmentList = new Fragment[tabCount];
        if (savedInstanceState != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            List<Fragment> list = getSupportFragmentManager().getFragments();
            if (list != null && list.size() >= 3) {
                for (Fragment fragment : list) {
                    if (fragment instanceof MessageFragment) {
                        fragmentList[0] = fragment;
                    } else if (fragment instanceof ContactsFragment) {
                        fragmentList[1] = fragment;
                    } else if (fragment instanceof DynamicFragment) {
                        fragmentList[2] = fragment;
                    }
                }
            }
            transaction.commit();
        }

        if (fragmentList[0] == null) {
            fragmentList[0] = new MessageFragment();
        }
        if (fragmentList[1] == null) {
            fragmentList[1] = new ContactsFragment();
        }
        if (fragmentList[2] == null) {
            fragmentList[2] = new DynamicFragment();
        }

        showFragment(0);
    }

    private void showFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        if (position < fragmentList.length) {
            currentFragment = fragmentList[position];

            if (!currentFragment.isAdded()) {
                transaction.add(R.id.fl_fragment, currentFragment);
            }
            transaction.show(currentFragment);
        }

        transaction.commit();
    }

    private void setData() {
        mQQMessageIntent = new Intent(QQMainActivity.this, QQMessageService.class);
        startService(mQQMessageIntent);
    }

    public void openMenu() {
        smMain.toggleMenu();
    }

    private View.OnClickListener leftClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_menu_background:
                    ToastMaster.toast("背景");
                    break;
                case R.id.iv_menu_scan:
                    ToastMaster.toast("二维码");
                    break;
                case R.id.ll_menu_sign:
                    ToastMaster.toast("签名");
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (smMain.isOpen()) {
            smMain.toggleMenu();
        } else {
            finish();
        }
    }
}
