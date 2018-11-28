package com.ithinkisam.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ithinkisam.wishlist.model.ManagedWish;

@Repository
public interface ManagedWishRepository extends JpaRepository<ManagedWish, Integer> {

	List<ManagedWish> findByUserId(Integer userId);
	
}
