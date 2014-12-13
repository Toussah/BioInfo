package app;

import ui.AppLabels;
import ui.MainWindow;

public class MainApp {

	MainWindow mWindow;
	/**
	 * @param args
	 */
	public MainApp(MainWindow w) {
		mWindow = w;
		mWindow.change_state(MainWindow.MODE_MENU);
		mWindow.set_title(AppLabels.APP_TITLE);
		
	}

}
