/*******************************************************************************
 * Copyright (c) 2012 Frédéric Gidouin.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * Contributors:
 *     Frédéric Gidouin - initial API and implementation
 ******************************************************************************/
package net.bakadesho.baddecaf.classfile.constantpool;

final public class IntegerInfo implements Constant {
	
	final private int integer;
	
	public IntegerInfo(final int integer) {
		this.integer = integer;
	}
	
	final public int getInteger() {
		return integer;
	}
	
	public String getText() {
		return "0x" + Long.toString(integer & 0xffffffffL, 16);
	}

}
