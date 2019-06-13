package ch.task.control;

import java.time.format.DateTimeFormatter;

import ch.task.file.JSONmanager;
import ch.task.user.Appointment;
import ch.task.user.UserProfile;
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

	void initialize() {
	}

	void initData(String profileN) {
		profileName = profileN;
	}

	@FXML
	void confirmAppointment(ActionEvent btn) {
		UserProfile profile = JSONmanager.loadProfile(profileName);
		try {
			DateTimeFormatter pattern = DateTimeFormatter.ISO_DATE;
			String newStart = startDate.getValue().format(pattern);
			String newDueDate = dueDate.getValue().format(pattern);
			String newTitle = title.getText();
			String newComment = comment.getText();
		if (newTitle.isEmpty()) {
			MainControl.alertBox(AlertType.ERROR, "Blank Title", null, "Please enter a valid title");
		} else {
			Appointment newApp = new Appointment(newStart, newDueDate, newTitle, newComment, false, false);
			profile.addAppointment(newApp);
			JSONmanager.writeToFile(profile);
			MainControl.alertBox(AlertType.INFORMATION, "Success", null, "Appointment added succussfully");
			closeWindow(btn);
		}
		}catch(NullPointerException e) {
			System.out.println(e);
			MainControl.alertBox(AlertType.ERROR, "Invalid Date", null, "Please enter a valid Date");
		}
	}

	@FXML
	void closeWindow(ActionEvent event) {
		Button btn = (Button) event.getSource();
		Stage window = (Stage) btn.getScene().getWindow();
		window.close();
	}


}
