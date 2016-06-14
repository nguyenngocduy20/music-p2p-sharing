package backend;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import test.frm;

public class receive implements Runnable
{
	public String threadName;
	public int yPort; // port nhan mac dinh
	public String yIP; // ip nhan mac dinh
	public int sPort; // port gui
	public ReadNode nodes = new ReadNode();
	public String content; // chua ten bai hat
	public List<available> v_avail = new ArrayList<available>();
	public DatagramPacket packet;
	
	public receive()
	{
		System.out.println("Init RECEIVE");
		nodes.read();
		this.yPort = nodes.lstNodes.nodes[0].port;
		this.yIP = nodes.lstNodes.nodes[0].ip;
		System.out.println("Your IP: " + yIP + "\tYour port: " + yPort);
	}
	
	public receive(String name)
	{
		System.out.println("Init RECEIVE");
		nodes.read();
		this.threadName = name;
		this.yPort = nodes.lstNodes.nodes[0].port;
		this.yIP = nodes.lstNodes.nodes[0].ip;
		System.out.println("Thread name: " + this.threadName +"\tYour IP: " + yIP + "\tYour port: " + yPort);
	}
	
	public void run()
	{
		while(true)
		{
		try
		{
			InetAddress IPAddress = InetAddress.getByName(yIP);
			System.out.println("Init DatagramSocket");
			//System.out.println("Thread name: " + this.threadName +"\tYour IP: " + IPAddress.toString() + "\tYour port: " + yPort);
			DatagramSocket ds = new DatagramSocket(yPort, IPAddress);
			System.out.println("Init RECEIVE datagramSocket successful");
			
			byte[] receiveData = new byte[1024];
			
			// nhan goi tin
			packet = new DatagramPacket(receiveData, receiveData.length);
			ds.receive(packet);
			System.out.println("Received packet from " + packet.getAddress().toString() + "\tport " + packet.getPort());

			System.out.println("Content <" + modifyPacket(packet) + ">");
			// phan tich goi tin
			String flag = PacketAnalysing(packet);
			System.out.println("Flag: " + flag);
			
			// nhan duoc goi syn yeu cau xac nhan song sot -> gui goi tin flag = "ack"
			if(flag == "syn")
			{
				send ack = new send("ACK", "ack");
				ack.IpDest = packet.getAddress();
				ack.PortDest = packet.getPort();
				
				// khoi chay ham run() cua class send
				Thread mt = new Thread(ack);
				mt.start();
			}
			
			// nhan duoc goi ack --> chua biet phai lam gi?
			if(flag == "ack")
			{
				// do nothing
			}
			
			// nhan duoc goi req songName yeu cau tim bai hat -> gui goi tin flag = "res"
			if(flag == "req")
			{
				// tao thread send
				send res = new send("Respond", "res");
				
				// init ip dich, port dich
				res.IpDest = packet.getAddress();
				res.PortDest = packet.getPort() - 1;
				
				String data = modifyPacket(packet);
				data = data.substring(4, data.length() - 1);
				
				// init content cho goi send flag="res" tiep theo.
				res.content = data;

				// khoi chay ham run() cua class send
				Thread mt = new Thread(res);
				mt.start();
			}
			
			// nhan duoc goi res songName tra ve tim thay hoac khong -> gui goi tin flag = "start"
			if(flag == "res")
			{
				System.out.println("Packet RES received");
				String songName = modifyPacket(packet).substring(4, modifyPacket(packet).length() - 2);
				System.out.println("Content <" + modifyPacket(packet) + ">");
				int h = Integer.parseInt(modifyPacket(packet).substring(modifyPacket(packet).length() - 1, modifyPacket(packet).length()), 10);
				v_avail.clear();
				if(h == 1)
				{
					v_avail.add(new available(songName, packet.getAddress().toString(), packet.getPort() - 1));
				}
				for(int i=0; i<v_avail.size(); i++)
				{
					System.out.println(v_avail.get(i).content + "\t" + v_avail.get(i).t_node.ip + "\t" + v_avail.get(i).t_node.port);
				}
				/*
				// init songName
				String data = packet.getData().toString();
				data = data.substring(4, data.length() - 1);
				
				if(data.charAt(data.length() - 1) == '1')
				{
					send start = new send("Start", "start");
						
					// init ip dich, port dich
					start.IpDest = packet.getAddress();
					start.PortDest = packet.getPort();
					
					start.content = data.substring(0,data.length() - 2);
					
					Thread mt = new Thread(start);
					mt.start();	
				}
				*/
			}
			
			// nhan duoc goi start -> chuan bi gui cac goi data
			if(flag == "start")
			{
				// tao socket moi de gui data
				sPort = yPort + (int) (Math.random()*50 + 1);
				System.out.println("Sending file to: " + yIP + "\t" + sPort);
				ds = new DatagramSocket(sPort, InetAddress.getByName(yIP));
				
				//Gui data tu file mp3
				File currentDirectory = new File(new File(".").getAbsolutePath());
				String path = currentDirectory.getCanonicalPath();
				content = new String(packet.getData(), "UTF-8").substring(6,modifyPacket(packet).length());
				path = path + "\\Music\\" + content;
				System.out.println("Sending: " + path);
				
				File file = new File(path);
				FileInputStream f = new FileInputStream(file);
				
				byte[] b = new byte[1024];
	            int remainLength = (int)file.length();
	            int count = 0;
	            
	            while(remainLength>=1024)
	            {
	                b = new byte[1024];
	                
	                // doc 1024 bytes tu f (file)
	                f.read(b, 0, 1024);
	                remainLength = remainLength - 1024;
	                
	                // gui packet 1024 bytes
	                DatagramPacket dp = new DatagramPacket(b,b.length,packet.getAddress(),packet.getPort());
	                ds.send(dp);
	                Thread.sleep(5);
	                System.out.println("Packet " + count++);
	            }
	            
	            if(remainLength > 0)
	            {
	                b = new byte[remainLength];
	                int read = f.read(b, 0, remainLength);

	                DatagramPacket dp = new DatagramPacket(b,b.length,packet.getAddress(),packet.getPort());
	                ds.send(dp);
	            }
	            f.close();		
				System.out.println("Send complete");
			}
			ds.close();
		}
		catch(IOException ex)
		{
			ex.getStackTrace();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	// phan tich goi tin loai nao (syn, req, res, start)
	String PacketAnalysing(DatagramPacket packet) throws UnsupportedEncodingException
	{
		String flag = modifyPacket(packet);
		if(flag.startsWith("res"))
			flag = "res";
		if(flag.startsWith("req"))
			flag = "req";
		if(flag.startsWith("syn"))
			flag = "syn";
		if(flag.startsWith("start"))
			flag = "start";
		return flag;
	}
	
	String modifyPacket(DatagramPacket packet) throws UnsupportedEncodingException
	{
		String s = new String(packet.getData(), "UTF-8");
		int index = -1;
		for(int i=0; i<s.length(); i++)
		{
			if((s.charAt(i) < 48 || (s.charAt(i)> 57 && s.charAt(i) < 65) || (s.charAt(i) > 90 && s.charAt(i) < 97)) && s.charAt(i) != ' ' && s.charAt(i) != '-' && s.charAt(i) != '.' && s.charAt(i) != '(' && s.charAt(i) != ')')
			{
				index = i;
				break;
			}
		}
		s = s.substring(0, index);
		return s;
	}
	
}
