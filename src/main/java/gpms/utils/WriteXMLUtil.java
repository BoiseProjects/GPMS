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

public class WriteXMLUtil {

	public static void main(String[] args) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Policy");

			Attr attr = doc.createAttribute("xmlns");
			attr.setValue("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("xmlns:xacml");
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
			attr.setValue("urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides");
			rootElement.setAttributeNode(attr);

			attr = doc.createAttribute("Version");
			attr.setValue("3.0");
			rootElement.setAttributeNode(attr);

			doc.appendChild(rootElement);

			Element rootDesc = doc.createElement("Description");
			rootDesc.appendChild(doc
					.createTextNode("Policy for any adminstrative policy for a proposal."));
			rootElement.appendChild(rootDesc);

			// staff elements
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

			// staff elements
			Element rule = doc.createElement("Rule");
			rootElement.appendChild(rule);

			// set attribute to staff element
			attr = doc.createAttribute("Effect");
			attr.setValue("Permit");
			rule.setAttributeNode(attr);

			// shorten way
			// staff.setAttribute("id", "1");

			rule.setAttribute("RuleId",
					"ApproveProposalByDepartmentChair-DelegationRule13a");

			// firstname elements
			Element desc = doc.createElement("Description");
			desc.appendChild(doc
					.createTextNode("\"Associate Chair\" can \"Approve\" a \"Whole Proposal\" when Delegated by \"Department Chair\" ApprovedByDepartmentChair = READYFORAPPROVAL and where condition check all department chairs are not approved."));
			rule.appendChild(desc);

			// lastname elements
			Element target = doc.createElement("Target");
			rule.appendChild(target);

			Element anyOf = doc.createElement("AnyOf");
			target.appendChild(anyOf);

			Element allOf = doc.createElement("AllOf");
			anyOf.appendChild(allOf);

			// Match STARTS here
			Element match1 = doc.createElement("Match");
			allOf.appendChild(match1);

			match1.setAttribute("MatchId",
					"urn:oasis:names:tc:xacml:1.0:function:string-equal");

			Element attributeValue1 = doc.createElement("AttributeValue");
			attributeValue1.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeValue1.appendChild(doc.createTextNode("Department Chair"));
			match1.appendChild(attributeValue1);

			Element attributeDesignator1 = doc
					.createElement("AttributeDesignator");
			attributeDesignator1.setAttribute("AttributeId",
					"urn:oasis:names:tc:xacml:1.0:subject:position.title");
			attributeDesignator1
					.setAttribute("Category",
							"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject");
			attributeDesignator1.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeDesignator1.setAttribute("MustBePresent", "false");
			match1.appendChild(attributeDesignator1);
			// Match ENDS here

			// Match STARTS here
			Element match2 = doc.createElement("Match");
			allOf.appendChild(match2);

			match2.setAttribute("MatchId",
					"urn:oasis:names:tc:xacml:1.0:function:string-equal");

			Element attributeValue2 = doc.createElement("AttributeValue");
			attributeValue2.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeValue2.appendChild(doc.createTextNode("Whole Proposal"));
			match2.appendChild(attributeValue2);

			Element attributeDesignator2 = doc
					.createElement("AttributeDesignator");
			attributeDesignator2.setAttribute("AttributeId",
					"urn:oasis:names:tc:xacml:1.0:resource:proposal.section");
			attributeDesignator2.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			attributeDesignator2.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeDesignator2.setAttribute("MustBePresent", "false");
			match2.appendChild(attributeDesignator2);
			// Match ENDS here

			// Match STARTS here
			Element match3 = doc.createElement("Match");
			allOf.appendChild(match3);

			match3.setAttribute("MatchId",
					"urn:oasis:names:tc:xacml:1.0:function:string-equal");

			Element attributeValue3 = doc.createElement("AttributeValue");
			attributeValue3.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeValue3.appendChild(doc.createTextNode("READYFORAPPROVAL"));
			match3.appendChild(attributeValue3);

			Element attributeDesignator3 = doc
					.createElement("AttributeDesignator");
			attributeDesignator3
					.setAttribute("AttributeId",
							"urn:oasis:names:tc:xacml:1.0:resource:ApprovedByDepartmentChair");
			attributeDesignator3.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			attributeDesignator3.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeDesignator3.setAttribute("MustBePresent", "false");
			match3.appendChild(attributeDesignator3);
			// Match ENDS here

			// Match STARTS here
			Element match4 = doc.createElement("Match");
			allOf.appendChild(match4);

			match4.setAttribute("MatchId",
					"urn:oasis:names:tc:xacml:1.0:function:string-equal");

			Element attributeValue4 = doc.createElement("AttributeValue");
			attributeValue4.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeValue4.appendChild(doc.createTextNode("Approve"));
			match4.appendChild(attributeValue4);

			Element attributeDesignator4 = doc
					.createElement("AttributeDesignator");
			attributeDesignator4.setAttribute("AttributeId",
					"urn:oasis:names:tc:xacml:1.0:action:proposal.action");
			attributeDesignator4.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:action");
			attributeDesignator4.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeDesignator4.setAttribute("MustBePresent", "false");
			match4.appendChild(attributeDesignator4);
			// Match ENDS here

			// nickname elements
			Element condition = doc.createElement("Condition");
			rule.appendChild(condition);

			Element conditionRootApply = doc.createElement("Apply");
			condition.appendChild(conditionRootApply);
			conditionRootApply.setAttribute("FunctionId",
					"urn:oasis:names:tc:xacml:1.0:function:boolean-equal");

			Element conditionApply = doc.createElement("Apply");
			conditionRootApply.appendChild(conditionApply);
			conditionApply
					.setAttribute("FunctionId",
							"urn:oasis:names:tc:xacml:1.0:function:boolean-one-and-only");

			Element conditionAttributeSelector = doc
					.createElement("AttributeSelector");
			conditionApply.appendChild(conditionAttributeSelector);
			conditionAttributeSelector.setAttribute("MustBePresent", "false");
			conditionAttributeSelector.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			conditionAttributeSelector.setAttribute("Path",
					"//ak:signedByAllChairs/text()");
			conditionAttributeSelector.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#boolean");

			Element conditionRootAttributeValue = doc
					.createElement("AttributeValue");
			conditionRootAttributeValue.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#boolean");
			conditionRootAttributeValue
					.appendChild(doc.createTextNode("false"));
			conditionRootApply.appendChild(conditionRootAttributeValue);

			// ObligationExpressions
			Element obligationExpressions = doc
					.createElement("ObligationExpressions");
			rule.appendChild(obligationExpressions);

			// ObligationExpression STARTS here
			Element obligationExpression1 = doc
					.createElement("ObligationExpression");
			obligationExpressions.appendChild(obligationExpression1);

			obligationExpression1.setAttribute("ObligationId", "sendAlert");
			obligationExpression1.setAttribute("FulfillOn", "Permit");

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression1 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression1.setAttribute("AttributeId",
					"obligationType");
			obligationExpression1.appendChild(attributeAssignmentExpression1);

			Element attributeAssignmentExpressionValue1 = doc
					.createElement("AttributeValue");
			attributeAssignmentExpressionValue1.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpressionValue1.appendChild(doc
					.createTextNode("preobligation"));
			attributeAssignmentExpression1
					.appendChild(attributeAssignmentExpressionValue1);
			// AttributeAssignmentExpression ENDS here

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression2 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression2.setAttribute("AttributeId",
					"signedByCurrentUser");
			obligationExpression1.appendChild(attributeAssignmentExpression2);

			Element attributeAssignmentExpressionSelector2 = doc
					.createElement("AttributeSelector");
			attributeAssignmentExpressionSelector2.setAttribute(
					"MustBePresent", "false");
			attributeAssignmentExpressionSelector2.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			attributeAssignmentExpressionSelector2.setAttribute("Path",
					"//ak:signedByCurrentUser/text()");
			attributeAssignmentExpressionSelector2.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#boolean");
			attributeAssignmentExpression2
					.appendChild(attributeAssignmentExpressionSelector2);
			// AttributeAssignmentExpression ENDS here

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression3 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression3.setAttribute("AttributeId",
					"alertMessage");
			obligationExpression1.appendChild(attributeAssignmentExpression3);

			Element attributeAssignmentExpressionValue3 = doc
					.createElement("AttributeValue");
			attributeAssignmentExpressionValue3.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpressionValue3.appendChild(doc
					.createTextNode("You need to sign the proposal first!"));
			attributeAssignmentExpression3
					.appendChild(attributeAssignmentExpressionValue3);
			// AttributeAssignmentExpression ENDS here

			// ObligationExpression STARTS here
			Element obligationExpression2 = doc
					.createElement("ObligationExpression");
			obligationExpressions.appendChild(obligationExpression2);

			obligationExpression2.setAttribute("ObligationId", "sendEmail");
			obligationExpression2.setAttribute("FulfillOn", "Permit");

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression4 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression4.setAttribute("AttributeId",
					"obligationType");
			obligationExpression2.appendChild(attributeAssignmentExpression4);

			Element attributeAssignmentExpressionValue4 = doc
					.createElement("AttributeValue");
			attributeAssignmentExpressionValue4.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpressionValue4.appendChild(doc
					.createTextNode("postobligation"));
			attributeAssignmentExpression4
					.appendChild(attributeAssignmentExpressionValue4);
			// AttributeAssignmentExpression ENDS here

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression5 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression5.setAttribute("AttributeId",
					"emailBody");
			obligationExpression2.appendChild(attributeAssignmentExpression5);

			Element attributeAssignmentExpressionValue5 = doc
					.createElement("AttributeValue");
			attributeAssignmentExpressionValue5.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpressionValue5
					.appendChild(doc
							.createTextNode("Hello User,&lt;br/&gt;&lt;br/&gt;The proposal has been approved by Department Chair. Now it is waiting for another Department Chair approval. &lt;br/&gt;&lt;br/&gt;Thank you, &lt;br/&gt; GPMS Team"));
			attributeAssignmentExpression5
					.appendChild(attributeAssignmentExpressionValue5);
			// AttributeAssignmentExpression ENDS here

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression6 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression6.setAttribute("AttributeId",
					"emailSubject");
			obligationExpression2.appendChild(attributeAssignmentExpression6);

			Element attributeAssignmentExpressionValue6 = doc
					.createElement("AttributeValue");
			attributeAssignmentExpressionValue6.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpressionValue6.appendChild(doc
					.createTextNode("Your proposal has been approved by: "));
			attributeAssignmentExpression6
					.appendChild(attributeAssignmentExpressionValue6);
			// AttributeAssignmentExpression ENDS here

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression7 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression7.setAttribute("AttributeId",
					"authorName");
			obligationExpression2.appendChild(attributeAssignmentExpression7);

			Element attributeAssignmentExpressionSelector3 = doc
					.createElement("AttributeSelector");
			attributeAssignmentExpressionSelector3.setAttribute(
					"MustBePresent", "false");
			attributeAssignmentExpressionSelector3.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			attributeAssignmentExpressionSelector3.setAttribute("Path",
					"//ak:authorprofile/ak:fullname/text()");
			attributeAssignmentExpressionSelector3.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpression7
					.appendChild(attributeAssignmentExpressionSelector3);
			// AttributeAssignmentExpression ENDS here

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression8 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression8.setAttribute("AttributeId",
					"piEmail");
			obligationExpression2.appendChild(attributeAssignmentExpression8);

			Element attributeAssignmentExpressionSelector4 = doc
					.createElement("AttributeSelector");
			attributeAssignmentExpressionSelector4.setAttribute(
					"MustBePresent", "false");
			attributeAssignmentExpressionSelector4.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			attributeAssignmentExpressionSelector4.setAttribute("Path",
					"//ak:pi/ak:workemail/text()");
			attributeAssignmentExpressionSelector4.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpression8
					.appendChild(attributeAssignmentExpressionSelector4);
			// AttributeAssignmentExpression ENDS here

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression9 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression9.setAttribute("AttributeId",
					"copisEmail");
			obligationExpression2.appendChild(attributeAssignmentExpression9);

			Element attributeAssignmentExpressionSelector5 = doc
					.createElement("AttributeSelector");
			attributeAssignmentExpressionSelector5.setAttribute(
					"MustBePresent", "false");
			attributeAssignmentExpressionSelector5.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			attributeAssignmentExpressionSelector5.setAttribute("Path",
					"//ak:copi/ak:workemail/text()");
			attributeAssignmentExpressionSelector5.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpression9
					.appendChild(attributeAssignmentExpressionSelector5);
			// AttributeAssignmentExpression ENDS here

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression10 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression10.setAttribute("AttributeId",
					"seniorsEmail");
			obligationExpression2.appendChild(attributeAssignmentExpression10);

			Element attributeAssignmentExpressionSelector6 = doc
					.createElement("AttributeSelector");
			attributeAssignmentExpressionSelector6.setAttribute(
					"MustBePresent", "false");
			attributeAssignmentExpressionSelector6.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			attributeAssignmentExpressionSelector6.setAttribute("Path",
					"//ak:senior/ak:workemail/text()");
			attributeAssignmentExpressionSelector6.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpression10
					.appendChild(attributeAssignmentExpressionSelector6);
			// AttributeAssignmentExpression ENDS here

			// AttributeAssignmentExpression STARTS here
			Element attributeAssignmentExpression11 = doc
					.createElement("AttributeAssignmentExpression");
			attributeAssignmentExpression11.setAttribute("AttributeId",
					"chairsEmail");
			obligationExpression2.appendChild(attributeAssignmentExpression11);

			Element attributeAssignmentExpressionSelector7 = doc
					.createElement("AttributeSelector");
			attributeAssignmentExpressionSelector7.setAttribute(
					"MustBePresent", "false");
			attributeAssignmentExpressionSelector7.setAttribute("Category",
					"urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			attributeAssignmentExpressionSelector7.setAttribute("Path",
					"//ak:chair/ak:workemail/text()");
			attributeAssignmentExpressionSelector7.setAttribute("DataType",
					"http://www.w3.org/2001/XMLSchema#string");
			attributeAssignmentExpression11
					.appendChild(attributeAssignmentExpressionSelector7);
			// AttributeAssignmentExpression ENDS here

			// // AttributeAssignmentExpression STARTS here
			// Element attributeAssignmentExpression12 = doc
			// .createElement("AttributeAssignmentExpression");
			// attributeAssignmentExpression12.setAttribute("AttributeId",
			// "associatechairsEmail");
			// obligationExpression2.appendChild(attributeAssignmentExpression12);
			//
			// Element attributeAssignmentExpressionSelector8 = doc
			// .createElement("AttributeSelector");
			// attributeAssignmentExpressionSelector8.setAttribute(
			// "MustBePresent", "false");
			// attributeAssignmentExpressionSelector8.setAttribute("Category",
			// "urn:oasis:names:tc:xacml:3.0:attribute-category:resource");
			// attributeAssignmentExpressionSelector8.setAttribute("Path",
			// "//ak:associatechair/ak:workemail/text()");
			// attributeAssignmentExpressionSelector8.setAttribute("DataType",
			// "http://www.w3.org/2001/XMLSchema#string");
			// attributeAssignmentExpression12
			// .appendChild(attributeAssignmentExpressionSelector8);
			// // AttributeAssignmentExpression ENDS here

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

}
