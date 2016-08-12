package gpms.utils;

import gpms.model.Delegation;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;

public class WriteXMLUtil {

	public static HashMap<String, String> saveDelegationPolicy(
			String userProfileID, String delegatorName, String policyLocation,
			Delegation existingDelegation) {

		String delegationFileName = existingDelegation.getDelegationFileName();

		// Delegatee
		String delegateeId = existingDelegation.getDelegateeId();
		String delegateeName = existingDelegation.getDelegatee();

		String departmentName = existingDelegation.getDelegateeDepartment();
		String positionTitle = existingDelegation.getDelegateePositionTitle();

		String action = existingDelegation.getAction();

		// For Revocation
		String delegationId = existingDelegation.getId().toString();

		DateFormat policyDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssXXX");
		final String fromDate = policyDateFormat.format(existingDelegation
				.getFrom());
		final String toDate = policyDateFormat.format(existingDelegation
				.getTo());

		HashMap<String, String> policyMap = new HashMap<String, String>();

		if (delegationFileName == null || delegationFileName.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
			delegationFileName = String.format(
					"%s.%s",
					RandomStringUtils.randomAlphanumeric(8) + "_"
							+ dateFormat.format(new Date()), "xml");

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
					.replaceAll(" ", "-")));

			policySet
					.setAttribute(new Attribute("RuleCombiningAlgId",
							"urn:oasis:names:tc:xacml:3.0:rule-combining-algorithm:permit-overrides"));

			policySet.setAttribute(new Attribute("Version", "1.0"));

			policySet.addContent(new Element("Description")
					.setText("PolicySet for" + delegateeName + " of "
							+ departmentName + " with position title "
							+ positionTitle + " is delegated to " + action
							+ " by " + delegatorName));
			policySet.addContent(new Element("Target"));

			Document doc = new Document(policySet);
			// doc.addContent(policySet);

			// Policy Goes here
			Element policy = new Element("Policy");

			String policyId = "Dynamic-Delegation-Policy-Rules-For-"
					+ delegateeName + "-of-" + departmentName + "-"
					+ RandomStringUtils.randomAlphanumeric(8);
			policyId = policyId.replaceAll(" ", "-");
			policy.setAttribute(new Attribute("PolicyId", policyId));

			policy.setAttribute(new Attribute("RuleCombiningAlgId",
					"urn:oasis:names:tc:xacml:3.0:rule-combining-algorithm:permit-overrides"));

			policy.setAttribute(new Attribute("Version", "1.0"));

