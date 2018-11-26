package com.ithinkisam.wishlist.secretsanta;

import java.util.Map;
import java.util.Set;

public interface AssignmentCalculator<T> {

	/**
	 * Makes assignments for a set of object given a set of exclusion rules.
	 * 
	 * @param objects
	 *            The objects to assign
	 * @param exclusionRules
	 *            Rules dictating which assignments should be disallowed
	 * @return A set of assignment rules
	 */
	Set<Rule<T>> assign(Set<T> objects, Set<Rule<T>> exclusionRules);
	
	Map<T, Set<T>> generateAssignmentOptions(Set<T> objects, Set<Rule<T>> exclusionRules);
	
	Map<T, Set<T>> generateAssigneeOptions(Set<T> objects, Set<Rule<T>> exclusionRules);

}
