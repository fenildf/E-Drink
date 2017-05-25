package com.wt.edrink.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wt.edrink.Constants;
import com.wt.edrink.R;
import com.wt.edrink.adapter.MyDeviceAdapter;
import com.wt.edrink.base.BaseActivity;
import com.wt.edrink.bean.DeviceBean;
import com.wt.edrink.bean.DeviceListBean;
import com.wt.edrink.http.HttpManage;
import com.wt.edrink.http.JavaBeanRequest;
import com.wt.edrink.utils.UserPrefs;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import butterknife.BindView;
import cn.droidlover.xdroid.kit.ToastUtils;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by 美时美课 on 2017/5/25.
 */

public class MyDevicesActivity extends BaseActivity {
    private static String TAG = "MyDevicesActivity";

    @BindView(R.id.xrv_mydevice)
    XRecyclerContentLayout rvDeviceLayout;

    private MyDeviceAdapter adapter;
    private XRecyclerView rvDevice;

    @Override
    public void initData(Bundle savedInstanceState) {
        setToolbarTitle("我的水杯");
        setShowBack(true);

        initRecycler();

        httpMyDevice();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mydevice;
    }

    private void initRecycler() {
        adapter = new MyDeviceAdapter(context);
        adapter.setRecItemClick(recyclerItemCallback);
        rvDevice = rvDeviceLayout.getRecyclerView();
        rvDevice.verticalLayoutManager(context);
        rvDevice.setAdapter(adapter);
        rvDevice.setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                httpMyDevice();
            }

            @Override
            public void onLoadMore(int page) {

            }
        });
    }

    private RecyclerItemCallback<DeviceBean, RecyclerView.ViewHolder> recyclerItemCallback = new RecyclerItemCallback<DeviceBean, RecyclerView.ViewHolder>() {
        @Override
        public void onItemClick(int position, DeviceBean model, int tag, RecyclerView.ViewHolder holder) {
            super.onItemClick(position, model, tag, holder);
            switch (tag) {
                case R.id.rl_mydevice:
                    UserPrefs userPrefs = new UserPrefs(context);
                    userPrefs.setDeviceId(model.getDevice_id());
                    context.finish();
                    break;
                case R.id.btn_mydevice_unbind:
                    break;
            }
        }
    };

    /**
     * 获取我的水杯
     */
    private void httpMyDevice() {
        Request<DeviceListBean> request = new JavaBeanRequest<DeviceListBean>(Constants.URL_DEVICE_LIST, DeviceListBean.class);
        request.add(Constants.AUTH_KEY, getAuthKey());
        HttpManage.httpRequest(0, request, new OnResponseListener<DeviceListBean>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<DeviceListBean> response) {
                DeviceListBean data = response.get();
                Log.e(TAG, "我的设备列表error_code:" + data.getError_code() + "----reason:" + data.getReason() + "----result:" + data.getResult());
                if (data.getError_code() == 10012) {
                    adapter.setData(data.getResult());
                } else if (data.getError_code() == 10013) {
                    ToastUtils.showShort(context, "请先绑定水杯");
                } else {
                    ToastUtils.showShort(context, data.getReason());
                }
            }

            @Override
            public void onFailed(int what, Response<DeviceListBean> response) {
                ToastUtils.showLong(context, "网络请求失败");
            }

            @Override
            public void onFinish(int what) {
                rvDeviceLayout.refreshState(false);
            }
        });
    }
}