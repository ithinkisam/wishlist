package com.ithinkisam.wishlist.model.support;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Range {

	@Column(name = "price_minimum")
	private BigDecimal minimum = BigDecimal.ZERO;

	@Column(name = "price_maximum")
	private BigDecimal maximum = BigDecimal.ZERO;

	public String getPrice() {
		return getPriceCurrency("-");
	}
	
	public String getPriceRange() {
		return getPriceDigits("-");
	}
	
	public String getPriceCurrency(String delimiter) {
		if (minimum.equals(maximum)) {
			return NumberFormat.getCurrencyInstance().format(minimum);
		}
		return NumberFormat.getCurrencyInstance().format(minimum) + delimiter
				+ NumberFormat.getCurrencyInstance().format(maximum);
	}
	
	public String getPriceDigits(String delimiter) {
		if (minimum.equals(maximum)) {
			return String.valueOf(minimum);
		}
		return String.valueOf(minimum) + delimiter + String.valueOf(maximum);
	}

}
