/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright 2015 Chiori Greene a.k.a. Chiori-chan <me@chiorichan.com>
 * All Right Reserved.
 */
package com.chiorichan.plugin.lua;

import java.util.Random;

import com.chiorichan.plugin.PluginManager;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaState.Library;

/**
 * 
 */
public class LuaStateFactory
{
	LuaState state = new LuaState();
	
	void createState()
	{
		try
		{
			state.openLib( Library.BASE );
			state.openLib( Library.COROUTINE );
			state.openLib( Library.DEBUG );
			// state.openLib( Library.ERIS );
			state.openLib( Library.MATH );
			state.openLib( Library.STRING );
			state.openLib( Library.TABLE );
			// state.openLib( Library.UTF8 );
			
			/*
			 * Disables Locale Changing
			 * state.openLib( Library.OS );
			 * state.setField( -1, "setlocale" );
			 * state.pushString( "C" );
			 * state.call( 1, 0 );
			 * state.pop( 1 );
			 */
			
			// Prepare Table for OS Stuff
			state.newTable();
			state.setGlobal( "os" );
			
			// Kill Compat Entries
			state.pushNil();
			state.setGlobal( "unpack" );
			state.pushNil();
			state.setGlobal( "loadstring" );
			state.getGlobal( "math" );
			state.pushNil();
			state.setField( -2, "log10" );
			state.pop( 1 );
			state.getGlobal( "table" );
			state.pushNil();
			state.setField( -2, "maxn" );
			state.pop( 1 );
			
			// Remove some other functions we don't need and are dangerous.
			state.pushNil();
			state.setGlobal( "dofile" );
			state.pushNil();
			state.setGlobal( "loadfile" );
			
			state.getGlobal( "math" );
			
			/*
			 * We give each Lua state it's own randomizer, since otherwise they'd
			 * use the good old rand() from C. Which can be terrible, and isn't
			 * necessarily thread-safe.
			 */
			final Random random = new Random();
			state.pushJavaFunction( new JavaFunction()
			{
				@Override
				public int invoke( LuaState lua )
				{
					double r = random.nextDouble();
					switch ( lua.getTop() )
					{
						case 0:
							lua.pushNumber( r );
							break;
						case 1:
						{
							double u = lua.checkNumber( 1 );
							lua.checkArg( 1, 1 <= u, "Interval is empty" );
							lua.pushNumber( Math.floor( r * u ) + 1 );
							break;
						}
						case 2:
						{
							double l = lua.checkNumber( 1 );
							double u = lua.checkNumber( 2 );
							lua.checkArg( 2, 1 <= u, "Interval is empty" );
							lua.pushNumber( Math.floor( r * ( u - l + 1 ) ) + l );
							break;
						}
						default:
							throw new IllegalArgumentException( "Wrong number of arguments" );
					}
					return 1;
				}
			} );
			state.setField( -2, "random" );
			
			state.pushJavaFunction( new JavaFunction()
			{
				@Override
				public int invoke( LuaState lua )
				{
					random.setSeed( ( long ) lua.checkNumber( 1 ) );
					return 0;
				}
			} );
			state.setField( -2, "randomseed" );
			
			// Pop the math table.
			state.pop( 1 );
		}
		catch ( Throwable t )
		{
			PluginManager.getLogger().warning( "Failed creating Lua state", t );
		}
	}
	
	public LuaState getLuaState()
	{
		return state;
	}
}