			policy.addContent(new Element("Description").setText(delegateeName
					+ " of " + departmentName + " with position title "
					+ positionTitle + " is delegated to " + action + " by "
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
			String ruleId = "DelegatedEditProposalSectionRuleFor-"
					+ positionTitle + "-DelegatedBy-" + delegatorName;
			rule1.setAttribute(new Attribute("RuleId", ruleId.replaceAll(" ",
					"-")));
			rule1.addContent(new Element("Description")
					.setText(delegateeName
							+ " of "
							+ departmentName
							+ " with position title "
							+ positionTitle
							+ " can \"Edit\" \"Certification/Signatures\" when Delegated by "
							+ delegatorName
							+ " with position title \"Department Chair\" and ApprovedByDepartmentChair = READYFORAPPROVAL"));

			Element allOf1 = new Element("Target").addContent(new Element(
					"AnyOf").addContent(new Element("AllOf")));

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

			rule1.addContent(allOf1);
			// END Rule EditProposalSectionByDepartmentChair-Rule40 HERE

			policy.addContent(rule1);

			// START Rule ApproveProposalByDepartmentChair-Rule13a HERE
			// Rule elements
			Element rule2 = new Element("Rule");
			rule2.setAttribute(new Attribute("Effect", "Permit"));
			ruleId = "DelegatedApproveProposalRule1For-" + positionTitle
					+ "-DelegatedBy-" + delegatorName;
			rule2.setAttribute(new Attribute("RuleId", ruleId.replaceAll(" ",
					"-")));
			rule2.addContent(new Element("Description")
					.setText(delegateeName
							+ " of "
							+ departmentName
							+ " with position title "
							+ positionTitle
							+ " can \"Approve\" a \"Whole Proposal\" when Delegated by "
							+ delegatorName
							+ " with position title \"Department Chair\" and ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are not approved."));

			Element allOf2 = new Element("Target").addContent(new Element(
					"AnyOf").addContent(new Element("AllOf")));

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

			allOf2.addContent(getMatch(action,
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			rule2.addContent(allOf2);
			// Condition elements

			Element condition1 = new Element("Condition")
					.addContent(new Element("Apply").setAttribute("FunctionId",
							"urn:oasis:names:tc:xacml:1.0:function:and"));

			condition1
					.addContent(getCondition(
							"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
							"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"http://www.w3.org/2001/XMLSchema#boolean",
							"//ak:signedByAllChairs/text()", "false"));
			condition1
					.addContent(getCondition(
							"urn:oasis:names:tc:xacml:1.0:function:string-equal",
							"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"http://www.w3.org/2001/XMLSchema#string",
							"//ak:authorprofile/ak:userid/text()", delegateeId));
			condition1
					.addContent(getCondition(
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"http://www.w3.org/2001/XMLSchema#dateTime",
							"//ak:currentdatetime/text()", fromDate));
			condition1
					.addContent(getCondition(
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-onlyy",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"http://www.w3.org/2001/XMLSchema#dateTime",
							"//ak:currentdatetime/text()", toDate));

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
			rule3.setAttribute(new Attribute("RuleId", ruleId.replaceAll(" ",
					"-")));
			rule3.addContent(new Element("Description")
					.setText(delegateeName
							+ " of "
							+ departmentName
							+ " with position title "
							+ positionTitle
							+ " can \"Approve\" a \"Whole Proposal\" when Delegated by "
							+ delegatorName
							+ " with position title \"Department Chair\" and ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are not approved and no IRB is required."));

			Element allOf3 = new Element("Target").addContent(new Element(
					"AnyOf").addContent(new Element("AllOf")));

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

			allOf3.addContent(getMatch(action,
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			rule3.addContent(allOf3);
			// Condition elements

			Element condition2 = new Element("Condition")
					.addContent(new Element("Apply").setAttribute("FunctionId",
							"urn:oasis:names:tc:xacml:1.0:function:and"));

			condition2
					.addContent(getCondition(
							"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
							"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"http://www.w3.org/2001/XMLSchema#boolean",
							"//ak:signedByAllChairs/text()", "true"));

			condition2
					.addContent(getCondition(
							"urn:oasis:names:tc:xacml:1.0:function:boolean-equal",
							"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"http://www.w3.org/2001/XMLSchema#boolean",
							"//ak:irbApprovalRequired/text()", "false"));
			condition2
					.addContent(getCondition(
							"urn:oasis:names:tc:xacml:1.0:function:string-equal",
							"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"http://www.w3.org/2001/XMLSchema#string",
							"//ak:authorprofile/ak:userid/text()", delegateeId));
			condition2
					.addContent(getCondition(
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"http://www.w3.org/2001/XMLSchema#dateTime",
							"//ak:currentdatetime/text()", fromDate));
			condition2
					.addContent(getCondition(
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-onlyy",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"http://www.w3.org/2001/XMLSchema#dateTime",
							"//ak:currentdatetime/text()", toDate));

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

			// END Rule ApproveProposalByDepartmentChair-Rule13a HERE
			policy.addContent(rule3);

			// Append Policy to the Root PolicySet
			policySet.addContent(policy);

			// display nice nice
			CustomXMLOutputProcessor output = new CustomXMLOutputProcessor();
			try {
				output.process(new FileWriter(policyLocation + "/"
						+ delegationFileName), Format.getPrettyFormat(), doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("File Saved!");
		}

		return policyMap;

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

	private static Element getMatch(String positionTitle, String string,
			String string2) {
		Element match = new Element("Match").setAttribute("MatchId",
				"urn:oasis:names:tc:xacml:1.0:function:string-equal");
		match.addContent(new Element("AttributeValue").setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#string").setText(
				positionTitle));
		match.addContent(new Element("AttributeDesignator")
				.setAttribute("AttributeId",
						"urn:oasis:names:tc:xacml:1.0:subject:position.title")
				.setAttribute("Category",
						"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject")
				.setAttribute("DataType",
						"http://www.w3.org/2001/XMLSchema#string")
				.setAttribute("MustBePresent", "false"));

		return match;
	}

	private static Element getCondition(String functionId,
			String compareFunctionId, String attrSelectorCategory,
			String attrSelectorDataType, String attrSelectorPath,
			String attrValue) {

		Element conditionApply = new Element("Apply").setAttribute(
				"FunctionId", functionId)
				.addContent(
						new Element("Apply")
								.setAttribute("FunctionId", compareFunctionId)
								.addContent(
										new Element("AttributeSelector")
												.setAttribute("Category",
														attrSelectorCategory)
												.setAttribute("DataType",
														attrSelectorDataType)
												.setAttribute("Path",
														attrSelectorPath)
												.setAttribute("MustBePresent",
														"false"))
								.addContent("AttributeValue")
								.setAttribute("DataType", attrSelectorDataType)
								.setText(attrValue));

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
}
