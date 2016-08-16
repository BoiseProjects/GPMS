package gpms.utils;

import gpms.model.Delegation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;

public class WriteXMLUtil {

	private static String delegationXMLFileName = File.separator
			+ "DelegationPolicy.xml";

	public static String saveDelegationPolicy(String userProfileID,
			String delegatorName, String policyLocation,
			Delegation existingDelegation) {

		String delegationPolicyId = existingDelegation.getDelegationPolicyId();

		// Delegatee
		String delegateeId = existingDelegation.getDelegateeId();
		String delegateeName = existingDelegation.getDelegatee();

		String departmentName = existingDelegation.getDelegateeDepartment();
		String positionTitle = existingDelegation.getDelegateePositionTitle();

		List<String> actions = existingDelegation.getActions();

		// For Revocation
		String delegationId = existingDelegation.getId().toString();

		DateFormat policyDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssXXX");
		final String fromDate = policyDateFormat.format(existingDelegation
				.getFrom());
		final String toDate = policyDateFormat.format(existingDelegation
				.getTo());

		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(policyLocation + delegationXMLFileName);
		if (!xmlFile.exists()) {
			// PolicySet
			Namespace ns = Namespace
					.getNamespace("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
			Element policySet = new Element("PolicySet", ns);
			Namespace XSI = Namespace.getNamespace("xsi",
					"http://www.w3.org/2001/XMLSchema-instance");
			Namespace XACML = Namespace.getNamespace("xacml",
					"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
			policySet.addNamespaceDeclaration(XSI);
			policySet.addNamespaceDeclaration(XACML);
			policySet
					.setAttribute(
							"schemaLocation",
							"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd",
							XSI);

			String policySetId = "Dynamic-Delegation-Policy-Rules-For-"
					+ delegateeName + "-of-" + departmentName + "-"
					+ RandomStringUtils.randomAlphanumeric(8);
			policySet.setAttribute(new Attribute("PolicySetId", policySetId
					.replaceAll("\\s", "-")));

			policySet
					.setAttribute(new Attribute("PolicyCombiningAlgId",
							"urn:oasis:names:tc:xacml:3.0:rule-combining-algorithm:permit-overrides"));

			policySet.setAttribute(new Attribute("Version", "1.0"));

			policySet.addContent(new Element("Description")
					.setText("PolicySet for" + delegateeName + " of "
							+ departmentName + " with position title "
							+ positionTitle + " is delegated to " + actions
							+ " by " + delegatorName));
			policySet.addContent(new Element("Target"));

			Document doc = new Document(policySet);

			CustomXMLOutputProcessor output = new CustomXMLOutputProcessor();
			try {
				output.process(new FileWriter(policyLocation
						+ delegationXMLFileName), Format.getPrettyFormat(), doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Document doc = null;
		try {
			doc = (Document) builder.build(xmlFile);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}

		if (doc != null) {
			Element policySet = doc.getRootElement();

			if (delegationPolicyId == null || delegationPolicyId.isEmpty()) {

				return createPolicyNode(userProfileID, delegatorName,
						policyLocation, delegateeId, delegateeName,
						departmentName, positionTitle, actions, delegationId,
						fromDate, toDate, policySet, doc);
			} else {
				Namespace ns = Namespace
						.getNamespace("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
				List<Element> policyElements = doc.getRootElement()
						.getChildren("Policy", ns);

				for (Element policy : policyElements) {
					String policyId = policy.getAttributeValue("PolicyId");

					if (policyId.equals(existingDelegation
							.getDelegationPolicyId())) {
						policy.getParent().removeContent(policy);

						return createPolicyNode(userProfileID, delegatorName,
								policyLocation, delegateeId, delegateeName,
								departmentName, positionTitle, actions,
								delegationId, fromDate, toDate, policySet, doc);
					}
				}

			}
		}
		return "";
	}

	/**
	 * @param userProfileID
	 * @param delegatorName
	 * @param policyLocation
	 * @param delegateeId
	 * @param delegateeName
	 * @param departmentName
	 * @param positionTitle
	 * @param action
	 * @param delegationId
	 * @param fromDate
	 * @param toDate
	 * @param policyMap
	 * @param policySet
	 * @param doc
	 * @return
	 */
	private static String createPolicyNode(String userProfileID,
			String delegatorName, String policyLocation, String delegateeId,
			String delegateeName, String departmentName, String positionTitle,
			List<String> actions, String delegationId, final String fromDate,
			final String toDate, Element policySet, Document doc) {
		// Policy Goes here
		Element policy = new Element("Policy");

		String policyId = "Dynamic-Delegation-Policy-Rules-For-"
				+ delegateeName + "-of-" + departmentName + "-"
				+ RandomStringUtils.randomAlphanumeric(8);
		policyId = policyId.replaceAll("\\s", "-");
		policy.setAttribute(new Attribute("PolicyId", policyId));

		policy.setAttribute(new Attribute("RuleCombiningAlgId",
				"urn:oasis:names:tc:xacml:3.0:rule-combining-algorithm:permit-overrides"));

		policy.setAttribute(new Attribute("Version", "1.0"));

		policy.addContent(new Element("Description").setText(delegateeName
				+ " of " + departmentName + " with position title "
				+ positionTitle + " is delegated to " + actions + " by "
				+ delegatorName));

		policy.addContent(new Element("PolicyDefaults").setContent(new Element(
				"XPathVersion")
				.setText("http://www.w3.org/TR/1999/REC-xpath-19991116")));

		policy.addContent(new Element("Target"));

		// TODO Add Rules here need condition for different Users Delegation
		// cause it need to generate different Policy Rules based on
		// Position Title
		// Here done for Department Chair Delegation
		// START Rule for Editing Signature Part:
		// EditProposalSectionByDepartmentChair-Rule40 HERE
		// Rule elements
		Element rule1 = new Element("Rule");
		rule1.setAttribute(new Attribute("Effect", "Permit"));
		String ruleId = "DelegatedEditProposalSectionRuleFor-" + positionTitle
				+ "-DelegatedBy-" + delegatorName;
		rule1.setAttribute(new Attribute("RuleId", ruleId
				.replaceAll("\\s", "-")));
		rule1.addContent(new Element("Description")
				.setText(delegateeName
						+ " of "
						+ departmentName
						+ " with position title "
						+ positionTitle
						+ " can \"Edit\" \"Certification/Signatures\" when Delegated by "
						+ delegatorName
						+ " with position title \"Department Chair\" and ApprovedByDepartmentChair = READYFORAPPROVAL"));

		Element allOf1 = new Element("AllOf");
		allOf1.addContent(getMatch(positionTitle,
				"urn:oasis:names:tc:xacml:1.0:subject:position.title",
				"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

		allOf1.addContent(getMatch("Certification/Signatures",
				"urn:oasis:names:tc:xacml:1.0:resource:proposal.section",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

		allOf1.addContent(getMatch(
				"READYFORAPPROVAL",
				"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

		allOf1.addContent(getMatch("Edit",
				"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

		Element target1 = new Element("Target").addContent(new Element("AnyOf")
				.addContent(allOf1));

		rule1.addContent(target1);
		// END Rule EditProposalSectionByDepartmentChair-Rule40 HERE

		policy.addContent(rule1);

		// START Rule ApproveProposalByDepartmentChair-Rule13a HERE
		// Rule elements
		Element rule2 = new Element("Rule");
		rule2.setAttribute(new Attribute("Effect", "Permit"));
		ruleId = "DelegatedApproveProposalRule1For-" + positionTitle
				+ "-DelegatedBy-" + delegatorName;
		rule2.setAttribute(new Attribute("RuleId", ruleId
				.replaceAll("\\s", "-")));
		rule2.addContent(new Element("Description")
				.setText(delegateeName
						+ " of "
						+ departmentName
						+ " with position title "
						+ positionTitle
						+ " can \"Approve\" a \"Whole Proposal\" when Delegated by "
						+ delegatorName
						+ " with position title \"Department Chair\" and ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are not approved."));

		Element allOf2 = new Element("AllOf");

		allOf2.addContent(getMatch(positionTitle,
				"urn:oasis:names:tc:xacml:1.0:subject:position.title",
				"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

		allOf2.addContent(getMatch("Whole Proposal",
				"urn:oasis:names:tc:xacml:1.0:resource:proposal.section",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

		allOf2.addContent(getMatch(
				"READYFORAPPROVAL",
				"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

		Element target2 = new Element("Target").addContent(new Element("AnyOf")
				.addContent(allOf2));

		rule2.addContent(target2);

		// Condition elements
		Element function1 = new Element("Apply").setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:and");

		Element condition1 = new Element("Condition");

		function1
				.addContent(getConditionActionBag(
						"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of",
						"urn:oasis:names:tc:xacml:1.0:function:string-bag",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:action",
						"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
						"http://www.w3.org/2001/XMLSchema#string", actions));
		function1.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
				"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#boolean",
				"//ak:signedByAllChairs/text()", "false"));
		function1.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:authorprofile/ak:userid/text()", delegateeId));
		function1
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", fromDate));
		function1
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", toDate));

		condition1.addContent(function1);

		rule2.addContent(condition1);

		// ObligationExpressions
		Element ObligationExpression1 = new Element("ObligationExpressions");

		ObligationExpression1.addContent(getObligationExpressionAlert(
				"sendAlert", "Permit"));

		ObligationExpression1
				.addContent(getObligationExpressionSendEmail(
						"sendEmail",
						"Permit",
						"Hello User,&amp;lt;br/&amp;gt;&amp;lt;br/&amp;gt;The proposal has been approved by Department Chair. Now it is waiting for another Department Chair approval. &amp;lt;br/&amp;gt;&amp;lt;br/&amp;gt;Thank you, &amp;lt;br/&amp;gt; GPMS Team"));

		rule2.addContent(ObligationExpression1);

		// END Rule ApproveProposalByDepartmentChair-Rule13a HERE
		policy.addContent(rule2);

		// START Rule ApproveProposalByDepartmentChair-Rule13b HERE
		// Rule elements
		Element rule3 = new Element("Rule");
		rule3.setAttribute(new Attribute("Effect", "Permit"));
		ruleId = "DelegatedApproveProposalRule2For-" + positionTitle
				+ "-DelegatedBy-" + delegatorName;
		rule3.setAttribute(new Attribute("RuleId", ruleId
				.replaceAll("\\s", "-")));
		rule3.addContent(new Element("Description")
				.setText(delegateeName
						+ " of "
						+ departmentName
						+ " with position title "
						+ positionTitle
						+ " can \"Approve\" a \"Whole Proposal\" when Delegated by "
						+ delegatorName
						+ " with position title \"Department Chair\" and ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are not approved and no IRB is required."));

		Element allOf3 = new Element("AllOf");
		allOf3.addContent(getMatch(positionTitle,
				"urn:oasis:names:tc:xacml:1.0:subject:position.title",
				"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

		allOf3.addContent(getMatch("Whole Proposal",
				"urn:oasis:names:tc:xacml:1.0:resource:proposal.section",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

		allOf3.addContent(getMatch(
				"READYFORAPPROVAL",
				"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

		Element target3 = new Element("Target").addContent(new Element("AnyOf")
				.addContent(allOf3));

		rule3.addContent(target3);

		// Condition elements
		Element function2 = new Element("Apply").setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:and");

		Element condition2 = new Element("Condition");

		function2
				.addContent(getConditionActionBag(
						"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of",
						"urn:oasis:names:tc:xacml:1.0:function:string-bag",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:action",
						"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
						"http://www.w3.org/2001/XMLSchema#string", actions));

		function2.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
				"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#boolean",
				"//ak:signedByAllChairs/text()", "true"));

		function2.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
				"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#boolean",
				"//ak:irbApprovalRequired/text()", "false"));
		function2.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:authorprofile/ak:userid/text()", delegateeId));
		function2
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", fromDate));
		function2
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", toDate));

		condition2.addContent(function2);

		rule3.addContent(condition2);

		// ObligationExpressions
		Element obligationExpression2 = new Element("ObligationExpressions");

		obligationExpression2.addContent(getObligationExpressionAlert(
				"sendAlert", "Permit"));

		obligationExpression2
				.addContent(getObligationExpressionSendEmail(
						"sendEmail",
						"Permit",
						"Hello User,&lt;br/&gt;&lt;br/&gt;The proposal has been approved by all Department Chairs.&lt;br/&gt;&lt;br/&gt;Thank you, &lt;br/&gt; GPMS Team"));

		rule3.addContent(obligationExpression2);

		// END Rule ApproveProposalByDepartmentChair-Rule13b HERE
		policy.addContent(rule3);

		// START Rule ApproveProposalByDepartmentChair-Rule13c HERE
		// Rule elements
		Element rule4 = new Element("Rule");
		rule4.setAttribute(new Attribute("Effect", "Permit"));
		ruleId = "DelegatedApproveProposalRule3For-" + positionTitle
				+ "-DelegatedBy-" + delegatorName;
		rule4.setAttribute(new Attribute("RuleId", ruleId
				.replaceAll("\\s", "-")));
		rule4.addContent(new Element("Description")
				.setText(delegateeName
						+ " of "
						+ departmentName
						+ " with position title "
						+ positionTitle
						+ " can \"Approve\" a \"Whole Proposal\" when Delegated by "
						+ delegatorName
						+ " with position title \"Department Chair\" ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are approved and IRB is required."));

		Element allOf4 = new Element("AllOf");

		allOf4.addContent(getMatch(positionTitle,
				"urn:oasis:names:tc:xacml:1.0:subject:position.title",
				"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

		allOf4.addContent(getMatch("Whole Proposal",
				"urn:oasis:names:tc:xacml:1.0:resource:proposal.section",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

		allOf4.addContent(getMatch(
				"READYFORAPPROVAL",
				"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

		Element target4 = new Element("Target").addContent(new Element("AnyOf")
				.addContent(allOf4));

		rule4.addContent(target4);

		// Condition elements
		Element function3 = new Element("Apply").setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:and");

		Element condition3 = new Element("Condition");

		function3
				.addContent(getConditionActionBag(
						"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of",
						"urn:oasis:names:tc:xacml:1.0:function:string-bag",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:action",
						"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
						"http://www.w3.org/2001/XMLSchema#string", actions));

		function3.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
				"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#boolean",
				"//ak:signedByAllChairs/text()", "true"));

		function3.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
				"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#boolean",
				"//ak:irbApprovalRequired/text()", "true"));
		function3.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:authorprofile/ak:userid/text()", delegateeId));
		function3
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", fromDate));
		function3
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", toDate));

		condition3.addContent(function3);

		rule4.addContent(condition3);

		// ObligationExpressions
		Element obligationExpression3 = new Element("ObligationExpressions");

		obligationExpression3.addContent(getObligationExpressionAlert(
				"sendAlert", "Permit"));

		obligationExpression3
				.addContent(getObligationExpressionSendEmail(
						"sendEmail",
						"Permit",
						"Hello User,&lt;br/&gt;&lt;br/&gt;The proposal has been approved by all Department Chairs.&lt;br/&gt;&lt;br/&gt;Thank you, &lt;br/&gt; GPMS Team"));

		rule4.addContent(obligationExpression3);

		// END Rule ApproveProposalByDepartmentChair-Rule13c HERE
		policy.addContent(rule4);

		// Add Revocation Rule HERE
		Element rule5 = new Element("Rule");
		rule5.setAttribute(new Attribute("Effect", "Permit"));
		ruleId = "Revoke " + policyId + " by Department Chair";
		rule5.setAttribute(new Attribute("RuleId", ruleId
				.replaceAll("\\s", "-")));
		rule5.addContent(new Element("Description")
				.setText("\"Department Chair\" can \"Revoke\" delegation from "
						+ delegateeName + " of " + departmentName
						+ " with position title " + positionTitle));

		Element allOf5 = new Element("AllOf");

		allOf5.addContent(getMatch("Revoke",
				"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

		Element target5 = new Element("Target").addContent(new Element("AnyOf")
				.addContent(allOf5));

		rule5.addContent(target5);

		// Condition elements
		Element function4 = new Element("Apply").setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:and");

		Element condition4 = new Element("Condition");

		function4.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:delegationid/text()", delegationId));

		function4.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:delegator/ak:id/text()", userProfileID));

		function4.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:delegationpolicyid/text()", policyId));

		function4.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
				"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#boolean",
				"//ak:revoked/text()", "false"));

		condition4.addContent(function4);

		rule5.addContent(condition4);

		// ObligationExpressions
		Element obligationExpression4 = new Element("ObligationExpressions");

		obligationExpression4
				.addContent(getObligationExpressionForRevokeSendEmail(
						"sendEmail",
						"Permit",
						"Hello User,&lt;br/&gt;&lt;br/&gt;You have been revoked from your delegation. &lt;br/&gt;&lt;br/&gt;Thank you, &lt;br/&gt; GPMS Team"));

		rule5.addContent(obligationExpression4);

		// END Rule Revocation HERE
		policy.addContent(rule5);

		// Add Action Button Show Rule HERE
		Element rule6 = new Element("Rule");
		rule6.setAttribute(new Attribute("Effect", "Permit"));
		ruleId = actions + "ShowFor" + positionTitle;
		rule6.setAttribute(new Attribute("RuleId", ruleId.replaceAll(
				"[\\[\\]\\s\\,]", "")));
		rule6.addContent(new Element("Description").setText("'" + positionTitle
				+ "' can see '" + actions
				+ "' button when ApprovedByDepartmentChair = READYFORAPPROVAL"));

		Element allOf6 = new Element("AllOf");

		allOf6.addContent(getMatch(positionTitle,
				"urn:oasis:names:tc:xacml:1.0:subject:position.title",
				"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

		Element target6 = new Element("Target").addContent(new Element("AnyOf")
				.addContent(allOf6));

		rule6.addContent(target6);

		// Condition elements
		Element function5 = new Element("Apply").setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:and");

		Element condition5 = new Element("Condition");

		function5
				.addContent(getConditionActionBag(
						"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of",
						"urn:oasis:names:tc:xacml:1.0:function:string-bag",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:action",
						"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
						"http://www.w3.org/2001/XMLSchema#string", actions));

		function5.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:approvedbydepartmentchair/text()", "READYFORAPPROVAL"));

		function5.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:authorprofile/ak:userid/text()", delegateeId));
		function5
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", fromDate));
		function5
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", toDate));

		condition5.addContent(function5);

		rule6.addContent(condition5);

		// END Action Button Show Rule for Delegatee HERE
		policy.addContent(rule6);

		// Add Action Button Hide Rule for Delegator HERE
		Element rule7 = new Element("Rule");
		rule7.setAttribute(new Attribute("Effect", "Deny"));
		ruleId = actions + "HideForDepartmentChair";
		rule7.setAttribute(new Attribute("RuleId", ruleId.replaceAll(
				"[\\[\\]\\s\\,]", "")));
		rule7.addContent(new Element("Description")
				.setText("'Department Chair' can not see '"
						+ actions
						+ "' button when ApprovedByDepartmentChair = READYFORAPPROVAL and Delegated to = "
						+ positionTitle));

		Element allOf7 = new Element("AllOf");

		allOf7.addContent(getMatch("Department Chair",
				"urn:oasis:names:tc:xacml:1.0:subject:position.title",
				"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

		Element target7 = new Element("Target").addContent(new Element("AnyOf")
				.addContent(allOf7));

		rule7.addContent(target7);

		// Condition elements
		Element function6 = new Element("Apply").setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:and");

		Element condition6 = new Element("Condition");

		function6
				.addContent(getConditionActionBag(
						"urn:oasis:names:tc:xacml:1.0:function:string-at-least-one-member-of",
						"urn:oasis:names:tc:xacml:1.0:function:string-bag",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:action",
						"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
						"http://www.w3.org/2001/XMLSchema#string", actions));

		function6.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:approvedbydepartmentchair/text()", "READYFORAPPROVAL"));

		function6.addContent(getCondition(
				"urn:oasis:names:tc:xacml:1.0:function:string-equal",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:authorprofile/ak:userid/text()", userProfileID));
		function6
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", fromDate));
		function6
				.addContent(getCondition(
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
						"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
						"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
						"http://www.w3.org/2001/XMLSchema#dateTime",
						"//ak:currentdatetime/text()", toDate));

		condition6.addContent(function6);

		rule7.addContent(condition6);

		// END Action Button Show Rule HERE
		policy.addContent(rule7);

		// Append Policy to the Root PolicySet
		policySet.addContent(policy);

		// display nice nice
		CustomXMLOutputProcessor output = new CustomXMLOutputProcessor();
		try {
			output.process(new FileWriter(policyLocation
					+ delegationXMLFileName), Format.getPrettyFormat(), doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("File Saved!" + policyId);
		return policyId;
	}

	private static Element getObligationExpressionForRevokeSendEmail(
			String obligationId, String fullFillOn, String emailBody) {
		// ObligationExpression STARTS here
		Element obligationExpression = new Element("ObligationExpression")
				.setAttribute("ObligationId", obligationId).setAttribute(
						"FulfillOn", fullFillOn);

		// obligationExpression.addContent(getObligationAssignmentAttrValue(
		// "obligationType", "postobligation"));

		obligationExpression.addContent(getObligationAssignmentAttrValue(
				"emailBody", emailBody));

		obligationExpression.addContent(getObligationAssignmentAttrValue(
				"emailSubject", "Your delegation is revoked by: "));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"delegatorName",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:delegator/ak:fullname/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"delegatorEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:delegator/ak:email/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"delegateeName",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:delegatee/ak:fullname/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"delegateeEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:delegatee/ak:email/text()"));

		return obligationExpression;
	}

	private static Element getObligationExpressionSendEmail(
			String obligationId, String fullFillOn, String emailBody) {
		// ObligationExpression STARTS here
		Element obligationExpression = new Element("ObligationExpression")
				.setAttribute("ObligationId", obligationId).setAttribute(
						"FulfillOn", fullFillOn);

		obligationExpression.addContent(getObligationAssignmentAttrValue(
				"obligationType", "postobligation"));

		obligationExpression.addContent(getObligationAssignmentAttrValue(
				"emailBody", emailBody));

		obligationExpression.addContent(getObligationAssignmentAttrValue(
				"emailSubject", "Your proposal has been approved by: "));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"authorName",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:authorprofile/ak:fullname/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"piEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:pi/ak:workemail/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"copisEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:copi/ak:workemail/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"seniorsEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:senior/ak:workemail/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"chairsEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:chair/ak:workemail/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"managersEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:manager/ak:workemail/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"irbsEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:irb/ak:workemail/text()"));

		return obligationExpression;
	}

	private static Element getMatch(String attrVal, String attrId,
			String attrCategory) {
		Element match = new Element("Match").setAttribute("MatchId",
				"urn:oasis:names:tc:xacml:1.0:function:string-equal");
		match.addContent(new Element("AttributeValue").setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#string").setText(attrVal));
		match.addContent(new Element("AttributeDesignator")
				.setAttribute("AttributeId", attrId)
				.setAttribute("Category", attrCategory)
				.setAttribute("DataType",
						"http://www.w3.org/2001/XMLSchema#string")
				.setAttribute("MustBePresent", "false"));

		return match;
	}

	private static Element getCondition(String functionId,
			String compareFunctionId, String attrSelectorCategory,
			String attrSelectorDataType, String attrSelectorPath,
			String attrValue) {

		Element conditionApply = new Element("Apply")
				.setAttribute("FunctionId", functionId)
				.addContent(
						new Element("Apply").setAttribute("FunctionId",
								compareFunctionId)
								.addContent(
										new Element("AttributeSelector")
												.setAttribute("Category",
														attrSelectorCategory)
												.setAttribute("DataType",
														attrSelectorDataType)
												.setAttribute("Path",
														attrSelectorPath)
												.setAttribute("MustBePresent",
														"false")))
				.addContent(
						new Element("AttributeValue").setAttribute("DataType",
								attrSelectorDataType).setText(attrValue));

		return conditionApply;
	}

	private static Element getConditionActionBag(String functionId,
			String compareFunctionId, String attrDesignatorCategory,
			String attrDesignatorAttrId, String attrDataType,
			List<String> actions) {

		Element apply = new Element("Apply").setAttribute("FunctionId",
				compareFunctionId);

		for (String action : actions) {
			Element attributVal = new Element("AttributeValue").setAttribute(
					"DataType", attrDataType).setText(action);
			apply.addContent(attributVal);

		}
		Element conditionApply = new Element("Apply")
				.setAttribute("FunctionId", functionId)
				.addContent(apply)
				.addContent(
						new Element("AttributeDesignator")
								.setAttribute("Category",
										attrDesignatorCategory)
								.setAttribute("DataType", attrDataType)
								.setAttribute("AttributeId",
										attrDesignatorAttrId)
								.setAttribute("MustBePresent", "false"));

		return conditionApply;
	}

	private static Element getObligationExpressionAlert(String obligationId,
			String fullFillOn) {
		// ObligationExpression STARTS here
		Element obligationExpression = new Element("ObligationExpression")
				.setAttribute("ObligationId", obligationId).setAttribute(
						"FulfillOn", fullFillOn);

		obligationExpression.addContent(getObligationAssignmentAttrValue(
				"obligationType", "preobligation"));

		obligationExpression.addContent(getObligationAssignmentAttrSelector(
				"signedByCurrentUser",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"http://www.w3.org/2001/XMLSchema#string",
				"//ak:signedByCurrentUser/text()"));

		obligationExpression.addContent(getObligationAssignmentAttrValue(
				"alertMessage", "You need to sign the proposal first!"));

		return obligationExpression;
	}

	private static Element getObligationAssignmentAttrValue(String attrId,
			String attrValue) {
		// AttributeAssignmentExpression STARTS here
		Element attributeAssignmentExpression = new Element(
				"AttributeAssignmentExpression").setAttribute("AttributeId",
				attrId).addContent(
				new Element("AttributeValue").setAttribute("DataType",
						"http://www.w3.org/2001/XMLSchema#string").setText(
						attrValue));
		// AttributeAssignmentExpression ENDS here

		return attributeAssignmentExpression;
	}

	private static Element getObligationAssignmentAttrSelector(String attrId,
			String attrSelectorCategory, String attrSelectorDataType,
			String attrSelectorPath) {
		// AttributeAssignmentExpression STARTS here
		Element attributeAssignmentExpression = new Element(
				"AttributeAssignmentExpression").setAttribute("AttributeId",
				attrId).addContent(
				new Element("AttributeSelector")
						.setAttribute("Category", attrSelectorCategory)
						.setAttribute("DataType", attrSelectorDataType)
						.setAttribute("Path", attrSelectorPath)
						.setAttribute("MustBePresent", "false"));
		// AttributeAssignmentExpression ENDS here

		return attributeAssignmentExpression;
	}

	public static void deletePolicyIdFromXML(String policyLocation,
			String policyId) {

		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(policyLocation + delegationXMLFileName);

		if (xmlFile.exists()) {
			Document doc = null;
			try {
				doc = (Document) builder.build(xmlFile);
			} catch (JDOMException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Namespace ns = Namespace
					.getNamespace("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
			List<Element> policyElements = doc.getRootElement().getChildren(
					"Policy", ns);

			for (Element policy : policyElements) {
				String existingPolicyId = policy.getAttributeValue("PolicyId");

				if (existingPolicyId.equals(policyId)) {
					policy.getParent().removeContent(policy);

					// display nice nice
					CustomXMLOutputProcessor output = new CustomXMLOutputProcessor();
					try {
						output.process(new FileWriter(policyLocation
								+ delegationXMLFileName),
								Format.getPrettyFormat(), doc);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("File Saved Using revocation!");
					break;
				}
			}
		}
	}
}
