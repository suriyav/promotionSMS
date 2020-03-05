package com.cat.promotionsms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class PackageInfoModel implements Serializable {

    String telephoneNumber;
    String packageId;
    String description;
    float dataPro;
    float voiOnnetPro;
    float voiOffnetPro;
    float iddPro;
    String smsMessage;

}
