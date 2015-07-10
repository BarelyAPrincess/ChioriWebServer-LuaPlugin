/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright 2015 Chiori Greene a.k.a. Chiori-chan <me@chiorichan.com>
 * All Right Reserved.
 */
package com.chiorichan.plugin.lua.api;

import com.chiorichan.plugin.lua.LuaStateFactory;
import com.naef.jnlua.LuaState;

/**
 * 
 */
public abstract class NativeLuaAPI
{
	LuaState lua;
	
	NativeLuaAPI( LuaStateFactory factory )
	{
		lua = factory.getLuaState();
	}
	
	// protected def node = machine.node
	// protected def components = machine.components
	
	abstract void initialize();
}
