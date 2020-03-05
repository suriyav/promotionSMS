package com.cat.promotionsms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class PromotionOverModel implements Serializable {
    private int telephoneNumber;
    private int packageID;
    private int newPackage;
    private String smsMessage;
    private String remarts;
    private boolean flg_send_sms;
    private int packageRecommend;
    private float voi_onnet;
    private float voi_offnet;
    private float data;
    private float idd;
    private String data_month;
}