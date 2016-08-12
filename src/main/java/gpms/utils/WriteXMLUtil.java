package gpms.utils;

import gpms.model.Delegation;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.RandomStringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Multimap;

public class WriteXMLUtil {

	public static HashMap<String, String> saveDelegationPolicy(
			String userProfileID, String delegatorName, String policyLocation,
			Delegation existingDelegation) {

		String delegationFileName = existingDelegation.getDelegationFileName();

		if (delegationFileName != null && !delegationFileName.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
			delegationFileName = String.format(
					"%s.%s",
					RandomStringUtils.randomAlphanumeric(8) + "_"
							+ dateFormat.format(new Date()), "xml");
		}

		HashMap<String, String> policyMap = new HashMap<String, String>();

		try {

			// Delegatee
			String delegateeId = existingDelegation.getDelegateeId();
			String delegateeName = existingDelegation.getDelegatee();

			String departmentName = existingDelegation.getDelegateeDepartment();
			// departmentName =
			// existingDelegation.getDepartment().replaceAll(" ",
			// "-");
			String positionTitle = existingDelegation
					.getDelegateePositionTitle();
			// positionTitle =
			// existingDelegation.getPositionTitle().replaceAll(" ", "-");

			String action = existingDelegation.getAction();

			// For Revocation
			String delegationId = existingDelegation.getId().toString();

			String policyId = new String();
			String id = new String();

			DateFormat policyDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ssXXX");
			final String fromDate = policyDateFormat.format(existingDelegation
					.getFrom());
			final String toDate = policyDateFormat.format(existingDelegation
					.getTo());

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElementNS(
					"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17", "Policy");

			// Attr attr = doc.createAttribute("xmlns:xacml");
			// attr.setValue("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
			// rootElement.setAttributeNode(attr);

			// attr = doc.createAttribute("xmlns:xsi");
			// attr.setValue("http://www.w3.org/2001/XMLSchema-instance");
			// rootElement.setAttributeNode(attr);
			//
			// attr = doc.createAttribute("xsi:schemaLocation");
			// attr.setValue("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd");
			// rootElement.setAttributeNode(attr);

			Attr attr = doc.createAttribute("PolicyId");
			policyId = "Dynamic-Delegation-Policy-Rules-For-" + delegateeName
					+ "-of-" + departmentName + "-"
					+ RandomStringUtils.randomAlphanumeric(8);
			attr.setValue(id.replaceAll(" ", "-"));
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("RuleCombiningAlgId");
			attr.setValue("urn:oasis:names:tc:xacml:3.0:rule-combining-algorithm:permit-overrides");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("Version");
			attr.setValue("1.0");
			rootElement.setAttributeNode(attr);

			doc.appendChild(rootElement);

			Element rootDesc = doc.createElement("Description");

			rootDesc.appendChild(doc.createTextNode(delegateeName + " of "
					+ departmentName + " with position title " + positionTitle
					+ " is delegated to " + action + " by " + delegatorName));
			rootElement.appendChild(rootDesc);

			// PolicyDefaults elements
			Element policyDefault = doc.createElement("PolicyDefaults");
			rootElement.appendChild(policyDefault);

			// XPathVersion elements
			Element xPathVersion = doc.createElement("XPathVersion");
			xPathVersion
					.appendChild(doc
							.createTextNode("http://www.w3.org/TR/1999/REC-xpath-19991116"));
			policyDefault.appendChild(xPathVersion);

			Element rootTarget = doc.createElement("Target");
			rootElement.appendChild(rootTarget);

			// START Rule for Editing Signature Part:
			// EditProposalSectionByDepartmentChair-Rule40 HERE
			// Rule elements
			Element rule = doc.createElement("Rule");
			rootElement.appendChild(rule);

			// set Effect attribute to Rule element
			attr = doc.createAttribute("Effect");
			attr.setValue("Permit");
			rule.setAttributeNode(attr);

			id = "DelegatedEditProposalSectionRuleFor-" + positionTitle
					+ "-DelegatedBy-" + delegatorName;

			rule.setAttribute("RuleId", id.replaceAll(" ", "-"));

			// Description elements
			Element desc = doc.createElement("Description");
			desc.appendChild(doc
					.createTextNode(delegateeName
							+ " of "
							+ departmentName
							+ " with position title "
							+ positionTitle
							+ " can \"Edit\" \"Certification/Signatures\" when Delegated by "
							+ delegatorName
							+ " with position title \"Department Chair\" and ApprovedByDepartmentChair = READYFORAPPROVAL"));
			rule.appendChild(desc);

			// Target elements
			Element target = doc.createElement("Target");
			rule.appendChild(target);

			Element anyOf = doc.createElement("AnyOf");
			target.appendChild(anyOf);

			Element allOf = doc.createElement("AllOf");
			anyOf.appendChild(allOf);

			allOf.appendChild(getMatch(doc, positionTitle,
					"urn:oasis:names:tc:xacml:1.0:subject:position.title",
					"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

			allOf.appendChild(getMatch(doc, "Certification/Signatures",
					"urn:oasis:names:tc:xacml:1.0:resource:proposal.section",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf.appendChild(getMatch(
					doc,
					"READYFORAPPROVAL",
					"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf.appendChild(getMatch(doc, "Edit",
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			// END Rule EditProposalSectionByDepartmentChair-Rule40 HERE

			// START Rule ApproveProposalByDepartmentChair-Rule13a HERE
			// Rule elements
			Element rule1 = doc.createElement("Rule");
			rootElement.appendChild(rule1);

			// set Effect attribute to Rule element
			attr = doc.createAttribute("Effect");
			attr.setValue("Permit");
			rule1.setAttributeNode(attr);

			id = "DelegatedApproveProposalRule1For-" + positionTitle
					+ "-DelegatedBy-" + delegatorName;
			rule1.setAttribute("RuleId", id.replaceAll(" ", "-"));

			// Description elements
			Element desc1 = doc.createElement("Description");
			desc1.appendChild(doc
					.createTextNode(delegateeName
							+ " of "
							+ departmentName
							+ " with position title "
							+ positionTitle
							+ " can \"Approve\" a \"Whole Proposal\" when Delegated by "
							+ delegatorName
							+ " with position title \"Department Chair\" and ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are not approved."));
			rule1.appendChild(desc1);

			// Target elements
			Element target1 = doc.createElement("Target");
			rule1.appendChild(target1);

			Element anyOf1 = doc.createElement("AnyOf");
			target1.appendChild(anyOf1);

			Element allOf1 = doc.createElement("AllOf");
			anyOf1.appendChild(allOf1);

			allOf1.appendChild(getMatch(doc, positionTitle,
					"urn:oasis:names:tc:xacml:1.0:subject:position.title",
					"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

			allOf1.appendChild(getMatch(doc, "Whole Proposal",
					"urn:oasis:names:tc:xacml:1.0:resource:proposal.section",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf1.appendChild(getMatch(
					doc,
					"READYFORAPPROVAL",
					"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf1.appendChild(getMatch(doc, action,
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			// Condition elements
			Element condition1 = doc.createElement("Condition");
			rule1.appendChild(condition1);

			Element conditionRootApply1 = doc.createElement("Apply");
			condition1.appendChild(conditionRootApply1);
			conditionRootApply1.setAttribute("FunctionId",
					"urn:oasis:names:tc:xacml:1.0:function:and");

			conditionRootApply1.appendChild(getConditionApplyBoolean(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:signedByAllChairs/text()", "false"));

			conditionRootApply1.appendChild(getConditionApplyString(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:authorprofile/ak:userid/text()", delegateeId));

			conditionRootApply1
					.appendChild(getConditionApplyDateTime(
							doc,
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"//ak:currentdatetime/text()", fromDate));

			conditionRootApply1
					.appendChild(getConditionApplyDateTime(
							doc,
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"//ak:currentdatetime/text()", toDate));

			// ObligationExpressions
			Element obligationExpressions = doc
					.createElement("ObligationExpressions");
			rule1.appendChild(obligationExpressions);

			obligationExpressions.appendChild(getObligationExpressionAlert(doc,
					"sendAlert", "Permit"));

			obligationExpressions
					.appendChild(getObligationExpressionSendEmail(
							doc,
							"sendEmail",
							"Permit",
							"Hello User,&amp;lt;br/&amp;gt;&amp;lt;br/&amp;gt;The proposal has been approved by Department Chair. Now it is waiting for another Department Chair approval. &amp;lt;br/&amp;gt;&amp;lt;br/&amp;gt;Thank you, &amp;lt;br/&amp;gt; GPMS Team"));

			// END Rule ApproveProposalByDepartmentChair-Rule13a HERE

			// START Rule ApproveProposalByDepartmentChair-Rule13b HERE
			// Rule elements
			Element rule2 = doc.createElement("Rule");
			rootElement.appendChild(rule2);

			// set Effect attribute to Rule element
			attr = doc.createAttribute("Effect");
			attr.setValue("Permit");
			rule2.setAttributeNode(attr);

			id = "DelegatedApproveProposalRule2For-" + positionTitle
					+ "-DelegatedBy-" + delegatorName;
			rule2.setAttribute("RuleId", id.replaceAll(" ", "-"));

			// Description elements
			Element desc2 = doc.createElement("Description");
			desc2.appendChild(doc
					.createTextNode(delegateeName
							+ " of "
							+ departmentName
							+ " with position title "
							+ positionTitle
							+ " can \"Approve\" a \"Whole Proposal\" when Delegated by "
							+ delegatorName
							+ " with position title \"Department Chair\" and ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are not approved and no IRB is required."));
			rule2.appendChild(desc2);

			// Target elements
			Element target2 = doc.createElement("Target");
			rule2.appendChild(target2);

			Element anyOf2 = doc.createElement("AnyOf");
			target2.appendChild(anyOf2);

			Element allOf2 = doc.createElement("AllOf");
			anyOf2.appendChild(allOf2);

			allOf2.appendChild(getMatch(doc, positionTitle,
					"urn:oasis:names:tc:xacml:1.0:subject:position.title",
					"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

			allOf2.appendChild(getMatch(doc, "Whole Proposal",
					"urn:oasis:names:tc:xacml:1.0:resource:proposal.section",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf2.appendChild(getMatch(
					doc,
					"READYFORAPPROVAL",
					"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf2.appendChild(getMatch(doc, action,
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			// Condition elements
			Element condition2 = doc.createElement("Condition");
			rule2.appendChild(condition2);

			Element conditionRootApply2 = doc.createElement("Apply");
			condition2.appendChild(conditionRootApply2);
			conditionRootApply2.setAttribute("FunctionId",
					"urn:oasis:names:tc:xacml:1.0:function:and");

			conditionRootApply2.appendChild(getConditionApplyBoolean(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:signedByAllChairs/text()", "true"));

			conditionRootApply2.appendChild(getConditionApplyBoolean(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:irbApprovalRequired/text()", "false"));

			conditionRootApply2.appendChild(getConditionApplyString(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:authorprofile/ak:userid/text()", delegateeId));

			conditionRootApply2
					.appendChild(getConditionApplyDateTime(
							doc,
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"//ak:currentdatetime/text()", fromDate));

			conditionRootApply2
					.appendChild(getConditionApplyDateTime(
							doc,
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"//ak:currentdatetime/text()", toDate));

			// ObligationExpressions
			Element obligationExpressions2 = doc
					.createElement("ObligationExpressions");
			rule2.appendChild(obligationExpressions2);

			obligationExpressions2.appendChild(getObligationExpressionAlert(
					doc, "sendAlert", "Permit"));

			obligationExpressions2
					.appendChild(getObligationExpressionSendEmail(
							doc,
							"sendEmail",
							"Permit",
							"Hello User,&lt;br/&gt;&lt;br/&gt;The proposal has been approved by all Department Chairs.&lt;br/&gt;&lt;br/&gt;Thank you, &lt;br/&gt; GPMS Team"));

			// END Rule ApproveProposalByDepartmentChair-Rule13b HERE

			// START Rule ApproveProposalByDepartmentChair-Rule13c HERE
			// Rule elements
			Element rule3 = doc.createElement("Rule");
			rootElement.appendChild(rule3);

			// set Effect attribute to Rule element
			attr = doc.createAttribute("Effect");
			attr.setValue("Permit");
			rule3.setAttributeNode(attr);

			id = "DelegatedApproveProposalRule3For-" + positionTitle
					+ "-DelegatedBy-" + delegatorName;
			rule3.setAttribute("RuleId", id.replaceAll(" ", "-"));

			// Description elements
			Element desc3 = doc.createElement("Description");
			desc3.appendChild(doc
					.createTextNode(delegateeName
							+ " of "
							+ departmentName
							+ " with position title "
							+ positionTitle
							+ " can \"Approve\" a \"Whole Proposal\" when Delegated by "
							+ delegatorName
							+ " with position title \"Department Chair\" ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are approved and IRB is required."));
			rule3.appendChild(desc3);

			// Target elements
			Element target3 = doc.createElement("Target");
			rule3.appendChild(target3);

			Element anyOf3 = doc.createElement("AnyOf");
			target3.appendChild(anyOf3);

			Element allOf3 = doc.createElement("AllOf");
			anyOf3.appendChild(allOf3);

			allOf3.appendChild(getMatch(doc, positionTitle,
					"urn:oasis:names:tc:xacml:1.0:subject:position.title",
					"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

			allOf3.appendChild(getMatch(doc, "Whole Proposal",
					"urn:oasis:names:tc:xacml:1.0:resource:proposal.section",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf3.appendChild(getMatch(
					doc,
					"READYFORAPPROVAL",
					"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf3.appendChild(getMatch(doc, action,
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			// Condition elements
			Element condition3 = doc.createElement("Condition");
			rule3.appendChild(condition3);

			Element conditionRootApply3 = doc.createElement("Apply");
			condition3.appendChild(conditionRootApply3);
			conditionRootApply3.setAttribute("FunctionId",
					"urn:oasis:names:tc:xacml:1.0:function:and");

			conditionRootApply3.appendChild(getConditionApplyBoolean(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:signedByAllChairs/text()", "true"));

			conditionRootApply3.appendChild(getConditionApplyBoolean(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:irbApprovalRequired/text()", "true"));

			conditionRootApply3.appendChild(getConditionApplyString(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:authorprofile/ak:userid/text()", delegateeId));

			conditionRootApply3
					.appendChild(getConditionApplyDateTime(
							doc,
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"//ak:currentdatetime/text()", fromDate));

			conditionRootApply3
					.appendChild(getConditionApplyDateTime(
							doc,
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"//ak:currentdatetime/text()", toDate));

			// ObligationExpressions
			Element obligationExpressions3 = doc
					.createElement("ObligationExpressions");
			rule3.appendChild(obligationExpressions3);

			obligationExpressions3.appendChild(getObligationExpressionAlert(
					doc, "sendAlert", "Permit"));

			obligationExpressions3
					.appendChild(getObligationExpressionSendEmail(
							doc,
							"sendEmail",
							"Permit",
							"Hello User,&lt;br/&gt;&lt;br/&gt;The proposal has been approved by all Department Chairs.&lt;br/&gt;&lt;br/&gt;Thank you, &lt;br/&gt; GPMS Team"));

			// END Rule ApproveProposalByDepartmentChair-Rule13c HERE

			// Add Revocation Rule HERE
			Element rule4 = doc.createElement("Rule");
			rootElement.appendChild(rule4);

			// set Effect attribute to Rule element
			attr = doc.createAttribute("Effect");
			attr.setValue("Permit");
			rule4.setAttributeNode(attr);

			id = "Revoke " + delegationFileName + " by Department Chair";
			rule4.setAttribute("RuleId", id.replaceAll(" ", "-"));

			// Description elements
			Element desc4 = doc.createElement("Description");
			desc4.appendChild(doc
					.createTextNode("\"Department Chair\" can \"Revoke\" delegation from "
							+ delegateeName
							+ " of "
							+ departmentName
							+ " with position title " + positionTitle));
			rule4.appendChild(desc4);

			// Target elements
			Element target4 = doc.createElement("Target");
			rule4.appendChild(target4);

			Element anyOf4 = doc.createElement("AnyOf");
			target4.appendChild(anyOf4);

			Element allOf4 = doc.createElement("AllOf");
			anyOf4.appendChild(allOf4);

			allOf4.appendChild(getMatch(doc, "Revoke",
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			// Condition elements
			Element condition4 = doc.createElement("Condition");
			rule4.appendChild(condition4);

			Element conditionRootApply4 = doc.createElement("Apply");
			condition4.appendChild(conditionRootApply4);
			conditionRootApply4.setAttribute("FunctionId",
					"urn:oasis:names:tc:xacml:1.0:function:and");

			conditionRootApply4.appendChild(getConditionApplyString(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:delegationid/text()", delegationId));

			conditionRootApply4.appendChild(getConditionApplyString(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:delegator/ak:id/text()", userProfileID));

			conditionRootApply4.appendChild(getConditionApplyString(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:delegationfilename/text()", delegationFileName));

			conditionRootApply4.appendChild(getConditionApplyBoolean(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:revoked/text()", "false"));

			// ObligationExpressions
			Element obligationExpressions4 = doc
					.createElement("ObligationExpressions");
			rule4.appendChild(obligationExpressions4);

			// obligationExpressions4.appendChild(getObligationExpressionAlert(
			// doc, "sendAlert", "Permit"));

			obligationExpressions4
					.appendChild(getObligationExpressionForRevokeSendEmail(
							doc,
							"sendEmail",
							"Permit",
							"Hello User,&lt;br/&gt;&lt;br/&gt;You have been revoked from your delegation. &lt;br/&gt;&lt;br/&gt;Thank you, &lt;br/&gt; GPMS Team"));

			// END Rule Revocation HERE

			// Add Action Button Show Rule HERE
			Element rule5 = doc.createElement("Rule");
			rootElement.appendChild(rule5);

			// set Effect attribute to Rule element
			attr = doc.createAttribute("Effect");
			attr.setValue("Permit");
			rule5.setAttributeNode(attr);

			id = action + "ShowFor" + positionTitle;
			rule5.setAttribute("RuleId", id.replaceAll(" ", "-"));

			// Description elements
			Element desc5 = doc.createElement("Description");
			desc5.appendChild(doc
					.createTextNode("'"
							+ positionTitle
							+ "' can see '"
							+ action
							+ "' button when ApprovedByDepartmentChair = READYFORAPPROVAL"));
			rule5.appendChild(desc5);

			// Target elements
			Element target5 = doc.createElement("Target");
			rule5.appendChild(target5);

			Element anyOf5 = doc.createElement("AnyOf");
			target5.appendChild(anyOf5);

			Element allOf5 = doc.createElement("AllOf");
			anyOf5.appendChild(allOf5);

			allOf5.appendChild(getMatch(doc, positionTitle,
					"urn:oasis:names:tc:xacml:1.0:subject:position.title",
					"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

			allOf5.appendChild(getMatch(doc, action,
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			// Condition elements
			Element condition5 = doc.createElement("Condition");
			rule5.appendChild(condition5);

			Element conditionRootApply5 = doc.createElement("Apply");
			condition5.appendChild(conditionRootApply5);
			conditionRootApply5.setAttribute("FunctionId",
					"urn:oasis:names:tc:xacml:1.0:function:and");

			conditionRootApply5
					.appendChild(getConditionApplyString(
							doc,
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"//ak:approvedbydepartmentchair/text()",
							"READYFORAPPROVAL"));

			conditionRootApply5.appendChild(getConditionApplyString(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:authorprofile/ak:userid/text()", delegateeId));

			conditionRootApply5
					.appendChild(getConditionApplyDateTime(
							doc,
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"//ak:currentdatetime/text()", fromDate));

			conditionRootApply5
					.appendChild(getConditionApplyDateTime(
							doc,
							"urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal",
							"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
							"//ak:currentdatetime/text()", toDate));

			// END Action Button Show Rule HERE

			policyMap.put("PolicyFileName", delegationFileName);
			policyMap.put("PolicyId", policyId);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(new File(policyLocation
					+ "/" + delegationFileName));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

		return policyMap;

	}

	// For Delegation Obligation
	private static Node getObligationExpressionForRevokeSendEmail(Document doc,
			String obligationId, String fullFillOn, String emailBody) {
		// ObligationExpression STARTS here
		Element obligationExpression = doc
				.createElement("ObligationExpression");

		obligationExpression.setAttribute("ObligationId", obligationId);
		obligationExpression.setAttribute("FulfillOn", fullFillOn);

		// obligationExpression.appendChild(getObligationAssignmentAttrValue(doc,
		// "obligationType", "postobligation"));

		obligationExpression.appendChild(getObligationAssignmentAttrValue(doc,
				"emailBody", emailBody));

		obligationExpression.appendChild(getObligationAssignmentAttrValue(doc,
				"emailSubject", "Your delegation is revoked by: "));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "delegatorName",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:delegator/ak:fullname/text()"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "delegatorEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:delegator/ak:email/text()"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "delegateeName",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:delegatee/ak:fullname/text()"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "delegateeEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:delegatee/ak:email/text()"));

		return obligationExpression;
	}

	private static Node getConditionApplyDateTime(Document doc,
			String functionId, String attrSelectorCategory,
			String attrSelectorPath, String attrValue) {
		Element conditionApply = doc.createElement("Apply");

		conditionApply.setAttribute("FunctionId", functionId);

		Element conditionLastApply = doc.createElement("Apply");
		conditionApply.appendChild(conditionLastApply);

		conditionLastApply.setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:dateTime-one-and-only");

		Element conditionAttributeSelector = doc
				.createElement("AttributeSelector");
		conditionLastApply.appendChild(conditionAttributeSelector);
		conditionAttributeSelector.setAttribute("MustBePresent", "false");
		conditionAttributeSelector.setAttribute("Category",
				attrSelectorCategory);
		conditionAttributeSelector.setAttribute("Path", attrSelectorPath);
		conditionAttributeSelector.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#dateTime");

		Element conditionAttrValue = doc.createElement("AttributeValue");
		conditionAttrValue.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#dateTime");
		conditionAttrValue.appendChild(doc.createTextNode(attrValue));
		conditionApply.appendChild(conditionAttrValue);

		return conditionApply;
	}

	private static Node getObligationExpressionAlert(Document doc,
			String obligationId, String fullFillOn) {
		// ObligationExpression STARTS here
		Element obligationExpression = doc
				.createElement("ObligationExpression");

		obligationExpression.setAttribute("ObligationId", obligationId);
		obligationExpression.setAttribute("FulfillOn", fullFillOn);

		obligationExpression.appendChild(getObligationAssignmentAttrValue(doc,
				"obligationType", "preobligation"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "signedByCurrentUser",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:signedByCurrentUser/text()"));

		// AttributeAssignmentExpression STARTS here
		obligationExpression.appendChild(getObligationAssignmentAttrValue(doc,
				"alertMessage", "You need to sign the proposal first!"));

		return obligationExpression;
	}

	private static Node getObligationExpressionSendEmail(Document doc,
			String obligationId, String fullFillOn, String emailBody) {
		// ObligationExpression STARTS here
		Element obligationExpression = doc
				.createElement("ObligationExpression");

		obligationExpression.setAttribute("ObligationId", obligationId);
		obligationExpression.setAttribute("FulfillOn", fullFillOn);

		obligationExpression.appendChild(getObligationAssignmentAttrValue(doc,
				"obligationType", "postobligation"));

		obligationExpression.appendChild(getObligationAssignmentAttrValue(doc,
				"emailBody", emailBody));

		obligationExpression.appendChild(getObligationAssignmentAttrValue(doc,
				"emailSubject", "Your proposal has been approved by: "));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "authorName",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:authorprofile/ak:fullname/text()"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "piEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:pi/ak:workemail/text()"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "copisEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:copi/ak:workemail/text()"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "seniorsEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:senior/ak:workemail/text()"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "chairsEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:chair/ak:workemail/text()"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "managersEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:manager/ak:workemail/text()"));

		obligationExpression.appendChild(getObligationAssignmentAttrSelector(
				doc, "irbsEmail",
				"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
				"//ak:irb/ak:workemail/text()"));

		return obligationExpression;
	}

	private static Node getObligationAssignmentAttrValue(Document doc,
			String attrId, String attrValue) {
		// AttributeAssignmentExpression STARTS here
		Element attributeAssignmentExpression = doc
				.createElement("AttributeAssignmentExpression");
		attributeAssignmentExpression.setAttribute("AttributeId", attrId);

		Element attributeAssignmentExpressionValue = doc
				.createElement("AttributeValue");
		attributeAssignmentExpressionValue.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#string");
		attributeAssignmentExpressionValue.appendChild(doc
				.createTextNode(attrValue));
		attributeAssignmentExpression
				.appendChild(attributeAssignmentExpressionValue);
		// AttributeAssignmentExpression ENDS here

		return attributeAssignmentExpression;
	}

	private static Node getObligationAssignmentAttrSelector(Document doc,
			String attrId, String attrSelectorCategory, String attrSelectorPath) {
		// AttributeAssignmentExpression STARTS here
		Element attributeAssignmentExpression = doc
				.createElement("AttributeAssignmentExpression");
		attributeAssignmentExpression.setAttribute("AttributeId", attrId);

		Element attributeAssignmentExpressionSelector2 = doc
				.createElement("AttributeSelector");
		attributeAssignmentExpressionSelector2.setAttribute("MustBePresent",
				"false");
		attributeAssignmentExpressionSelector2.setAttribute("Category",
				attrSelectorCategory);
		attributeAssignmentExpressionSelector2.setAttribute("Path",
				attrSelectorPath);
		attributeAssignmentExpressionSelector2.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#string");
		attributeAssignmentExpression
				.appendChild(attributeAssignmentExpressionSelector2);
		// AttributeAssignmentExpression ENDS here

		return attributeAssignmentExpression;
	}

	private static Node getConditionApplyBoolean(Document doc,
			String attrSelectorCategory, String attrSelectorPath,
			String attrValue) {
		Element conditionApply = doc.createElement("Apply");

		conditionApply.setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:boolean-equal");

		Element conditionLastApply = doc.createElement("Apply");
		conditionApply.appendChild(conditionLastApply);

		conditionLastApply.setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only");

		Element conditionAttributeSelector = doc
				.createElement("AttributeSelector");
		conditionLastApply.appendChild(conditionAttributeSelector);
		conditionAttributeSelector.setAttribute("MustBePresent", "false");
		conditionAttributeSelector.setAttribute("Category",
				attrSelectorCategory);
		conditionAttributeSelector.setAttribute("Path", attrSelectorPath);
		conditionAttributeSelector.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#boolean");

		Element conditionAttrValue = doc.createElement("AttributeValue");
		conditionAttrValue.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#boolean");
		conditionAttrValue.appendChild(doc.createTextNode(attrValue));
		conditionApply.appendChild(conditionAttrValue);

		return conditionApply;
	}

	private static Node getConditionApplyString(Document doc,
			String attrSelectorCategory, String attrSelectorPath,
			String attrValue) {
		Element conditionApply = doc.createElement("Apply");

		conditionApply.setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:string-equal");

		Element conditionLastApply = doc.createElement("Apply");
		conditionApply.appendChild(conditionLastApply);

		conditionLastApply.setAttribute("FunctionId",
				"urn:oasis:names:tc:xacml:1.0:function:string-one-and-only");

		Element conditionAttributeSelector = doc
				.createElement("AttributeSelector");
		conditionLastApply.appendChild(conditionAttributeSelector);
		conditionAttributeSelector.setAttribute("MustBePresent", "false");
		conditionAttributeSelector.setAttribute("Category",
				attrSelectorCategory);
		conditionAttributeSelector.setAttribute("Path", attrSelectorPath);
		conditionAttributeSelector.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#string");

		Element conditionAttrValue = doc.createElement("AttributeValue");
		conditionAttrValue.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#string");
		conditionAttrValue.appendChild(doc.createTextNode(attrValue));
		conditionApply.appendChild(conditionAttrValue);

		return conditionApply;
	}

	private static Node getMatch(Document doc, String attrValue, String attrId,
			String attrCategory) {
		// Match STARTS here
		Element match = doc.createElement("Match");

		match.setAttribute("MatchId",
				"urn:oasis:names:tc:xacml:1.0:function:string-equal");

		Element attributeValue = doc.createElement("AttributeValue");
		attributeValue.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#string");
		attributeValue.appendChild(doc.createTextNode(attrValue));
		match.appendChild(attributeValue);

		Element attributeDesignator = doc.createElement("AttributeDesignator");
		attributeDesignator.setAttribute("AttributeId", attrId);
		attributeDesignator.setAttribute("Category", attrCategory);
		attributeDesignator.setAttribute("DataType",
				"http://www.w3.org/2001/XMLSchema#string");
		attributeDesignator.setAttribute("MustBePresent", "false");
		match.appendChild(attributeDesignator);
		// Match ENDS here

		return match;
	}

}
