package com.ithinkisam.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ithinkisam.wishlist.model.ManagedUser;

@Repository
public interface ManagedUserRepository extends JpaRepository<ManagedUser, Integer> {

}
