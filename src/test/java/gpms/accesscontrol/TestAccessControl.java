package gpms.accesscontrol;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class TestAccessControl {

	Accesscontrol ac = null;

	@Before
	public void setUp() throws Exception {
		ac = new Accesscontrol();
	}

	@After
	public void tearDown() throws Exception {
		ac = null;
	}

	@Test
	public void test() {
		// ac.getXACMLdecision("Faculty", "Proposal", "Create");
		HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();

		Multimap<String, String> subjectMap = ArrayListMultimap.create();
		subjectMap.put("position-type", "Tenured/tenure-track faculty");
		// subjectMap.put("Proposal Role", "PI");
		attrMap.put("Subject", subjectMap);

		Multimap<String, String> resourceMap = ArrayListMultimap.create();
		resourceMap.put("proposal-section", "Whole Proposal");
		//resourceMap.put("status", "Withdraw by Research Office");
		attrMap.put("Resource", resourceMap);

		Multimap<String, String> actionMap = ArrayListMultimap.create();
		actionMap.put("proposal-action", "Create");
		attrMap.put("Action", actionMap);

		// Multimap<String, String> environmentMap = ArrayListMultimap.create();
		// environmentMap.put("device-type", "Android Device");
		// attrMap.put("Environment", environmentMap);

		// ac.getXACMLdecision("Tenured", "Proposal", "Create");
		// ac.getXACMLdecision("Research Staff", "Proposal", "Create");
		// ac.getXACMLdecision("Tenured", "Proposal", "Delete");
		// ac.getXACMLdecision("PI", "Proposal", "Delete");
		// ac.getXACMLdecision("PI", "Co-PI-List", "Edit");
		// ac.getXACMLdecision("PI", "Co-PI-List", "View");
		// ac.getXACMLdecision("Co-PI", "Co-PI-List", "Edit");
		// ac.getXACMLdecision("Co-PI", "Co-PI-List", "View");
		// ac.getXACMLdecision("Senior-Personnel", "Co-PI-List", "Edit");
		// ac.getXACMLdecision("Senior-Personnel", "Co-PI-List", "View");
		// ac.getXACMLdecision("PI", "Senior-Personnel-List", "View");
		// ac.getXACMLdecision("PI", "Senior-Personnel-List", "View");
		// ac.getXACMLdecision("Co-PI", "Senior-Personnel-List", "Edit");
		// ac.getXACMLdecision("Co-PI", "Senior-Personnel-List", "View");
		// ac.getXACMLdecision("Senior-Personnel", "Senior-Personnel-List",
		// "Edit");
		// ac.getXACMLdecision("Senior-Personnel", "Senior-Personnel-List",
		// "View");
		// ac.getXACMLdecision("PI", "Project-Info", "View");
		// ac.getXACMLdecision("PI", "Project-Info", "Edit");
		// ac.getXACMLdecision("Co-PI", "Project-Info", "View");
		// ac.getXACMLdecision("Co-PI", "Project-Info", "Edit");
		// ac.getXACMLdecision("Senior-Personnel", "Project-Info", "View");
		// ac.getXACMLdecision("Research-Administrator", "OSP-Section", "Edit");

		// String decision = ac.getXACMLdecision("PI", "Project-Info", "View");

		String decision = ac.getXACMLdecision(attrMap);

		assertEquals("Permit", decision);
	}

}
