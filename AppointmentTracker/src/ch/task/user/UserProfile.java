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
	public UserProfile(@JsonProperty("userName") String user, @JsonProperty("appointments") ArrayList<Appointment> apps) {
		userName = user;
		appointments = apps;
	}  
	
	public UserProfile(String user) {
		userName = user;
		appointments = new ArrayList<>();
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}
	public void setAppointments(ArrayList<Appointment> appointments) {
		this.appointments = appointments;
	}
	
	public void addAppointment(Appointment newApp) {
		appointments.add(newApp);
		sortAppointments();
	}
	
	public void deleteAppointment(Appointment app) {
		appointments.remove(app);
	}
	
	public void sortAppointments() {
		Collections.sort(appointments);
	}
	
	public void removeExpired() {
		for(int i=0; i<appointments.size();i++) {
			Appointment app = appointments.get(i);
			LocalDate due = LocalDate.parse(app.getDueDate());
			LocalDate today = LocalDate.now();
			int daysBetween = (int)ChronoUnit.DAYS.between(due, today);
			if(daysBetween > 30) {
				deleteAppointment(app);
				System.out.println("Removed!");
				System.out.println(app.toString());
			}
			
			
		}
	}

}
