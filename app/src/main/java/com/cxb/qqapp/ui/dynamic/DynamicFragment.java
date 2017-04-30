package com.cxb.qqapp.ui.dynamic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxb.qqapp.R;
import com.cxb.qqapp.model.LoginUserInfo;
import com.cxb.qqapp.utils.GlideCircleTransform;
import com.cxb.qqapp.utils.PreferencesUtil;
import com.cxb.qqapp.utils.ThreadPoolUtil;
import com.cxb.qqapp.utils.ToastMaster;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 动态
 */

public class DynamicFragment extends Fragment {

    private View rootView;
    private ImageView ivAvatar;
    private TextView tvMore;

    private LoginUserInfo userInfo = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_dynamic, container, false);

            initView();
            setData();

        }

        return rootView;
    }

    private void initView() {
        ivAvatar = (ImageView) rootView.findViewById(R.id.iv_avatar);
        tvMore = (TextView) rootView.findViewById(R.id.tv_more);
    }

    private void setData() {
        try {
            Gson gson = new Gson();
            String json = PreferencesUtil.getString(PreferencesUtil.FILE_NAME_USER_INFO, PreferencesUtil.KEY_LOGIN_USER_INFO, "");
            userInfo = gson.fromJson(json, LoginUserInfo.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        Glide.get(getActivity()).clearMemory();
        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                Glide.get(getActivity()).clearDiskCache();
            }
        });

        if (userInfo != null) {
            Glide.with(getActivity()).load(userInfo.getAvatar())
                    .transform(new GlideCircleTransform(getActivity()))
                    .dontAnimate()
                    .into(ivAvatar);
        }

        tvMore.setOnClickListener(click);
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_more:
                    ToastMaster.toast("更多");
                    break;
            }
        }
    };

}
