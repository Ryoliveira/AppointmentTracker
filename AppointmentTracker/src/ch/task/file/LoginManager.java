package ch.task.file;

import java.util.List;

import org.jdom2.Element;

import BCrypt.BCrypt;

public class LoginManager {
	
	/*
	 * Validates profile name/password for match
	 * 
	 * @return if profile credentials match
	 */
	public static boolean validateLogIn(String profile, String password) {
		if (profile.isEmpty() || password.isEmpty()) {
			return false;
		}
		try {
			List<Element> users = UserXmlRepository.getUsernames();
			// Search for profile name
			for (int i = 0; i < users.size(); i++) {
				Element currentUser = users.get(i);
				String profileName = currentUser.getAttributeValue("username");
				if (profileName.equals(profile)) {
					String profilePass = currentUser.getAttributeValue("password");
					// Compare given password to hash
					if (BCrypt.checkpw(password, profilePass)) {
						return true;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
