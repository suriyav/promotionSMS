package com.cat.promotionsms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class PackageOverResultModel implements Serializable {
    private int telephoneNumber;
    private int packageID;
    private int newPackage;
    private String smsMessage;
    private String remarts;
    private boolean flg_send_sms;
}
