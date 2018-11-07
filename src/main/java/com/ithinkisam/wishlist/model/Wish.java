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

import com.ithinkisam.wishlist.model.support.Range;
import com.ithinkisam.wishlist.model.support.Reference;

import lombok.Data;

@Entity
@Data
@Table(name = "wish")
public class Wish {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "wish_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	private String description;
	
	private Range price;
	
	@OneToMany(
		mappedBy = "wish",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<Reference> references = Collections.emptyList();
	
	private Boolean purchased;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchaser_id")
	private User purchaser;
	
	public void addReference(Reference reference) {
		references.add(reference);
		reference.setWish(this);
	}
	
	public void removeReference(Reference reference) {
		references.remove(reference);
		reference.setWish(null);
	}
	
	
}
