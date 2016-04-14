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
			AbstractRequestCtx arc = rcf.getRequestCtx(request.replaceAll(
					">\\s+<", "><"));
			rc = pdp.evaluate(arc);

		} catch (Exception e) {
			System.out.print("somethingwrong");
		}
		return rc;
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
		String request = createXACMLRequest(attrMap);

		ResponseCtx response = getResponse(request);

		// initBalana();
		// ResponseCtx response = PolicyTestUtil.evaluate(getPDPNewInstance(),
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

	public Set<AbstractResult> getXACMLdecisionWithObligations(
			HashMap<String, Multimap<String, String>> attrMap,
			StringBuffer contentProfile, String authorFullName) {
		// String request = createXACMLRequestWithProfile(attrMap,
		// contentProfile);

		String request = "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\" Issuer=\"med.example.com\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
				+ authorFullName
				+ "</AttributeValue>"
				+ "</Attribute>"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:position.type\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Tenured/tenure-track faculty</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">"
				+ "<Content>"
				+ "<ak:record xmlns:ak='http://akpower.org'>"
				+ "<ak:proposal>"
				+ "<ak:proposalid>5702a60865dbb30b09a492cf</ak:proposalid>"
				+ "<ak:proposaltitle>Proposal1</ak:proposaltitle>"
				+ "<ak:submittedbypi>Not Submitted</ak:submittedbypi>"
				+ "<ak:readyforsubmissionbypi>false</ak:readyforsubmissionbypi>"
				+ "<ak:deletedbypi>Deleted</ak:deletedbypi>"
				+ "<ak:approvedbydepartmentchair>Not Ready for Approval</ak:approvedbydepartmentchair>"
				+ "<ak:approvedbybusinessmanager>Not Ready for Approval</ak:approvedbybusinessmanager>"
				+ "<ak:approvedbyirb>Not Ready for Approval</ak:approvedbyirb>"
				+ "<ak:approvedbydean>Not Ready for Approval</ak:approvedbydean>"
				+ "<ak:approvedbyuniversityresearchadministrator>Not Ready for Approval</ak:approvedbyuniversityresearchadministrator>"
				+ "<ak:withdwarnbyuniversityresearchadmisntrator>Not Withdrawn</ak:withdwarnbyuniversityresearchadmisntrator>"
				+ "<ak:submittedbyuniversityresearchadminstrator>Not Submitted</ak:submittedbyuniversityresearchadminstrator>"
				+ "<ak:approvedbyuniversityresearchdirector>Not Deleted</ak:approvedbyuniversityresearchdirector>"
				+ "<ak:deletedbyuniversityresearchdirector>Not Deleted</ak:deletedbyuniversityresearchdirector>"
				+ "<ak:archivedbyuniversityresearchdirector>Not Archived</ak:archivedbyuniversityresearchdirector>"
				+ "<ak:authorprofile>"
				+ "<ak:fullname>Milson Munakami</ak:fullname>"
				+ "</ak:authorprofile>"
				+ "<ak:pi>"
				+ "<ak:fullname>Milson Munakami</ak:fullname>"
				+ "<ak:workemail>milsonmun@yahoo.com</ak:workemail>"
				+ "<ak:userid>56e45a50af68c71ea4248edf</ak:userid>"
				+ "</ak:pi>"
				+ "</ak:proposal>"
				+ "</ak:record>"
				+ "</Content>"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:profile:multiple:content-selector\" IncludeInResult=\"false\">"
				+ "<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">/ak:record/ak:proposal</AttributeValue>"
				+ "</Attribute>"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:proposal.section\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Whole Proposal</AttributeValue>"
				+ "</Attribute>"
				+ "</Attributes>"
				+ "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">"
				+ "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:proposal.action\" IncludeInResult=\"false\">"
				+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Add</AttributeValue>"
				+ "</Attribute>" + "</Attributes>" + "</Request>";

		ResponseCtx response = getResponse(request);

		if (response != null) {
			System.out
					.println("\n======================== XACML Response ====================");
			System.out.println(response.encode());
			System.out
					.println("===========================================================");

			Set<AbstractResult> set = response.getResults();
			return set;
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

		StringBuffer subjectAttr = new StringBuffer();
		StringBuffer resourceAttr = new StringBuffer();
		StringBuffer actionAttr = new StringBuffer();
		StringBuffer environmentAttr = new StringBuffer();

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
									subjectAttr
											.append("<Attributes Category=\""
													+ attrRecord.getCategory()
													+ "\">");
								}
								subjectAttr.append("<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>");
								isFirstSubject = false;
							}
						}
					}
				}
				subjectAttr.append("</Attributes>");
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
									resourceAttr
											.append("<Attributes Category=\""
													+ attrRecord.getCategory()
													+ "\">");
								}

								resourceAttr.append("<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>");

								isFirstResource = false;
							}
						}
					}
				}
				resourceAttr.append("</Attributes>");
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
									actionAttr.append("<Attributes Category=\""
											+ attrRecord.getCategory() + "\">");
								}

								actionAttr.append("<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>");
								isFirstAction = false;
							}
						}
					}
				}
				actionAttr.append("</Attributes>");
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
									environmentAttr
											.append("<Attributes Category=\""
													+ attrRecord.getCategory()
													+ "\">");
								}

								environmentAttr
										.append("<Attribute AttributeId=\""
												+ attrRecord
														.getFullAttributeName()
												+ "\" IncludeInResult=\"false\">"
												+ "<AttributeValue DataType=\""
												+ attrRecord.getDataType()
												+ "\">" + value
												+ "</AttributeValue>"
												+ "</Attribute>");

								isFirstEnvironment = false;
							}
						}
					}
				}
				environmentAttr.append("</Attributes>");
				break;

			default:
				break;
			}
		}

		StringBuffer finalRequest = new StringBuffer();

		finalRequest
				.append("<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">")
				.append(subjectAttr).append(resourceAttr).append(actionAttr)
				.append(environmentAttr).append("</Request>");

		return finalRequest.toString();
	}

	private String createXACMLRequestWithProfile(
			HashMap<String, Multimap<String, String>> attributesMap,
			StringBuffer contentProfile) {

		System.out.println("Attribute Policy Request List\n"
				+ this.attrSpreadSheet.getAllAttributeRecords());

		StringBuffer subjectAttr = new StringBuffer();
		StringBuffer resourceAttr = new StringBuffer();
		StringBuffer actionAttr = new StringBuffer();
		StringBuffer environmentAttr = new StringBuffer();

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
									subjectAttr
											.append("<Attributes Category=\""
													+ attrRecord.getCategory()
													+ "\">");
								}
								subjectAttr.append("<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>");
								isFirstSubject = false;
							}
						}
					}
				}
				subjectAttr.append("</Attributes>");
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
									resourceAttr
											.append("<Attributes Category=\""
													+ attrRecord.getCategory()
													+ "\">");

									resourceAttr.append(contentProfile);
								}

								resourceAttr.append("<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>");

								isFirstResource = false;
							}
						}
					}
				}
				resourceAttr.append("</Attributes>");
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
									actionAttr.append("<Attributes Category=\""
											+ attrRecord.getCategory() + "\">");
								}

								actionAttr.append("<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>"
										+ "</Attribute>");
								isFirstAction = false;
							}
						}
					}
				}
				actionAttr.append("</Attributes>");
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
									environmentAttr
											.append("<Attributes Category=\""
													+ attrRecord.getCategory()
													+ "\">");
								}

								environmentAttr
										.append("<Attribute AttributeId=\""
												+ attrRecord
														.getFullAttributeName()
												+ "\" IncludeInResult=\"false\">"
												+ "<AttributeValue DataType=\""
												+ attrRecord.getDataType()
												+ "\">" + value
												+ "</AttributeValue>"
												+ "</Attribute>");

								isFirstEnvironment = false;
							}
						}
					}
				}
				environmentAttr.append("</Attributes>");
				break;

			default:
				break;
			}
		}

		StringBuffer finalRequest = new StringBuffer();

		finalRequest
				.append("<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">")
				.append(subjectAttr).append(resourceAttr).append(actionAttr)
				.append(environmentAttr).append("</Request>");

		return finalRequest.toString();
	}

}
