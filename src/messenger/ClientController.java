package messenger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import communications.*;

public final class ClientController extends Thread implements PacketHandler {
	
	ProgramController program;
	
	private static String serverName = "localhost"; // "tinky-winky.cs.bham.ac.uk";
	private static int portNum = 54321;
    private Socket server;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;

    private ServerMemo openingMemo;
    
    private boolean running;
	
	public ClientController(ProgramController program, ServerMemo openingMemo) {
		this.program = program;
		
		//System.out.println("ClientController constructor: " + (!openingMemo.getClass().equals(Login.class)) + " " + (!openingMemo.getClass().equals(SignUp.class)));
		//System.out.println("ClientController constructor: " + (!(openingMemo.getClass().equals(Login.class) || openingMemo.getClass().equals(SignUp.class))));
		if(!(openingMemo.getClass().equals(Login.class) || openingMemo.getClass().equals(SignUp.class))) {
			throw new IllegalArgumentException();
		}
		this.openingMemo = openingMemo;
		
		running = false;
	}
	
	public void run() {
		
		running = true;
		
		try {
			if(openingMemo.getClass().equals(Login.class)) System.out.println("In ClientController: Attempt to login with username " + ((Login)openingMemo).getUsername() + " and password " + ((Login)openingMemo).getPassword() + ".");
			if(openingMemo.getClass().equals(SignUp.class)) System.out.println("In ClientController: Attempt to create user with username " + ((SignUp)openingMemo).getUsername() + " and password " + ((SignUp)openingMemo).getPassword() + ".");
			
			System.out.println("Log on to server " + serverName + " on port " + portNum + ".");
			server = new Socket(serverName, portNum);
			System.out.println("Server connection made.");
            toServer = new ObjectOutputStream(server.getOutputStream());
            fromServer = new ObjectInputStream(server.getInputStream());
			
			System.out.println("Request/await Setup from server.");
			
			Setup mySetup = new Setup();
			
			toServer.writeObject(new Packet(openingMemo));
			
			Packet response;
			if((response = (Packet)fromServer.readObject()) != null) {
				if(response.getType() == 9) {
					mySetup = (Setup)response.getMemo();
				}
				else throw new IllegalArgumentException();
			}
			
			program.receiveSetup(mySetup);
			if(!mySetup.isValidSetup()) {
				logoff();
				return;
			}
		}
		catch (Exception e) {
			System.out.println("Could not login.");
			e.printStackTrace();
			
			program.receiveSetup(new Setup());
			logoff();
			return;
		}
		
		System.out.println("In ClientController: Logged in.");
		
		/////////////////
		//////////
		////////// This is the important bit.
		////////// The PacketReader.readPacket(item, this); call, where 'this' is
		////////// a controller which implements PacketHandler is the thing which
		////////// allows the current class to process any incoming packet.
		////////// Note that a packet can be passed to PacketReader either as a
		////////// Packet (through readPacket) or as an Object (through ReadRawPacket).
		//////////
		/////////////////
		try {
			Packet item;
			while(running) {
				if((item = (Packet)fromServer.readObject()) != null) {
					PacketReader.readPacket(item, this);
				}
			}
		}
		catch (ClassNotFoundException e) {
			System.out.println("ClientController.run() ClassNotFoundException.");
			//e.printStackTrace();
		}
		catch (EOFException e) {
			System.out.println("ClientController.run() EOFException.");
			program.informUser("Server error. Client logged off.");
			logoff();
			//e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("ClientController.run() IOException: " + e.getClass().getName());
			if(e.getClass().equals(EOFException.class)) {
				program.informUser("Server error. Client logged off.");
				logoff();
			}
			//e.printStackTrace();
		}

	}
	
	public static void updateServerInfo(String serverName, int port) {
		ClientController.serverName = serverName;
		ClientController.portNum = port;
	}
	
