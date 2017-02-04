package net.coatli.confluent;

import java.util.Date;

public class Item {

  private Integer key;
  private String  description;
  private Date    checkInDate;

  public Item() {
  }

  public Integer getKey() {
    return key;
  }

  public Item setKey(final Integer key) {
    this.key = key;

    return this;
  }

  public String getDescription() {
    return description;
  }

  public Item setDescription(final String description) {
    this.description = description;

    return this;
  }

  public Date getCheckInDate() {
    return checkInDate;
  }

  public Item setCheckInDate(final Date checkInDate) {
    this.checkInDate = checkInDate;

    return this;
  }

}
