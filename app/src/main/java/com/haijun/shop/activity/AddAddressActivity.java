package com.haijun.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.haijun.shop.R;
import com.haijun.shop.bean.DeliveryAddress;
import com.haijun.shop.util.DialogUtil;
import com.haijun.shop.util.ToastUtil;
import com.haijun.shop.util.UserUtil;
import com.lljjcoder.citypickerview.widget.CityPicker;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddAddressActivity extends BaseActivity {

    private EditText et_address_name;
    private EditText et_address_phone;
    private TextView tv_address_address;
    private EditText et_address_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
    }

    @Override
    protected void initView() {
        setLeftImage(R.drawable.back_normal);
        setCenterText("添加收货地址");
        setRightText("保存");
        getTv_base_rightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });

        et_address_name = findViewById(R.id.et_address_name);
        et_address_phone = findViewById(R.id.et_address_phone);
        tv_address_address = findViewById(R.id.tv_address_address);
        et_address_detail = findViewById(R.id.et_address_detail);

        et_address_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        tv_address_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 强制隐藏软键盘
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                showChooseAreaDialog();
            }
        });
    }

    private void showChooseAreaDialog() {

        CityPicker cityPicker = new CityPicker.Builder(this)
                .textSize(14)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                //.titleTextColor("#696969")
                .confirTextColor("#696969")
                .cancelTextColor("#696969")
                .province("江苏省")
                .city("常州市")
                .district("天宁区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                //为TextView赋值
                tv_address_address.setText(province.trim() + " " + city.trim() + " " + district.trim());
            }
        });


    }

    @Override
    protected void initData() {

    }

    private void saveAddress() {
        String name = et_address_name.getText().toString();
        String phone = et_address_phone.getText().toString();
        String address = tv_address_address.getText().toString();
        String detail = et_address_detail.getText().toString();

        if (TextUtils.isEmpty(name)){
            ToastUtil.showToask("姓名未填写");
        }
        else if (TextUtils.isEmpty(phone)){
            ToastUtil.showToask("电话未填写");
        }
        else if (TextUtils.isEmpty(address)){
            ToastUtil.showToask("区域未填写");
        }
        else if (TextUtils.isEmpty(detail)){
            ToastUtil.showToask("详细地址未填写");
        }
        else {
            if (UserUtil.isLoginEd()){
                final DeliveryAddress deliveryAddress = new DeliveryAddress(UserUtil.getUserInfo().getObjectId(),name,phone,address+" "+detail,false);
                DialogUtil.showDialog("正在添加",this);
                deliveryAddress.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        DialogUtil.hideDialog(AddAddressActivity.this);
                        if (e==null){
                            ToastUtil.showToask("添加成功");
                            Intent intent = getIntent();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("deliveryAddress",deliveryAddress);
                            intent.putExtra("bundle",bundle);

                            setResult(RESULT_OK,intent);
                            finish();
                        }
                        else{
                            ToastUtil.showToask("添加失败，请检查网络是否正常");
                        }
                    }
                });
            }
            else {
                ToastUtil.showToask("用户未登陆，请先登陆");
            }

        }
    }


}
