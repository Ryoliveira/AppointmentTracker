package ch.task.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.task.user.Appointment;

public class AppointmentJsonRepository implements AppointmentRepository {

	final private static String FILE_NAME = "./appointmentLists.json";

	private static ObjectMapper mapper = new ObjectMapper();

	/*
	 * Writes users appointments to JSON file
	 * 
	 * @param profile User profile to be saved
	 */
	@Override
	public void save(Appointment app) {
		File file = new File(FILE_NAME);
		boolean foundProfile = false;
		HashMap<String, List<Appointment>> profiles = new HashMap<>();
		try {
			if (file.exists()) {
				profiles = mapper.readValue(file, new TypeReference<HashMap<String, List<Appointment>>>() {
				});
				if (profiles.containsKey(app.getCreator())) {
					List<Appointment> userList = profiles.get(app.getCreator());
					userList.add(app);
					Collections.sort(userList);
					foundProfile = true;
				}
			}
			if (!foundProfile) {
				List<Appointment> apps = new ArrayList<>();
				apps.add(app);
				profiles.put(app.getCreator(), apps);
			}
			mapper.writeValue(new File(FILE_NAME), profiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Return requested user profile if it exists and remove all appointments 30
	 * days from due date.
	 * 
	 * @return profile if it exists, otherwise a new profile will be returned
	 */
	@Override
	public List<Appointment> load(String profileName) {
		HashMap<String, List<Appointment>> profiles = new HashMap<>();
		File file = new File(FILE_NAME);
		try {
			if (file.exists()) {
				profiles = mapper.readValue(file, new TypeReference<HashMap<String, List<Appointment>>>() {
				});
				if (profiles.containsKey(profileName)) {
					return profiles.get(profileName);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// If no profile is found, original profile will be returned
		List<Appointment> emptyList = new ArrayList<>();
		return emptyList;
	}

	/*
	 * Update user list of appointments
	 * 
	 * @param profileName username of appointment list
	 */
	@Override
	public void update(List<Appointment> apps) {
		File file = new File(FILE_NAME);
		HashMap<String, List<Appointment>> profiles = new HashMap<>();
		if (!apps.isEmpty()) {
			String profileName = apps.get(0).getCreator();
			try {
				if (file.exists()) {
					profiles = mapper.readValue(file, new TypeReference<HashMap<String, List<Appointment>>>() {
					});
					if (profiles.containsKey(profileName)) {
						profiles.put(profileName, apps);
					}
				}
				mapper.writeValue(new File(FILE_NAME), profiles);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
