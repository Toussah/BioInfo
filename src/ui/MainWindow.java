package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import utils.Interpreter;
import utils.Linker;

public class MainWindow implements Runnable{

	private static final MainWindow INSTANCE = new MainWindow();
	
	private BorderLayout borderLayout;
	private InitAppPanel mInitAppPanel;
	private MainAppPanel mMainAppPanel;
	private JFrame frame;
	public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

	public static final int MODE_INIT = 1;
	public static final int MODE_MENU = 2;
	public static final int MODE_DOWNLOAD = 3;

	private int current_mode;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		current_mode = MODE_INIT;
	}
	
	
	/*
	 * 
	 * PUBLIC METHODS
	 * 
	 */
	
	/**
	 * Launch the application.
	 */
	public MainWindow launch_instance() {
		if(this.frame.isActive())
			this.frame.dispose();
		EventQueue.invokeLater(this);
		return this;
	}

	public void run() {
		try {
			this.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int get_state(){
		return this.current_mode;
	}
	
	public void change_state(int new_mode){
		if( check_mode_ok(new_mode)){
			current_mode = new_mode;
			this.load_mode();
		}
	}
	
	public void set_title(String title){
		this.frame.setTitle(title);
	}
	
	public String get_title(){
		return this.frame.getTitle();
	}
	/*
	public void set_initPanel_progressBar_value(int value){
		if(value >= 0 && value <= 100){
			this.mInitAppPanel.set_progressBar_value(value);
			this.mInitAppPanel.set_progressBar_stringPainted(true);
			this.mInitAppPanel.set_progressBar_indeterminate(false);
		}
	}
	/*
	public int get_initPanel_progressBar_value(){
		return this.mInitAppPanel.get_progressBar_value();
	}
	
	public void set_initPanel_progressBar_indeterminate(){
		this.mInitAppPanel.set_progressBar_indeterminate(true);
		this.mInitAppPanel.set_progressBar_stringPainted(false);
	}
	*/
	public void set_progressBar_value(int i) {
		this.mMainAppPanel.getStatusInfoPanel().setProgressBarValue(i);
		
	}
	
	public int get_progressBar_value() {
		return this.mMainAppPanel.getStatusInfoPanel().getProgressBar().getValue();
		
	}
/*
	public void append_text_initPanel_textArea(String txt){
		this.mInitAppPanel.add_text(txt, InitAppPanel.APPEND);
	}
	
	public void add_newline_initPanel_textArea(String txt){
		this.mInitAppPanel.add_text(txt, InitAppPanel.NEWLINE);
	}
	
	public void clear_initPanel_textArea(){
		this.mInitAppPanel.clear_text();
	}
*/
	public void print_tree(){
		this.mMainAppPanel.printSelectedPaths();
	}
	
	public void update_tree(JTree tree){
		this.mMainAppPanel.setCheckBoxTree(tree);
	}
	
	public void setSelectedItems(int v){
		this.mMainAppPanel.changeSelectedItemsValue(v);
	}
	
	public void add_to_log(String txt) {
		this.mMainAppPanel.getStatusInfoPanel().add_text(txt, StatusInfoPanel.NEWLINE);
	}


	public void append_to_log(String txt) {
		this.mMainAppPanel.getStatusInfoPanel().add_text(txt, StatusInfoPanel.APPEND);
	}
	
	public void clear_log(){
		this.mMainAppPanel.getStatusInfoPanel().add_text("", StatusInfoPanel.REPLACE);
	}
	
	
	public TreePath[] getSelectedTreePaths() {
		return this.mMainAppPanel.getTreeSelection();
	}
	/*
	 * 
	 * PRIVATE METHODS
	 * 
	 */
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = Toolkit.getDefaultToolkit().getImage(this.frame.getClass().getResource("/ui/icon.png"));    
		this.frame.setIconImage(icon);
        // Set the window's bounds, centering the window
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - MainWindow.WINDOW_WIDTH) / 2;
        int y = (screen.height - MainWindow.WINDOW_HEIGHT) / 2;
	    this.frame.setBounds(x, y, MainWindow.WINDOW_WIDTH, MainWindow.WINDOW_HEIGHT);
	        
	    this.frame.pack();
	    this.frame.setVisible(true);
	}
	
	private boolean check_mode_ok(int mode){
		boolean v = false;
		switch(mode){
			case MODE_INIT:
			case MODE_MENU:
			case MODE_DOWNLOAD:
				v= true;
				break;
			default:
				v = false;
		}
		return v;
	}
	
	private void load_mode(){
		switch(this.current_mode){
			case MODE_INIT:
				this.launch_instance();
				this.setInitAppContent();
				break;
			case MODE_MENU:
				this.setMainAppContent();
				break;
			case MODE_DOWNLOAD:
				break;
		}	
	}
	
	private void reinit_window(){
		this.frame.getContentPane().removeAll();
	}

	private void setInitAppContent(){
		this.reinit_window();
		this.frame.setBounds(700, 300, 700, 500);
		mInitAppPanel = new InitAppPanel();
		this.frame.getContentPane().add(mInitAppPanel, BorderLayout.CENTER);
		this.frame.paintComponents(this.frame.getGraphics());
	}
	
	private void setMainAppContent(){
		this.reinit_window();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - MainWindow.WINDOW_WIDTH) / 2;
        int y = (screen.height - MainWindow.WINDOW_HEIGHT) / 2;
	    this.frame.setBounds(x, y, MainWindow.WINDOW_WIDTH, MainWindow.WINDOW_HEIGHT);

		mMainAppPanel = new MainAppPanel();
		mMainAppPanel.setSize(MainWindow.WINDOW_WIDTH, MainWindow.WINDOW_HEIGHT);
		mMainAppPanel.buildContent(Linker.JTreeFromJsonArray(Interpreter.open_json_file()));
		this.frame.getContentPane().add(mMainAppPanel);
		this.frame.revalidate();
		this.frame.paintComponents(this.frame.getGraphics());
		this.frame.repaint();

	}


	/**
	 * @return the borderLayout
	 */
	public BorderLayout getBorderLayout() {
		return borderLayout;
	}


	/**
	 * @param borderLayout the borderLayout to set
	 */
	public void setBorderLayout(BorderLayout borderLayout) {
		this.borderLayout = borderLayout;
	}
	
	
	public static MainWindow getInstance() {	
        return INSTANCE;
    }


	public void start_progressBar() {
		this.mMainAppPanel.getStatusInfoPanel().getProgressBar().setStringPainted(true);
		
	}
	
	public void stop_progressBar() {
		this.mMainAppPanel.getStatusInfoPanel().getProgressBar().setStringPainted(false);
		
	}


	public void set_progressBar_indeterminate() {
		this.mMainAppPanel.getStatusInfoPanel().getProgressBar().setIndeterminate(true);
		
	}
	
}
