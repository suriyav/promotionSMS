package com.cat.promotionsms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class KieJsonResponseModel implements Serializable {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private KieResultModel result;
}
