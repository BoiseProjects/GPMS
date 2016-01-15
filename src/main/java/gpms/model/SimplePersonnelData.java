package gpms.model;

import org.bson.types.ObjectId;

/**
 * This class is used to build a quick object used along with the 
 * Supervisory Personnel Search from the ProposalDAO Class
 * it should return a format of ID, Name
 * 
 * @author Thomas Volz
 *
 */



public class SimplePersonnelData 
{
	ObjectId personnelID=null;
	String personnelName="";

	/**
	 * Constructor
	 */
	public SimplePersonnelData()
	{

	}

	/**
	 * Parameterized constructor
	 * @param id id of the person
	 * @param name of the person
	 */
	public SimplePersonnelData(UserProfile profile)
	{
		personnelName = profile.getFirstName()+ " " + profile.getLastName();
		personnelID = profile.getId();
	}

	/**
	 * 
	 * @param id sets the id of this object
	 */
	public void setID(ObjectId id)
	{
		personnelID = id;
	}

	/**
	 * 
	 * @return the ID
	 */
	public ObjectId getID()
	{
		return personnelID;
	}

	/**
	 * 
	 * @param name sets the name of this object by concatenating first and last name
	 */
	public void setName(String firstName, String lastName)
	{
		personnelName = firstName+" "+lastName;
	}

	/**
	 * 
	 * @return the name of this object
	 */
	public String getName()
	{
		return personnelName;
	}

	/**
	 * @return A string of id, Name
	 */
	@Override
	public String toString()
	{
		String personnelString = personnelID.toString()+ " " +personnelName;

		return personnelString;
	}

}
