package ch.task.file;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Element;

public class RegistrationManager {
	final String FILE_NAME = "./userProfiles.xml";
	
	/*
	 * Check to see if profile name already exists
	 * 
	 * @return if profile exists
	 */
	public boolean checkForProfile(String profileName) {
		File xmlFile = new File(FILE_NAME);
		if (xmlFile.exists()) {

			try {
				List<Element> users = UserXmlRepository.getUsernames();
				// Search for profile name
				for (int i = 0; i < users.size(); i++) {
					Element currentUser = users.get(i);
					String username = currentUser.getAttributeValue("username");
					if (username.equals(profileName)) {
						return true;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}
	
	
	/*
	 * validates text to match pattern
	 * 
	 * @return if text is valid
	 */
	public boolean validate(String text, String pattern) {
		Pattern pat = Pattern.compile(pattern);
		Matcher match = pat.matcher(text);
		return match.matches();
	}


}
