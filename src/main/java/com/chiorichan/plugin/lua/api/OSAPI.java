/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright 2015 Chiori Greene a.k.a. Chiori-chan <me@chiorichan.com>
 * All Right Reserved.
 */
package com.chiorichan.plugin.lua.api;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.chiorichan.plugin.lua.LuaStateFactory;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

/**
 * 
 */
public class OSAPI extends NativeLuaAPI
{
	public OSAPI( LuaStateFactory factory )
	{
		super( factory );
	}
	
	@Override
	void initialize()
	{
		// Push a couple of functions that override original Lua API functions or
		// that add new functionality to it.
		lua.getGlobal( "os" );
		
		// Custom os.clock() implementation returning the time the computer has
		// been actively running, instead of the native library...
		lua.pushJavaFunction( new JavaFunction()
		{
			@Override
			public int invoke( LuaState lua )
			{
				lua.pushNumber( System.currentTimeMillis() );
				return 1;
			}
		} );
		lua.setField( -2, "clock" );
		
		lua.pushJavaFunction( new JavaFunction()
		{
			@Override
			public int invoke( LuaState lua )
			{
				String format = lua.getTop() > 0 && lua.isString( 1 ) ? lua.toString( 1 ) : "%d/%m/%y %H:%M:%S";
				long time = ( long ) ( lua.getTop() > 1 && lua.isNumber( 2 ) ? lua.toNumber( 2 ) * 1000 / 60 / 60 : System.currentTimeMillis() );
				DateTime dt = new DateTime( time );
				if ( format == "*t" )
				{
					lua.newTable( 0, 8 );
					lua.pushInteger( dt.year().get() );
					lua.setField( -2, "year" );
					lua.pushInteger( dt.monthOfYear().get() );
					lua.setField( -2, "month" );
					lua.pushInteger( dt.dayOfMonth().get() );
					lua.setField( -2, "day" );
					lua.pushInteger( dt.hourOfDay().get() );
					lua.setField( -2, "hour" );
					lua.pushInteger( dt.minuteOfHour().get() );
					lua.setField( -2, "min" );
					lua.pushInteger( dt.secondOfMinute().get() );
					lua.setField( -2, "sec" );
					lua.pushInteger( dt.dayOfWeek().get() );
					lua.setField( -2, "wday" );
					lua.pushInteger( dt.dayOfYear().get() );
					lua.setField( -2, "yday" );
				}
				else
					lua.pushString( dt.toString( DateTimeFormat.forPattern( format ) ) );
				return 1;
			}
		} );
		lua.setField( -2, "date" );
		
		/*
		 * // Date formatting function.
		 * lua.pushScalaFunction(lua => {
		 * val format =
		 * if (lua.getTop > 0 && lua.isString(1)) lua.toString(1)
		 * else "%d/%m/%y %H:%M:%S"
		 * val time =
		 * if (lua.getTop > 1 && lua.isNumber(2)) lua.toNumber(2) * 1000 / 60 / 60
		 * else machine.worldTime + 5000
		 * 
		 * val dt = GameTimeFormatter.parse(time)
		 * def fmt(format: String) {
		 * if (format == "*t") {
		 * lua.newTable(0, 8)
		 * lua.pushInteger(dt.year)
		 * lua.setField(-2, "year")
		 * lua.pushInteger(dt.month)
		 * lua.setField(-2, "month")
		 * lua.pushInteger(dt.day)
		 * lua.setField(-2, "day")
		 * lua.pushInteger(dt.hour)
		 * lua.setField(-2, "hour")
		 * lua.pushInteger(dt.minute)
		 * lua.setField(-2, "min")
		 * lua.pushInteger(dt.second)
		 * lua.setField(-2, "sec")
		 * lua.pushInteger(dt.weekDay)
		 * lua.setField(-2, "wday")
		 * lua.pushInteger(dt.yearDay)
		 * lua.setField(-2, "yday")
		 * }
		 * else {
		 * lua.pushString(GameTimeFormatter.format(format, dt))
		 * }
		 * }
		 * 
		 * // Just ignore the allowed leading '!', Minecraft has no time zones...
		 * if (format.startsWith("!"))
		 * fmt(format.substring(1))
		 * else
		 * fmt(format)
		 * 1
		 * })
		 * lua.setField(-2, "date")
		 * 
		 * // Return ingame time for os.time().
		 * lua.pushScalaFunction(lua => {
		 * if (lua.isNoneOrNil(1)) {
		 * // Game time is in ticks, so that each day has 24000 ticks, meaning
		 * // one hour is game time divided by one thousand. Also, Minecraft
		 * // starts days at 6 o'clock, versus the 1 o'clock of timestamps so we
		 * // add those five hours. Thus:
		 * // timestamp = (time + 5000) * 60[kh] * 60[km] / 1000[s]
		 * lua.pushNumber((machine.worldTime + 5000) * 60 * 60 / 1000)
		 * }
		 * else {
		 * def getField(key: String, d: Int) = {
		 * lua.getField(-1, key)
		 * val res = lua.toIntegerX(-1)
		 * lua.pop(1)
		 * if (res == null)
		 * if (d < 0) throw new Exception("field '" + key + "' missing in date table")
		 * else d
		 * else res: Int
		 * }
		 * 
		 * lua.checkType(1, LuaType.TABLE)
		 * lua.setTop(1)
		 * 
		 * val sec = getField("sec", 0)
		 * val min = getField("min", 0)
		 * val hour = getField("hour", 12)
		 * val mday = getField("day", -1)
		 * val mon = getField("month", -1)
		 * val year = getField("year", -1)
		 * 
		 * GameTimeFormatter.mktime(year, mon, mday, hour, min, sec) match {
		 * case Some(time) => lua.pushNumber(time)
		 * case _ => lua.pushNil()
		 * }
		 * }
		 * 1
		 * })
		 * lua.setField(-2, "time")
		 */
		// Pop the os table.
		lua.pop( 1 );
	}
}
