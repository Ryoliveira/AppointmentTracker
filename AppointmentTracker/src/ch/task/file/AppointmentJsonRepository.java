package ch.task.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.task.user.UserProfile;

public class AppointmentJsonRepository implements AppointmentRepository {

	final private static String FILE_NAME = "./appointmentLists.json";

	private static ObjectMapper mapper = new ObjectMapper();

	/*
	 * Writes users appointments to JSON file
	 * 
	 * @param profile User profile to be saved
	 */
	@Override
	public void save(UserProfile profile) {
		File file = new File(FILE_NAME);
		boolean foundProfile = false;
		ArrayList<UserProfile> profiles = new ArrayList<>();
		try {
			if (file.exists()) {
				profiles = mapper.readValue(file, new TypeReference<ArrayList<UserProfile>>() {
				});
			}
			for (UserProfile user : profiles) {
				if (user.getUserName().equals(profile.getUserName())) {
					profiles.set(profiles.indexOf(user), profile);
					foundProfile = true;
					break;
				}
			}
			if (!foundProfile) {
				profiles.add(profile);
			}
			mapper.writeValue(new File(FILE_NAME), profiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Return requested user profile if it exists and remove all appointments 
	 * 30 days from due date.
	 * 
	 * @return profile if it exists, otherwise a new profile will be returned
	 */
	@Override
	public UserProfile load(String profileName) {
		UserProfile profile = new UserProfile(profileName);
		File file = new File(FILE_NAME);
		if (file.exists()) {
			try {
				ArrayList<UserProfile> profiles = mapper.readValue(file, new TypeReference<ArrayList<UserProfile>>() {
				});

				for (UserProfile user : profiles) {
					if (user.getUserName().equals(profileName)) {
						return user;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// If no profile is found, original profile will be returned
		return profile;
	}

}
