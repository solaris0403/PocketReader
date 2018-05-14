package com.pocket.reader.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pocket.reader.PLog;
import com.pocket.reader.R;
import com.pocket.reader.data.LinkManager;
import com.pocket.reader.fragment.adpter.HotAdapter;
import com.pocket.reader.model.dao.LinkDao;

import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HotFragment extends BaseFragment {
    private static final String TAG = HotFragment.class.getSimpleName();
    Unbinder unbinder;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.edt_url)
    EditText edtUrl;
    @BindView(R.id.btn_query)
    Button btnQuery;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private HotAdapter mHotAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        mHotAdapter = new HotAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
//        mHotAdapter.setOnItemClickListener(new LinkAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, Link link) {
//                Intent intent= new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                Uri content_url = Uri.parse(link.getUrl());
//                intent.setData(content_url);
//                startActivity(intent);
//            }
//        });
//        mLinkAdapter.setOnItemLongClickListener(new LinkAdapter.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(View view, int position, Link link) {
//                showDialogFragment(link);
//            }
//        });
        recyclerView.setAdapter(mHotAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_add, R.id.btn_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                String url = edtUrl.getText().toString().trim();
                if (TextUtils.isEmpty(url)) {
                    url = "https://blog.csdn.net/qq_35114086/article/details/52644644";
                }
                LinkDao.saveLink(url, new LinkDao.OnLinkListener() {
                    @Override
                    public void onSuccess() {
                        PLog.e("add onSuccess");
                    }

                    @Override
                    public void onFail() {
                        PLog.e("add onFail");
                    }
                });
                break;
            case R.id.btn_query:
                LinkDao.queryLinks();
                break;
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
        tvResult.setText(String.valueOf(LinkManager.getInstance().getLinks().size()));
    }
}
