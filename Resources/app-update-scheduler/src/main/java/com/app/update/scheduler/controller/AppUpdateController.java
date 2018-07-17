package com.app.update.scheduler.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.app.update.scheduler.controller.form.AppUpdateForm;
import com.app.update.scheduler.controller.validator.AppUpdateValidator;
import com.app.update.scheduler.jamfpro.api.JssApi;
import com.app.update.scheduler.jamfpro.api.JssApi.FORMAT;
import com.app.update.scheduler.option.AppUpdateSchedulerOption;
import com.app.update.scheduler.option.TimeFrame;
import com.app.update.scheduler.service.ApplicationListService;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class AppUpdateController implements Initializable {

	@FXML
	private TextField jamfProServerUrl;

	@FXML
	private TextField userName;

	@FXML
	private PasswordField password;

	@FXML
	private ComboBox<String> appSchedulerOptions;

	@FXML
	private Label timeFrameLabel;

	@FXML
	private HBox timeFrameOptions;

	@FXML
	private ComboBox<String> timeFrameStartOptions;

	@FXML
	private ComboBox<String> timeFrameEndOptions;
	
	@FXML
	private TextFlow actiontargetPane;

	@FXML
	private Text actiontarget;

	@FXML
	private Button button;

	@FXML
	private ProgressBar progressBar;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		
		AppUpdateForm appUpdateForm = new AppUpdateForm(jamfProServerUrl, userName, password, appSchedulerOptions, timeFrameLabel, timeFrameOptions, timeFrameStartOptions, 
				timeFrameEndOptions, actiontargetPane, actiontarget, button, progressBar);
                
		AppUpdateValidator appUpdateValidator = new AppUpdateValidator(appUpdateForm);		
		appUpdateValidator.validate();		
		if (appUpdateValidator.hasErrors()) {
			return;
		}
		
		// Clear any existing text, disable the button and activate the progress bar
//		actiontarget.setText("");
                appUpdateForm.getActiontargetPane().getStyleClass().clear();
                appUpdateForm.getActiontargetPane().getStyleClass().add("alert");
		appUpdateForm.getActiontargetPane().getStyleClass().add("alert-success");
		appUpdateForm.getActiontarget().setText("");
		button.setDisable(true);
		progressBar.setVisible(true);
		
		AppUpdateSchedulerOption schedulerOption = AppUpdateSchedulerOption.fromDisplayText(appSchedulerOptions.getValue());
	
		JssApi jssApi = new JssApi(jamfProServerUrl.getText(), userName.getText(), password.getText(), FORMAT.XML, FORMAT.XML);
		try {
			new ApplicationListService(jssApi, actiontarget, progressBar, button, timeFrameStartOptions, timeFrameEndOptions, schedulerOption, appUpdateForm).start();
			System.out.println("ApplicationListService has STARTED.");
		} catch(Exception e){
			actiontarget.setText("Something really went wrong. Please file an issue on Github.");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		for (AppUpdateSchedulerOption schedulerOption : AppUpdateSchedulerOption.values()) {
			appSchedulerOptions.getItems().add(schedulerOption.getDisplayText());

			if (schedulerOption.isDefaultSelectedValue()) {
				appSchedulerOptions.getSelectionModel().select(schedulerOption.getDisplayText());
			}
		}

		for (TimeFrame timeFrameOption : TimeFrame.values()) {
			timeFrameStartOptions.getItems().add(timeFrameOption.getDisplayText());
		}

		for (TimeFrame timeFrameOption : TimeFrame.values()) {
			timeFrameEndOptions.getItems().add(timeFrameOption.getDisplayText());
		}

		appSchedulerOptions.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			try {
				AppUpdateSchedulerOption schedulerOption = AppUpdateSchedulerOption.fromDisplayText(observable.getValue());

				if (schedulerOption == AppUpdateSchedulerOption.TimeInterval) {
					timeFrameLabel.setDisable(false);
					timeFrameOptions.setDisable(false);
				} else {
					timeFrameLabel.setDisable(true);
					timeFrameOptions.setDisable(true);
				}
			} catch (Exception e) {
				actiontarget.setText("Something really went wrong. Please file an issue on Github.");
			}
		});
	}
}