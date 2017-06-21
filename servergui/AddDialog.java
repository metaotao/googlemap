package servergui;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
* @author tao
*/
public class AddDialog extends JDialog implements ActionListener{
	private JTextField defaultField;
	private JButton button;
	private JLabel label;

	public static AddDialog addDialog;
	
	public static AddDialog instance(){
		if(addDialog==null){
			addDialog=new AddDialog();
		}
		return addDialog;
	}

	public AddDialog(){
		addDialog=this;
		setSize(400,300);
		setLocation(700,300);
		setLayout(null);
		init();
		
		setVisible(true);
	}

	public void init(){
		defaultField=new JTextField("3x4");
		defaultField.setFont(new Font("Ó×Ô²",Font.PLAIN,16));
		defaultField.setBounds(70,100,160,30);
		add(defaultField);

		button=new JButton("add");
		button.setFont(new Font("Ó×Ô²",Font.PLAIN,16));
		button.setBounds(250,100,80,30);
		button.addActionListener(this);
		add(button);

		label=new JLabel();
		label.setText("Çë°´ÕÕÉÏÊöÐÎÊ½ÌîÐ´!");
		label.setFont(new Font("Ó×Ô²",Font.PLAIN,16));
		label.setBounds(100,150,200,30);
		add(label);
	}

	public void actionPerformed(ActionEvent e){
		String str=defaultField.getText();
		try{
			String[] split=str.split("x");	
		}
		catch(Exception e1){
		}
		new IPDialog(str);
		this.setVisible(false);		
	}

}