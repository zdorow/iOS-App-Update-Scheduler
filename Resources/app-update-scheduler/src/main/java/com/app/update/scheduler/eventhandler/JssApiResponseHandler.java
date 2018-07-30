package com.app.update.scheduler.eventhandler;

import com.app.update.scheduler.controller.form.AppUpdateForm;
import com.app.update.scheduler.jamfpro.api.JssApi;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class JssApiResponseHandler implements EventHandler<WorkerStateEvent> {

	private JssApi jssApi;
	private Text actiontarget;
	private TextFlow actiontargetPane;
	private Button button;
        
	public JssApiResponseHandler(JssApi jssApi, AppUpdateForm appUpdateForm) {
		this.jssApi = jssApi;
		this.actiontarget = appUpdateForm.getActiontarget();
		this.actiontargetPane = appUpdateForm.getActiontargetPane();
		this.button = appUpdateForm.getButton();
	}
	
	@Override
	public void handle(WorkerStateEvent event) {
		switch (jssApi.getLastResponseCode()) {
		case 401:
			markError("Username and/or password not accepted.");
			break;
		case 0:
			markError("URL was not found.");
			break;
        case 403:
			markError("Please check your hostname.");
			break;
       case 404:
			markError("Please check the port number. For locally hosted we need :8443");
			break;
       case 200: 
            markError("No app updates are enabled at the individual level.");
            break;
		default:                       
			markError("Please file an issue on Github. Last API response code: " + jssApi.getLastResponseCode());
			System.out.println("This is the last API response code: " + jssApi.getLastResponseCode());
			break;
		}
		
		button.setDisable(false);
	}
        
	private void markError(String errorMessage) {
		actiontargetPane.getStyleClass().clear();
		actiontargetPane.getStyleClass().add("alert");
		actiontargetPane.getStyleClass().add("alert-danger");
		actiontarget.setText(errorMessage);
	}
}
