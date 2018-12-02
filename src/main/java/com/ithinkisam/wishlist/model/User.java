package com.ithinkisam.wishlist.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer id;
	
	private String email;
	
	@JsonIgnore
	private String password;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;

	private String wishlistDescription;
	
	private Boolean active;
	
	@OneToMany(
		mappedBy = "user",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<UserProfile> profiles;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	@OneToMany(
		mappedBy = "user",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<Wish> wishes;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "managed_user_relationship", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "managed_user_id"))
	@OrderBy("firstName")
	private List<ManagedUser> managedUsers;
	
	public void addProfile(UserProfile profile) {
		profiles.add(profile);
		profile.setUser(this);
	}
	
	public void removeProfile(UserProfile profile) {
		profiles.remove(profile);
		profile.setUser(null);
	}
	
	public String profileValue(String key) {
		for (UserProfile profile : profiles) {
			if (profile.getKey().equalsIgnoreCase(key)) {
				return profile.getValue();
			}
		}
		return null;
	}
	
	public void addWish(Wish wish) {
		wishes.add(wish);
		wish.setUser(this);
	}
	
	public void removeWish(Wish wish) {
		wishes.remove(wish);
		wish.setUser(null);
	}
	
}
