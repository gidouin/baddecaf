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
package net.bakadesho.baddecaf.bytecode;

/**
 * Opcode
 * 
 * @author "gidouin"
 *
 */
public class Opcode {

	/**
	 * (still) non implemented opcode 
	 */
	final public static Opcode UNKNOWN = new Opcode("UNKNOWN", null);
	
	/** name of the opcode */
	private String name;
	
	/** info associated to the opcode */
	private String info;
	
	/** parameters to the opcode */
	private Object[] args;
	
	/**
	 * Create a new Opcode with optional parameters
	 * 
	 * @param name
	 * @param args
	 */
	public Opcode(String name, Object... args) {
		this.name = name;
		this.args = args;
	}
	
	/**
	 * Sets the info associated to the opcode.
	 * 
	 * @param info
	 */
	public void setInfo(final String info) {
		this.info = info;
	}
	
	/**
	 * Create a String representation of the opcode
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (null != args) {
			for (int i = 0; i < args.length; i++) {
				sb.append(' ').append(args[i]);
			}
		}
		
		if (null != info) {
			sb.append("\t\t; " + info);
		}
		
		return sb.toString();
	}
}
