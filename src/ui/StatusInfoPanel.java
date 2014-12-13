package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import app.App;

public class StatusInfoPanel extends JPanel implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NEWLINE = 0;
	public static final int APPEND = 1;
	public static final int REPLACE = 2;
	private JProgressBar status_progress;
	private JButton cancel_button;
	private JTextArea progress_log;
	private JScrollPane textContainer;
	
	@SuppressWarnings("unused")
	private Runnable maj_AreaText = new Runnable()
    {
         public void run()
         {

        	 
         }
    };
	
	public StatusInfoPanel() {
		super();
		this.buildContentPane();
	}
	
	private void buildContentPane(){

		this.setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel upPanel = new JPanel();
		upPanel.setLayout(new GridBagLayout());
 		status_progress = new JProgressBar();
 		c.fill = GridBagConstraints.BOTH;
 		c.weightx = 1.0;
 		c.weighty = 1.0;
 		c.gridx = 0;
 		c.gridy = 0;
 		c.ipady = 5;
 		c.anchor = GridBagConstraints.FIRST_LINE_START;
 		c.insets = new Insets(3,5,3,5);
 		upPanel.add(status_progress,  c);

 		status_progress.setValue(0);
 		status_progress.setIndeterminate(false);
 		status_progress.setDoubleBuffered(true);
 		status_progress.setStringPainted(false);
 		status_progress.setForeground(new Color(102, 204, 0));
 		status_progress.setFont(new Font("Dialog", Font.BOLD, 18));

 		
		cancel_button = new JButton("Cancel");
		cancel_button.addActionListener(this);
		c.gridx = 1;
 		c.gridy = 0;
 		c.gridwidth = 1;
 		c.weightx = 0.0;
 		c.fill = GridBagConstraints.NONE;
 		c.insets = new Insets(5,5,5,10);
 		c.anchor = GridBagConstraints.FIRST_LINE_END;
 		cancel_button.setVisible(false);
		upPanel.add(cancel_button, c);
		
		
		progress_log = new JTextArea();
		progress_log.setSize(800, 150);
		progress_log.setAutoscrolls(true);
		progress_log.setLineWrap(true);
		progress_log.setEditable(false);
		progress_log.setWrapStyleWord(true);
		progress_log.setDoubleBuffered(true);
		
		textContainer = new JScrollPane();
		textContainer.setAutoscrolls(true);
		textContainer.setViewportView(progress_log);
		this.add(upPanel, BorderLayout.NORTH);
	
		this.add(textContainer, BorderLayout.CENTER);

	}
	
	public JTextArea getTextArea(){
		return this.progress_log;
	}
	
	public JProgressBar getProgressBar(){
		return this.status_progress;
	}
	
	public void setProgressBarValue(int v){
		this.status_progress.setValue(v);
		this.status_progress.setIndeterminate(false);
		this.paintImmediately(this.status_progress.getX(), this.status_progress.getY(), this.status_progress.getWidth(), this.status_progress.getHeight());
	}
 
	public void add_text(String txt, int mode){
		switch(mode){
			case NEWLINE:
				this.progress_log.append("\n" + txt);
				break;
			case APPEND:
				this.progress_log.append(txt);
				break;
			case REPLACE:
				this.progress_log.setText("");
			default:
				break;
		}

		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textContainer.getVerticalScrollBar().setValue(textContainer.getVerticalScrollBar().getMaximum());
	        	 textContainer.paintImmediately(progress_log.getX(), progress_log.getY(), progress_log.getWidth(), progress_log.getHeight());
			}
		});

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
 		Object source = e.getSource();
		if(source == cancel_button){
			App.cancel();
		}
	}


}