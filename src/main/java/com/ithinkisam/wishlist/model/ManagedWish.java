package com.ithinkisam.wishlist.model;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ithinkisam.wishlist.model.support.Range;

import lombok.Data;

@Entity
@Data
@Table(name = "managed_wish")
public class ManagedWish {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "managed_wish_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managed_user_id")
	@JsonIgnore
	private ManagedUser user;
	
	private String description;
	
	private Range price;
	
	@OneToMany(
		mappedBy = "wish",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<ManagedReference> references = Collections.emptyList();
	
	private Integer quantity;
	
	private Boolean purchased;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchaser_id")
	@JsonIgnore
	private User purchaser;

	public void addReference(ManagedReference reference) {
		references.add(reference);
		reference.setWish(this);
	}
	
	public void removeReference(ManagedReference reference) {
		references.remove(reference);
		reference.setWish(null);
	}
	
}
