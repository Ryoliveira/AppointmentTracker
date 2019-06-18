package ch.task.file;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import BCrypt.*;

public class UserXmlRepository implements UserRepository{

	final private static String FILE_NAME = "./userProfiles.xml";

	/*
	 * Writes new profile to XML file with hashed password
	 * 
	 * @param username profile username
	 * 
	 * @param password profile password
	 */
	public void writeToFile(String username, String password) {
		Document document = null;
		Element root = null;
		File xmlFile = new File(FILE_NAME);

		// If file exists, build file. Otherwise create new file
		if (xmlFile.exists()) {
			try {
			FileInputStream fis = new FileInputStream(xmlFile);
			SAXBuilder builder = new SAXBuilder();
			document = builder.build(fis);
			root = document.getRootElement();
			fis.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			document = new Document();
			root = new Element("profiles");
		}
		// Set new user
		Element user = new Element("user");
		// Hash Password
		String hashedPass = BCrypt.hashpw(password, BCrypt.gensalt(12));
		int profileCount = root.getChildren().size();
		user.setAttribute("id", String.valueOf(profileCount + 1));
		user.setAttribute("username", username);
		user.setAttribute("password", hashedPass);

		// Add user to root
		root.addContent(user);
		document.setContent(root);

		// Write File
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutput.output(document, new FileOutputStream(xmlFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Gets list of users in XML file
	 * 
	 * @return list of user elements
	 */
	public static List<Element> getUsernames() throws JDOMException, IOException {
		File file = new File(FILE_NAME);
		List<Element> users = new ArrayList<>();
		if (file.exists()) {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(file);
			Element root = document.getRootElement();
			users = root.getChildren();
		}
		return users;
	}

	/*
	 * Check to see if profile name already exists
	 * 
	 * @return if profile exists
	 */
	public boolean checkForProfile(String profileName) {
		File xmlFile = new File(FILE_NAME);
		if (xmlFile.exists()) {

			try {
				List<Element> users = getUsernames();
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

}
