/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package org.appcelerator.titanium;

import java.util.ArrayList;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * API for accessing, storing, and modifying application properties that are 
 * exposed via Ti.App.Properties.
 */
public class TiProperties
{
	private static final String LCAT = "TiProperties";
	public static boolean DBG = TiConfig.LOGD && false;

	SharedPreferences preferences;

	/**
	 * Instantiates the private SharedPreferences collection with the given name and context.
	 * This means no other Android application will have access to they keys and values.
	 * @param context the context used to create/retrieve preferences.
	 * @param name the name used to create/retrieve preferences.
	 * @param clear whether to clear all keys and values in the instantiated SharedPreferences collection.
	 */
	public TiProperties(Context context, String name, boolean clear) {
		preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
		if (clear) {
			preferences.edit().clear().commit();
		}
	}

	/**
	 * Returns the mapping of a specified key, in String format. If key does not exist, returns the default value.
	 * @param key the lookup key.
	 * @param def the default value.
	 * @return mapping of key, or default value.
	 */
	public String getString(String key, String def)
	{
		if (DBG) {
			Log.d(LCAT,"getString called with key:"+key+", def:"+def);
		}

		if (!preferences.contains(key))
			return def;

		return preferences.getAll().get(key).toString();
	}

	/**
	 * Maps the specified key with a String value. If value is null, existing key will be removed from preferences.
	 * Otherwise, its value will be overwritten.
	 * @param key the key to set.
	 * @param value the value to set.
	 */
	public void setString(String key, String value)
	{
		if (DBG) {
			Log.d(LCAT,"setString called with key:"+key+", value:"+value);
		}
		SharedPreferences.Editor editor = preferences.edit();
		if (value==null)
		{
			editor.remove(key);
		}
		else
		{
			editor.putString(key,value);
		}
		editor.commit();
	}

	/**
	 * Returns the mapping of a specified key as an Integer. If key does not exist, returns the default value.
	 * @param key the lookup key.
	 * @param def the default value.
	 * @return mapping of key, or default value.
	 */
	public int getInt(String key, int def)
	{
		if (DBG) {
			Log.d(LCAT,"getInt called with key:"+key+", def:"+def);
		}
		return preferences.getInt(key,def);
	}
	
	/**
	 * Maps the specified key with an int value. If key exists, its value will be overwritten.
	 * @param key the key to set.
	 * @param value the value to set.
	 */
	public void setInt(String key, int value)
	{
		if (DBG) {
			Log.d(LCAT,"setInt called with key:"+key+", value:"+value);
		}

		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key,value);
		editor.commit();
	}
	
	/**
	 * Returns the mapping of a specified key as a Double. If key does not exist, returns the default value.
	 * @param key the lookup key.
	 * @param def the default value.
	 * @return mapping of key, or default value.
	 */
	public double getDouble(String key, double def)
	{
		if (DBG) {
			Log.d(LCAT,"getDouble called with key:"+key+", def:"+def);
		}
		if (!hasProperty(key)) {
			return def;
		}

		String stringValue = preferences.getString(key, "");
		try {
			return Double.parseDouble(stringValue);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * Maps the specified key with a double value. If key exists, its value will be
	 * overwritten.
	 * @param key the key to set.
	 * @param value the value to set.
	 */
	public void setDouble(String key, double value)
	{
		if (DBG) {
			Log.d(LCAT,"setDouble called with key:"+key+", value:"+value);
		}
		
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key,value + "");
		editor.commit();
	}
	
	/**
	 * Returns the mapping of a specified key, as a Boolean. If key does not exist, returns the default value.
	 * @param key the lookup key.
	 * @param def the default value.
	 * @return mapping of key, or default value.
	 */
	public boolean getBool(String key, boolean def)
	{
		if (DBG) {
			Log.d(LCAT,"getBool called with key:"+key+", def:"+def);
		}
		return preferences.getBoolean(key,def);
	}
	
	/**
	 * Maps the specified key with a boolean value. If key exists, its value will be
	 * overwritten.
	 * @param key the key to set.
	 * @param value the value to set.
	 */
	public void setBool(String key, boolean value)
	{
		if (DBG) {
			Log.d(LCAT,"setBool called with key:"+key+", value:"+value);
		}

		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key,value);
		editor.commit();
	}

	/**
	 * Returns the mapping of a specified key as a String array. If key does not exist, returns the default value.
	 * @param key the lookup key.
	 * @param def the default value.
	 * @return mapping of key, or default value.
	 */
	public String[] getList(String key, String def[])
	{
		if (DBG) {
			Log.d(LCAT,"getList called with key:"+key+", def:"+def);
		}

		int length = preferences.getInt(key+".length", -1);
		if (length == -1) {
			return def;
		}

		String list[] = new String[length];
		for (int i = 0; i < length; i++) {
			list[i] = preferences.getString(key+"."+i, "");
		}
		return list;
	}

	/**
	 * Maps the specified key with String[] value. Also maps 'key.length' to 'value.length'.
	 * If key exists, its value will be overwritten.
	 * @param key the key to set.
	 * @param value the value to set.
	 */
	public void setList(String key, String[] value)
	{
		if (DBG) {
			Log.d(LCAT,"setList called with key:"+key+", value:"+value);
		}

		SharedPreferences.Editor editor = preferences.edit();
		for (int i = 0; i < value.length; i++)
		{
			editor.putString(key+"."+i, value[i]);
		}
		editor.putInt(key+".length", value.length);

		editor.commit();

	}

	public boolean hasListProperty(String key) {
		return hasProperty(key+".0");
	}
	
	/**
	 * Returns whether key exists in preferences.
	 * @param key the lookup key.
	 * @return true if key exists in preferences.
	 */
	public boolean hasProperty(String key)
	{
		return preferences.contains(key);
	}

	/**
	 * Returns an array of keys whose values are lists.
	 * @return an array of keys.
	 */
	public String[] listProperties()
	{
		ArrayList<String> properties = new ArrayList<String>();
		for (String key : preferences.getAll().keySet())
		{
			if (key.endsWith(".length")) {
				properties.add(key.substring(0, key.length()-7));
			}
			else if (key.matches(".+\\.\\d+$")) {

			}
			else {
				properties.add(key);
			}
		}
		return properties.toArray(new String[properties.size()]);
	}

	/**
	 * Removes the key from preferences if it exists.
	 * @param key the key to remove.
	 */
	public void removeProperty(String key)
	{
		if (preferences.contains(key)) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.remove(key);
			editor.commit();
		}
	}
}
