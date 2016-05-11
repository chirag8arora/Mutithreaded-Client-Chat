import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class ChatClient extends JFrame implements ActionListener
{
	static JTextArea message_area;
	JTextField send_area;
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	static String name="";
	static JButton send;
	static JButton clear;
	JScrollPane jsp;
	ChatClient()
	{
		// this.addActionListener(this);
		this.setSize(800,600);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		message_area = new JTextArea();
		message_area.setEditable(false);
		this.add(message_area,"Center");
		message_area.setFont(new Font("Arial",Font.PLAIN,16));


		Panel p = new Panel();
		p.setLayout(new FlowLayout());

		send_area=new JTextField(30);
		// send_area.addActionListenter(this);
		send_area.setFont(new Font("Arial",Font.PLAIN,16));


		p.add(send_area);
		p.setBackground(new Color(221,221,221));

		send =new JButton("Send");
		send.addActionListener(this);
		p.add(send);

		jsp=new JScrollPane(message_area);
		jsp.setBounds(50,60,320,150);
		JButton clear=new JButton("Clear");
		clear.addActionListener(this);
		p.add(clear);

		this.add(p,"South");
		this.setVisible(true);
		message_area.setText("Enter Your Name");
		try{
		s=new Socket("localhost",1099);
		din=new DataInputStream(s.getInputStream());
		dout=new DataOutputStream(s.getOutputStream());
		}catch(Exception ioe)
		{
			System.out.println(ioe);
		}
		
	}
		
	

	public void actionPerformed(ActionEvent e)
	{
		
		// send_area.setText("");
		if(e.getSource() == send)
		{
			try{
			if(ChatClient.name.equals(""))
			{
				String str=send_area.getText();
				ChatClient.name=str;
				ChatClient.message_area.setText("");
				JOptionPane.showMessageDialog(null, "Name Successfully Saved");
				JOptionPane.showMessageDialog(null, "You are connected");
				send_area.setText("");
			}
			else
			{	
			clientChat_send();
			send_area.setText("");
			}
		}catch(Exception eio)
		{
			System.out.println(eio);
		}
		}

	}
	public void clientChat_send() throws IOException
	{

		
		// System.out.print("Type : ");
		String msg1=send_area.getText();
		// message_area.setText(msg1);

		do
		{
			String s1=name+" : "+msg1;
			dout.writeUTF(s1);
			dout.flush();
			// message_area.append(s1);
			break;
		}while(true);
	}
	public void clientChat_receive()
	{
		My m=new My(din,name);
		Thread t1=new Thread(m);
		t1.start();
	}
	public static void main(String[] args) {
		 ChatClient cl =new ChatClient();
		 cl.clientChat_receive();
	}
}

class My implements Runnable
{
	
	private final static String newline = "\n";
	DataInputStream din;
	String name;
	My(DataInputStream din,String name)
	{
		this.din=din;
		this.name=name;
	}
	public void run()
	{
		String s2="";

		// System.out.println(s2);
		// System.out.println(name);
		do{
			try
			{
				s2=din.readUTF();
				// if(!s2.contains(name))
					 ChatClient.message_area.append(s2 + newline);
					// System.out.println(s2);
				
			}catch(Exception e)
			{
				System.out.println(e);
			}
			}while(true);
	}
	}
