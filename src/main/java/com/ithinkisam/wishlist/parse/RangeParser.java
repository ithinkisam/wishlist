package com.ithinkisam.wishlist.parse;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Parser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ithinkisam.wishlist.model.support.Range;

@Component
public class RangeParser implements Parser<Range> {

	@Override
	public Range parse(String text, Locale locale) throws ParseException {
		String noWhitespace = StringUtils.trimAllWhitespace(text);
		if (!StringUtils.hasLength(noWhitespace)) {
			return null;
		}
		if (isNumeric(noWhitespace)) {
			BigDecimal value = new BigDecimal(noWhitespace);
			return new Range(value, value);
		} else if (noWhitespace.contains("-")) {
			String[] parts = noWhitespace.split("-");
			if (parts.length != 2) {
				return null;
			}
			if (isNumeric(parts[0]) && isNumeric(parts[1])) {
				return new Range(new BigDecimal(parts[0]), new BigDecimal(parts[1]));
			}
		}
		return null;
	}
	
	boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

}
