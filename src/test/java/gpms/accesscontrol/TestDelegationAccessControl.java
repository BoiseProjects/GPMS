package gpms.accesscontrol;

import static org.junit.Assert.assertEquals;
import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;
import org.wso2.balana.ObligationResult;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.AttributeAssignment;
import org.wso2.balana.xacml3.Advice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.MongoClient;

public class TestDelegationAccessControl {

	Accesscontrol ac = null;

	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	ProposalDAO proposalDAO = null;

	@Before
	public void setUp() throws Exception {
		ac = new Accesscontrol();

		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		proposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	}

	@After
	public void tearDown() throws Exception {
		ac = null;
	}

	@Test
	public void test() throws UnknownHostException {
		// ac.getXACMLdecision("Faculty", "Proposal", "Create");
		HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();

		Multimap<String, String> subjectMap = ArrayListMultimap.create();
		subjectMap.put("position.title", "Associate Chair");
		subjectMap.put("group", "Computer Science");

		attrMap.put("Subject", subjectMap);

		Multimap<String, String> resourceMap = ArrayListMultimap.create();
		resourceMap.put("proposal.section", "Whole Proposal");
		resourceMap.put("ApprovedByDepartmentChair", "READYFORAPPROVAL");

		// resourceMap.put("proposal.section", "Certification/Signatures");
		// resourceMap.put("ApprovedByDepartmentChair", "READYFORAPPROVAL");
		// resourceMap.put("DeletedByPI", "NOTDELETED");

		attrMap.put("Resource", resourceMap);

		Multimap<String, String> actionMap = ArrayListMultimap.create();
		// actionMap.put("proposal.action", "Add");
		// actionMap.put("proposal.action", "Save");

		actionMap.put("proposal.action", "Approve");

		attrMap.put("Action", actionMap);

		// Multimap<String, String> environmentMap = ArrayListMultimap.create();
		// environmentMap.put("device-type", "Android Device");
		// attrMap.put("Environment", environmentMap);

		// String proposalID = "5702a60865dbb30b09a492cf";
		// String authorFullName = "Milson Munakami";

		// ObjectId proposalId = new ObjectId(proposalID);

		// Proposal existingProposal = proposalDAO
		// .findProposalByProposalID(proposalId);

		String decision = ac.getXACMLdecision(attrMap);

		assertEquals("Permit", decision);
	}
}
