package com.ithinkisam.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ithinkisam.wishlist.model.Wish;

@Repository
public interface WishRepository extends JpaRepository<Wish, Integer> {

	List<Wish> findByUserId(Integer userId);
	
}
