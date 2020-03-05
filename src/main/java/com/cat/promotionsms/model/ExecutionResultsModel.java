package com.cat.promotionsms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@ToString
public class ExecutionResultsModel implements Serializable {
    private List<ValueModel> results;
}
