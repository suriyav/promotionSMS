package com.cat.promotionsms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class KieContainersModel implements Serializable {
    @SerializedName("kie-container")
    @Expose
    private List<KieContainerModel> kieContainer = null;
}
