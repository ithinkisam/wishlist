package com.ithinkisam.wishlist.secretsanta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SecretSantaAssignmentCalculatorTest {

	private SecretSantaAssignmentCalculator sut;
	
	@Before
	public void setUp() {
		sut = new SecretSantaAssignmentCalculator();
	}
	
	@Test
	public void getRandomWithNullInputShouldReturnNull() {
		Integer random = sut.getRandom(null);
		assertNull(random);
	}
	
	@Test
	public void getRandomWithEmptyArrayShouldReturnNull() {
		Integer random = sut.getRandom(new Integer[] {});
		assertNull(random);
	}
	
	@Test
	public void getRandomWithSingleValuedArrayShouldReturnTheOneValue() {
		Integer random = sut.getRandom(new Integer[] { 1 });
		assertEquals(Integer.valueOf(1), random);
	}
	
	@Test
	public void getRandomWithMultiValuedArrayShouldReturnAValue() {
		sut = new SecretSantaAssignmentCalculator(1);
		Integer random = sut.getRandom(new Integer[] { 1, 2, 3, 4, 5 });
		assertEquals(Integer.valueOf(1), random);
	}
	
	@Test
	public void assignTwoObjectNoRules() {
		sut = new SecretSantaAssignmentCalculator();
		Set<Integer> objects = new HashSet<Integer>();
		Set<Rule<Integer>> exclusionRules = new HashSet<Rule<Integer>>();
		
		objects.add(1);
		objects.add(2);
		
		Set<Rule<Integer>> assignments = sut.assign(objects, exclusionRules);
		
		assertEquals(2, assignments.size());
		assertTrue(assignments.contains(new Rule<Integer>(1, 2)));
		assertTrue(assignments.contains(new Rule<Integer>(2, 1)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void assignTwoObjectOneImpossibleRule() {
		sut = new SecretSantaAssignmentCalculator();
		Set<Integer> objects = new HashSet<Integer>();
		Set<Rule<Integer>> exclusionRules = new HashSet<Rule<Integer>>();
		
		objects.add(1);
		objects.add(2);
		
		exclusionRules.add(new Rule<Integer>(1, 2));
		
		sut.assign(objects, exclusionRules);
	}

	@Test
	public void assignThreeObjectsOneRule() {
		sut = new SecretSantaAssignmentCalculator();
		Set<Integer> objects = new HashSet<Integer>();
		Set<Rule<Integer>> exclusionRules = new HashSet<Rule<Integer>>();
		
		objects.add(1);
		objects.add(2);
		objects.add(3);
		
		exclusionRules.add(new Rule<Integer>(1, 2));
		
		Set<Rule<Integer>> assignments = sut.assign(objects, exclusionRules);
		
		assertEquals(3, assignments.size());
		assertTrue(assignments.contains(new Rule<Integer>(1, 3)));
		assertTrue(assignments.contains(new Rule<Integer>(2, 1)));
		assertTrue(assignments.contains(new Rule<Integer>(3, 2)));
	}

	@Test
	public void assignFourObjectsTwoRules() {
		sut = new SecretSantaAssignmentCalculator();
		Set<Integer> objects = new HashSet<Integer>();
		Set<Rule<Integer>> exclusionRules = new HashSet<Rule<Integer>>();
		
		objects.add(1);
		objects.add(2);
		objects.add(3);
		objects.add(4);
		
		exclusionRules.add(new Rule<Integer>(1, 2));
		exclusionRules.add(new Rule<Integer>(2, 1));
		exclusionRules.add(new Rule<Integer>(3, 4));
		exclusionRules.add(new Rule<Integer>(4, 3));
		
		Set<Rule<Integer>> assignments = sut.assign(objects, exclusionRules);
		
		assertEquals(4, assignments.size());
		assertTrue(assignments.contains(new Rule<Integer>(1, 3)) || assignments.contains(new Rule<Integer>(1, 4)));
		assertTrue(assignments.contains(new Rule<Integer>(2, 3)) || assignments.contains(new Rule<Integer>(2, 4)));
		assertTrue(assignments.contains(new Rule<Integer>(3, 1)) || assignments.contains(new Rule<Integer>(3, 2)));
		assertTrue(assignments.contains(new Rule<Integer>(4, 1)) || assignments.contains(new Rule<Integer>(4, 2)));
	}
	
	@Test
	public void assignMultiObjectMultiRule() {
		sut = new SecretSantaAssignmentCalculator();
		Set<Integer> objects = new HashSet<Integer>();
		Set<Rule<Integer>> exclusionRules = new HashSet<Rule<Integer>>();
		
		objects.add(1);
		objects.add(2);
		objects.add(3);
		objects.add(4);
		objects.add(5);
		objects.add(6);
		objects.add(7);
		
		exclusionRules.add(new Rule<Integer>(1, 2));
		exclusionRules.add(new Rule<Integer>(2, 1));
		exclusionRules.add(new Rule<Integer>(3, 4));
		exclusionRules.add(new Rule<Integer>(4, 3));
		exclusionRules.add(new Rule<Integer>(6, 7));
		exclusionRules.add(new Rule<Integer>(7, 6));
		exclusionRules.add(new Rule<Integer>(3, 5));
		exclusionRules.add(new Rule<Integer>(3, 7));
		
		Set<Rule<Integer>> assignments;
		for (int i = 0; i < 20; i++) {
			assignments = sut.assign(objects, exclusionRules);
			System.out.println(assignments);
		}
	}

}
