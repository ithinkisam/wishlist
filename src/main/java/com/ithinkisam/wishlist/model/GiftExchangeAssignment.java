package com.ithinkisam.wishlist.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "gift_exchange_assignment")
public class GiftExchangeAssignment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	@JsonIgnore
	private Event event;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignee_user_id")
	@JsonIgnore
	private User assignee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient_user_id")
	@JsonIgnore
	private User recipient;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GiftExchangeAssignment)) return false;
		return id != null && id.equals(((GiftExchangeAssignment) o).id);
	}
	
	@Override
	public int hashCode() {
		return 23;
	}
	
}
