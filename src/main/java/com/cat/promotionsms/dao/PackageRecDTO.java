package com.cat.promotionsms.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class PackageRecDTO implements Serializable {
    String TelephoneRec;
    String packageRecID;
}
