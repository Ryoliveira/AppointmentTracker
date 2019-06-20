package ch.task.control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import ch.task.file.AppointmentJsonRepository;
import ch.task.file.AppointmentRepository;
import ch.task.user.Appointment;
import ch.task.control.MainControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AppointmentControl {
	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker dueDate;
	@FXML
	private TextArea comment;
	@FXML
	private TextField title;

	private String profileName;
	private AppointmentRepository JM = new AppointmentJsonRepository();

	void initialize() {
	}

	/*
	 * sets profile name
	 */
	void initData(String profileN) {
		profileName = profileN;
	}

	/*
	 * Creates new user appointment
	 */
	@FXML
	void confirmAppointment(ActionEvent btn) {
		DateTimeFormatter pattern = DateTimeFormatter.ISO_DATE;
		try {
			if (isValidDueDate()) {
				String newStart = startDate.getValue().format(pattern);
				String newDueDate = dueDate.getValue().format(pattern);
				String newTitle = title.getText();
				String newComment = comment.getText();
				if(newTitle.length() > 25) {
					MainControl.alertBox(AlertType.ERROR, "Title length error", null, "Title must be less than 25 characters.");
				}
				else if (newTitle.isEmpty()) {
					MainControl.alertBox(AlertType.ERROR, "Blank Title", null, "Please enter a valid title");
				} else {
					Appointment newApp = new Appointment(newStart, newDueDate, newTitle, newComment, profileName);
					JM.save(newApp);
					MainControl.alertBox(AlertType.INFORMATION, "Success", null, "Appointment added succussfully");
					closeWindow(btn);
				}
			} else {
				MainControl.alertBox(AlertType.ERROR, "Invalid date", null, "Due date must be within 60 days after todays date and after start date.");
			}
		} catch (NullPointerException e) {
			MainControl.alertBox(AlertType.ERROR, "Invalid Date", null, "Please enter a valid Date");
		}
	}

	/*
	 * Appointments due dates must be within 30 days
	 * 
	 * @return if due date is within 30 days
	 */
	private boolean isValidDueDate() {
		LocalDate now = LocalDate.now();
		int daysBetween = (int) ChronoUnit.DAYS.between(now, dueDate.getValue());
		return daysBetween > 0 && daysBetween <= 60 && startDate.getValue().isBefore(dueDate.getValue());
	}

	/*
	 * Closes current window
	 */
	@FXML
	void closeWindow(ActionEvent event) {
		Button btn = (Button) event.getSource();
		Stage window = (Stage) btn.getScene().getWindow();
		window.close();
	}

}
