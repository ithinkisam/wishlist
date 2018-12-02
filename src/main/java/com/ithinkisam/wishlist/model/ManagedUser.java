package com.ithinkisam.wishlist.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Data
@Table(name = "managed_user")
public class ManagedUser {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "managed_user_id")
	private Integer id;
	
	@NotEmpty
	private String firstName;
	
	@NotEmpty
	private String lastName;
	
	private String wishlistDescription;
	
	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true
		)
	private List<ManagedWish> wishes;

	public void addWish(ManagedWish wish) {
		wishes.add(wish);
		wish.setUser(this);
	}
	
	public void removeWish(ManagedWish wish) {
		wishes.remove(wish);
		wish.setUser(null);
	}
	
}
