package com.haijun.shop.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;

/**
 * @anthor haijun
 * @project name: Shop
 * @class name：com.haijun.shop.bean
 * @time 2018-02-27 4:49 PM
 * @describe
 */
public class DeliveryAddress extends BmobObject implements Parcelable {
    private String userId;
    private String name;
    private String phone;
    private String address;
    private boolean isDefault;


    public DeliveryAddress(String name, String phone, String address, boolean isDefault) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }

    public DeliveryAddress(String userId, String name, String phone, String address, boolean isDefault) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DeliveryAddress{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", isDefault=" + isDefault +
                ", objectId=" + getObjectId() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(address);
        parcel.writeByte((byte) (isDefault ? 1 : 0));
        parcel.writeString(getObjectId());
    }

    public static final Creator<DeliveryAddress> CREATOR = new Creator<DeliveryAddress>() {
        @Override
        public DeliveryAddress createFromParcel(Parcel source) {
            return new DeliveryAddress(source);
        }

        @Override
        public DeliveryAddress[] newArray(int size) {
            return new DeliveryAddress[size];
        }
    };

    public DeliveryAddress(Parcel source){
        this.userId = source.readString();
        this.name = source.readString();
        this.phone = source.readString();
        this.address = source.readString();
        this.isDefault = source.readByte() != 0;
        this.setObjectId(source.readString());
    }
}
