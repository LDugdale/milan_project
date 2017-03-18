package Server.database;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.postgresql.util.PSQLException;
import communications.Message;
import communications.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MessengerDatabaseTests{

	private Connection conn;
	private MessengerDatabase dbm;
	User[] userArray;
	List<Message> messageList;
	User aiden;
	User justinT;
	User justin;
	User marina;
	User austin;
	User joey;
	User joanne;
	Message one;
	Message two;
	Message three;
	Message four;
	
	@Before
	public void setup(){
		aiden = new User(1, "aiden_paul");
		justinT = new User(2, "justin_trudeau");
		justin = new User(3, "justin_johnson");
		marina = new User(4, "MarinaAbramovic0003");
		austin = new User(5, "TheRealAustinPowers");
		joey = new User(6, "Joey_Santonlini");
		joanne = new User(7, "joanneGermanotta");

		User[] userArray = {austin, justinT, marina, aiden};
		
		one = new Message(userArray, 1, 2099064, 9, 4, "", "Hello, how are you?");
		two = new Message(userArray, 1, 2099064, 10, 2, "", "Im good thank you, how are you?");
		three = new Message(userArray, 1, 2099064, 11, 4, "", "ok thanks, is you dog feeling better?");
		four = new Message(userArray, 1, 2099064, 12, 2, "", "a lot better cheers");
		messageList = new ArrayList<Message>(Arrays.asList(four, three, two, one));
		
		dbm = new MessengerDatabase();
		conn = dbm.getConn();
	}
	
	/**
	 * Test addUser to see whether a new, unique user can be successfully created.
	 */
//	@Test
//	public void testAddUser1() {
//		String result = "";
//		String expected = "00ps1D1D1Taga1n!!";
//		dbm.addUser("Britney_spears", "00ps1D1D1Taga1n!!");
//		try {
//			PreparedStatement resultSet = conn.prepareStatement("SELECT password FROM users WHERE user_name = ?");
//			resultSet.setString(1, "Britney_spears");
//			ResultSet results = resultSet.executeQuery();
//			while(results.next())
//			{
//				result = results.getString("password");
//			}
//			try {
//				PreparedStatement deleteAddUserTest1 = conn.prepareStatement("DELETE FROM users WHERE user_name = ?");
//				deleteAddUserTest1.setString(1 , "Britney_spears");
//				deleteAddUserTest1.executeUpdate();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbm.close();
//		assertTrue(result.equals(expected));
//	}
//	
//	/**
//	 * Test addUser to see if it throws an SQL Exception when trying to add a user that already exists.
//	 */
//	@Test
//	public void testAddUser2(){
//		boolean result = dbm.addUser("aiden_paul", "1234");
//		boolean expected = false;
//		dbm.close();
//		assertEquals(result, expected);
//	}
//	
//	@Test
//	public void testAddUser3(){
//		boolean result = dbm.addUser("Laurie", "password");
//		boolean expected = false;
//		dbm.close();
//		assertEquals(result, expected);
//	}
//	
//	@Test
//	public void testAddUser4(){
//		boolean result = dbm.addUser("joanneGermanotta", "password");
//		boolean expected = false;
//		dbm.close();
//		assertEquals(result, expected);
//	}
	/**
	 * Test checkCredentials to see if it successfully returns true for correct username and password.
	 */
	@Test
	public void testCheckCredentials1(){
		boolean result;
		boolean expected = true;
		result = dbm.checkCredentials("justin_trudeau","trump1sStup1d");
		dbm.close();
		assertEquals(result, expected);
	}
	
	/**
	 * Test checkCredentials to see if it successfully returns false for incorrect password.
	 */
	@Test
	public void testCheckCredentials2(){
		boolean result;
		boolean expected = false;
		result = dbm.checkCredentials("justin_trudeau", "trump1sstup1d");
		dbm.close();
		assertEquals(result, expected);
	}
	
	/**
	 * Test checkCredentials to see if it returns false when given a username that doesn't exist.
	 */
	@Test
	public void testCheckCredentials3(){
		boolean result;
		boolean expected = false;
		result = dbm.checkCredentials("joey_santonlini", "1234");
		dbm.close();
		assertEquals(result, expected);
	}
	
	@Test
	public void testCheckCredentials4(){
		boolean result;
		boolean expected = true;
		result = dbm.checkCredentials("Laurie", "password");
		dbm.close();
		assertEquals(result, expected);
	}
	
	@Test
	public void testCheckCredentials5(){
		boolean result;
		boolean expected = false;
		result = dbm.checkCredentials("Laurie", "passwor");
		dbm.close();
		assertEquals(result, expected);
	}
	
	@Test
	public void testCheckCredentials6(){
		boolean result;
		result = dbm.checkCredentials("ANOther", "password");
		dbm.close();
		assertTrue(result);
	}
	
	/**
	 * Tests to see whether you can successfully get the user ID for a user that exists.
	 */
	@Test
	public void testGetUserId1(){
		int expected = 3;
		String username = "justin_johnson";
		int result = dbm.getUserID(username);
		dbm.close();
		assertEquals(expected, result);
	}
	
	/**
	 * Tests to see whether you can successfully get the user ID for a user that exists.
	 */
	@Test
	public void testGetUserId2(){
		int expected = 6;
		String username = "Joey_Santonlini";
		int result = dbm.getUserID(username);
		dbm.close();
		assertEquals(expected, result);
	}
	
	/**
	 * Tests to see whether you return 0 if that user does not exists.
	 */
	@Test
	public void testGetUserId3(){
		int expected = 0;
		String username = "joey_santonlini";
		int result = dbm.getUserID(username);
		dbm.close();
		assertEquals(expected, result);
	}
	
	/**
	 * Tests to see whether you can successfully get the chat ID for a user that exists.
	 */
	@Test
	public void testGetChats1(){
		List<Integer> expected = new ArrayList<Integer>();
		expected.add(1);
		String username = "justin_trudeau";
		List<Integer> result = dbm.getChats(username);
		dbm.close();
		assertEquals(expected, result);
	}
	
	/**
	 * Tests to see whether you can successfully get the chat ID for a user that exists.
	 */
	@Test
	public void testGetChats2(){
		List<Integer> expected = new ArrayList<Integer>();
		expected.add(1);
		String username = "TheRealAustinPowers";
		List<Integer> result = dbm.getChats(username);
		dbm.close();
		assertEquals(expected, result);
	}
	
	/**
	 * Tests to see whether you can sucessfully retrieve multiple chat IDs for a user that exists.
	 */
	@Test
	public void testGetChats3(){
		List<Integer> expected = new ArrayList<Integer>();
		expected.add(1);
		String username = "aiden_paul";
		List<Integer> result = dbm.getChats(username);
		dbm.close();
		assertEquals(expected, result);
		}
	
	/**
	 * Tests to see whether you can successfully retrieve an active chat from the database.
	 * 
	 * Still need to figure this test out, not quite sure why it's not working.
	 */
	@Test
	public void testGetActiveChat1(){
		
		List<Message> expected = new ArrayList<Message>();
		List<Message> result = dbm.getActiveChat("justin_trudeau");
		boolean stillSame = true;
		User[] oneU = {aiden, justin, marina, justinT};
		Message one = new Message(oneU, 1, 2099064, 1, 4, "", "Hello, how are you?");
		Message two = new Message(oneU, 1, 2099064, 2, 2, "", "Im good thank you, how are you?");
		Message three = new Message(oneU, 1, 2099064, 3, 4, "", "ok thanks, is your cat feeling better?");
		Message four = new Message(oneU, 1, 2099064, 4, 2, "", "a lot better cheers");
		expected.add(four);
		expected.add(three);
		expected.add(two);
		expected.add(one);
		
		if (expected.size() != result.size())
		{
			stillSame = false;
		}
		
		
		for (int i = 0; i < expected.size(); i++)
		{
			if (expected.get(i).getSenderID() != result.get(i).getSenderID())
			{
				stillSame = false;
			}
			if (expected.get(i).getMeta().equals(result.get(i).getMeta()) == false)
			{
				stillSame = false;
			}
			if (expected.get(i).getMessage().equals(result.get(i).getMessage()) == false)
			{
				stillSame = false;
			}
		}	
		assertTrue(stillSame);
	}
	
	// return type of createChat has changed, will revisit this later.
	
//	/**
//	 * Tests whether you can successfully add a chat to the database using the creatChat method.
//	 */
//	@Test
//	public void testCreateChat1()
//	{
//		int[] userIDs = {7, 4, 6, 1};
//		int chatIDNo = 0;
//		boolean successful = false;
//		dbm.createChat(userIDs);
//		
//		try {
//			PreparedStatement findChat = conn.prepareStatement("SELECT chat_id FROM users2chats WHERE user_id = 7 UNION SELECT "
//					+ "chat_id FROM users2chats WHERE user_id = 4 UNION SELECT chat_id FROM users2chats WHERE user_id = 6 UNION "
//					+ "SELECT chat_id FROM users2chats WHERE user_id = 1 GROUP BY chat_id ORDER BY chat_id DESC LIMIT 1");
//			ResultSet chatID = findChat.executeQuery();
//			while (chatID.next()){
//				chatIDNo = chatID.getInt("chat_id");
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			PreparedStatement checkForUserIds = conn.prepareStatement("SELECT * FROM users2chats WHERE chat_id = ?");
//			checkForUserIds.setInt(1, chatIDNo);
//			ResultSet checkUsers = checkForUserIds.executeQuery();
//			boolean hasSeven = false;
//			boolean hasFour = false;
//			boolean hasSix = false;
//			boolean hasOne = false;
//			while (checkUsers.next()){
//				int id = checkUsers.getInt("user_id");
//				if (hasSeven == false && id == 7){
//					hasSeven = true;
//				}
//				if (hasFour == false && id == 4){
//					hasFour = true;
//				}
//				if (hasSix == false && id == 6){
//					hasSix = true;
//				}
//				if (hasOne == false && id == 1){
//					hasOne = true;
//				}
//			}
//			if (hasSeven == true && hasFour == true && hasSix == true && hasOne == true){
//				successful = true;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			PreparedStatement cleanUp = conn.prepareStatement("DELETE FROM chats WHERE chat_id = ?");
//			cleanUp.setInt(1, chatIDNo);
//			if (successful == true)
//			{
//				cleanUp.executeUpdate();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertTrue(successful);
//	}
//	
//	/**
//	 * Tests to check whether true is returned by createChat when a chat is successfully created.
//	 */
//	@Test
//	public void testCreateChat2(){
//		int[] userIDs = {7, 4, 6, 1};
//		int chatIDNo = 0;
//		boolean successful = false;
//		boolean result = dbm.createChat(userIDs);
//		
//		try {
//			PreparedStatement findChat = conn.prepareStatement("SELECT chat_id FROM users2chats WHERE user_id = 7 UNION SELECT "
//					+ "chat_id FROM users2chats WHERE user_id = 4 UNION SELECT chat_id FROM users2chats WHERE user_id = 6 UNION "
//					+ "SELECT chat_id FROM users2chats WHERE user_id = 1 GROUP BY chat_id ORDER BY chat_id DESC LIMIT 1");
//			ResultSet chatID = findChat.executeQuery();
//			while (chatID.next()){
//				chatIDNo = chatID.getInt("chat_id");
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			PreparedStatement checkForUserIds = conn.prepareStatement("SELECT * FROM users2chats WHERE chat_id = ?");
//			checkForUserIds.setInt(1, chatIDNo);
//			ResultSet checkUsers = checkForUserIds.executeQuery();
//			boolean hasSeven = false;
//			boolean hasFour = false;
//			boolean hasSix = false;
//			boolean hasOne = false;
//			while (checkUsers.next()){
//				int id = checkUsers.getInt("user_id");
//				if (hasSeven == false && id == 7){
//					hasSeven = true;
//				}
//				if (hasFour == false && id == 4){
//					hasFour = true;
//				}
//				if (hasSix == false && id == 6){
//					hasSix = true;
//				}
//				if (hasOne == false && id == 1){
//					hasOne = true;
//				}
//			}
//			if (hasSeven == true && hasFour == true && hasSix == true && hasOne == true){
//				successful = true;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			PreparedStatement cleanUp = conn.prepareStatement("DELETE FROM chats WHERE chat_id = ?");
//			cleanUp.setInt(1, chatIDNo);
//			if (successful == true)
//			{
//				cleanUp.executeUpdate();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertTrue(result);
//	}
	
//	@Test
//	public void testCreatChat3(){
//		int[] userIds = {justin.getUserID(), joey.getUserID(), joanne.getUserID(), marina.getUserID()};
//		dbm.createChat(userIds);
//	}
	
	/**
	 * Tests to see whether getActiveChatId can return a chat ID for users that exist.
	 */
//	@Test
//	public void testGetActiveChatID1(){
//		int expected = 1;
//		int result = dbm.getActiveChatID("MarinaAbramovic0003");
//		
//		assertEquals(result, expected);
//	}
	
	/**
	 * Tests to see whether getActiveChatId returns 0 for users that don't exist.
	 */
//	@Test
//	public void testGetActiveChatID2(){
//		int expected = 0;
//		int result = dbm.getActiveChatID("Falorzpzalorp");
//	}
	
	/**
	 * Tests to see whether getChatUsers works for when there are corresponding users
	 * to a chat.
	 */
	@Test
	public void testGetChatUsers1(){
		boolean stillSame = false;
		User[] expected = {aiden, justinT, marina, austin};
		User[] result = dbm.getChatUsers(1);
		
		ArrayList<User> listResult = new ArrayList<User>(Arrays.asList(result));
		
		boolean hasFive = false;
		boolean hasTwo = false;
		boolean hasFour = false;
		boolean hasOne = false;
		
		for (User user: expected){
			if (user.getUserID() == 1 && hasOne == false){
				hasOne = true;
			}
			if (user.getUserID() == 2 && hasTwo == false){
				hasTwo = true;
			}
			if (user.getUserID() == 4 && hasFour == false){
				hasFour = true;
			}
			if (user.getUserID() == 5 && hasFive == false){
				hasFive = true;
			}
		}
		if (hasFour == true && hasFive == true && hasTwo == true && hasOne == true){
			stillSame = true;
		}
		assertTrue(stillSame);
	}
	
	/**
	 * Tests to see whether getFriends works for users that exist.
	 */
	@Test
	public void testGetFriends1(){
		boolean stillSame = true;
		List<User> result = dbm.getFriends(4);
		List<User> expected = new ArrayList<User>();
		
		expected.add(aiden);
		expected.add(justinT);
		expected.add(austin);
		
		for (int i = 0; i < expected.size(); i++){
			if (result.contains(expected.get(i)) == false){
				stillSame = false;
			}
		}
		
		assertTrue(stillSame);
	}
	
	/**
	 * Tests to see whether getFriends works for users that exist.
	 */
	@Test
	public void testGetFriends2(){
		boolean stillSame = true;
		List<User> result = dbm.getFriends(5);
		List<User> expected = new ArrayList<User>();
		
		expected.add(aiden);
		expected.add(justinT);
		expected.add(marina);
		
		for (int i = 0; i < expected.size(); i++){
			if (result.contains(expected.get(i)) == false){
				stillSame = false;
			}
		}
		
		assertTrue(stillSame);
	}
	
	// Need a method that 
	// A) Invokes the addHistory method
	// B) Checks to see if the newHistory has been added
	// C) CLEANS UP - deletes the new history and replaces with the old (CHAT ID = 1)
	/**
	 * A test that sees if the addHistory method successfully creates a new history.
	 */
//	@Test
//	public void testAddHistory(){
//		dbm.addHistory(1, 2099064, messageList);
//		int[] userIds = {5, 2, 4, 1};
//		int[] ids = {5, 6, 7, 8};
//		
//		// DELETE THE NEW CHAT
//		try {
//			PreparedStatement clearChat = conn.prepareStatement("DELETE FROM chats WHERE chat_id = 1");
//			clearChat.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		// REPOPULATE CHAT
//		try {
//			PreparedStatement repopulate = conn.prepareStatement("INSERT INTO chats(chat_id) VALUES (1)");
//			repopulate.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		// REPOPULATE MESSAGES
//		try {
//			PreparedStatement messages = conn.prepareStatement("INSERT INTO messages(message_id, app_target, metadata, "
//					+ "message_content, message_creator, chat_id) VALUES (?, ?, ?, ?, ?, ?)");
//			int appTarget = 2099064;
//			String metadata = "";
//			String[] messageContent = {"Hello, how are you?", "Im good thank you, how are you?", 
//					"ok thanks, is your cat feeling better?", "a lot better cheers"};
//			int[] creator = {4, 2, 4, 2};
//			int chatID = 1;
//			for (int i = 0; i < 4; i++){
//				messages.setInt(1, ids[i]);
//				messages.setInt(2, appTarget);
//				messages.setString(3, metadata);
//				messages.setString(4, messageContent[i]);
//				messages.setInt(5, creator[i]);
//				messages.setInt(6, chatID);
//				messages.executeUpdate();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		// REPOPULATE USERS2CHATS
//		try {
//			PreparedStatement users2chats = conn.prepareStatement("INSERT INTO users2chats(user_id, chat_id) VALUES(?, ?)");
//			int chatId = 1;
//			for (int i = 0; i < 4; i++){
//				users2chats.setInt(1, userIds[i]);
//				users2chats.setInt(2, chatId);
//				users2chats.executeUpdate();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		// REPOPULATE most_recent_chat
//		try {
//			PreparedStatement mostRecent = conn.prepareStatement("UPDATE users SET most_recent_chat = 1 WHERE user_id = ?");
//			for (int i = 0; i < 4; i++){
//				mostRecent.setInt(1, userIds[i]);
//				mostRecent.executeUpdate();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//REPOPULATE USERS2MESSAGES
//		try {
//			PreparedStatement users2messages = conn.prepareStatement("INSERT INTO users2messages(user_id, message_id) VALUES(?, ?)");
//			for (int i = 0; i < 4; i++){
//				for (int j = 0; j < 4; j++){
//					users2messages.setInt(1, userIds[i]);
//					users2messages.setInt(2, ids[j]);
//					users2messages.executeUpdate();
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testGetHistory1(){
//		dbm.getHistory(1, 2099064, 2);
//	}
	
//	@Test
//	public void testSetMessage1(){
//		boolean worked = dbm.setMessage(1, 1, 2099064, "Hi guys, thanks for adding me!", "");
//	}
	
//	@Test
//	public void testGetMessage1(){
//		String message = dbm.getMessage(1, 2099064, "Hi guys, thanks for adding me!", "");
//		String expected = "Hi guys, thanks for adding me!";
//		
//		assertTrue(message.equals(expected));
//	}
	
//	@Test
//	public void testAddFriend1(){
//		dbm.addFriend(4, 7);
//	}
	
//	@Test
//	public void testUserChat1(){
//		dbm.getUserChats(4);
//	}
	
	@Test
	public void testUpdateProfile1(){
		boolean[] edited = {false, false, false, true};
		String[] fields = {"", "", "", "abc"};
		dbm.updateProfile(3, edited, fields);
	}
}
