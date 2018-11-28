package com.ithinkisam.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ithinkisam.wishlist.model.ManagedReference;

@Repository
public interface ManagedReferenceRepository extends JpaRepository<ManagedReference, Integer> {

}
