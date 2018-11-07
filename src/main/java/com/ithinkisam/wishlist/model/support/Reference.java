package com.ithinkisam.wishlist.model.support;

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

import com.ithinkisam.wishlist.model.Wish;

import lombok.Data;

@Entity
@Data
@Table(name = "wish_reference")
public class Reference {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "reference_id")
	private Integer id;
	
	private URL url;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wish_id")
	private Wish wish;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Reference)) return false;
		return id != null && id.equals(((Reference) o).id);
	}
	
	@Override
	public int hashCode() {
		return 31;
	}
	
}
