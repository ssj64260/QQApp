package com.cxb.qqapp.ui.message;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxb.qqapp.R;
import com.cxb.qqapp.model.QQMessageBean;
import com.cxb.qqapp.ui.BaseActivity;
import com.cxb.qqapp.utils.ToastMaster;

/**
 * QQ好友聊天界面
 */

public class ChatPageActivity extends BaseActivity {

    private TextView tvBack;
    private TextView tvName;
    private TextView tvStatus;
    private ImageView ivTwocall;
    private ImageView ivSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        initView();
        setData();

    }

    private void initView() {
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        ivTwocall = (ImageView) findViewById(R.id.iv_twocall);
        ivSingle = (ImageView) findViewById(R.id.iv_single);
    }

    private void setData() {
        QQMessageBean qqMessage = getIntent().getParcelableExtra(QQMessageBean.TAG_QQ_MESSAGE);
        tvName.setText(qqMessage.getName());

        tvBack.setOnClickListener(click);
        ivTwocall.setOnClickListener(click);
        ivSingle.setOnClickListener(click);
    }

    //点击监听
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_back:
                    finish();
                    break;
                case R.id.iv_twocall:
                    ToastMaster.toast("打电话");
                    break;
                case R.id.iv_single:
                    ToastMaster.toast("聊天设置");
                    break;
            }
        }
    };

}
