package com.ithinkisam.wishlist.secretsanta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class DefaultAssignmentCalculator<T> implements AssignmentCalculator<T> {

	@Override
	public Map<T, Set<T>> generateAssignmentOptions(Set<T> objects, Set<Rule<T>> exclusionRules) {
		Map<T, Set<T>> assignmentOptions = new HashMap<T, Set<T>>(objects.size());
		// set up one-to-all assignment options as a start
		objects.forEach((o) -> {
			assignmentOptions.put(o, new HashSet<T>(objects));
			assignmentOptions.get(o).remove(o);
		});

		// remove options per exclusion rules
		exclusionRules.forEach((r) -> assignmentOptions.get(r.getAssignee()).remove(r.getRecipient()));

		return assignmentOptions;
	}

	@Override
	public Map<T, Set<T>> generateAssigneeOptions(Set<T> objects, Set<Rule<T>> exclusionRules) {
		Map<T, Set<T>> assigneeOptions = new HashMap<T, Set<T>>(objects.size());
		// set up one-to-all assignment options as a start
		objects.forEach((o) -> {
			assigneeOptions.put(o, new HashSet<T>(objects));
			assigneeOptions.get(o).remove(o);
		});
		
		// remove options per exclusion rules
		exclusionRules.forEach((r) -> {
			assigneeOptions.get(r.getRecipient()).remove(r.getAssignee());
		});
		
		return assigneeOptions;
	}

}
