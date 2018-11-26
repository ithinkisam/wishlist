package com.ithinkisam.wishlist.model;

import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "managed_wish_reference")
public class ManagedReference {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "managed_reference_id")
	private Integer id;
	
	private URL url;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managed_wish_id")
	@JsonIgnore
	private ManagedWish wish;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ManagedReference)) return false;
		return id != null && id.equals(((ManagedReference) o).id);
	}
	
	@Override
	public int hashCode() {
		return 17;
	}
	
}
