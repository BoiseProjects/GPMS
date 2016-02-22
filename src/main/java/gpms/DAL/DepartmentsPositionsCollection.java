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

		HashMap<String, ArrayList<String>> positionTypeCS = new HashMap<String, ArrayList<String>>();
		positionTypeCS.put("Tenured/tenure-track faculty", tenuredTitles);
		positionTypeCS
				.put("Non-tenure-track research faculty", nonTenuredTitles);
		positionTypeCS.put("Teaching faculty", teachingFaculty);
		positionTypeCS.put("Research staff", researchStaff);
		positionTypeCS.put("Professional staff", professionalStaff);
		positionTypeCS.put("Administrator", administratorStaff);

		HashMap<String, ArrayList<String>> positionTypeEE = new HashMap<String, ArrayList<String>>();
		positionTypeEE.put("Tenured/tenure-track faculty", tenuredTitles);
		positionTypeEE
				.put("Non-tenure-track research faculty", nonTenuredTitles);
		positionTypeEE.put("Teaching faculty", teachingFaculty);
		positionTypeEE.put("Research staff", researchStaff);
		positionTypeEE.put("Professional staff", professionalStaff);
		positionTypeEE.put("Administrator", administratorStaff);

		HashMap<String, ArrayList<String>> positionTypeCE = new HashMap<String, ArrayList<String>>();
		positionTypeCE.put("Tenured/tenure-track faculty", tenuredTitles);
		positionTypeCE
				.put("Non-tenure-track research faculty", nonTenuredTitles);
		positionTypeCE.put("Teaching faculty", teachingFaculty);
		positionTypeCE.put("Research staff", researchStaff);
		positionTypeCE.put("Professional staff", professionalStaff);
		positionTypeCE.put("Administrator", administratorStaff);

		HashMap<String, ArrayList<String>> positionTypePhysics = new HashMap<String, ArrayList<String>>();
		positionTypePhysics.put("Tenured/tenure-track faculty", tenuredTitles);
		positionTypePhysics.put("Non-tenure-track research faculty",
				nonTenuredTitles);
		positionTypePhysics.put("Teaching faculty", teachingFaculty);
		positionTypePhysics.put("Research staff", researchStaff);
		positionTypePhysics.put("Professional staff", professionalStaff);
		positionTypePhysics.put("Administrator", administratorStaff);

		HashMap<String, ArrayList<String>> positionTypeChemistry = new HashMap<String, ArrayList<String>>();
		positionTypeChemistry.put("Tenured/tenure-track faculty", tenuredTitles);
		positionTypeChemistry.put("Non-tenure-track research faculty",
				nonTenuredTitles);
		positionTypeChemistry.put("Teaching faculty", teachingFaculty);
		positionTypeChemistry.put("Research staff", researchStaff);
		positionTypeChemistry.put("Professional staff", professionalStaff);
		positionTypeChemistry.put("Administrator", administratorStaff);

		HashMap<String, HashMap<String, ArrayList<String>>> departmentEngineering = new HashMap<String, HashMap<String, ArrayList<String>>>();
		departmentEngineering.put("Computer Science", positionTypeCS);
		departmentEngineering.put("Electrical Engineering", positionTypeEE);
		departmentEngineering.put("Computer Engineering", positionTypeCE);

		HashMap<String, HashMap<String, ArrayList<String>>> departmentScience = new HashMap<String, HashMap<String, ArrayList<String>>>();
		departmentScience.put("Physics", positionTypePhysics);
		departmentScience.put("Chemistry", positionTypeChemistry);

		ht.put("Engineering", departmentEngineering);
		ht.put("Science", departmentScience);
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
