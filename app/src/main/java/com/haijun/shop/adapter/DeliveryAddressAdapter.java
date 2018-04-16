package com.haijun.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.haijun.shop.R;
import com.haijun.shop.bean.DeliveryAddress;

import java.util.List;

/**
 * @anthor haijun
 * @project name: Shop
 * @class name：com.haijun.shop.adapter
 * @time 2018-02-27 4:43 PM
 * @describe
 */
public class DeliveryAddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<DeliveryAddress> goodsList;
    private int mCurSelectedPosition = -1;  //当前列表中选中的位置（单选的情况）

    public DeliveryAddressAdapter(Context mContext, List<DeliveryAddress> goodsList) {
        this.mContext = mContext;
        this.goodsList = goodsList;
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public DeliveryAddress getItem(int position) {
        return goodsList == null ? null : goodsList.get(position);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_address, null);
            holder.rb_item_setdefault = view.findViewById(R.id.rb_item_setdefault);
            holder.tv_item_name = view.findViewById(R.id.tv_item_name);
            holder.tv_item_phone = view.findViewById(R.id.tv_item_phone);
            holder.tv_item_address = view.findViewById(R.id.tv_item_address);
            holder.tv_item_defalut = view.findViewById(R.id.tv_item_defalut);
            holder.ll_item_setdefault = view.findViewById(R.id.ll_item_setdefault);
            holder.ll_item_delete = view.findViewById(R.id.ll_item_delete);
            holder.ll_item_edit = view.findViewById(R.id.ll_item_edit);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final DeliveryAddress deliveryAddress = getItem(i);
        if (deliveryAddress!=null){
            holder.tv_item_name.setText(deliveryAddress.getName());
            holder.tv_item_phone.setText(deliveryAddress.getPhone());
            holder.tv_item_address.setText(deliveryAddress.getAddress());
            holder.rb_item_setdefault.setChecked(deliveryAddress.isDefault());
            if (mCurSelectedPosition==i){
                holder.tv_item_defalut.setText("默认地址");
                holder.rb_item_setdefault.setChecked(true);
            }
            else {
                holder.tv_item_defalut.setText("设为默认");
                holder.rb_item_setdefault.setChecked(false);
            }
        }

        MyOnClickLisenter mMyOnClickLisenter = new MyOnClickLisenter(i);

        holder.ll_item_setdefault.setOnClickListener(mMyOnClickLisenter);
        holder.ll_item_delete.setOnClickListener(mMyOnClickLisenter);
        holder.ll_item_edit.setOnClickListener(mMyOnClickLisenter);


        return view;
    }

    private class ViewHolder {
        private RadioButton rb_item_setdefault;
        private TextView tv_item_name;
        private TextView tv_item_phone;
        private TextView tv_item_address;
        private TextView tv_item_defalut;
        private LinearLayout ll_item_setdefault;
        private LinearLayout ll_item_delete;
        private LinearLayout ll_item_edit;
    }

    class MyOnClickLisenter implements View.OnClickListener{
        int position;

        MyOnClickLisenter(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            int type = -1;
            switch (v.getId()){
                case R.id.ll_item_setdefault:
                    type = OnListViewContentClickListener.type_setdefault;
                    break;
                case R.id.ll_item_delete:
                    type = OnListViewContentClickListener.type_delete;
                    break;
                case R.id.ll_item_edit:
                    type = OnListViewContentClickListener.type_edit;
                    break;
            }
            if (onListViewContentClickListener!=null){
                onListViewContentClickListener.onClick(position,type);
            }
        }
    }

    //
    public interface OnListViewContentClickListener {
        void onClick(int position,int type);
        int type_setdefault =0;
        int type_delete =1;
        int type_edit =2;
    }

    private OnListViewContentClickListener onListViewContentClickListener;

    public void setOnListViewContentClickListener(OnListViewContentClickListener onListViewContentClickListener) {
        this.onListViewContentClickListener = onListViewContentClickListener;
    }

    public void setmCurSelectedPosition(int mCurSelectedPosition) {
        this.mCurSelectedPosition = mCurSelectedPosition;
    }

    public int getmCurSelectedPosition() {
        return mCurSelectedPosition;
    }
}