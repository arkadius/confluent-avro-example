package net.coatli.confluent.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).appendSuper(super.toString()).append("key", key)
        .append("description", description).append("checkInDate", checkInDate).toString();
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Item)) {
      return false;
    }
    final Item castOther = (Item) other;
    return new EqualsBuilder().append(key, castOther.key).append(description, castOther.description)
        .append(checkInDate, castOther.checkInDate).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(key).append(description).append(checkInDate).toHashCode();
  }

}
