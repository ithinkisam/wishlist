package com.ithinkisam.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ithinkisam.wishlist.model.Event;
import com.ithinkisam.wishlist.model.User;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	List<Event> findByMembersId(Integer userId);
	
	List<Event> findByAdminsId(Integer userId);
	
}
