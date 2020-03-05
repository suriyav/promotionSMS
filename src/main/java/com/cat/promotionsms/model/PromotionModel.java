package com.cat.promotionsms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class PromotionModel implements Serializable {
    private String type;
    private String msg;
    private ResultModel result;
}