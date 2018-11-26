package com.ithinkisam.wishlist.secretsanta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class DefaultAssignmentCalculatorTest {

	private DefaultAssignmentCalculator<Integer> sut;
	
	@Before
	public void setUp() {
		sut = new DefaultAssignmentCalculator<Integer>() {
			@Override
			public Set<Rule<Integer>> assign(Set<Integer> objects, Set<Rule<Integer>> exclusionRules) {
				return null;
			}
		};
	}
	
	@Test
	public void generateAssignmentOptionsShouldReturnEmptyMapOnEmptyInput() {
		Set<Integer> objects = Collections.emptySet();
		Set<Rule<Integer>> exclusionRules = Collections.emptySet();
		
		Map<Integer, Set<Integer>> assignmentOptions = sut.generateAssignmentOptions(objects, exclusionRules);
		
		assertTrue(assignmentOptions.isEmpty());
	}

	@Test
	public void generateAssignmentOptionsShouldConsiderExclusionRules() {
		Set<Integer> objects = new HashSet<Integer>();
		Set<Rule<Integer>> exclusionRules = new HashSet<Rule<Integer>>();
		
		objects.add(1);
		objects.add(2);
		objects.add(3);
		objects.add(4);
		objects.add(5);
		
		exclusionRules.add(new Rule<Integer>(1, 2));
		exclusionRules.add(new Rule<Integer>(1, 3));
		exclusionRules.add(new Rule<Integer>(1, 4));
		exclusionRules.add(new Rule<Integer>(1, 5));
		exclusionRules.add(new Rule<Integer>(2, 1));
		exclusionRules.add(new Rule<Integer>(2, 3));
		exclusionRules.add(new Rule<Integer>(2, 4));
		exclusionRules.add(new Rule<Integer>(3, 1));
		exclusionRules.add(new Rule<Integer>(3, 2));
		exclusionRules.add(new Rule<Integer>(4, 1));
		
		Map<Integer, Set<Integer>> assignmentOptions = sut.generateAssignmentOptions(objects, exclusionRules);
		
		System.out.println(assignmentOptions);
		
		assertEquals(0, assignmentOptions.get(1).size());
		assertEquals(1, assignmentOptions.get(2).size());
		assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 5 })), assignmentOptions.get(2));
		assertEquals(2, assignmentOptions.get(3).size());
		assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 4, 5 })), assignmentOptions.get(3));
		assertEquals(3, assignmentOptions.get(4).size());
		assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 2, 3, 5 })), assignmentOptions.get(4));
		assertEquals(4, assignmentOptions.get(5).size());
		assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 1, 2, 3, 4 })), assignmentOptions.get(5));
	}

	@Test
	public void generateAssigneeOptionsShouldReturnEmptyMapOnEmptyInput() {
		Set<Integer> objects = Collections.emptySet();
		Set<Rule<Integer>> exclusionRules = Collections.emptySet();
		
		Map<Integer, Set<Integer>> assigneeOptions = sut.generateAssigneeOptions(objects, exclusionRules);
		
		assertTrue(assigneeOptions.isEmpty());
	}

	@Test
	public void generateAssigneeOptionsShouldConsiderExclusionRules() {
		Set<Integer> objects = new HashSet<Integer>();
		Set<Rule<Integer>> exclusionRules = new HashSet<Rule<Integer>>();
		
		objects.add(1);
		objects.add(2);
		objects.add(3);
		objects.add(4);
		objects.add(5);
		
		exclusionRules.add(new Rule<Integer>(1, 2));
		exclusionRules.add(new Rule<Integer>(1, 3));
		exclusionRules.add(new Rule<Integer>(1, 4));
		exclusionRules.add(new Rule<Integer>(1, 5));
		exclusionRules.add(new Rule<Integer>(2, 1));
		exclusionRules.add(new Rule<Integer>(2, 3));
		exclusionRules.add(new Rule<Integer>(2, 4));
		exclusionRules.add(new Rule<Integer>(3, 1));
		exclusionRules.add(new Rule<Integer>(3, 2));
		exclusionRules.add(new Rule<Integer>(4, 1));
		
		Map<Integer, Set<Integer>> assigneeOptions = sut.generateAssigneeOptions(objects, exclusionRules);
		
		System.out.println(assigneeOptions);
		
		assertEquals(1, assigneeOptions.get(1).size());
		assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 5 })), assigneeOptions.get(1));
		assertEquals(2, assigneeOptions.get(2).size());
		assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 4, 5 })), assigneeOptions.get(2));
		assertEquals(2, assigneeOptions.get(3).size());
		assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 4, 5 })), assigneeOptions.get(3));
		assertEquals(2, assigneeOptions.get(4).size());
		assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 3, 5 })), assigneeOptions.get(4));
		assertEquals(3, assigneeOptions.get(5).size());
		assertEquals(new HashSet<>(Arrays.asList(new Integer[] { 2, 3, 4 })), assigneeOptions.get(5));
	}
	
}