	public void logoff() {
		System.out.println("In ClientController: Logoff.");
		
		if(program.isLoggedIn()) {
			try{	
				toServer.writeObject(new Packet(new Logout(program.getUserID())));
			}
			catch (Exception e) {
				System.out.println("Could not pass Logout packet.");
				//e.printStackTrace();
			}
		}
		
		//this.interrupt(); // Is this needed/correct?
		running = false;
		
		try {
			try {
				// Send Logout memo to server?
				// toServer.writeObject(new Packet(new Logout(program.getUserID())));
				
				toServer.close();
				fromServer.close();
			}
			catch (IOException | NullPointerException e2) {
				System.out.println("Could not close streams.");
			}
			
			try {
				server.close();
			} catch (IOException e2) {
				System.out.println("Could not close server connection.");
			}
			
			program.setLoggedOut(); // Inform ProgramController of logout.
		}
		catch (Exception e2) {
			System.out.println("Logoff error.");
		}
	}
	
	public boolean passMemoToServer(Memo memo) {
		
		try{
			// Convert Memo to Packet
			// Pass to server
			// Return success
			//////// OR Should the Packet be added to a Queue of Packets to be sent, which the run method then cycles through
					// In which case, how do we return a success bool? Do we bother?
					// Or does the run() thread eventually give up on sending the Packet, unpack it to find the origin,
					// and then send a fail message to the app?
			
			toServer.writeObject(new Packet(memo));
			
			return true;
		}
		catch (Exception e) {
			System.out.println("Could not pass packet.");
			e.printStackTrace();
			return false;
		}
		
	}
	

	public boolean handleMessage(Message message) {
		
		try{
			//System.out.println("Message: " + message.getMessage());
			
			return program.passMessageToApps(message);
		}
		catch (Exception e) {
			System.out.println("Could not handle message.");
			//e.printStackTrace();
			return false;
		}
		
	}

	public boolean handleHistory(History history) {
		
		try{
			//System.out.println("History: " + history.getHistory());
			
			return program.passHistoryToApps(history);
		}
		catch (Exception e) {
			System.out.println("Could not handle history.");
			e.printStackTrace();
			return false;
		}
		
	}

	public boolean handleAddFriend(AddFriend addFriend) {
		
		try{
			System.out.println("AddFriend: " + addFriend.getFriend().getUsername());
			
			return program.addFriend(addFriend.getFriend());
		}
		catch (Exception e) {
			System.out.println("Could not handle addFriend.");
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean handleServerMessage(ServerMessage serverMessage) {
		
		try{
			System.out.println("ServerMessage: " + serverMessage.getMessage());
			
			// Deal with server message
			
			return true;
		}
		catch (Exception e) {
			System.out.println("Could not handle serverMessage.");
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean handleEditUser(EditUser editUser) {
		
		try{
			System.out.println("EditUser received.");
			
			// Deal with editUser
			program.updateUser(editUser);
			
			return true;
		}
		catch (Exception e) {
			System.out.println("Could not handle editUser.");
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean handleFriendRequest(FriendRequest friendRequest) {
		System.out.println("Client controller does not handle FriendRequests.");
		// Need some graceful handling of this odd situation...
		return true;
	}

	public boolean handleSetup(Setup setup) {
		System.out.println("Client controller does not handle Setup objects outside of login() method.");
		// Need some graceful handling of this odd situation...
		return true;
	}
	
	public boolean handleHistoryRequest(HistoryRequest historyRequest) {
		System.out.println("Client controller does not handle HistoryRequests.");
		// Need some graceful handling of this odd situation...
		return true;
	}

	public boolean handleSignUp(SignUp signUp) {
		System.out.println("Client controller does not handle SignUp.");
		// Need some graceful handling of this odd situation...
		return true;
	}

	public boolean handleLogout(Logout logout) {
		System.out.println("Client controller does not handle Logout.");
		// Need some graceful handling of this odd situation...
		return true;
	}

	public boolean handleCreateChat(CreateChat createChat) {
		System.out.println("Client controller does not handle CreateGroup.");
		// Need some graceful handling of this odd situation...
		return true;
	}

	public boolean handleLogin(Login login) {
		System.out.println("Client controller does not handle Login.");
		// Need some graceful handling of this odd situation...
		return true;
	}
	
}
