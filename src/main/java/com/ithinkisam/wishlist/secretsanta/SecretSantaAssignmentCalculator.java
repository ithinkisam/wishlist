package com.ithinkisam.wishlist.secretsanta;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SecretSantaAssignmentCalculator extends DefaultAssignmentCalculator<Integer> {

	private Random random;
	
	public SecretSantaAssignmentCalculator() {
		this.random = new Random(System.currentTimeMillis());
	}
	
	public SecretSantaAssignmentCalculator(long seed) {
		this.random = new Random(seed);
	}
	
	@Override
	public Set<Rule<Integer>> assign(Set<Integer> objects, Set<Rule<Integer>> exclusionRules) {
		
		Set<Rule<Integer>> resultSet = new LinkedHashSet<>();
		
		Map<Integer, Set<Integer>> assignmentOptions = generateAssignmentOptions(objects, exclusionRules);
		Map<Integer, Set<Integer>> assigneeOptions = generateAssigneeOptions(objects, exclusionRules);
		
		ensurePossible(assignmentOptions);
		ensurePossible(assigneeOptions);
		
		int counter = 0;
		int safety = objects.size() * 3;
		while(resultSet.size() < objects.size()) {
			counter++;
			if (counter > safety) {
				throw new IllegalArgumentException("Unable to determine appropriate assignments in alloted cycles");
			}
			
			Integer[] nextAssignment = determineNextKey(assignmentOptions, Integer.MAX_VALUE);
			Integer[] nextAssignee = determineNextKey(assigneeOptions, nextAssignment[0]);
			// repeat on assignments in case assignee's have lowest availability
			nextAssignment = determineNextKey(assignmentOptions, nextAssignee[0]);
			
			if (nextAssignment[0] == Integer.MAX_VALUE && nextAssignee[0] == Integer.MAX_VALUE) {
				// we've hit a breaking point and cannot continue
				throw new IllegalArgumentException("Cannot determine next action");
			} else if (nextAssignment[1] != null && nextAssignee[1] != null) {
				// it's a toss up between whether to process an assignment
				// or a receipt
				if (getRandom(new Integer[] { 0, 1 }) == 0) {
					nextAssignment[1] = null;
				} else {
					nextAssignee[1] = null;
				}
			}
			
			Rule<Integer> rule = new Rule<>();
			if (nextAssignment[1] != null) {
				// process as assignment
				rule.setAssignee(nextAssignment[1]);
				rule.setRecipient(getRandom(assignmentOptions.get(nextAssignment[1]).toArray(new Integer[0])));
			} else {
				// process as receipt
				rule.setRecipient(nextAssignee[1]);
				rule.setAssignee(getRandom(assigneeOptions.get(nextAssignee[1]).toArray(new Integer[0])));
			}
			
			resultSet.add(rule);
			
			// adjust for new rule
			assignmentOptions.remove(rule.getAssignee());
			assignmentOptions.entrySet().stream().forEach(assignment -> {
				if (assignment.getKey() != rule.getAssignee()) {
					assignment.getValue().removeIf(r -> r == rule.getRecipient());
				}
			});
			
			assigneeOptions.remove(rule.getRecipient());
			assigneeOptions.entrySet().stream().forEach(assignee -> {
				if (assignee.getKey() != rule.getRecipient()) {
					assignee.getValue().removeIf(r -> r == rule.getAssignee());
				}
			});
		}
		
		return resultSet;
	}
	
	void ensurePossible(Map<Integer, Set<Integer>> options) {
		if (options == null) {
			throw new IllegalArgumentException("Option Map must not be null");
		}
		options.entrySet().forEach((entry) -> {
			if (entry.getValue() == null || entry.getValue().isEmpty()) {
				throw new IllegalArgumentException("Invalid option map for value " + entry.getKey());
			}
		});
	}
	
	Integer[] determineNextKey(Map<Integer, Set<Integer>> options, int floor) {
		Integer min = options.values().stream()
				.mapToInt(v -> v.size()).min().orElse(-1);
		
		Set<Integer> nextKeys = new HashSet<>();
		options.entrySet().stream().forEach(entry -> {
			if (entry.getValue().size() <= min && entry.getValue().size() <= floor) {
				nextKeys.add(entry.getKey());
			}
		});
		
		return new Integer[] { Integer.min(min, floor), getRandom(nextKeys.toArray(new Integer[0])) };
	}
	
	Integer getRandom(Integer[] input) {
		if (input == null || input.length == 0) {
			return null;
		}
		return input[random.nextInt(input.length)];
	}
	
}
