package com.cxb.qqapp.ui.message;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxb.qqapp.R;
import com.cxb.qqapp.db.LiteOrmHelper;
import com.cxb.qqapp.model.LoginUserInfo;
import com.cxb.qqapp.ui.BaseActivity;
import com.cxb.qqapp.ui.QQMainActivity;
import com.cxb.qqapp.utils.GlideCircleTransform;
import com.cxb.qqapp.utils.PreferencesUtil;
import com.cxb.qqapp.utils.ThreadPoolUtil;
import com.cxb.qqapp.utils.ToastMaster;
import com.google.gson.Gson;

import java.util.List;

import static android.view.View.TRANSLATION_Y;

/**
 * QQ登录页面
 */

public class QQLoginActivity extends BaseActivity {

    private ImageView ivLoginAvatar;
    private LinearLayout llContent;
    private EditText etUsername;
    private ImageView ivShowUser;
    private ImageView ivCleanUsername;
    private EditText etPassword;
    private ImageView ivCleanPassword;
    private TextView tvDoLogin;
    private TextView tvForgetPassword;
    private TextView tvRegister;
    private LinearLayout llAgain;
    private ImageView ivAgain;
    private TextView tvTerms;

    private boolean isAgain = true;

    private LiteOrmHelper liteOrmHelper;
    private List<LoginUserInfo> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_login);

        initView();
        setData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        liteOrmHelper.closeDB();
    }

    private void initView() {
        ivLoginAvatar = (ImageView) findViewById(R.id.iv_login_avatar);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        etUsername = (EditText) findViewById(R.id.et_username);
        ivShowUser = (ImageView) findViewById(R.id.iv_show_user);
        ivCleanUsername = (ImageView) findViewById(R.id.iv_clean_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        ivCleanPassword = (ImageView) findViewById(R.id.iv_clean_password);
        tvDoLogin = (TextView) findViewById(R.id.tv_do_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        llAgain = (LinearLayout) findViewById(R.id.ll_again);
        ivAgain = (ImageView) findViewById(R.id.iv_again);
        tvTerms = (TextView) findViewById(R.id.tv_terms);
    }

    private void setData() {
        Glide.get(this).clearMemory();
        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                Glide.get(QQLoginActivity.this).clearDiskCache();
            }
        });

        liteOrmHelper = new LiteOrmHelper(this);
        userList = liteOrmHelper.getQueryAllOrderDescBy(LoginUserInfo.class, "timeStamp");

        ivShowUser.setOnClickListener(click);
        ivCleanUsername.setOnClickListener(click);
        ivCleanPassword.setOnClickListener(click);
        tvDoLogin.setOnClickListener(click);
        tvForgetPassword.setOnClickListener(click);
        tvRegister.setOnClickListener(click);
        llAgain.setOnClickListener(click);
        tvTerms.setOnClickListener(click);

        etUsername.addTextChangedListener(usernameWatcher);
        etPassword.addTextChangedListener(passwordWatcher);

        etUsername.setOnFocusChangeListener(focusChange);
        etPassword.setOnFocusChangeListener(focusChange);

        ivAgain.setSelected(isAgain);

        llContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                float contentY = llContent.getY();
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator translationY = ObjectAnimator.ofFloat(llContent, TRANSLATION_Y, -contentY);
                translationY.setInterpolator(new DecelerateInterpolator(1.5f));
                translationY.setDuration(300);
                animatorSet.play(translationY);
                animatorSet.start();

                if (userList.size() > 0) {
                    LoginUserInfo userInfo = userList.get(0);
                    Glide.with(QQLoginActivity.this)
                            .load(userInfo.getAvatar())
                            .placeholder(R.drawable.ic_login_avatar)
                            .centerCrop()
                            .transform(new GlideCircleTransform(QQLoginActivity.this))
                            .crossFade(200)
                            .into(ivLoginAvatar);
                    etUsername.setText(userInfo.getUsername());
                    etPassword.setText(userInfo.getPassword());
                }
            }
        }, 300);
    }

    private void compareQQWithDB(String qqNumber) {
        for (LoginUserInfo userInfo : userList) {
            if (qqNumber.equals(userInfo.getUsername())) {
                Glide.with(this)
                        .load(userInfo.getAvatar())
                        .placeholder(R.drawable.ic_login_avatar)
                        .centerCrop()
                        .transform(new GlideCircleTransform(this))
                        .into(ivLoginAvatar);
                return;
            }
        }

        ivLoginAvatar.setImageResource(R.drawable.ic_login_avatar);
    }

    private void doLogin() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastMaster.toast("请输入账号。");
            return;
        } else if (TextUtils.isEmpty(password)) {
            ToastMaster.toast("请输入QQ密码。");
            return;
        }

        Gson gson = new Gson();
        LoginUserInfo userInfo = new LoginUserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setTimeStamp(System.currentTimeMillis());

        if ("799536767".equals(username) && "123456".equals(password)) {
            userInfo.setAvatar(R.drawable.ic_coku_avatar);
            userInfo.setNickname("COKU");
            userInfo.setQqLevel(255);
            userInfo.setSign("");
            int code = liteOrmHelper.update(userInfo);
            if (code == 0) {
                liteOrmHelper.save(userInfo);
            }

            String json = gson.toJson(userInfo);
            PreferencesUtil.setData(PreferencesUtil.FILE_NAME_USER_INFO, PreferencesUtil.KEY_LOGIN_USER_INFO, json);

            startActivity(new Intent(QQLoginActivity.this, QQMainActivity.class));
            finish();
        } else if ("1056125823".equals(username) && "654321".equals(password)) {
            userInfo.setAvatar(R.drawable.ic_co_avatar);
            userInfo.setNickname("圈圈熊");
            userInfo.setQqLevel(66);
            userInfo.setSign("（リングマ、Ursaring）是第二世代登场的宝可梦。");
            int code = liteOrmHelper.update(userInfo);
            if (code == 0) {
                liteOrmHelper.save(userInfo);
            }

            String json = gson.toJson(userInfo);
            PreferencesUtil.setData(PreferencesUtil.FILE_NAME_USER_INFO, PreferencesUtil.KEY_LOGIN_USER_INFO, json);

            startActivity(new Intent(QQLoginActivity.this, QQMainActivity.class));
            finish();
        } else {
            ToastMaster.toast("登录失败\n帐号或密码错误，请重新输入。");
        }
    }

    private View.OnFocusChangeListener focusChange = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int viewId = v.getId();
            if (viewId == R.id.et_username) {
                String username = etUsername.getText().toString();
                if (hasFocus && !TextUtils.isEmpty(username)) {
                    ivCleanUsername.setVisibility(View.VISIBLE);
                } else {
                    ivCleanUsername.setVisibility(View.GONE);
                }
            } else if (viewId == R.id.et_password) {
                String password = etPassword.getText().toString();
                if (hasFocus && !TextUtils.isEmpty(password)) {
                    ivCleanPassword.setVisibility(View.VISIBLE);
                } else {
                    ivCleanPassword.setVisibility(View.GONE);
                }
            }
        }
    };

    private TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s) || !etUsername.hasFocus()) {
                ivCleanUsername.setVisibility(View.GONE);
            } else {
                ivCleanUsername.setVisibility(View.VISIBLE);
                compareQQWithDB(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s) || !etPassword.hasFocus()) {
                ivCleanPassword.setVisibility(View.GONE);
            } else {
                ivCleanPassword.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //点击监听
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_show_user:
                    ToastMaster.toast("历史登录用户");
                    break;
                case R.id.iv_clean_username:
                    etUsername.setText("");
                    etPassword.setText("");
                    ivLoginAvatar.setImageResource(R.drawable.ic_login_avatar);
                    break;
                case R.id.iv_clean_password:
                    etPassword.setText("");
                    break;
                case R.id.tv_do_login:
                    doLogin();
                    break;
                case R.id.tv_forget_password:
                    ToastMaster.toast("忘记密码?");
                    break;
                case R.id.tv_register:
                    ToastMaster.toast("新用户注册");
                    break;
                case R.id.ll_again:
                    isAgain = !isAgain;
                    ivAgain.setSelected(isAgain);
                    if (isAgain) {
                        tvDoLogin.setClickable(true);
                        tvDoLogin.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        tvDoLogin.setClickable(false);
                        tvDoLogin.setTextColor(Color.parseColor("#CCCCCC"));
                    }
                    break;
                case R.id.tv_terms:
                    ToastMaster.toast("服务条款");
                    break;
            }
        }
    };

}
