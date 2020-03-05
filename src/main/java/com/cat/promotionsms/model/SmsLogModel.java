package com.cat.promotionsms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class SmsLogModel implements Serializable {
    int MDN;
    String SmsMessage;
    String Sender;
    Timestamp SendDateTime;
    String Status;
}
