import java.io.*;
import java.net.*;
import java.util.*;

class MyServerChat {
	ArrayList al=new ArrayList();
	ServerSocket ss;
	Socket s;
	int count=1;
	public MyServerChat()
	{
		try
		{
			ss=new ServerSocket(1099);
			System.out.println("Server Started");
			while(true)
			{
				s=ss.accept();
				System.out.println("Client Connected No " + count++);
				al.add(s);
				MyThread mt =new MyThread(s,al);
				Thread t=new Thread(mt);
				t.start();
			}
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public static void main(String[] args) {
		new MyServerChat();
	}

}
class MyThread implements Runnable
{
	Socket s;
	ArrayList al;
	MyThread(Socket s,ArrayList al)
	{
		this.s=s;
		this.al=al;
	}
	public void run()
	{
		String s1;
		try
		{
			DataInputStream din=new DataInputStream(s.getInputStream());
			do
			{
				s1=din.readUTF();
				System.out.println(s1);
				if(!s1.equals("stop"))
					tellEveryOne(s1);
				else
				{
					DataOutputStream  dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF(s1);
					dout.flush();
					al.remove(s);
				}
			}while(!s1.equals("stop"));
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public void tellEveryOne(String s1)
	{
		Iterator i=al.iterator();
		while(i.hasNext())
		{
			try
			{
				Socket sc=(Socket)i.next();
				DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
				dout.writeUTF(s1);
				dout.flush();
				//System.out.println("Client");
			}catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
}