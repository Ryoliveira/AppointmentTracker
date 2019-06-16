package ch.task.control;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import ch.task.app.MainApp;
import ch.task.file.UserJsonRepository;
import ch.task.file.UserRepository;
import ch.task.user.Appointment;
import ch.task.user.UserProfile;
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
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

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
	private UserRepository JM = new UserJsonRepository();

	void initialize() {
	}

	/*
	 * initialize profile and window data
	 */
	void initData(String profileN) {
		profile = JM.load(profileN);
		profileName.setText(profile.getUserName());
		setCurrentDate();
		listAppointments();
		setChoiceBox();
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
		// System.out.println(dtf.format(localDate));
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
			window.show();
			window.setResizable(false);
			window.sizeToScene();
			AppointmentControl control = loader.<AppointmentControl>getController();
			control.initData(profile.getUserName());
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
			JM.save(profile);
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
	void markComplete(ActionEvent event) {
		Appointment modifyApp = listBox.getSelectionModel().getSelectedItem();
		if (confirmAlert()) {
			for (Appointment app : profile.getAppointments()) {
				if (app.equals(modifyApp)) {
					app.setCompleted(true);
				}
			}
			JM.save(profile);
			listBox.refresh();
			displayDetails();
		}
	}

	/*
	 * Mark currently selected appointment "uncompleted"
	 * 
	 * @param event event to trigger method
	 */
	@FXML
	void markUncomplete(ActionEvent event) {
		Appointment modifyApp = listBox.getSelectionModel().getSelectedItem();
		if (confirmAlert()) {
			for (Appointment app : profile.getAppointments()) {
				if (app.equals(modifyApp)) {
					app.setCompleted(false);
				}
			}
			JM.save(profile);
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
	 * Selects first item in listView and displays details in the GUI detail box
	 */
	public void displayFirstItem() {
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
	public void showSelectedDate(String dateCase) {
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
		profile = JM.load(profile.getUserName());
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
	public void listAppointments() {
		profile = JM.load(profile.getUserName());
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
	 * @return true if 'OK' button was clicked, false otherwise
	 */
	public static boolean confirmAlert() {
		Alert confirm = new Alert(AlertType.CONFIRMATION, "Are you sure?");
		confirm.setHeaderText("");
		confirm.setTitle("Confirm");
		Optional<ButtonType> result = confirm.showAndWait();
		return result.get() == ButtonType.OK;
	}

}
