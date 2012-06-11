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
package net.bakadesho.baddecaf.classfile.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

import net.bakadesho.baddecaf.classfile.Attribute;
import net.bakadesho.baddecaf.classfile.AttributeFactory;

public class CodeAttribute extends AbstractAttribute {

	public final static String CODE_ATTRIBUTE = "Code";

	private short maxStack;
	
	private short maxLocals;
	
	private int codeLength;
	
	private byte[] bytecode;
	
	private short exceptionTableLength;
	
	private ExceptionEntry[] exceptions;
	
	private Map<String, Attribute> attributes;

	public CodeAttribute(final DataInputStream dis, final AttributeFactory af) throws IOException {
		super(CODE_ATTRIBUTE);
		
		maxStack = dis.readShort();
		maxLocals = dis.readShort();
		codeLength = dis.readInt();
		bytecode = new byte[codeLength];
		dis.read(bytecode);//TODO loop
		
		exceptionTableLength = dis.readShort();
		exceptions = new ExceptionEntry[exceptionTableLength];
		
		for (int i = 0; i < exceptionTableLength; i++) {
			final short startPc = dis.readShort();
			final short endPc = dis.readShort();
	        final short handlerPc = dis.readShort();
	        final short catchType = dis.readShort();
	        exceptions[i] = new ExceptionEntry(startPc, endPc, handlerPc, catchType);
		}

		attributes = af.getAttributes(dis);
		
	}
	
	public int getMaxLocals() {
		return maxLocals;
	}
	
	public String toString() {
		return CODE_ATTRIBUTE + " maxStacks=" + maxStack +
			" maxLocals=" + maxLocals +
			" codeLength= " + codeLength +
			" exceptionTableLength=" + exceptionTableLength;
	}
	
	public byte[] getBytecode() {
		return bytecode;
	}
	
	static final public class ExceptionEntry {
		final private short startPc;
		final private short endPc;
		final private short handlerPc;
		final private short catchType;

        public ExceptionEntry(short startPc, short endPc, short handlerPc, short catchType) {
			this.startPc = startPc;
			this.endPc = endPc;
			this.handlerPc = handlerPc;
			this.catchType = catchType;
		}
	}
}
