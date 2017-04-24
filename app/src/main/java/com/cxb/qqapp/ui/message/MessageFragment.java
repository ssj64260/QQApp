package com.cxb.qqapp.ui.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cxb.qqapp.R;
import com.cxb.qqapp.adapter.OnListClickListener;
import com.cxb.qqapp.adapter.QQMainAdapter;
import com.cxb.qqapp.model.QQMessageBean;
import com.cxb.qqapp.ui.QQMainActivity;
import com.cxb.qqapp.utils.GlideCircleTransform;
import com.cxb.qqapp.utils.NetworkUtil;
import com.cxb.qqapp.utils.ThreadPoolUtil;
import com.cxb.qqapp.utils.ToastMaster;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 消息
 */

public class MessageFragment extends Fragment {

    private final String[] titles = {"消息", "电话"};

    private View rootView;

    private ImageView ivMainAvatar;//主页头像
    private SegmentTabLayout titleTab;
    private ImageView ivAdd;
    private RelativeLayout rlNetworkWarm;

    private LinearLayout llQQSearch;
    private XRecyclerView recyclerView;
    private List<QQMessageBean> list;
    private QQMainAdapter adapter;

    private String[] QQ_NAME = {};
    private String[] QQ_TIME = {};
    private int[] QQ_AVATAR = {};

    private Handler mHandler;
    private QQMainActivity qqMainActivity;

        BroadcastReceiver checkNetwork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int networkStatus = NetworkUtil.checkNetWorkType(getActivity());

            switch (networkStatus) {
                case NetworkUtil.NETWORK_NONE:
                    rlNetworkWarm.setVisibility(View.VISIBLE);
                    break;
                case NetworkUtil.NETWORK_MOBILE:
                    rlNetworkWarm.setVisibility(View.GONE);
                    break;
                case NetworkUtil.NETWORK_WIFI:
                    rlNetworkWarm.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message, container, false);

            initView();
            initData();
            setData();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            getActivity().registerReceiver(checkNetwork, intentFilter);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        qqMainActivity = (QQMainActivity) context;
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(checkNetwork);
        super.onDestroy();
    }

    private void initData() {
        mHandler = new Handler(Looper.getMainLooper());

        QQ_NAME = new String[]{
                getString(R.string.text_general), getString(R.string.text_fighting), getString(R.string.text_flight),
                getString(R.string.text_poison), getString(R.string.text_ground), getString(R.string.text_rock),
                getString(R.string.text_insect), getString(R.string.text_ghost), getString(R.string.text_steel),
                getString(R.string.text_fire), getString(R.string.text_water), getString(R.string.text_grass),
                getString(R.string.text_electricity), getString(R.string.text_superpower), getString(R.string.text_ice),
                getString(R.string.text_dragon), getString(R.string.text_evil), getString(R.string.text_fairy)
        };
        QQ_TIME = new String[]{
                "23:33", "22:22", "20:00", "16:66", "06:66",
                "00:01", "星期日", "星期一", "星期二", "星期三",
                "星期四", "星期五", "星期六", "2016-12-22", "2016-11-11",
                "2016-02-33", "2015-11-11", "1992-11-24"
        };
        QQ_AVATAR = new int[]{
                R.drawable.shape_bg_general,
                R.drawable.shape_bg_fighting,
                R.drawable.shape_bg_flight,
                R.drawable.shape_bg_poison,
                R.drawable.shape_bg_ground,
                R.drawable.shape_bg_rock,
                R.drawable.shape_bg_insect,
                R.drawable.shape_bg_ghost,
                R.drawable.shape_bg_steel,
                R.drawable.shape_bg_fire,
                R.drawable.shape_bg_water,
                R.drawable.shape_bg_grass,
                R.drawable.shape_bg_electricity,
                R.drawable.shape_bg_superpower,
                R.drawable.shape_bg_ice,
                R.drawable.shape_bg_dragon,
                R.drawable.shape_bg_evil,
                R.drawable.shape_bg_fairy
        };
    }

    private void initView() {
        titleTab = (SegmentTabLayout) rootView.findViewById(R.id.stl_title_tab);
        ivMainAvatar = (ImageView) rootView.findViewById(R.id.iv_main_avatar);
        ivAdd = (ImageView) rootView.findViewById(R.id.iv_qq_add);
        rlNetworkWarm = (RelativeLayout) rootView.findViewById(R.id.rl_network_warning);
    }

    private void setData() {
        Glide.get(getActivity()).clearMemory();
        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                Glide.get(getActivity()).clearDiskCache();
            }
        });

        Glide.with(getActivity()).load(R.drawable.ic_avatar)
                .transform(new GlideCircleTransform(getActivity()))
                .dontAnimate()
                .into(ivMainAvatar);

        titleTab.setTabData(titles);
        titleTab.setOnTabSelectListener(tabSelect);
        ivMainAvatar.setOnClickListener(click);
        ivAdd.setOnClickListener(click);
        rlNetworkWarm.setOnClickListener(click);

        list = new ArrayList<>();
        adapter = new QQMainAdapter(getActivity(), list);
        adapter.setOnListClickListener(listClick);

        View searchView = LayoutInflater.from(getActivity()).inflate(R.layout.include_qq_search, null);
        llQQSearch = (LinearLayout) searchView.findViewById(R.id.ll_qq_search);
        llQQSearch.setOnClickListener(click);

        recyclerView = (XRecyclerView) rootView.findViewById(R.id.xrv_list);
        recyclerView.addHeaderView(searchView);
        recyclerView.setArrowImageView(R.drawable.ic_qq_refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }

            @Override
            public void onLoadMore() {
            }
        });

        for (int i = 0; i < QQ_AVATAR.length; i++) {
            QQMessageBean qq = new QQMessageBean();
            qq.setAvatarRes(QQ_AVATAR[i]);
            qq.setName(QQ_NAME[i]);
            qq.setContent(QQ_NAME[i]);
            qq.setTime(QQ_TIME[i]);
            list.add(qq);
        }
        adapter.notifyDataSetChanged();

    }

    private OnListClickListener listClick = new OnListClickListener() {
        @Override
        public void onItemClick(int position) {
            ToastMaster.toast(list.get(position).getName());
        }

        @Override
        public void onTagClick(int tag, int position) {

        }
    };

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_qq_search:
                    ToastMaster.toast("搜索");
                    break;
                case R.id.iv_main_avatar:
                    qqMainActivity.openMenu();
                    break;
                case R.id.rl_network_warning:
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    break;
                case R.id.iv_qq_add:
                    ToastMaster.toast("添加");
                    break;
            }
        }
    };

    private OnTabSelectListener tabSelect = new OnTabSelectListener() {
        @Override
        public void onTabSelect(int position) {
            setTopTab(position);
        }

        @Override
        public void onTabReselect(int position) {
            // TODO: 2016/11/21 点击当前选中的tab后的操作
        }
    };

    public void setTopTab(int position) {
        if (position == 0) {
            recyclerView.setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.tv_qq_phone).setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.tv_qq_phone).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void doRefresh() {
        ThreadPoolUtil.getInstache().scheduled(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        for (int i = 0; i < QQ_AVATAR.length; i++) {
                            QQMessageBean qq = new QQMessageBean();
                            qq.setAvatarRes(QQ_AVATAR[i]);
                            qq.setName(QQ_NAME[i]);
                            qq.setContent(QQ_NAME[i]);
                            qq.setTime(QQ_TIME[i]);
                            list.add(qq);
                        }
                        adapter.notifyDataSetChanged();
                        recyclerView.refreshComplete();
                    }
                });
            }
        }, 2, TimeUnit.SECONDS);
    }
}
