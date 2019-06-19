package ch.task.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProfile {

	private String userName;
	private List<Appointment> appointments;

	public UserProfile(String user, List<Appointment> apps) {
		userName = user;
		appointments = apps != null ? apps : new ArrayList<Appointment>();
	}

	public UserProfile(String user) {
		this(user, new ArrayList<Appointment>());
	}

	/*
	 * @return profile username
	 */
	public String getUserName() {
		return userName;
	}

	/*
	 * @param userName to be set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/*
	 * @return list of appointments
	 */
	public List<Appointment> getAppointments() {
		return Collections.unmodifiableList(appointments);
	}

	/*
	 * @param appointment list
	 */
	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	/*
	 * Adds an appointment to appointment list
	 * 
	 * @param newApp appointment to be added
	 */
	public void addAppointment(Appointment newApp) {
		appointments.add(newApp);
		sortAppointments();
	}

	/*
	 * Removes provided appointment object
	 * 
	 * @param app appointment object to be removed
	 */
	public void deleteAppointment(Appointment app) {
		appointments.remove(app);
	}

	/*
	 * Sorts list of appointments by due date in ascending order
	 */
	public void sortAppointments() {
		Collections.sort(appointments);
	}
}
