package org.jsmpp.bean;

import java.util.Date;

public interface DeliveryReceiptInterface<T> {

  String getId();

  int getSubmitted();

  int getDelivered();

  Date getSubmitDate();

  Date getDoneDate();

  T getFinalStatus();

  String getError();

  String getText();

}
