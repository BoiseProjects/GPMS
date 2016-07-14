package gpms.utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WriteXMLUtil {

	public static void main(String[] args) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElementNS(
					"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17", "Policy");

			Attr attr = doc.createAttribute("xmlns:xacml");
			attr.setValue("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("xmlns:xsi");
			attr.setValue("http://www.w3.org/2001/XMLSchema-instance");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("xsi:schemaLocation");
			attr.setValue("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("PolicyId");
			attr.setValue("Administrative-Proposal-Rules");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("RuleCombiningAlgId");
			attr.setValue("urn:oasis:names:tc:xacml:3.0:rule-combining-algorithm:deny-overrides");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("Version");
			attr.setValue("3.0");
			rootElement.setAttributeNode(attr);

			doc.appendChild(rootElement);

			Element rootDesc = doc.createElement("Description");
			rootDesc.appendChild(doc
					.createTextNode("Policy for any adminstrative policy for a proposal."));
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

			// START Rule ApproveProposalByDepartmentChair-Rule13a HERE
			// Rule elements
			Element rule = doc.createElement("Rule");
			rootElement.appendChild(rule);

			// set Effect attribute to Rule element
			attr = doc.createAttribute("Effect");
			attr.setValue("Permit");
			rule.setAttributeNode(attr);

			rule.setAttribute("RuleId",
					"ApproveProposalByDepartmentChair-DelegationRule13a");

			// Description elements
			Element desc = doc.createElement("Description");
			desc.appendChild(doc
					.createTextNode("\"Associate Chair\" can \"Approve\" a \"Whole Proposal\" when Delegated by \"Department Chair\" ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are not approved."));
			rule.appendChild(desc);

			// Target elements
			Element target = doc.createElement("Target");
			rule.appendChild(target);

			Element anyOf = doc.createElement("AnyOf");
			target.appendChild(anyOf);

			Element allOf = doc.createElement("AllOf");
			anyOf.appendChild(allOf);

			allOf.appendChild(getMatch(doc, "Department Chair",
					"urn:oasis:names:tc:xacml:1.0:subject:position.title",
					"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));

			allOf.appendChild(getMatch(doc, "Whole Proposal",
					"urn:oasis:names:tc:xacml:1.0:resource:proposal.section",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf.appendChild(getMatch(
					doc,
					"READYFORAPPROVAL",
					"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));

			allOf.appendChild(getMatch(doc, "Approve",
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			// Condition elements
			Element condition = doc.createElement("Condition");
			rule.appendChild(condition);

			condition.appendChild(getConditionApply(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:signedByAllChairs/text()", "false"));

			// ObligationExpressions
			Element obligationExpressions = doc
					.createElement("ObligationExpressions");
			rule.appendChild(obligationExpressions);

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

			rule2.setAttribute("RuleId",
					"ApproveProposalByDepartmentChair-DelegationRule13b");

			// Description elements
			Element desc2 = doc.createElement("Description");
			desc2.appendChild(doc
					.createTextNode("\"Associate Chair\" can \"Approve\" a \"Whole Proposal\" when Delegated by \"Department Chair\" ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are approved and no IRB is required."));
			rule2.appendChild(desc2);

			// Target elements
			Element target2 = doc.createElement("Target");
			rule2.appendChild(target2);

			Element anyOf2 = doc.createElement("AnyOf");
			target2.appendChild(anyOf2);

			Element allOf2 = doc.createElement("AllOf");
			anyOf2.appendChild(allOf2);

			allOf2.appendChild(getMatch(doc, "Department Chair",
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

			allOf2.appendChild(getMatch(doc, "Approve",
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			// Condition elements
			Element condition2 = doc.createElement("Condition");
			rule2.appendChild(condition2);

			Element conditionRootApply = doc.createElement("Apply");
			condition2.appendChild(conditionRootApply);
			conditionRootApply.setAttribute("FunctionId",
					"urn:oasis:names:tc:xacml:1.0:function:and");

			conditionRootApply.appendChild(getConditionApply(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:signedByAllChairs/text()", "true"));

			conditionRootApply.appendChild(getConditionApply(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:irbApprovalRequired/text()", "false"));

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

			rule3.setAttribute("RuleId",
					"ApproveProposalByDepartmentChair-DelegationRule13b");

			// Description elements
			Element desc3 = doc.createElement("Description");
			desc3.appendChild(doc
					.createTextNode("\"Associate Chair\" can \"Approve\" a \"Whole Proposal\" when Delegated by \"Department Chair\" ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are approved and IRB is required."));
			rule3.appendChild(desc3);

			// Target elements
			Element target3 = doc.createElement("Target");
			rule3.appendChild(target3);

			Element anyOf3 = doc.createElement("AnyOf");
			target3.appendChild(anyOf3);

			Element allOf3 = doc.createElement("AllOf");
			anyOf3.appendChild(allOf3);

			allOf3.appendChild(getMatch(doc, "Department Chair",
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

			allOf3.appendChild(getMatch(doc, "Approve",
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action"));

			// Condition elements
			Element condition3 = doc.createElement("Condition");
			rule3.appendChild(condition3);

			Element conditionRootApply2 = doc.createElement("Apply");
			condition3.appendChild(conditionRootApply2);
			conditionRootApply2.setAttribute("FunctionId",
					"urn:oasis:names:tc:xacml:1.0:function:and");

			conditionRootApply2.appendChild(getConditionApply(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:signedByAllChairs/text()", "true"));

			conditionRootApply2.appendChild(getConditionApply(doc,
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource",
					"//ak:irbApprovalRequired/text()", "true"));

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

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("D:\\file.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
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
				"http://www.w3.org/2001/XMLSchema#boolean");
		attributeAssignmentExpression
				.appendChild(attributeAssignmentExpressionSelector2);
		// AttributeAssignmentExpression ENDS here

		return attributeAssignmentExpression;
	}

	private static Node getConditionApply(Document doc,
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
