/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright 2015 Chiori Greene a.k.a. Chiori-chan <me@chiorichan.com>
 * All Right Reserved.
 */
package com.chiorichan.plugin.lua;

import io.netty.buffer.ByteBuf;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.chiorichan.factory.ScriptBinding;
import com.chiorichan.factory.ScriptingContext;
import com.chiorichan.factory.ScriptingEngine;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

/**
 * Handles the processing of Lua Scripts
 */
public class LuaScriptEngine implements ScriptingEngine
{
	@Override
	public boolean eval( ScriptingContext context ) throws Exception
	{
		LuaStateFactory factory = new LuaStateFactory();
		factory.createState();
		LuaState lua = factory.state;
		
		// new OSAPI( factory );
		
		final StringWriter writer = new StringWriter();
		
		lua.pushJavaObject( writer );
		lua.setGlobal( "writer" );
		
		lua.pushJavaFunction( new JavaFunction()
		{
			@Override
			public int invoke( LuaState lua )
			{
				writer.append( lua.checkString( 1 ) );
				return 1;
			}
		} );
		lua.setGlobal( "print" );
		
		lua.pushJavaFunction( new JavaFunction()
		{
			@Override
			public int invoke( LuaState lua )
			{
				writer.append( lua.checkString( 1 ) + "\n" );
				return 1;
			}
		} );
		lua.setGlobal( "println" );
		
		String prescript = "Server = java.require(\"com.chiorichan.factory.api.Server\")";
		
		lua.load( prescript + "\n" + context.readString(), "script-test" );
		
		lua.call( 0, 1 );
		
		try
		{
			context.result().object( lua.toJavaObject( 1, Object.class ) );
		}
		finally
		{
			lua.pop( 1 );
		}
		
		context.resetAndWrite( writer.toString() );
		
		lua.close();
		
		return true;
	}
	
	@Override
	public List<String> getTypes()
	{
		return Arrays.asList( "lua" );
	}
	
	@Override
	public void setBinding( ScriptBinding binding )
	{
		// Do Nothing - For Now!
	}
	
	@Override
	public void setOutput( ByteBuf buffer, Charset charset )
	{
		// Do Nothing - For Now!
	}
}
