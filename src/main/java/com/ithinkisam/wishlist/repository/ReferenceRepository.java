package com.ithinkisam.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ithinkisam.wishlist.model.support.Reference;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Integer> {

}
