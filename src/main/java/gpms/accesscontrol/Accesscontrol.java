package gpms.accesscontrol;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
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
						// .getCanonicalPath() + File.separator +"policy";
						System.setProperty(
								FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY,
								policyLocation);
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

		return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ resource
				+ "</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:action-category:action\">"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ action
				+ "</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ userName
				+ "</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>" + "</Request>";

	}

	private String createXACMLRequest(String userName, String resource,
			String action, String environment) {

		return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ action
				+ "</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ userName
				+ "</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ resource
				+ "</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:environment\">"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:environment:environment-id\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ environment
				+ "</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>" + "</Request>";

	}

	private ResponseCtx getResponse(String request) {
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

	public String getXACMLdecision(
			HashMap<String, Multimap<String, String>> attrMap) {
		// String request = createXACMLRequest(attrMap);

		// String request = "<Request
		// xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\"
		// ReturnPolicyIdList=\"false\" CombinedDecision=\"false\">"
		// +"<Attributes
		// Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\"
		// >"
		// +"<Attribute IncludeInResult=\"false\"
		// AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\">"
		// +"<AttributeValue
		// DataType=\"http://www.w3.org/2001/XMLSchema#string\">asela@asela.com</AttributeValue>"
		// +"</Attribute>"
		// +"</Attributes>"
		// +"<Attributes
		// Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">"
		// +"<Content>"
		// +"<ak:record xmlns:ak=\"http://akpower.org\">"
		// +"<ak:patient>"
		// +"<ak:patientId>p111</ak:patientId>"
		// +"<ak:patientName>"
		// +"<ak:first>Bob</ak:first>"
		// +"<ak:last>Allan</ak:last>"
		// +"</ak:patientName>"
		// +"<ak:patientContact>"
		// +"<ak:street>51 Main road</ak:street>"
		// +"<ak:city>Gampaha</ak:city>"
		// +"<ak:state>Western</ak:state>"
		// +"<ak:zip>11730</ak:zip>"
		// +"<ak:phone>94332189873</ak:phone>"
		// +"<ak:email>bob@asela.com</ak:email>"
		// +"</ak:patientContact>"
		// +"<ak:patientDoB>1991-05-11</ak:patientDoB>"
		// +"<ak:patientGender>male</ak:patientGender>"
		// +"</ak:patient>"
		// +"<ak:patient>"
		// +"<ak:patientId>p222</ak:patientId>"
		// +"<ak:patientName>"
		// +"<ak:first>Joe</ak:first>"
		// +"<ak:last>Allan</ak:last>"
		// +"</ak:patientName>"
		// +"<ak:patientContact>"
		// +"<ak:street>51 Main road</ak:street>"
		// +"<ak:city>Gampaha</ak:city>"
		// +"<ak:state>Western</ak:state>"
		// +"<ak:zip>11730</ak:zip>"
		// +"<ak:phone>94332189873</ak:phone>"
		// +"<ak:email>joe@xacmlinfo.com</ak:email>"
		// +"</ak:patientContact>"
		// +"<ak:patientDoB>1991-05-11</ak:patientDoB>"
		// +"<ak:patientGender>male</ak:patientGender>"
		// +"</ak:patient>"
		// +"<ak:patient>"
		// +"<ak:patientId>p333</ak:patientId>"
		// +"<ak:patientName>"
		// +"<ak:first>Peter</ak:first>"
		// +"<ak:last>Allan</ak:last>"
		// +"</ak:patientName>"
		// +"<ak:patientContact>"
		// +"<ak:street>51 Main road</ak:street>"
		// +"<ak:city>Gampaha</ak:city>"
		// +"<ak:state>Western</ak:state>"
		// +"<ak:zip>11730</ak:zip>"
		// +"<ak:phone>94332189873</ak:phone>"
		// +"<ak:email>peter@xacmlinfo.com</ak:email>"
		// +"</ak:patientContact>"
		// +"<ak:patientDoB>1991-05-11</ak:patientDoB>"
		// +"<ak:patientGender>male</ak:patientGender>"
		// +"</ak:patient>"
		// +"<ak:patient>"
		// +"<ak:patientId>p444</ak:patientId>"
		// +"<ak:patientName>"
		// +"<ak:first>Alice</ak:first>"
		// +"<ak:last>Allan</ak:last>"
		// +"</ak:patientName>"
		// +"<ak:patientContact>"
		// +"<ak:street>51 Main road</ak:street>"
		// +"<ak:city>Gampaha</ak:city>"
		// +"<ak:state>Western</ak:state>"
		// +"<ak:zip>11730</ak:zip>"
		// +"<ak:phone>94332189873</ak:phone>"
		// +"<ak:email>alice@asela.com</ak:email>"
		// +"</ak:patientContact>"
		// +"<ak:patientDoB>1991-05-11</ak:patientDoB>"
		// +"<ak:patientGender>male</ak:patientGender>"
		// +"</ak:patient>"
		// +"</ak:record>"
		// +"</Content>"
		// +"<Attribute IncludeInResult=\"false\"
		// AttributeId=\"urn:oasis:names:tc:xacml:3.0:profile:multiple:content-selector\">"
		// +"<AttributeValue
		// XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\"
		// DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">/ak:record/ak:patient</AttributeValue>"
		// +"</Attribute>"
		// +"</Attributes>"
		// +"<Attributes
		// Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">"
		// +"<Attribute IncludeInResult=\"false\"
		// AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\">"
		// +"<AttributeValue
		// DataType=\"http://www.w3.org/2001/XMLSchema#string\">read</AttributeValue>"
		// +"</Attribute>" +"</Attributes>" +"</Request>";

		String request = "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">"
				+ "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" Issuer=\"med.example.com\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Name of Department Chair</AttributeValue>"
				+ "</Attribute>"
				+ "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:position.type\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Tenured/tenure-track faculty</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">"
				+ "<Content>"
				+ "<ak:record xmlns:ak='http://akpower.org'>"
				+ "<ak:patient>"
				+ "<ak:patientId>bob</ak:patientId>"
				+ "<ak:patientName>"
				+ "<ak:first>Bob</ak:first>"
				+ "<ak:last>Allan</ak:last>"
				+ "</ak:patientName>"
				+ "<ak:patientContact>"
				+ "<ak:street>51 Main road</ak:street>"
				+ "<ak:city>Gampaha</ak:city>"
				+ "<ak:state>Western</ak:state>"
				+ "<ak:zip>11730</ak:zip>"
				+ "<ak:phone>94332189873</ak:phone>"
				+ "<ak:email>asela@gmail.com</ak:email>"
				+ "</ak:patientContact>"
				+ "<ak:patientDoB>1991-05-11</ak:patientDoB>"
				+ "<ak:patientGender>male</ak:patientGender>"
				+ "</ak:patient>"
				+ "<ak:patient>"
				+ "<ak:patientId>alice</ak:patientId>"
				+ "<ak:patientName>"
				+ "<ak:first>Alice</ak:first>"
				+ "<ak:last>In Wonderland</ak:last>"
				+ "</ak:patientName>"
				+ "<ak:patientContact>"
				+ "<ak:street>51 Main road</ak:street>"
				+ "<ak:city>Gampaha</ak:city>"
				+ "<ak:state>Western</ak:state>"
				+ "<ak:zip>11730</ak:zip>"
				+ "<ak:phone>94332189873</ak:phone>"
				+ "<ak:email>alice@gmail.com</ak:email>"
				+ "</ak:patientContact>"
				+ "<ak:patientDoB>1991-05-11</ak:patientDoB>"
				+ "<ak:patientGender>male</ak:patientGender>"
				+ "</ak:patient>"
				+ "</ak:record>"
				+ "</Content>"
				+ "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:3.0:profile:multiple:content-selector\">"
				+ "<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">/ak:record/ak:patient</AttributeValue>"
				+ "</Attribute>"
				+ "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:proposal.section\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Whole Proposal</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">"
				+ "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:proposal.action\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Add</AttributeValue>"
				+ "</Attribute>" + "</Attributes>" + "</Request>";

		ResponseCtx response = getResponse(request);

		// ResponseCtx response = TestUtil.evaluate(getPDPNewInstance(),
		// request);

		if (response != null) {
			System.out
					.println("\n======================== XACML Response ====================");
			System.out.println(response.encode());
			System.out
					.println("===========================================================");
			Set<AbstractResult> set = response.getResults();
			Iterator<AbstractResult> it = set.iterator();
			int intDecision = 3;
			while (it.hasNext()) {
				ar = it.next();
				intDecision = ar.getDecision();

				System.out
						.println("\n======================== Printing Obligations ====================");
				List<ObligationResult> obligations = ar.getObligations();
				for (ObligationResult obligation : obligations) {
					if (obligation instanceof org.wso2.balana.xacml3.Obligation) {
						List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
								.getAssignments();
						for (AttributeAssignment assignment : assignments) {
							System.out.println("Obligation :  "
									+ assignment.getContent() + "\n");
						}
					}
				}
				System.out
						.println("===========================================================");
				// System.out.println("\n");

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
				// System.out.println("\n");

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
		try {
			PDPConfig pdpConfig = balana.getPdpConfig();
			pdpConfig = new PDPConfig(pdpConfig.getAttributeFinder(),
					pdpConfig.getPolicyFinder(), pdpConfig.getResourceFinder(),
					true);
			return new PDP(pdpConfig);
		} catch (Exception e) {
			return null;
		}
	}

	private String createXACMLRequest(
			HashMap<String, Multimap<String, String>> attributesMap) {

		System.out.println("Attribute Policy Request List\n"
				+ this.attrSpreadSheet.getAllAttributeRecords());

		String subjectAttr = "";
		String resourceAttr = "";
		String actionAttr = "";
		String environmentAttr = "";

		for (Entry<String, Multimap<String, String>> entry : attributesMap
				.entrySet()) {

			Set<String> keySet = entry.getValue().keySet();
			Iterator<String> keyIterator = keySet.iterator();

			switch (entry.getKey()) {
			case "Subject":
				boolean isFirstSubject = true;
				while (keyIterator.hasNext()) {
					String key = (String) keyIterator.next();
					Collection<String> values = entry.getValue().get(key);
					AttributeRecord attrRecord = this.attrSpreadSheet
							.findAttributeRecord(key);
					if (attrRecord != null) {
						for (String value : values) {
							if (attrRecord.getValues().contains(value)) {
								System.out.println(key + " :::::: " + value);
								if (isFirstSubject) {
									subjectAttr += "<Attributes Category=\""
											+ attrRecord.getCategory() + "\">";
								}
								subjectAttr += "<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>";
								isFirstSubject = false;
							}
						}
					}
				}
				subjectAttr += "</Attributes>";
				break;
			case "Resource":
				boolean isFirstResource = true;
				while (keyIterator.hasNext()) {
					String key = (String) keyIterator.next();
					Collection<String> values = entry.getValue().get(key);
					AttributeRecord attrRecord = this.attrSpreadSheet
							.findAttributeRecord(key);
					if (attrRecord != null) {
						for (String value : values) {
							if (attrRecord.getValues().contains(value)) {
								System.out.println(key + " :::::: " + value);
								if (isFirstResource) {
									resourceAttr += "<Attributes Category=\""
											+ attrRecord.getCategory() + "\">";
								}

								resourceAttr += "<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>";
								isFirstResource = false;
							}
						}
					}
				}
				resourceAttr += "</Attributes>";
				break;
			case "Action":
				boolean isFirstAction = true;
				while (keyIterator.hasNext()) {
					String key = (String) keyIterator.next();
					Collection<String> values = entry.getValue().get(key);
					AttributeRecord attrRecord = this.attrSpreadSheet
							.findAttributeRecord(key);
					if (attrRecord != null) {
						for (String value : values) {
							if (attrRecord.getValues().contains(value)) {
								System.out.println(key + " :::::: " + value);
								if (isFirstAction) {
									actionAttr += "<Attributes Category=\""
											+ attrRecord.getCategory() + "\">";
								}

								actionAttr += "<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>";
								isFirstAction = false;
							}
						}
					}
				}
				actionAttr += "</Attributes>";
				break;
			case "Environment":
				boolean isFirstEnvironment = true;
				while (keyIterator.hasNext()) {
					String key = (String) keyIterator.next();
					Collection<String> values = entry.getValue().get(key);
					AttributeRecord attrRecord = this.attrSpreadSheet
							.findAttributeRecord(key);
					if (attrRecord != null) {
						for (String value : values) {
							if (attrRecord.getValues().contains(value)) {
								System.out.println(key + " :::::: " + value);
								if (isFirstEnvironment) {
									environmentAttr += "<Attributes Category=\""
											+ attrRecord.getCategory() + "\">";
								}

								environmentAttr += "<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>";

								isFirstEnvironment = false;
							}
						}
					}
				}
				environmentAttr += "</Attributes>";
				break;

			default:
				break;
			}

		}

		return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">"
				+ resourceAttr
				+ actionAttr
				+ subjectAttr
				+ environmentAttr
				+ "</Request>";
	}

}
