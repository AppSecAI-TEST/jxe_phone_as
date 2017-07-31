package com.eruitong.model.net;

import com.eruitong.model.ImageItemData;
import com.eruitong.model.InputDataInfo;

import java.util.List;

/**
 * Created by lyl on 2017/7/29.
 */
public class I_QueryWaybill {
    public String error;
    public List<InputDataInfo> pdaWaybillList;
    public boolean success;

    public ImageItemData imageItemData;

    public Double commission;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<InputDataInfo> getPdaWaybillList() {
        return pdaWaybillList;
    }

    public void setPdaWaybillList(List<InputDataInfo> pdaWaybillList) {
        this.pdaWaybillList = pdaWaybillList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ImageItemData getImageItemData() {
        return imageItemData;
    }

    public void setImageItemData(ImageItemData imageItemData) {
        this.imageItemData = imageItemData;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }
}
