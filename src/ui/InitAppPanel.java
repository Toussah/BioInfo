package ui;

import javax.swing.JPanel;

public class InitAppPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NEWLINE = 0;
	public static final int APPEND = 1;
	public static final int REPLACE = 2;
	
	/**
	 * Create the panel.
	 */
	public InitAppPanel() {
		/*setLayout(new MigLayout("", "[][537.00px,grow][]", "[229.00px][grow][98.00px:98.00px][:114.00px:114px][]"));
		
		JLabel titleLabel = new JLabel("");
		add(titleLabel, "cell 1 0,alignx center,aligny center");
		titleLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 28));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setText(AppLabels.APP_INIT_TITLE);
		titleLabel.setLabelFor(this);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		add(verticalGlue_1, "cell 1 1,alignx center,growy");
		
		progressBar = new JProgressBar();
		add(progressBar, "cell 1 2,growx,aligny center");
		progressBar.setValue(0);
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(false);
		progressBar.setForeground(new Color(102, 204, 0));
		progressBar.setFont(new Font("Dialog", Font.BOLD, 18));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		add(horizontalStrut, "cell 0 3");
		
		scrollPane = new JScrollPane();
		add(scrollPane, "cell 1 3,grow");
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setBackground(new Color(238, 238, 238));
		textPane.setEditable(false);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		add(horizontalStrut_1, "cell 2 3");
		
		Component verticalGlue_2 = Box.createVerticalGlue();
		add(verticalGlue_2, "flowx,cell 1 4");
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut, "cell 1 4,growx");
		
		ImageIcon labelimg = new ImageIcon(InitAppPanel.class.getResource(AppPaths.APP_INIT_BACKGROUND ));
		background_img = labelimg.getImage();*/
	}
	/*
	public void set_progressBar_value(int v){
		this.progressBar.setValue(v);
		this.paintImmediately(this.progressBar.getX(), this.progressBar.getY(), this.progressBar.getWidth(), this.progressBar.getHeight());

	}
	
	public int get_progressBar_value(){
		return this.progressBar.getValue();
	}
	
	public void set_progressBar_stringPainted(boolean b){
		this.progressBar.setStringPainted(b);
	}
	
	public void set_progressBar_indeterminate(boolean b){
		this.progressBar.setIndeterminate(b);
	}
	
	public void add_text(String txt, int mode){
		switch(mode){
			case NEWLINE:
				this.textPane.setText(this.textPane.getText() + "\n" + txt);
				break;
			case APPEND:
				this.textPane.setText(this.textPane.getText() + txt);
				break;
			case REPLACE:
				this.textPane.setText(txt);
			default:
				break;
		}
		scrollPaneToBottom();
	}

	
	public void clear_text(){
		this.textPane.setText("");
	}
	
	@Override
	public void paintComponent(final Graphics g) {
	    super.paintComponent(g);
	    if (background_img != null)
	        g.drawImage(background_img, 0, 0, this.getWidth(), this.getHeight(), null);
	}
	
	
	private void scrollPaneToBottom() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				scrollPane.paintImmediately(textPane.getX(), textPane.getY(), textPane.getWidth(), textPane.getHeight());
			}
		});
	} */
}
