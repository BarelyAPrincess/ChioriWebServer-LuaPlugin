/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright 2015 Chiori Greene a.k.a. Chiori-chan <me@chiorichan.com>
 * All Right Reserved.
 */
package com.chiorichan.plugin.lua;

import com.chiorichan.factory.ScriptingContext;
import com.chiorichan.factory.ScriptingEngine;
import com.chiorichan.factory.ScriptingRegistry;

/**
 * 
 */
public class LuaScriptRegistry implements ScriptingRegistry
{
	@Override
	public ScriptingEngine[] makeEngines( ScriptingContext context )
	{
		return new ScriptingEngine[] {new LuaScriptEngine()};
	}
}
