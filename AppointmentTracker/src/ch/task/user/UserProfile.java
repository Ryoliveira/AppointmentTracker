package ch.task.user;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
	 * @return cloned list of appointments
	 */
	public List<Appointment> getAppointments() {
		List<Appointment> clonedList = new ArrayList<>();
		for(Appointment app : appointments) {
			clonedList.add(app.clone());
		}
		return clonedList;
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
	public void deleteAppointment(UUID id) {
		Iterator<Appointment> iter = appointments.iterator();
		while(iter.hasNext()) {
			if(iter.next().getId() == id) {
				iter.remove();
				break;
			}
		}
	}
	
	/*
	 * mark appointment completed/uncompleted
	 * 
	 * @param appID Appointment unique id
	 * 
	 * @param mark boolean for completed
	 */
	public void markAppointment(UUID appID, boolean mark) {
		for(Appointment app: appointments) {
			if(app.getId() == appID) {
				app.setCompleted(mark);
				break;
			}
		}
	}
	
	/*
	 * removes all appointments that have been due for more than 30 days
	 * 
	 */
	public void removeExpired() {
		Iterator<Appointment> iter = appointments.iterator();
		while(iter.hasNext()) {
			LocalDate due = LocalDate.parse(iter.next().getDueDate());
			LocalDate today = LocalDate.now();
			int daysBetween = (int) ChronoUnit.DAYS.between(due, today);
			if (daysBetween > 30) {
				iter.remove();
				break;
			}
		}
	}

	/*
	 * Sorts list of appointments by due date in ascending order
	 */
	public void sortAppointments() {
		Collections.sort(appointments);
	}

}
