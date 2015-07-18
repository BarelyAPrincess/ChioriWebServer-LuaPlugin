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
import com.chiorichan.factory.ExceptionCallback;
import com.chiorichan.factory.ScriptingContext;
import com.chiorichan.factory.ScriptingFactory;
import com.chiorichan.lang.ErrorReporting;
import com.chiorichan.lang.EvalException;
import com.chiorichan.plugin.lang.PluginException;
import com.chiorichan.plugin.loader.Plugin;
import com.naef.jnlua.LuaGcMetamethodException;
import com.naef.jnlua.LuaMemoryAllocationException;
import com.naef.jnlua.LuaMessageHandlerException;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaSyntaxException;

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
		getLogger().info( "Registering the Lua Script Registry with the EvalFactory" );
		ScriptingFactory.register( new LuaScriptRegistry() );
		ContentTypes.setType( "lua", "text/html" );
		
		EvalException.registerException( new ExceptionCallback()
		{
			@Override
			public ErrorReporting callback( Throwable cause, ScriptingContext context )
			{
				context.result().addException( new EvalException( ErrorReporting.E_ERROR, cause ) );
				return ErrorReporting.E_ERROR;
			}
		}, LuaRuntimeException.class, LuaSyntaxException.class, LuaMemoryAllocationException.class, LuaGcMetamethodException.class, LuaMessageHandlerException.class );
	}
	
	@Override
	public void onLoad() throws PluginException
	{
		// TODO New Empty Method
	}
}
