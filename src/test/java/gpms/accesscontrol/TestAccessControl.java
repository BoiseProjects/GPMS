package gpms.accesscontrol;

import static org.junit.Assert.assertEquals;
import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.model.InvestigatorRefAndPosition;
import gpms.model.Proposal;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;
import org.w3c.dom.NamedNodeMap;
import org.wso2.balana.ObligationResult;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.AttributeAssignment;
import org.wso2.balana.xacml3.Advice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.MongoClient;

public class TestAccessControl {

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
		// subjectMap.put("position.type", "Tenured/tenure-track faculty");
		// subjectMap.put("position.title", "Department Chair");
		subjectMap.put("proposal.role", "PI");

		attrMap.put("Subject", subjectMap);

		Multimap<String, String> resourceMap = ArrayListMultimap.create();
		resourceMap.put("DeletedByPI", "NOTDELETED");
		resourceMap.put("proposal.section", "Whole Proposal");
		resourceMap.put("SubmittedByPI", "NOTSUBMITTED");

		attrMap.put("Resource", resourceMap);

		Multimap<String, String> actionMap = ArrayListMultimap.create();
		// actionMap.put("proposal.action", "Add");
		actionMap.put("proposal.action", "Save");

		attrMap.put("Action", actionMap);

		// Multimap<String, String> environmentMap = ArrayListMultimap.create();
		// environmentMap.put("device-type", "Android Device");
		// attrMap.put("Environment", environmentMap);

		String proposalID = "5702a60865dbb30b09a492cf";
		String authorFullName = "Milson Munakami";

		ObjectId proposalId = new ObjectId(proposalID);

		Proposal existingProposal = proposalDAO
				.findProposalByProposalID(proposalId);

		StringBuffer contentProfile = new StringBuffer();

		contentProfile.append("<Content>");
		contentProfile.append("<ak:record xmlns:ak=\"http://akpower.org\">");
		contentProfile.append("<ak:proposal>");

		contentProfile.append("<ak:proposalid>");
		contentProfile.append(proposalID);
		contentProfile.append("</ak:proposalid>");

		contentProfile.append("<ak:proposaltitle>");
		contentProfile.append("Proposal 11");
		contentProfile.append("</ak:proposaltitle>");

		contentProfile.append("<ak:submittedbypi>");
		contentProfile.append("Not Submitted");
		contentProfile.append("</ak:submittedbypi>");

		contentProfile.append("<ak:readyforsubmissionbypi>");
		contentProfile.append(false);
		contentProfile.append("</ak:readyforsubmissionbypi>");

		contentProfile.append("<ak:deletedbypi>");
		contentProfile.append("Not Ready for Approval");
		contentProfile.append("</ak:deletedbypi>");

		contentProfile.append("<ak:approvedbydepartmentchair>");
		contentProfile.append("Not Ready for Approval");
		contentProfile.append("</ak:approvedbydepartmentchair>");

		contentProfile.append("<ak:approvedbybusinessmanager>");
		contentProfile.append("Not Ready for Approval");
		contentProfile.append("</ak:approvedbybusinessmanager>");

		contentProfile.append("<ak:approvedbyirb>");
		contentProfile.append("Not Ready for Approval");
		contentProfile.append("</ak:approvedbyirb>");

		contentProfile.append("<ak:approvedbydean>");
		contentProfile.append("Not Ready for Approval");
		contentProfile.append("</ak:approvedbydean>");

		contentProfile.append("<ak:approvedbyuniversityresearchadministrator>");
		contentProfile.append("Not Ready for Approval");
		contentProfile
				.append("</ak:approvedbyuniversityresearchadministrator>");

		contentProfile.append("<ak:withdwarnbyuniversityresearchadmisntrator>");
		contentProfile.append("Not Withdrawn");
		contentProfile
				.append("</ak:withdwarnbyuniversityresearchadmisntrator>");

		contentProfile.append("<ak:submittedbyuniversityresearchadminstrator>");
		contentProfile.append("Not Submitted");
		contentProfile
				.append("</ak:submittedbyuniversityresearchadminstrator>");

		contentProfile.append("<ak:approvedbyuniversityresearchdirector>");
		contentProfile.append("Not Deleted");
		contentProfile.append("</ak:approvedbyuniversityresearchdirector>");

		contentProfile.append("<ak:deletedbyuniversityresearchdirector>");
		contentProfile.append("Not Deleted");
		contentProfile.append("</ak:deletedbyuniversityresearchdirector>");

		contentProfile.append("<ak:archivedbyuniversityresearchdirector>");
		contentProfile.append("Not Archived");
		contentProfile.append("</ak:archivedbyuniversityresearchdirector>");

		contentProfile.append("<ak:authorprofile>");
		// contentProfile.append("<ak:firstname>");
		// contentProfile.append(authorProfile.getFirstName());
		// contentProfile.append("</ak:firstname>");
		// contentProfile.append("<ak:middlename>");
		// contentProfile
		// .append(authorProfile.getMiddleName());
		// contentProfile.append("</ak:middlename>");
		//
		// contentProfile.append("<ak:lastname>");
		// contentProfile.append(authorProfile.getLastName());
		// contentProfile.append("</ak:lastname>");

		contentProfile.append("<ak:fullname>");
		contentProfile.append(authorFullName);
		contentProfile.append("</ak:fullname>");
		contentProfile.append("</ak:authorprofile>");

