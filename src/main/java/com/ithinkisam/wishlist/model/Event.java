package com.ithinkisam.wishlist.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
	
	@OneToMany(
		mappedBy = "event",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private Set<GiftExchangeAssignment> assignments = new LinkedHashSet<>();
	
	@ManyToMany(
		fetch = FetchType.LAZY,
		targetEntity = User.class
	)
	private List<User> members = new ArrayList<>();
	
	@ManyToMany(
		fetch = FetchType.LAZY,
		targetEntity = User.class
	)
	private List<User> admins = new ArrayList<>();
	
	@ManyToMany(
		fetch = FetchType.LAZY,
		targetEntity = ManagedUser.class
	)
	private List<ManagedUser> managedUsers = new ArrayList<>();
	
	public void addAssignment(GiftExchangeAssignment assignment) {
		assignments.add(assignment);
		assignment.setEvent(this);
	}
	
	public void removeAssignment(GiftExchangeAssignment assignment) {
		assignments.remove(assignment);
		assignment.setEvent(null);
	}
	
	public GiftExchangeAssignment getAssignmentByUserId(User user) {
		for (GiftExchangeAssignment assignment : assignments) {
			if (assignment.getAssignee().getId() == user.getId()) {
				return assignment;
			}
		}
		return null;
	}
}
