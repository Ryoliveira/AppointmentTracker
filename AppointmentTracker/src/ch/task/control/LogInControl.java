package ch.task.control;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import org.jdom2.JDOMException;

import ch.task.app.MainApp;
import ch.task.file.LoginManager;
import ch.task.file.RegistrationManager;
import ch.task.file.UserRepository;
import ch.task.file.UserXmlRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LogInControl {
	// Username/Password validation requirements
	// Username must be between 8 and 16 characters
	final String USERNAME_PAT = "^(?=.{8,20}$)[a-zA-Z0-9._]+$";
	// Password must be between 8 and 16 digits and include at least one digit.
	final String PASSWORD_PAT = "^(?=.*\\d).{8,16}$";
	// Main log in fields
	@FXML
	private TextField userLogIn;
	@FXML
	private PasswordField userPassword;

	// Registration fields
	@FXML
	private TextField newUsername;
	@FXML
	private PasswordField newPassword;
	@FXML
	private PasswordField matchPassword;

	private UserRepository userRepo = new UserXmlRepository();
	private RegistrationManager RM = new RegistrationManager();

	/*
	 * Validates and creates new profile for user
	 */
	@FXML
	void createNewProfile(ActionEvent event) throws JDOMException, IOException {
		String username = newUsername.getText();
		String password = newPassword.getText();
		String checkMatchPass = matchPassword.getText();
		// Check for a none blank unique username
		boolean isValidUserName = RM.validate(username, USERNAME_PAT);
		boolean validPass = RM.validate(password, PASSWORD_PAT);
		boolean isUniqueName = !RM.checkForProfile(username);
		boolean notBlank = !password.isEmpty();
		// check if password was entered correctly in both fields
		boolean isMatch = password.equals(checkMatchPass);
		if (!isValidUserName) {
			MainControl.alertBox(AlertType.ERROR, "Invalid Username", null,
					"Username must be between 8 and 16 characters");
		} else if (!notBlank) {
			MainControl.alertBox(AlertType.ERROR, "Empty password", null, "Password cannot be blank!");
		} else if (!validPass) {
			MainControl.alertBox(AlertType.ERROR, "Invalid Password", null,
					"Password must be between 8 and 16 characters and include one digit.");
		} else if (!isMatch) {
			MainControl.alertBox(AlertType.ERROR, "Password mismatch", null, "Repeated password does not match!");
		} else if (!isUniqueName) {
			MainControl.alertBox(AlertType.ERROR, "Username taken", null, "Username already in use!");
			newUsername.clear();
		} else {
			userRepo.writeToFile(username, password);
			MainControl.alertBox(AlertType.INFORMATION, "Profile Creation", null, "Profile has been created!");
			closeWindow(event);
		}
		newPassword.clear();
		matchPassword.clear();

	}

	/*
	 * Checks for profile based on user provided username and password
	 */
	@FXML
	void AttemptLogIn(ActionEvent event) throws IOException {
		String profile = userLogIn.getText();
		String password = userPassword.getText();
		boolean valid = LoginManager.validateLogIn(profile, password);
		if (valid) {
			launchHomePage(profile);
			closeWindow(event);
		} else {
			MainControl.alertBox(AlertType.ERROR, "Denied", null, "Invalid Username or Password.");
			userLogIn.clear();
			userPassword.clear();
		}

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

	/*
	 * opens registration window to create new profile for user
	 */
	@FXML
	void openRegWin(ActionEvent event) {
		try {
			Stage window = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/Registration.fxml"));
			Scene scene = new Scene(loader.load());
			window.setScene(scene);
			window.setResizable(false);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * launch home page for application
	 */
	private void launchHomePage(String profile) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Main.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		Stage stage = new Stage();
		stage.setTitle("Appointment Tracker");
		stage.setScene(scene);
		stage.show();
		stage.setResizable(false);
		stage.sizeToScene();

		MainControl controller = loader.<MainControl>getController();
		controller.initData(profile);
	}

}
