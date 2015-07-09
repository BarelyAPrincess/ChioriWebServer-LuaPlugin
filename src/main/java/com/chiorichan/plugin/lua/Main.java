/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright 2015 Chiori Greene a.k.a. Chiori-chan <me@chiorichan.com>
 * All Right Reserved.
 */
package com.chiorichan.plugin.lua;

import com.chiorichan.lang.PluginException;
import com.chiorichan.plugin.loader.Plugin;

/**
 * Implements JNLua as a web language for Chiori-chan's Web Server
 */
public class Main extends Plugin
{
	@Override
	public void onDisable() throws PluginException
	{
		// TODO New Empty Method
	}
	
	@Override
	public void onEnable() throws PluginException
	{
		getLogger().debug( "Enabling Lua Plugin!!!" );
	}
	
	@Override
	public void onLoad() throws PluginException
	{
		// TODO New Empty Method
	}
}
