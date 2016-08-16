package gpms.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegexTest {

	public static void main(String[] args) {
		List<String> actions = new ArrayList<String>();
		actions.add("Approve");
		actions.add("Disapprove");
		String ruleId = actions + "Show For";

		System.out.println(ruleId.replaceAll("[\\[\\]\\s\\,]", ""));
	}
}
