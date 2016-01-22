package gpms.DAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DepartmentsPositionsCollection {
	private static final HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> ht = new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>();

	public DepartmentsPositionsCollection() {
		ArrayList<String> tenuredTitles = new ArrayList<String>();
		tenuredTitles.add("Distinguished Professor");
		tenuredTitles.add("Professor");
		tenuredTitles.add("Associate Professor");
		tenuredTitles.add("Assistant Professor");

		ArrayList<String> nonTenuredTitles = new ArrayList<String>();
		nonTenuredTitles.add("Research Professor");
		nonTenuredTitles.add("Associate Research Professor");
		nonTenuredTitles.add("Assistant Research Professor");
		nonTenuredTitles.add("Clinical Professor");
		nonTenuredTitles.add("Clinical Associate Professor");
		nonTenuredTitles.add("Clinical Assistant Professor");
		nonTenuredTitles.add("Visiting Professor");
		nonTenuredTitles.add("Visiting Associate Professor");
		nonTenuredTitles.add("Visiting Assistant Professor");

		ArrayList<String> teachingFaculty = new ArrayList<String>();
		teachingFaculty.add("Lecturer");
		teachingFaculty.add("Senior Lecturer");
		teachingFaculty.add("Adjunct Professor");

		ArrayList<String> researchStaff = new ArrayList<String>();
		researchStaff.add("Research Associate");
		researchStaff.add("Research Scientist");
		researchStaff.add("Senior Research Scientist");

		ArrayList<String> professionalStaff = new ArrayList<String>();
		professionalStaff.add("Business Manager");
		professionalStaff.add("University Research Administrator");
		professionalStaff.add("Department Administrative Assistant");

		ArrayList<String> administratorStaff = new ArrayList<String>();
		administratorStaff.add("Department Chair");
		administratorStaff.add("Associate Chair");
		administratorStaff.add("Dean");
		administratorStaff.add("Associate Dean");
		administratorStaff.add("Research Administrator");
		administratorStaff.add("University Research Director");

		HashMap<String, ArrayList<String>> TypeTitleHtCS = new HashMap<String, ArrayList<String>>();
		TypeTitleHtCS.put("Tenured/tenure-track faculty", tenuredTitles);
		TypeTitleHtCS
				.put("Non-tenure-track research faculty", nonTenuredTitles);
		TypeTitleHtCS.put("Teaching faculty", teachingFaculty);
		TypeTitleHtCS.put("Research staff", researchStaff);
		TypeTitleHtCS.put("Professional staff", professionalStaff);
		TypeTitleHtCS.put("Administrator", administratorStaff);

		HashMap<String, ArrayList<String>> TypeTitleHtEE = new HashMap<String, ArrayList<String>>();
		TypeTitleHtEE.put("Tenured/tenure-track faculty", tenuredTitles);
		TypeTitleHtEE
				.put("Non-tenure-track research faculty", nonTenuredTitles);
		TypeTitleHtEE.put("Teaching faculty", teachingFaculty);
		TypeTitleHtEE.put("Research staff", researchStaff);
		TypeTitleHtEE.put("Professional staff", professionalStaff);
		TypeTitleHtEE.put("Administrator", administratorStaff);

		HashMap<String, ArrayList<String>> TypeTitleHtCE = new HashMap<String, ArrayList<String>>();
		TypeTitleHtCE.put("Tenured/tenure-track faculty", tenuredTitles);
		TypeTitleHtCE
				.put("Non-tenure-track research faculty", nonTenuredTitles);
		TypeTitleHtCE.put("Teaching faculty", teachingFaculty);
		TypeTitleHtCE.put("Research staff", researchStaff);
		TypeTitleHtCE.put("Professional staff", professionalStaff);
		TypeTitleHtCE.put("Administrator", administratorStaff);

		HashMap<String, ArrayList<String>> TypeTitleHtFis = new HashMap<String, ArrayList<String>>();
		TypeTitleHtFis.put("Tenured/tenure-track faculty", tenuredTitles);
		TypeTitleHtFis.put("Non-tenure-track research faculty",
				nonTenuredTitles);
		TypeTitleHtFis.put("Teaching faculty", teachingFaculty);
		TypeTitleHtFis.put("Research staff", researchStaff);
		TypeTitleHtFis.put("Professional staff", professionalStaff);
		TypeTitleHtFis.put("Administrator", administratorStaff);

		HashMap<String, ArrayList<String>> TypeTitleHtChe = new HashMap<String, ArrayList<String>>();
		TypeTitleHtChe.put("Tenured/tenure-track faculty", tenuredTitles);
		TypeTitleHtChe.put("Non-tenure-track research faculty",
				nonTenuredTitles);
		TypeTitleHtChe.put("Teaching faculty", teachingFaculty);
		TypeTitleHtChe.put("Research staff", researchStaff);
		TypeTitleHtChe.put("Professional staff", professionalStaff);
		TypeTitleHtChe.put("Administrator", administratorStaff);

		HashMap<String, HashMap<String, ArrayList<String>>> departmentTypeHtEng = new HashMap<String, HashMap<String, ArrayList<String>>>();
		departmentTypeHtEng.put("Computer Science", TypeTitleHtCS);
		departmentTypeHtEng.put("Electrical Engineering", TypeTitleHtEE);
		departmentTypeHtEng.put("Computer Engineering", TypeTitleHtCE);

		HashMap<String, HashMap<String, ArrayList<String>>> departmentTypeHtSci = new HashMap<String, HashMap<String, ArrayList<String>>>();
		departmentTypeHtSci.put("Physics", TypeTitleHtFis);
		departmentTypeHtSci.put("Chemistry", TypeTitleHtChe);

		ht.put("Engineering", departmentTypeHtEng);
		ht.put("Science", departmentTypeHtSci);
	}

	public HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> getAvailableDepartmentsAndPositions() {
		return ht;
	}

	public Set<String> getCollegeKeys() {
		return ht.keySet();
	}

	public Set<String> getDepartmentKeys(String college) {
		return ht.get(college).keySet();
	}

	public Set<String> getPositionType(String college, String department) {
		return ht.get(college).get(department).keySet();
	}

	public List<String> getPositionTitle(String college, String department,
			String positionType) {
		return ht.get(college).get(department).get(positionType);
	}

}