		contentProfile.append("<ak:pi>");
		contentProfile.append("<ak:fullname>");
		contentProfile.append("Milson Munakami");
		contentProfile.append("</ak:fullname>");

		contentProfile.append("<ak:workemail>");
		contentProfile.append("milsonmun@yahoo.com");
		contentProfile.append("</ak:workemail>");

		contentProfile.append("<ak:userid>");
		contentProfile.append("56fee3e965dbb35ce5c900fa");
		contentProfile.append("</ak:userid>");
		contentProfile.append("</ak:pi>");

		contentProfile.append("<ak:copis>");

		contentProfile.append("<ak:copi>");
		contentProfile.append("<ak:fullname>");
		contentProfile.append("PS Wang");
		contentProfile.append("</ak:fullname>");
		contentProfile.append("<ak:workemail>");
		contentProfile.append("fdsafda@yahoo.comss");
		contentProfile.append("</ak:workemail>");
		contentProfile.append("<ak:userid>");
		contentProfile.append("56fee3e965dbb35ce5c900fx");
		contentProfile.append("</ak:userid>");
		contentProfile.append("</ak:copi>");

		contentProfile.append("<ak:copi>");
		contentProfile.append("<ak:fullname>");
		contentProfile.append("Thomas Voltz");
		contentProfile.append("</ak:fullname>");
		contentProfile.append("<ak:workemail>");
		contentProfile.append("fdsafda@yahoo.comsss");
		contentProfile.append("</ak:workemail>");
		contentProfile.append("<ak:userid>");
		contentProfile.append("56fee3e965dbb35ce5c900fx");
		contentProfile.append("</ak:userid>");
		contentProfile.append("</ak:copi>");

		contentProfile.append("</ak:copis>");

		contentProfile.append("<ak:seniors>");

		contentProfile.append("<ak:senior>");
		contentProfile.append("<ak:fullname>");
		contentProfile.append("Nisha Shrestha");
		contentProfile.append("</ak:fullname>");
		contentProfile.append("<ak:workemail>");
		contentProfile.append("fdsafda@yahoo.cdomss");
		contentProfile.append("</ak:workemail>");
		contentProfile.append("<ak:userid>");
		contentProfile.append("56fee3e965dbb35ce5c910dx");
		contentProfile.append("</ak:userid>");
		contentProfile.append("</ak:senior>");

		contentProfile.append("<ak:senior>");
		contentProfile.append("<ak:fullname>");
		contentProfile.append("Arthur Shu");
		contentProfile.append("</ak:fullname>");
		contentProfile.append("<ak:workemail>");
		contentProfile.append("fdsafda@yahoo.camss");
		contentProfile.append("</ak:workemail>");
		contentProfile.append("<ak:userid>");
		contentProfile.append("56fee3e965dbb35ce5c920dx");
		contentProfile.append("</ak:userid>");
		contentProfile.append("</ak:senior>");
		contentProfile.append("</ak:seniors>");

		contentProfile.append("</ak:proposal>");
		contentProfile.append("</ak:record>");
		contentProfile.append("</Content>");

		contentProfile
				.append("<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:content-selector\" IncludeInResult=\"false\">");
		contentProfile
				.append("<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">//ak:record/ak:proposal</AttributeValue>");
		contentProfile.append("</Attribute>");

		Set<AbstractResult> set = ac.getXACMLdecisionWithObligations(attrMap,
				contentProfile);

		Iterator<AbstractResult> it = set.iterator();
		int intDecision = 3;
		while (it.hasNext()) {
			AbstractResult ar = it.next();
			intDecision = ar.getDecision();

			System.out
					.println("\n======================== Printing Obligations ====================");
			List<ObligationResult> obligations = ar.getObligations();
			for (ObligationResult obligation : obligations) {
				if (obligation instanceof org.wso2.balana.xacml3.Obligation) {

					// obligation.encode();

					// Object root;
					// NamedNodeMap nodeAttributes = root.getAttributes();
					// attributeId = new URI(((Object)
					// obligations).getNamedItem(
					// "obligationId").getNodeValue());

					System.out.println("Obligation Id: "
							+ ((org.wso2.balana.xacml3.Obligation) obligation)
									.getObligationId());
					List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
							.getAssignments();

					for (AttributeAssignment assignment : assignments) {
						System.out.println("Obligation :  "
								+ assignment.getContent() + " ::::: "
								+ assignment.getAttributeId() + "\n");
					}
				}
			}
			System.out
					.println("===========================================================");
			System.out
					.println("\n======================== Printing Advices ====================");
			List<Advice> advices = ar.getAdvices();
			for (Advice advice : advices) {
				if (advice instanceof org.wso2.balana.xacml3.Advice) {
					List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Advice) advice)
							.getAssignments();
					for (AttributeAssignment assignment : assignments) {
						System.out.println("Advice :  "
								+ assignment.getContent() + "\n");
					}
				}
			}
			System.out
					.println("===========================================================");
			if (intDecision >= 4 && intDecision <= 6) {
				intDecision = 2;
			}
			System.out.println("Decision:" + intDecision + " that is: "
					+ AbstractResult.DECISIONS[intDecision]);

			assertEquals("Permit", AbstractResult.DECISIONS[intDecision]);
		}

		// String decision = ac.getXACMLdecision(attrMap);
		//
		// assertEquals("Permit", decision);
	}
}
