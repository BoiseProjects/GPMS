package gpms.accesscontrol;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.wso2.balana.Balana;
import org.wso2.balana.ObligationResult;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ctx.AbstractRequestCtx;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.AttributeAssignment;
import org.wso2.balana.ctx.RequestCtxFactory;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;
import org.wso2.balana.xacml3.Advice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Accesscontrol {

	private static Balana balana = null;
	private AbstractResult ar;
	AttributeSpreadSheet attrSpreadSheet = null;
	private static String policyLocation = new String();

	private static Set<String> policies = new HashSet<String>();

	public Accesscontrol() throws Exception {
		String file = "/XACMLDatasheet.xls";
		InputStream inputStream = this.getClass().getResourceAsStream(file);

		String policyFolderName = "/policy";
		policyLocation = this.getClass().getResource(policyFolderName).toURI()
				.getPath();

		this.attrSpreadSheet = new AttributeSpreadSheet(inputStream);

	}

	// whatever, go knows whether we need multi-thread in the future
	public static void initBalana() {
		if (balana == null) {
			synchronized (Accesscontrol.class) {
				if (balana == null) {
					try {
						// String policyLocation = (new File("."))
						// .getCanonicalPath() + File.separator + "policy";
						System.setProperty(
								FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY,
								policyLocation);

						policies.add(FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY);

					} catch (Exception e) {
						System.err.println("Can not locate policy repository");
					}
					balana = Balana.getInstance();
				}
			}
		}
	}

	private String createXACMLRequest(String userName, String resource,
			String action) {

		return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">\n"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" IncludeInResult=\"false\">\n"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ resource
				+ "</AttributeValue>\n"
				+ "</Attribute>\n"
				+ "</Attributes>\n"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:action-category:action\">\n"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\">\n"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ action
				+ "</AttributeValue>\n"
				+ "</Attribute>\n"
				+ "</Attributes>\n"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">\n"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ userName
				+ "</AttributeValue>\n"
				+ "</Attribute>\n"
				+ "</Attributes>\n" + "</Request>";

	}

	private String createXACMLRequest(String userName, String resource,
			String action, String environment) {

		return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">\n"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\">\n"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ action
				+ "</AttributeValue>\n"
				+ "</Attribute>\n"
				+ "</Attributes>\n"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">\n"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ userName
				+ "</AttributeValue>\n"
				+ "</Attribute>\n"
				+ "</Attributes>\n"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">\n"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" IncludeInResult=\"false\">\n"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ resource
				+ "</AttributeValue>\n"
				+ "</Attribute>\n"
				+ "</Attributes>\n"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:environment\">\n"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:environment:environment-id\" IncludeInResult=\"false\">\n"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ environment
				+ "</AttributeValue>\n"
				+ "</Attribute>\n"
				+ "</Attributes>\n" + "</Request>";

	}

	private/* String */ResponseCtx getResponse(String request) {
		ResponseCtx rc = null;
		initBalana();
		PDP pdp = getPDPNewInstance();
		System.out
				.println("\n======================== XACML Request ====================");
		System.out.println(request);
		System.out
				.println("===========================================================");
		try {

			RequestCtxFactory rcf = RequestCtxFactory.getFactory();
			AbstractRequestCtx arc = rcf.getRequestCtx(request);
			rc = pdp.evaluate(arc);

		} catch (Exception e) {
			System.out.print("somethingwrong");
		}
		return rc;
	}

	public String getXACMLdecision(String userName, String resource,
			String action) {
		String request = createXACMLRequest(userName, resource, action);
		ResponseCtx responseCtx = getResponse(request);

		Set<AbstractResult> set = responseCtx.getResults();
		Iterator<AbstractResult> it = set.iterator();
		int intDecision = 3;
		while (it.hasNext()) {
			ar = it.next();
			intDecision = ar.getDecision();
			if (intDecision >= 4 && intDecision <= 6) {
				intDecision = 2;
			}
			System.out.println("Decision:" + intDecision + " that is: "
					+ AbstractResult.DECISIONS[intDecision]);
			break; // WARNING: We currently take the first decision as the final
					// one, but multipul decisions may be returned
		}
		return AbstractResult.DECISIONS[intDecision];
	}

	public String getXACMLdecision(String userName, String resource,
			String action, String environment) {
		String request = createXACMLRequest(userName, resource, action,
				environment);
		ResponseCtx responseCtx = getResponse(request);

		Set<AbstractResult> set = responseCtx.getResults();
		Iterator<AbstractResult> it = set.iterator();
		int intDecision = 3;
		while (it.hasNext()) {
			ar = it.next();
			intDecision = ar.getDecision();
			if (intDecision >= 4 && intDecision <= 6) {
				intDecision = 2;
			}
			System.out.println("Decision:" + intDecision + " that is: "
					+ AbstractResult.DECISIONS[intDecision]);
			break; // WARNING: We currently take the first decision as the final
					// one, but multipul decisions may be returned
		}
		return AbstractResult.DECISIONS[intDecision];
	}

	public String getXACMLdecision(
			HashMap<String, Multimap<String, String>> attrMap) {
		// String request = createXACMLRequest(attrMap);

//		String request = "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" ReturnPolicyIdList=\"false\" CombinedDecision=\"false\">\n"
//				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\" >\n"
//				+ "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\">\n"
//				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">asela@asela.com</AttributeValue>\n"
//				+ "</Attribute>\n"
//				+ "</Attributes>\n"
//				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">\n"
//				+ "<Content>\n"
//				+ "<ak:record xmlns:ak=\"http://akpower.org\">\n"
//				+ "<ak:patient>\n"
//				+ "<ak:patientId>p111</ak:patientId>\n"
//				+ "<ak:patientName>\n"
//				+ "<ak:first>Bob</ak:first>\n"
//				+ "<ak:last>Allan</ak:last>\n"
//				+ "</ak:patientName>\n"
//				+ "<ak:patientContact>\n"
//				+ "<ak:street>51 Main road</ak:street>\n"
//				+ "<ak:city>Gampaha</ak:city>\n"
//				+ "<ak:state>Western</ak:state>\n"
//				+ "<ak:zip>11730</ak:zip>\n"
//				+ "<ak:phone>94332189873</ak:phone>\n"
//				+ "<ak:email>bob@asela.com</ak:email>\n"
//				+ "</ak:patientContact>\n"
//				+ "<ak:patientDoB>1991-05-11</ak:patientDoB>\n"
//				+ "<ak:patientGender>male</ak:patientGender>\n"
//				+ "</ak:patient>\n"
//				+ "<ak:patient>\n"
//				+ "<ak:patientId>p222</ak:patientId>\n"
//				+ "<ak:patientName>\n"
//				+ "<ak:first>Joe</ak:first>\n"
//				+ "<ak:last>Allan</ak:last>\n"
//				+ "</ak:patientName>\n"
//				+ "<ak:patientContact>\n"
//				+ "<ak:street>51 Main road</ak:street>\n"
//				+ "<ak:city>Gampaha</ak:city>\n"
//				+ "<ak:state>Western</ak:state>\n"
//				+ "<ak:zip>11730</ak:zip>\n"
//				+ "<ak:phone>94332189873</ak:phone>\n"
//				+ "<ak:email>joe@xacmlinfo.com</ak:email>\n"
//				+ "</ak:patientContact>\n"
//				+ "<ak:patientDoB>1991-05-11</ak:patientDoB>\n"
//				+ "<ak:patientGender>male</ak:patientGender>\n"
//				+ "</ak:patient>\n"
//				+ "<ak:patient>\n"
//				+ "<ak:patientId>p333</ak:patientId>\n"
//				+ "<ak:patientName>\n"
//				+ "<ak:first>Peter</ak:first>\n"
//				+ "<ak:last>Allan</ak:last>\n"
//				+ "</ak:patientName>\n"
//				+ "<ak:patientContact>\n"
//				+ "<ak:street>51 Main road</ak:street>\n"
//				+ "<ak:city>Gampaha</ak:city>\n"
//				+ "<ak:state>Western</ak:state>\n"
//				+ "<ak:zip>11730</ak:zip>\n"
//				+ "<ak:phone>94332189873</ak:phone>\n"
//				+ "<ak:email>peter@xacmlinfo.com</ak:email>\n"
//				+ "</ak:patientContact>\n"
//				+ "<ak:patientDoB>1991-05-11</ak:patientDoB>\n"
//				+ "<ak:patientGender>male</ak:patientGender>\n"
//				+ "</ak:patient>\n"
//				+ "<ak:patient>\n"
//				+ "<ak:patientId>p444</ak:patientId>\n"
//				+ "<ak:patientName>\n"
//				+ "<ak:first>Alice</ak:first>\n"
//				+ "<ak:last>Allan</ak:last>\n"
//				+ "</ak:patientName>\n"
//				+ "<ak:patientContact>\n"
//				+ "<ak:street>51 Main road</ak:street>\n"
//				+ "<ak:city>Gampaha</ak:city>\n"
//				+ "<ak:state>Western</ak:state>\n"
//				+ "<ak:zip>11730</ak:zip>\n"
//				+ "<ak:phone>94332189873</ak:phone>\n"
//				+ "<ak:email>alice@asela.com</ak:email>\n"
//				+ "</ak:patientContact>\n"
//				+ "<ak:patientDoB>1991-05-11</ak:patientDoB>\n"
//				+ "<ak:patientGender>male</ak:patientGender>\n"
//				+ "</ak:patient>\n"
//				+ "</ak:record>\n"
//				+ "</Content>\n"
//				+ "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:3.0:profile:multiple:content-selector\">\n"
//				+ "<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">/ak:record/ak:patient</AttributeValue>\n"
//				+ "</Attribute>\n"
//				+ "</Attributes>\n"
//				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">\n"
//				+ "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\">\n"
//				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">read</AttributeValue>\n"
//				+ "</Attribute>\n" + "</Attributes>\n" + "</Request>";
		
		 String request = "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n"
				 + "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n"		
				 + "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" Issuer=\"med.example.com\">\n"
				 + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Name of Department Chair</AttributeValue>\n"
				 + "</Attribute>\n"
				 + "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:position.type\">\n"
				 + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Tenured/tenure-track faculty</AttributeValue>\n"
				 + "</Attribute>\n"
				 + "</Attributes>\n"
				 + "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">\n"
				 + "<Content>\n"
				 + "<ak:record xmlns:ak='http://akpower.org'>\n"
				 + "<ak:patient>\n"
				 + "<ak:patientId>bob</ak:patientId>\n"
				 + "<ak:patientName>\n"
				 + "<ak:first>Bob</ak:first>\n"
				 + "<ak:last>Allan</ak:last>\n"
				 + "</ak:patientName>\n"
				 + "<ak:patientContact>\n"
				 + "<ak:street>51 Main road</ak:street>\n"
				 + "<ak:city>Gampaha</ak:city>\n"
				 + "<ak:state>Western</ak:state>\n"
				 + "<ak:zip>11730</ak:zip>\n"
				 + "<ak:phone>94332189873</ak:phone>\n"
				 + "<ak:email>asela@gmail.com</ak:email>\n"
				 + "</ak:patientContact>\n"
				 + "<ak:patientDoB>1991-05-11</ak:patientDoB>\n"
				 + "<ak:patientGender>male</ak:patientGender>\n"
				 + "</ak:patient>\n"
				 + "</ak:record>\n"
				 + "</Content>\n"
				 + "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:3.0:profile:multiple:content-selector\">\n"
				 + "<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">/ak:record/ak:patient</AttributeValue>\n"
				 + "</Attribute>\n"
				 + "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:proposal.section\">\n"
				 + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Whole Proposal</AttributeValue>\n"
				 + "</Attribute>\n"
				 + "</Attributes>\n"
				 + "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">\n"
				 + "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:proposal.action\">\n"
				 + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Add</AttributeValue>\n"
				 + "</Attribute>\n" + "</Attributes>\n" + "</Request>";
		
		// ResponseCtx responseCtx = getResponse(request);
		System.out.println(request);
		ResponseCtx responseCtx = TestUtil.evaluate(getPDPNewInstance(),
				request);
		if (responseCtx != null) {
			System.out.println("Response that is received from the PDP :  "
					+ responseCtx.encode());
			Set<AbstractResult> set = responseCtx.getResults();
			Iterator<AbstractResult> it = set.iterator();
			int intDecision = 3;
			while (it.hasNext()) {
				ar = it.next();
				intDecision = ar.getDecision();

				// List<ObligationResult> obligations = ar.getObligations();
				//
				// System.out.println("Obligations = " + obligations);

				System.out.println("\nPrinting Obligations\n");
				List<ObligationResult> obligations = ar.getObligations();
				for (ObligationResult obligation : obligations) {
					if (obligation instanceof org.wso2.balana.xacml3.Obligation) {
						List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
								.getAssignments();
						for (AttributeAssignment assignment : assignments) {
							System.out.println("Obligation :  "
									+ assignment.getContent() + "\n\n");
						}
					}
				}

				System.out.println("\nPrinting Advices\n");
				List<Advice> advices = ar.getAdvices();
				for (Advice advice : advices) {
					if (advice instanceof org.wso2.balana.xacml3.Advice) {
						List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Advice) advice)
								.getAssignments();
						for (AttributeAssignment assignment : assignments) {
							System.out.println("Advice :  "
									+ assignment.getContent() + "\n\n");
						}
					}
				}

				if (intDecision >= 4 && intDecision <= 6) {
					intDecision = 2;
				}
				System.out.println("Decision:" + intDecision + " that is: "
						+ AbstractResult.DECISIONS[intDecision]);
				break; // WARNING: We currently take the first decision as the
						// final
				// one, but multipul decisions may be returned
			}
			return AbstractResult.DECISIONS[intDecision];
		} else {
			System.out.println("Response received PDP is Null");
		}
		return null;
	}

	private PDP getPDPNewInstance() {
		initBalana();

		Balana balana = Balana.getInstance();
		PDPConfig pdpConfig = balana.getPdpConfig();
		pdpConfig = new PDPConfig(pdpConfig.getAttributeFinder(),
				pdpConfig.getPolicyFinder(), pdpConfig.getResourceFinder(),
				true);
		return new PDP(pdpConfig);

	}

	// private PDP getPDPNewInstance() {
	// try {
	// PDPConfig pdpConfig = balana.getPdpConfig();
	// AttributeFinder attributeFinder = pdpConfig.getAttributeFinder();
	// List<AttributeFinderModule> finderModules = attributeFinder
	// .getModules();
	// attributeFinder.setModules(finderModules);
	//
	// return new PDP(new PDPConfig(attributeFinder,
	// pdpConfig.getPolicyFinder(), pdpConfig.getResourceFinder(),
	// true));
	// } catch (Exception e) {
	// return null;
	// }
	// }

	public static void main(String[] args) throws Exception {
		Accesscontrol ac = new Accesscontrol();
		// ac.getXACMLdecision("Faculty", "Proposal", "Create");
		HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();
		Multimap<String, String> subjectMap = ArrayListMultimap.create();
		Multimap<String, String> resourceMap = ArrayListMultimap.create();
		Multimap<String, String> actionMap = ArrayListMultimap.create();

		// Test case for Rule : FacultyCreateProposal-Rule1
		subjectMap.put("position-type", "Tenured/tenure-track faculty");
		// subjectMap.put("position-title", "Assistant Research Professor");
		// subjectMap.put("proposal-role", "PI");
		attrMap.put("Subject", subjectMap);

		resourceMap.put("proposal-section", "Whole Proposal");
		// resourceMap.put("status", "Withdraw by Research Office");
		attrMap.put("Resource", resourceMap);

		actionMap.put("proposal-action", "Create");
		// actionMap.put("proposal-section-action", "View");
		attrMap.put("Action", actionMap);

		// Multimap<String, String> environmentMap = ArrayListMultimap.create();
		// environmentMap.put("device-type", "Android Device");
		// environmentMap.put("network-type", "Campus");
		// attrMap.put("Environment", environmentMap);

		ac.getXACMLdecision(attrMap);
	}
}
