package ch.task.control;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Optional;

import ch.task.app.MainApp;
import ch.task.file.AppointmentJsonRepository;
import ch.task.file.AppointmentRepository;
import ch.task.user.Appointment;
import ch.task.user.UserProfile;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class MainControl {
	@FXML
	private Label profileName;
	@FXML
	private Label currentDate;
	@FXML
	private ListView<Appointment> listBox;
	@FXML
	private ChoiceBox<String> choiceBox;
	@FXML
	private DatePicker mainDatePicker;

	// Detail Variables
	@FXML
	private Text startDetail;
	@FXML
	private Text dueDateDetail;
	@FXML
	private TextArea commentDetail;
	@FXML
	private Text dueDetail;
	@FXML
	private Text completedDetail;
	@FXML
	private Text titleDetail;

	// User data manager variables
	private UserProfile profile;
	private AppointmentRepository JM = new AppointmentJsonRepository();

	void initialize() {
	}

	/*
	 * initialize profile and window data
	 */
	void initData(String profileN) {
		profile = new UserProfile(profileN, JM.load(profileN));
		removeExpired();
		profileName.setText(profileN);
		setCurrentDate();
		setChoiceBox();
		listAppointments();
		displayFirstItem();
	}

	/*
	 * populates choice box with choices
	 */
	public void setChoiceBox() {
		ObservableList<String> choices = FXCollections.observableArrayList("Any Date", "Specific Date", "Due Today");
		choiceBox.setItems(choices);
		choiceBox.setValue("Any Date");
	}

	/*
	 * sets current date label with current date
	 */
	public void setCurrentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate localDate = LocalDate.now();
		currentDate.setText(dtf.format(localDate));
	}

	/*
	 * Launches appointment window to add new appointment
	 * 
	 * @param event event to trigger method
	 */
	@FXML
	void addAppointment(ActionEvent event) {
		try {
			Stage window = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/AddAppointmentWindow.fxml"));
			Scene scene = new Scene(loader.load());
			window.setTitle("Add Appointment");
			window.setScene(scene);
			AppointmentControl control = loader.<AppointmentControl>getController();
			control.initData(profile.getUserName());
			window.setResizable(false);
			window.sizeToScene();
			window.showAndWait();
			listAppointments();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * removes selected appointment from listView
	 * 
	 * @param event event to trigger method
	 */
	@FXML
	void removeAppointment(ActionEvent event) {
		if (confirmAlert()) {
			Appointment app = listBox.getSelectionModel().getSelectedItem();
			profile.deleteAppointment(app);
			JM.update(profile.getAppointments());
			listBox.getItems().remove(app);
			listBox.refresh();
			displayFirstItem();
		}
	}

	/*
	 * Mark currently selected appointment "completed"
	 * 
	 * @param event event that triggers method
	 */
	@FXML
	void markProgress(ActionEvent event) {
		Appointment modifyApp = listBox.getSelectionModel().getSelectedItem();
		if (confirmAlert()) {
			MenuItem contextItem = (MenuItem) event.getSource();
			String mark = contextItem.getText();
			for (Appointment app : profile.getAppointments()) {
				if (app.equals(modifyApp)) {
					if (mark.equals("Mark Completed") && !app.isCompleted()) {
						app.setCompleted(true);
					} else if (mark.equals("Mark Uncompleted") && app.isCompleted()) {
						app.setCompleted(false);
					}
				}
			}
			JM.update(profile.getAppointments());
			listBox.refresh();
			displayDetails();
		}
	}

	/*
	 * Displays details of appointment in detail space in GUI
	 */
	@FXML
	void displayDetails() {
		if (!listBox.getItems().isEmpty()) {
			Appointment app = listBox.getSelectionModel().getSelectedItem();
			titleDetail.setText(app.getTitle());
			startDetail.setText(app.reformatDate(app.getStartDate()));
			dueDateDetail.setText(app.reformatDate(app.getDueDate()));
			dueDetail.setText("" + app.isDueToday());
			completedDetail.setText("" + app.isCompleted());
			commentDetail.setText(app.getComment());
		}

	}

	/*
	 * populate listView with user saved appointments
	 * 
	 * @param event event to trigger the method
	 */
	public void showAppointments(ActionEvent event) {
		String menuText = choiceBox.getValue();
		if (menuText.equals("Any Date")) {
			listAppointments();
		} else if (menuText.equals("Specific Date")) {
			showSelectedDate(menuText);
		} else if (menuText.equals("Due Today")) {
			showSelectedDate(menuText);
		}
		displayFirstItem();

	}

	/*
	 * Removes all appointments that have been due for over 30 days
	 */
	private void removeExpired() {
		Iterator<Appointment> iter = profile.getAppointments().iterator();
		while (iter.hasNext()) {
			LocalDate due = LocalDate.parse(iter.next().getDueDate());
			LocalDate today = LocalDate.now();
			int daysBetween = (int) ChronoUnit.DAYS.between(due, today);
			if (daysBetween > 30) {
				iter.remove();
			}
		}
		listBox.refresh();
		JM.update(profile.getAppointments());
	}

	/*
	 * Selects first item in listView and displays details in the GUI detail box
	 */
	private void displayFirstItem() {
		if (listBox.getItems().size() != 0) {
			listBox.getSelectionModel().selectFirst();
			displayDetails();
		} else {
			String na = "N/A";
			titleDetail.setText(na);
			startDetail.setText(na);
			dueDateDetail.setText(na);
			dueDetail.setText(na);
			completedDetail.setText(na);
			commentDetail.setText("");
		}
	}

	/*
	 * populates listView with appointments of a specified date
	 * 
	 * @param dateCase determines whether to display current date or other specified
	 * date
	 */
	private void showSelectedDate(String dateCase) {
		profile.setAppointments(JM.load(profile.getUserName()));
		String date = "";
		if (dateCase.equals("Due Today")) {
			date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
		} else {
			if (mainDatePicker.getValue() != null) {
				date = mainDatePicker.getValue().format(DateTimeFormatter.ISO_DATE);
			} else {
				alertBox(AlertType.ERROR, "Invalid Date", null, "Please select a valid date!");
			}
		}
		listBox.getItems().clear();
		for (Appointment apps : profile.getAppointments()) {
			if (apps.getDueDate().equals(date)) {
				listBox.getItems().add(apps);
			}
		}
	}

	/*
	 * populates listView with all user appointments
	 */
	private void listAppointments() {
		profile.setAppointments(JM.load(profile.getUserName()));
		listBox.getItems().clear();
		for (Appointment apps : profile.getAppointments()) {
			listBox.getItems().add(apps);
		}
	}

	/*
	 * Closes current window
	 * 
	 * @param event event that triggers method
	 */
	public static void closeWindow(ActionEvent event) {
		Button btn = (Button) event.getSource();
		Stage window = (Stage) btn.getScene().getWindow();
		window.close();
	}

	/*
	 * Displays alertBox to screen
	 * 
	 * @param type type of alert
	 * 
	 * @param title alert title
	 * 
	 * @param header alert header
	 * 
	 * @param text alert text
	 */
	public static void alertBox(AlertType type, String title, String header, String text) {
		Alert a = new Alert(type, text);
		a.setTitle(title);
		a.setHeaderText(header);
		a.show();
	}

	/*
	 * displays confirm alert to user
	 * 
	 * @return if 'OK' button was clicked
	 */
	private static boolean confirmAlert() {
		Alert confirm = new Alert(AlertType.CONFIRMATION, "Are you sure?");
		confirm.setHeaderText("");
		confirm.setTitle("Confirm");
		Optional<ButtonType> result = confirm.showAndWait();
		return result.get() == ButtonType.OK;
	}

}
