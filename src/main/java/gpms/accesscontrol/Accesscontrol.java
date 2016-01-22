package gpms.accesscontrol;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.wso2.balana.Balana;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ctx.AbstractRequestCtx;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.RequestCtxFactory;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.finder.AttributeFinder;
import org.wso2.balana.finder.AttributeFinderModule;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;

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
						// .getCanonicalPath() + File.separator + "policy";
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

	private PDP getPDPNewInstance() {
		try {
			PDPConfig pdpConfig = balana.getPdpConfig();
			AttributeFinder attributeFinder = pdpConfig.getAttributeFinder();
			List<AttributeFinderModule> finderModules = attributeFinder
					.getModules();
			attributeFinder.setModules(finderModules);

			return new PDP(new PDPConfig(attributeFinder,
					pdpConfig.getPolicyFinder(), null, true));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Does this need a user name or their position details?
	 * 
	 * @param userName
	 * @param resource
	 * @param action
	 * @return
	 */
	// private String createXACMLRequest(String userName, String resource,
	// String action) {
	//
	// return
	// "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n"
	// +
	// "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">\n"
	// +
	// "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\">\n"
	// + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
	// + action
	// + "</AttributeValue>\n"
	// + "</Attribute>\n"
	// + "</Attributes>\n"
	// +
	// "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n"
	// +
	// "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">\n"
	// + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
	// + userName
	// + "</AttributeValue>\n"
	// + "</Attribute>\n"
	// + "</Attributes>\n"
	// +
	// "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">\n"
	// +
	// "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" IncludeInResult=\"false\">\n"
	// + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
	// + resource
	// + "</AttributeValue>\n"
	// + "</Attribute>\n"
	// + "</Attributes>\n"
	// +"</Request>";
	//
	// }

	// ///////////////////////////////////////////////////////////////
	// ////////THIS IS A COPY THAT I AM MODIFYING FOR TESTING/////////
	// ///////////////////////////////////////////////////////////////

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
		String response = "";
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

	/**
	 * I made this public so that it can be accessed by ProposalService.java
	 * -Tommy
	 * 
	 * @param attrMap
	 * @return
	 */
	public String getXACMLdecision(
			HashMap<String, Multimap<String, String>> attrMap) {
		String request = createXACMLRequest(attrMap);
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

			Set keySet = entry.getValue().keySet();
			Iterator keyIterator = keySet.iterator();

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
											+ attrRecord.getCategory()
											+ "\">\n";
								}
								subjectAttr += "<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">\n"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>\n"
										+ "</Attribute>\n";
								isFirstSubject = false;
							}
						}
					}
				}
				subjectAttr += "</Attributes>\n";
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
											+ attrRecord.getCategory()
											+ "\">\n";
								}

								resourceAttr += "<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">\n"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>\n"
										+ "</Attribute>\n";
								isFirstResource = false;
							}
						}
					}
				}
				resourceAttr += "</Attributes>\n";
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
											+ attrRecord.getCategory()
											+ "\">\n";
								}

								actionAttr += "<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">\n"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>\n"
										+ "</Attribute>\n";
								isFirstAction = false;
							}
						}
					}
				}
				actionAttr += "</Attributes>\n";
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
											+ attrRecord.getCategory()
											+ "\">\n";
								}

								environmentAttr += "<Attribute AttributeId=\""
										+ attrRecord.getFullAttributeName()
										+ "\" IncludeInResult=\"false\">\n"
										+ "<AttributeValue DataType=\""
										+ attrRecord.getDataType() + "\">"
										+ value + "</AttributeValue>\n"
										+ "</Attribute>\n";

								isFirstEnvironment = false;
							}
						}
					}
				}
				environmentAttr += "</Attributes>\n";
				break;

			default:
				break;
			}

		}

		return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n"
				+ resourceAttr
				+ actionAttr
				+ subjectAttr
				+ environmentAttr
				+ "</Request>";
	}
}
