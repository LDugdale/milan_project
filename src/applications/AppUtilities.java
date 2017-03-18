package applications;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import applications.chat.ChatApp;

public final class AppUtilities {
	
	// How about if the appTargetID is generated as a HashCode at
	// start, and not a pre-allocated thing.
	// HashCode for String should be invariant across devices and
	// runs.
	// Or, even, what if the appTargetID is a String, and the app
	// name?
	//
	// Then we will have a list of possible apps, their names, class
	// objects, and IDs. This can be used in instantiation, and in 
	// generating menus. (Maybe even in updates)
	//
	// We would probably have to restart after an update...
	// Or just close after prompting the user to restart.
	
	public static String applicationsLocation = "applications";
	
	private static Map<Integer, Class<? extends App>> appTypes;
	public static int chatTargetID;
	
	static {
		
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        //for(URL url: urls){
        	//System.out.println(url.getFile());
        //}
        String strClassPath = urls[0].getFile();
		
		//String strClassPath = System.getProperty("java.class.path");
		System.out.println("Classpath is " + strClassPath);
		String appsPackageClassPath = strClassPath + "/applications";
		System.out.println("Apps path is " + appsPackageClassPath);
		
		File file = new File(appsPackageClassPath);
		String[] directoryNames = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		System.out.println(Arrays.toString(directoryNames));
		
		List<String> appDirectories = Arrays.asList(directoryNames);
		
		List<String> appNames = new ArrayList<String>();
		for ( String name : appDirectories ) {
			if(name.length() > 0) appNames.add(name.substring(0, 1).toUpperCase() + name.substring(1) + "App");
		}
		
		
		appTypes = new TreeMap<Integer, Class<? extends App>>();
		
		for(int i=0; i< appDirectories.size(); i++) {
			try {
				String nameToCheck = applicationsLocation + "." + appDirectories.get(i) + "." + appNames.get(i);
				//System.out.println("Check location: " + nameToCheck);
				@SuppressWarnings("unchecked")
				Class<? extends App> tryClass = (Class<? extends App>) Class.forName(nameToCheck);
				
				//System.out.println("Found class with name: " + tryClass.getName());
				
				// App instance = tryClass.getConstructor(AppModel.class, Integer.class).newInstance(new AppModel(), 0);
				
				// No longer need as we are getting name from tryClass...
				//App instance = tryClass.newInstance();
				
				try {
					String panelNameToCheck = applicationsLocation + "." + appDirectories.get(i) + "." + appNames.get(i).substring(0, appNames.get(i).length()-3) + "Panel";
					Class.forName(panelNameToCheck);
					appTypes.put(tryClass.getSimpleName().hashCode(), tryClass);
				}
				catch(ClassNotFoundException e) {
					// Do nothing
				}
			}
			catch (ClassNotFoundException e) { System.out.println("Could not find " + appDirectories.get(i) + "\\" + appNames.get(i)); }
			catch (ClassCastException ignored) { }
			catch (Exception ignored) { }
		}
		
		// ChatApp is a special case, and we shall deal with it as follows:
		int chatID = (new ChatApp()).getTargetID();
		//System.out.println("Chat ID = " + chatID + ", corresponds to " + appTypes.get(chatID).getName());
		chatTargetID = 0;
		//appTypes.put(chatTargetID, (new ChatApp()).getClass());
		appTypes.remove(chatID);
		//System.out.println("appTypes.contains(" + chatID + ") = " + appTypes.containsKey(chatID));
		//System.out.println("appTypes.get(" + chatTargetID + ").getName() = " + appTypes.get(chatTargetID).getName());
	}
	
	public static Map<Integer, Class<? extends App>> getAppTypes() {
		return appTypes;
	}
	
	public static Integer getTargetID(Class<? extends App> appClass) {
		for(Entry<Integer,Class<? extends App>> entry : appTypes.entrySet()) {
			//System.out.println("getTargetID: " + entry.getValue().getName() + " == " + appClass.getName());
			if(entry.getValue() == appClass) {
				//System.out.println("Are equal.");
				return entry.getKey();
			}
		}
		//System.out.println("None equal.");
		return null;
	}
	
	public static Class<? extends App> getClass(int targetID) {
		return appTypes.get(targetID);
	}
	
}
