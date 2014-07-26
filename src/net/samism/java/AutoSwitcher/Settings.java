package net.samism.java.AutoSwitcher;

import java.io.File;
import java.util.HashMap;

class Settings {

	private static final String tempdir = System.getProperty("java.io.tmpdir");

	private HashMap<String, Boolean> settings;

	private final boolean fileExists;

	public Settings() {
		File saveFile = new File(tempdir + "/autoswitcher.ini");
		this.fileExists = saveFile.exists();
		this.settings = new HashMap<String, Boolean>();

		settings.put("save", true);
		settings.put("restore_mouse", true);
	}

	public void setSettings(HashMap<String, Boolean> settings) {
		this.settings = settings;
	}

	public HashMap<String, Boolean> getSettings() {
		return settings;
	}

	public boolean getFileExists() {
		return fileExists;
	}
}
