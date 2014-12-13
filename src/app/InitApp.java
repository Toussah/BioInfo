package app;

import ui.AppLabels;
import ui.MainWindow;

public class InitApp {
	
	MainWindow mWindow;
	
	public InitApp(MainWindow w){
		mWindow = w;
		mWindow.change_state(MainWindow.MODE_INIT);
		mWindow.set_title(AppLabels.APP_TITLE);
		
		/*demo utilisation de la fenêtre d'initialisation*/
		//mWindow.add_newline_initPanel_textArea(AppLabels.APP_PREPARE);
	}
}
