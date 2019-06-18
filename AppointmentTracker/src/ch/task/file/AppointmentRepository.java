package ch.task.file;

import java.util.List;

import ch.task.user.Appointment;

public interface AppointmentRepository {
	void save(Appointment app);
	List<Appointment> load(String profileName);
	void update(List<Appointment> apps);
}
