package communications;

/**
 * Memo to edit a user's data. This object holds the userID and
 * password of the user who wishes to edit their data - password
 * is used to validate the changes to the user's settings.
 * 
 * The edited boolean array indicates whether a given field has 
 * been edited. The fields String array indicates the new values
 * of the fields which are marked as being edited (true) in the
 * edited array.
 * 
 * Both edited and fields should be of length 4, with the listing
 * of these fields as follows:
 * 	1. Username
 * 	2. Password
 * 	3. User bio
 * 	4. User avatar index
 * 
 * @author Tom Crossland, tdc222
 */
public class EditUser extends ServerMemo {

	private static final long serialVersionUID = 2843087147234602986L;

	private String password;
	
	private boolean[] edited;
	private String[] fields;
	
	public EditUser(int userID, String password, boolean[] edited, String[] fields) {
		super(userID);
		
		this.password = password;
		
		this.edited = edited;
		this.fields = fields;
	}

	public String getPassword() {
		return password;
	}

	public boolean[] getEdited() {
		return edited;
	}

	public String[] getFields() {
		return fields;
	}

}
