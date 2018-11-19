package com.ithinkisam.wishlist.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "event")
public class Event {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Integer id;
	
	private String title;
	
	private String description;
	
	private LocalDateTime date;
	
	private String location;
	
	@ManyToMany(
		fetch = FetchType.LAZY,
		targetEntity = User.class
	)
	private List<User> members;
	
	@ManyToMany(
		fetch = FetchType.LAZY,
		targetEntity = User.class
	)
	private List<User> admins;
	
}
