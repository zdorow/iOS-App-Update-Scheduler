package com.app.update.scheduler.controller.form;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AppUpdateForm {

	private TextField jamfProServerUrl;
	private TextField userName;
	private PasswordField password;
	private ComboBox<String> appSchedulerOptions;
	private Label timeFrameLabel;
	private HBox timeFrameOptions;
	private ComboBox<String> timeFrameStartOptions;
	private ComboBox<String> timeFrameEndOptions;
	private TextFlow actiontargetPane;
	private Text actiontarget;
	private Button button;
	private ProgressBar progressBar;
	
	public AppUpdateForm(TextField jamfProServerUrl, TextField userName, PasswordField password, ComboBox<String> appSchedulerOptions, Label timeFrameLabel, HBox timeFrameOptions,
			ComboBox<String> timeFrameStartOptions, ComboBox<String> timeFrameEndOptions, TextFlow actiontargetPane, Text actiontarget, Button button, ProgressBar progressBar) {
		this.jamfProServerUrl = jamfProServerUrl;
		this.userName = userName;
		this.password = password;
		this.appSchedulerOptions = appSchedulerOptions;
		this.timeFrameLabel = timeFrameLabel;
		this.timeFrameOptions = timeFrameOptions;
		this.timeFrameStartOptions = timeFrameStartOptions;
		this.timeFrameEndOptions = timeFrameEndOptions;
		this.actiontargetPane = actiontargetPane;
		this.actiontarget = actiontarget;
		this.button = button;
		this.progressBar = progressBar;
	}

	public TextField getJamfProServerUrl() {
		return jamfProServerUrl;
	}

	public TextField getUserName() {
		return userName;
	}

	public PasswordField getPassword() {
		return password;
	}

	public ComboBox<String> getAppSchedulerOptions() {
		return appSchedulerOptions;
	}

	public Label getTimeFrameLabel() {
		return timeFrameLabel;
	}

	public HBox getTimeFrameOptions() {
		return timeFrameOptions;
	}

	public ComboBox<String> getTimeFrameStartOptions() {
		return timeFrameStartOptions;
	}

	public ComboBox<String> getTimeFrameEndOptions() {
		return timeFrameEndOptions;
	}

	public TextFlow getActiontargetPane() {
		return actiontargetPane;
	}

	public Text getActiontarget() {
		return actiontarget;
	}

	public Button getButton() {
		return button;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}
}
