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

public class ConstantPool {

	private Constant[] pool;
	
	public ConstantPool(final int size) {
		pool = new Constant[size]; 
	}
	
	public Constant get(final int index) {
		return pool[index];
	}

	public void set(int index, Constant entry) {
		pool[index] = entry;
	}

	public String getText(final int index) {
		return pool[index].getText();
	}
	
	public String getClassInfoName(final short index) {
		ClassInfo classInfo = (ClassInfo) pool[index];
		return getText(classInfo.getNameIndex());
	}
	
	public int getInteger(final short index) {
		return ((IntegerInfo) pool[index]).getInteger();
	}
	
	public String getRef(final int index) {
		RefInfo ref = (RefInfo) pool[index];
		
		ClassInfo classInfo = (ClassInfo) pool[ref.getClassIndex()];
		NameAndTypeInfo natInfo = (NameAndTypeInfo) pool[ref.getNameAndTypeIndex()];
		
		short descriptorIndex = natInfo.getDescriptorIndex();
		short nameIndex= natInfo.getNameIndex();
		short classNameIndex = classInfo.getNameIndex();

		String className = getText(classNameIndex).replace('/', '.');
		
		return className + "." + pool[nameIndex].getText() + " type: " + pool[descriptorIndex].getText();
	}
	
	public NameAndTypeInfo getNameAndTypeInfo(short index) {
		return (NameAndTypeInfo) pool[index];
	}
	
	
}
