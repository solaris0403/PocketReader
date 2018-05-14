package com.pocket.reader.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.pocket.reader.BaseActivity;
import com.pocket.reader.R;
import com.pocket.reader.core.ShareHelper;
import com.pocket.reader.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by tony on 5/9/18.
 */

public class ExternalShareActivity extends BaseActivity {
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.tv_share)
    TextView tvShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        ShareHelper.processIntent(getIntent());
    }

    @OnClick(R.id.btn_finish)
    public void onViewClicked() {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getType() == MessageEvent.TYPE_PROCESS_LINK){
            tvShare.setText(messageEvent.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
