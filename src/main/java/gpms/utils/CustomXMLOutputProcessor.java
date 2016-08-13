package gpms.utils;

import java.io.Writer;

import org.jdom2.Namespace;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.FormatStack;

public class CustomXMLOutputProcessor extends AbstractXMLOutputProcessor {
	protected void printNamespace(Writer out, FormatStack fstack, Namespace ns)
			throws java.io.IOException {
		System.out.println("namespace is " + ns);
		if (ns == Namespace.NO_NAMESPACE) {
			System.out.println("refusing to print empty namespace");
			return;
		} else {
			super.printNamespace(out, fstack, ns);
		}
	}
}
