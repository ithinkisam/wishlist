package com.ithinkisam.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ithinkisam.wishlist.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);
	
	List<User> findByManagedUsersId(Integer id);
	
}
