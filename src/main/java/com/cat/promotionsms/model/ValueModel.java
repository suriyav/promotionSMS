package com.cat.promotionsms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ValueModel implements Serializable {
    private ResultsModel value;
}