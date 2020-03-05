package com.cat.promotionsms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class KieContainerModel implements Serializable {
    @SerializedName("container-id")
    @Expose
    private String containerId;
}
