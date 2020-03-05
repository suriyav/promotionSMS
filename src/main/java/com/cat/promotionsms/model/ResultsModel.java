package com.cat.promotionsms.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ResultsModel implements Serializable {
    @SerializedName("com.cat.promotionsms.PromotionRequest")
    private PromotionOverModel promotionOverModel;
}