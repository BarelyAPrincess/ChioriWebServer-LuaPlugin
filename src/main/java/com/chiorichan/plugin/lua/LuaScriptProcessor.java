/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright 2015 Chiori Greene a.k.a. Chiori-chan <me@chiorichan.com>
 * All Right Reserved.
 */
package com.chiorichan.plugin.lua;

import java.io.StringWriter;

import com.chiorichan.factory.EvalExecutionContext;
import com.chiorichan.factory.ShellFactory;
import com.chiorichan.factory.processors.ScriptingProcessor;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

/**
 * Handles the processing of Lua Scripts
 */
public class LuaScriptProcessor implements ScriptingProcessor
{
	@Override
	public boolean eval( EvalExecutionContext context, ShellFactory shell ) throws Exception
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
		
		lua.load( context.readString(), "script-test" );
		
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
	public String[] getHandledTypes()
	{
		return new String[] {"lua"};
	}
}
