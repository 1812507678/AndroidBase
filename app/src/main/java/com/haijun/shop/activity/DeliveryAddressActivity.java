package com.haijun.shop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.haijun.shop.R;
import com.haijun.shop.adapter.DeliveryAddressAdapter;
import com.haijun.shop.bean.DeliveryAddress;
import com.haijun.shop.util.DialogUtil;
import com.haijun.shop.util.LogUtil;
import com.haijun.shop.util.ShowToaskDialogUtil;
import com.haijun.shop.util.ToastUtil;
import com.haijun.shop.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class DeliveryAddressActivity extends BaseActivity{

    private static final String TAG = DeliveryAddressActivity.class.getSimpleName();
    private ListView lv_address_item;
    private List<DeliveryAddress> deliveryAddressList;
    private DeliveryAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
    }

    @Override
    protected void initView() {
        setLeftImage(R.drawable.back_normal);
        setCenterText("管理收货地址");

        lv_address_item = findViewById(R.id.lv_address_item);

        deliveryAddressList = new ArrayList<>();

        adapter = new DeliveryAddressAdapter(this, deliveryAddressList);
        lv_address_item.setAdapter(adapter);

        adapter.setOnListViewContentClickListener(new DeliveryAddressAdapter.OnListViewContentClickListener() {
            @Override
            public void onClick(int position, int type) {
                DeliveryAddress deliveryAddress = deliveryAddressList.get(position);
                switch (type){
                    case DeliveryAddressAdapter.OnListViewContentClickListener.type_setdefault:
                        //ToastUtil.showToask("setdefault:"+position);
                        updateDefalutSelectAddress(position, adapter);
                        break;
                    case DeliveryAddressAdapter.OnListViewContentClickListener.type_delete:
                        //ToastUtil.showToask("delete:"+position);
                        deleteAddress(deliveryAddress);
                        break;
                    case DeliveryAddressAdapter.OnListViewContentClickListener.type_edit:
                        //ToastUtil.showToask("edit:"+position);
                        break;
                }
            }
        });
    }

    private void deleteAddress(final DeliveryAddress deliveryAddress) {
        LogUtil.i(TAG,"deliveryAddress:"+deliveryAddress);
        ShowToaskDialogUtil.showTipDialog(this, "确定要删除当前收货地址吗？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogUtil.showDialog("正在删除",DeliveryAddressActivity.this);
                deliveryAddress.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        LogUtil.i(TAG,"e:"+e);
                        DialogUtil.hideDialog(DeliveryAddressActivity.this);
                        if (e==null){
                            ToastUtil.showToask("删除成功");
                            deliveryAddressList.remove(deliveryAddress);
                            adapter.notifyDataSetChanged();
                            updateSelectState();
                        }
                        else {
                            ToastUtil.showToask("删除失败，请检查网络是否正常");
                        }
                    }
                });
            }
        });


    }

    private void updateDefalutSelectAddress(int position, DeliveryAddressAdapter adapter) {
        if (adapter.getmCurSelectedPosition()==position){
            return;
        }
        adapter.setmCurSelectedPosition(position);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void initData() {
        if (UserUtil.isLoginEd()){
            BmobQuery<DeliveryAddress> deliveryAddressBmobQuery = new BmobQuery<>();
            deliveryAddressBmobQuery.addWhereEqualTo("userId", UserUtil.getUserInfo().getObjectId());
            DialogUtil.showDialog("正在加载数据",this);
            deliveryAddressBmobQuery.findObjects(new FindListener<DeliveryAddress>() {
                @Override
                public void done(List<DeliveryAddress> list, BmobException e) {
                    DialogUtil.hideDialog(DeliveryAddressActivity.this);
                    if (e==null){
                        deliveryAddressList.clear();
                        deliveryAddressList.addAll(list);
                        adapter.notifyDataSetChanged();
                        updateSelectState();
                    }
                    else {
                        ToastUtil.showToask("数据加载失败，请检查网络是否正常");
                    }
                }
            });
        }
        else {
            ToastUtil.showToask("用户未登陆，请先登陆");
        }

    }

    private void updateSelectState() {
        int curSelectedPosition = -1;
        if (deliveryAddressList.size()>1){
            for (int i = 0; i< deliveryAddressList.size(); i++){
                if (deliveryAddressList.get(i).isDefault()){
                    curSelectedPosition = i;
                    break;
                }
            }
        }
        if (curSelectedPosition!=-1){
            adapter.setmCurSelectedPosition(curSelectedPosition);
            adapter.notifyDataSetChanged();
        }
    }


    public void addAddress(View view) {
        startActivityForResult(new Intent(this,AddAddressActivity.class),100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100 && resultCode==RESULT_OK){
            Bundle bundle = data.getBundleExtra("bundle");
            if (bundle!=null){
                DeliveryAddress deliveryAddress = bundle.getParcelable("deliveryAddress");
                if (deliveryAddress!=null){
                    deliveryAddressList.add(deliveryAddress);
                    adapter.notifyDataSetChanged();
                }
            }

        }
    }
}
