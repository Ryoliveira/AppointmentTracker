package ch.task.user;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {

	private String userName;
	private ArrayList<Appointment> appointments;

	@JsonCreator
	public UserProfile(@JsonProperty("userName") String user,
			@JsonProperty("appointments") ArrayList<Appointment> apps) {
		userName = user;
		appointments = apps;
	}

	public UserProfile(String user) {
		userName = user;
		appointments = new ArrayList<>();
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
	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}

	/*
	 * @param appointment list
	 */
	public void setAppointments(ArrayList<Appointment> appointments) {
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

	/*
	 * Removes all appointments that have been due for over 30 days
	 */
	public void removeExpired() {
		for (int i = 0; i < appointments.size(); i++) {
			Appointment app = appointments.get(i);
			LocalDate due = LocalDate.parse(app.getDueDate());
			LocalDate today = LocalDate.now();
			int daysBetween = (int) ChronoUnit.DAYS.between(due, today);
			if (daysBetween > 30) {
				deleteAppointment(app);
				System.out.println("Removed!");
				System.out.println(app.toString());
			}

		}
	}

}
