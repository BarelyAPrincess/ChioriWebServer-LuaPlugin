/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright 2015 Chiori Greene a.k.a. Chiori-chan <me@chiorichan.com>
 * All Right Reserved.
 */
package com.chiorichan.plugin.lua;

import com.chiorichan.ContentTypes;
import com.chiorichan.factory.EvalExceptionCallback;
import com.chiorichan.factory.EvalFactory;
import com.chiorichan.factory.EvalFactoryResult;
import com.chiorichan.factory.ShellFactory;
import com.chiorichan.lang.ErrorReporting;
import com.chiorichan.lang.EvalException;
import com.chiorichan.plugin.lang.PluginException;
import com.chiorichan.plugin.loader.Plugin;
import com.naef.jnlua.LuaRuntimeException;

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
		getLogger().info( "Registering the Lua Script Processor with the EvalFactory" );
		EvalFactory.register( new LuaScriptProcessor() );
		ContentTypes.setType( "lua", "text/html" );
		
		EvalException.registerException( new EvalExceptionCallback()
		{
			@Override
			public boolean callback( Throwable cause, ShellFactory factory, EvalFactoryResult result, ErrorReporting level, String message )
			{
				result.addException( message == null ? new EvalException( level, cause, factory ) : new EvalException( level, message, cause, factory ) );
				return true;
			}
		}, LuaRuntimeException.class );
	}
	
	@Override
	public void onLoad() throws PluginException
	{
		// TODO New Empty Method
	}
}
