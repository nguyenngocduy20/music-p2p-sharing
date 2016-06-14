package backend;
import java.io.*;
import java.net.*;

public class send implements Runnable
{
	public String threadName;
	public String flag; 	// syn: kiem tra lang gieng
					// ack: xac nhan con song
					// req songName: yeu cau gui bai hat.
					// res songName state: tra loi yeu cau (state = 0 hoac 1)
					// start songName: bat dau truyen du lieu
	public String content; // syn: null
					// req: ten bai hat
					// res: ten bai hat
					// start: ten bai hat chinh xac
	public InetAddress IpDest; // ip dich cua goi send
	public int PortDest; // port dich cua goi send
	public int yPort; // port gui
	public ReadNode nodes = new ReadNode(); 	// doc file nodes.txt
										// dong dau tien la ip va port cua chuong trinh dang chay
	
	// ham dung voi ten thread send
	public send(String name)
	{
		System.out.println("Init thread SEND");
		nodes.read();
		this.threadName = name;
		System.out.println("Thread name: " + this.threadName);
	}
	
	// ham dung voi ten thread send va flag (req, res, syn, start)
	public send(String name, String Flag)
	{
		System.out.println("Init thread SEND");
		nodes.read();
		this.threadName = name;
		this.flag = Flag;
		System.out.println("Thread name: " + this.threadName + "\tFlag: " + this.flag);
	}
	
	// ham chay cua thread (bat buoc, khong duoc doi ten)
	public void run()
	{
		try
		{
			InetAddress IPAddress = InetAddress.getByName(nodes.lstNodes.nodes[0].ip);
			int port = nodes.lstNodes.nodes[0].port + 1;
			System.out.println("Your IP: " + IPAddress.toString() + "\tYour port: " + port);
			//System.out.println("Your IP: " + nodes.lstNodes.nodes[0].ip + "\tYour port: " + port);
			System.out.println("Init DatagramSocket");
			DatagramSocket dsoc = new DatagramSocket(port, IPAddress);
			System.out.println("Datagram Init Successful");
			
			// kiem tra lang gieng
			if(flag == "syn")
			{
				try 
				{
					System.out.println("Packet SYN");
					byte[] sendData = "syn".getBytes();

					System.out.println("Broadcast packet SYN");
					for(int i = 1; i<nodes.lstNodes.length(); i++)
					{
						IpDest = InetAddress.getByName(nodes.lstNodes.nodes[i].ip);
						PortDest = nodes.lstNodes.nodes[i].port;
						System.out.println("Dest IP: " + IpDest.toString() + "\tDest Port: " + PortDest);
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IpDest, PortDest);
						dsoc.send(sendPacket);
						System.out.println("Packet SYN " + i + " was sent");
					}
					dsoc.close();
					System.out.println("DatagramSocket Closed");
				}
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(flag == "ack")
			{
				try
				{
					System.out.println("Packet ACK");
					byte[] sendData = "ack".getBytes();
					
					// IpDest va PortDest phai duoc init truoc.
					System.out.println("Dest IP: " + IpDest.toString() + "\tDest Port: " + PortDest);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IpDest, PortDest);
					dsoc.send(sendPacket);
					System.out.println("Packet ACK was sent");
					dsoc.close();
					System.out.println("DatagramSocket Closed");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			
			// gui yeu cau bai hat
			if(flag == "req")
			{
				try
				{
					System.out.println("Packet REQ");
					String req = "req " + content;
					System.out.println("Content <" + req + ">");
					byte[] sendData = req.getBytes();

					System.out.println("Broadcast packet REQ");
					for(int i = 1; i<nodes.lstNodes.length(); i++)
					{	
						IpDest = InetAddress.getByName(nodes.lstNodes.nodes[i].ip);
						PortDest = nodes.lstNodes.nodes[i].port;
						System.out.println("Dest IP: " + IpDest.toString() + "\tDest Port: " + PortDest);
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IpDest, PortDest);
						dsoc.send(sendPacket);
						System.out.println("Packet REQ " + i + " was sent");
					}
					dsoc.close();
					System.out.println("DatagramSocket closed");
					Thread.interrupted();
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
			// tra loi yeu cau bai hat
			if(flag == "res")
			{
				System.out.println("Packet RES");
				System.out.println("Finding File " + content + " in database");
				int index = Files.FindFile(content); // tim tu khoa trong danh sach file dang giu
				if(index >= 0)
				{
					content = Files.listFile.get(index); // ten dung cua bai hat
					System.out.println("Found " + content);
				}
				
				try
				{
					// content bao gom ten bai hat phai duoc init truoc
					String res = "res " + content;
					if(index > -1) // tim thay
						res = res.concat(" 1");
					else // khong tim thay
						res = res.concat(" 0");
					System.out.println("Content <" + res + ">");
				
					byte[] sendData = res.getBytes();
				
					// IpDest va PortDest phai duoc init truoc.

					System.out.println("Dest IP: " + IpDest.toString() + "\tDest Port: " + PortDest);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IpDest, PortDest);
					dsoc.send(sendPacket);
					System.out.println("Packet RES was sent");
					
					dsoc.close();
					System.out.println("DatagramSocket closed");
				}
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// gui giu lieu
			if(flag == "start")
			{
				try
				{
					System.out.println("Packet START");
					// random port de gui goi start va cung la port nhan data
					// tao socket moi de nhan data
					System.out.println("Changeing port for packet START and DATA");
					yPort = port + (int) (Math.random()*50 + 1);
					System.out.println("Port was changed | Your IP: " + IPAddress.toString() + "\tYour Port: " + yPort);
					System.out.println("Creating new DatagramSocket for tranfer START and DATA packet");
					dsoc.close();
					dsoc = new DatagramSocket(yPort, IPAddress);
					
					// Gui flag start
					// content bao gom ten bai hat phai duoc init truoc.
					String start = "start " + content;
					System.out.println("Content <" + start + ">");
					byte[] sendData = start.getBytes();

					// IpDest va PortDest phai duoc init truoc.
					System.out.println("Dest IP: " + IpDest.toString() + "\tDest Port: " + PortDest);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IpDest, PortDest);
					// goi tin flag = "start"
					dsoc.send(sendPacket);
					
					Thread.sleep(1000);
					
					// chuan bi nhan cac goi tin data
					// start songName
					System.out.println("Packet DATA");
					String songName = content;
					File currentDirectory = new File(new File(".").getAbsolutePath());
					songName = currentDirectory.getCanonicalPath() + "\\Music\\" + songName;
					System.out.println("Creating " + songName);
					System.out.println("Receiving " + songName);
					try
			        {
			            int off = 0;
			            int size = 0;
			            // tao file moi
			            FileOutputStream fos = new FileOutputStream(new File(songName));
			            
			            while(true) 
			            { 
			                dsoc.setSoTimeout(5000);

			                try 
			                {
			                    byte[] receiveData = new byte[1024];

			                    DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

			                    dsoc.receive(packet);
			                    size += packet.getLength();
			                    byte[] b1 = new byte[packet.getLength()];
			                    fos.write(receiveData, 0, b1.length);
			                    off = off + b1.length;
								System.out.println("Received " + packet.getLength() + " bytes from " + packet.getAddress().toString());
								System.out.println("Size of file increased to " + size);
			                }
			                catch (SocketTimeoutException ste)
			                {
			                    break;
			                }
			            } 
			            fos.close();
			            dsoc.close();
						System.out.println("DatagramSocket Closed");
			        }
			        catch (SocketException ex) 
					{
			        	System.exit(1);
			        }
				}
				catch(IOException e)
				{
					e.printStackTrace();
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		 
		catch (SocketException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
