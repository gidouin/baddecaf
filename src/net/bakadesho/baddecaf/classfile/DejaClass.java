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
package net.bakadesho.baddecaf.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.bakadesho.baddecaf.classfile.constantpool.ClassInfo;
import net.bakadesho.baddecaf.classfile.constantpool.Constant;
import net.bakadesho.baddecaf.classfile.constantpool.ConstantPool;
import net.bakadesho.baddecaf.classfile.constantpool.IntegerInfo;
import net.bakadesho.baddecaf.classfile.constantpool.NameAndTypeInfo;
import net.bakadesho.baddecaf.classfile.constantpool.RefInfo;
import net.bakadesho.baddecaf.classfile.constantpool.StringInfo;
import net.bakadesho.baddecaf.classfile.constantpool.Utf8Info;

/**
 * Parse and represent a java .class 
 * 
 * @author "gidouin"
 *
 */
public class DejaClass implements Serializable {

	/** Java class MAGIC value */
	final private static int MAGIC = 0xCAFEBABE;

	private Set<ClassField> classFields = new HashSet<ClassField>();
	
	private Set<ClassMethod> classMethods = new HashSet<ClassMethod>();

	private ConstantPool cp;

	public Set<ClassMethod> getMethods() {
		return classMethods;
	}
	
	public ConstantPool cp() {
		return cp;
	}

	/**
	 * Create a DejaClass by reading the given InputStream
	 * 
	 * @param is
	 * @return a DejaClass
	 * @throws Exception
	 */
	public static DejaClass createFromClassFile(final InputStream is) throws Exception {
		final DejaClass dejaClass = new DejaClass();
		final DataInputStream dis = new DataInputStream(is);
		
		final int magic = dis.readInt();
		if (MAGIC != magic) {
			throw new IOException("Not a Java class");
		}
		
		final short minor = dis.readShort();		
		final short major = dis.readShort();		
		System.out.println("Version: " + major + "." + minor);

		//
		// constant pool
		//

		final short constantPoolCount = dis.readShort();
		System.out.println("Constant Pool Count: " + constantPoolCount);	
		dejaClass.cp = new ConstantPool(constantPoolCount);

		AttributeFactory attributeFactory = new AttributeFactory(dejaClass.cp);

		System.out.println("Constants:");
		for (short i = 1 ; i < constantPoolCount; i++) {
			byte tag = dis.readByte();
			System.out.print("\t" + i + "\tTag=" + tag + "\t");
			
			Constant entry = null;
			switch(tag) {
				case 1 : // Utf8_Info
					short len = dis.readShort();
					byte[] bytes = new byte[len];
					dis.read(bytes);//TODO: loop
					String utf8 = new String(bytes, "utf-8");
					
					entry = new Utf8Info(utf8);
					System.out.println("Utf8_Info len=" + len + "\t'" + utf8 + "'");

					break;
				case 3 : // Integer_Info
					int integer = dis.readInt();
					entry = new IntegerInfo(integer);
					System.out.println("Integer_Info" + integer);
					break;
				case 4:
					System.out.println("FLOAT **** TODO");
					break;
				case 5:
					System.out.println("LONG **** TODO");
					break;
				case 6:
					System.out.println("DOUBLE **** TODO");
					break;
				case 7 : // Class_Info
					short nameIndex = dis.readShort();
					System.out.println("Class_Info nameIndex=" + nameIndex);
					entry = new ClassInfo(nameIndex, dejaClass.cp());					
					break;
				case 8 : // String_Info
					short stringIndex = dis.readShort();
					System.out.println("String_Info stringIndex=" + stringIndex);
					entry = new StringInfo(stringIndex, dejaClass.cp());
					break;
				case 9 : // Fieldref_Info
					short classIndex1 = dis.readShort();
					short nameAndTypeIndex1 = dis.readShort();
					System.out.println("Fieldref_Info classIndex1=" + classIndex1 + ", nameAndTypeIndex=" + nameAndTypeIndex1);
					entry = new RefInfo(classIndex1, nameAndTypeIndex1, dejaClass.cp());
					break;
				case 10 : // Methodref_Info
					short classIndex = dis.readShort();
					short nameAndTypeIndex = dis.readShort();
					System.out.println("Methodref_Info classIndex=" + classIndex + ", nameAndTypeIndex=" + nameAndTypeIndex);
					entry = new RefInfo(classIndex, nameAndTypeIndex, dejaClass.cp());
					break;
				case 11 : // InterfaceMethodref_Info
					short classIndex2 = dis.readShort();
					short nameAndTypeIndex2 = dis.readShort();
					System.out.println("InterfaceMethodref_Info classIndex=" + classIndex2 + ", nameAndTypeIndex=" + nameAndTypeIndex2);
					break;
				case 12 : // NameAndType_Info 
					short nameIndex2 = dis.readShort();
					short descriptorIndex = dis.readShort();
					System.out.println("NameAndType_Info nameIndex=" + nameIndex2 + ", descriptorIndex=" + descriptorIndex);
					entry = new NameAndTypeInfo(nameIndex2, descriptorIndex);
					break;
//					CONSTANT_MethodHandle 	15
//					CONSTANT_MethodType 	16
//					CONSTANT_InvokeDynamic 	18				
				default:
					System.out.println("UNKNOWN");
					break;
			}
			
			dejaClass.cp.set(i, entry);
			
		}

		short accessFlags = dis.readShort();
		System.out.println("Acces Flag: " + accessFlags + " => " + AccessFlags.getModifiers(accessFlags));
		
		//
		// this ->  Class_Info entry in cp
		//

		short thisClass = dis.readShort();
		System.out.println("this Class: " + thisClass + " => " + dejaClass.cp.getClassInfoName(thisClass));
		
		//
		// super -> Class_Info entry in cp
		//
		
		short superClass = dis.readShort();
		System.out.println("super Class: " + superClass + " => " + dejaClass.cp.getClassInfoName(superClass));
		
		//
		// interfaces
		//
		
		short interfacesCount = dis.readShort();
		System.out.println("Interface Counts: " + interfacesCount);
		for (int i = 0; i < interfacesCount; i++) {
			short interfaceEntry = dis.readShort();
			System.out.println("\tInterface " + dejaClass.cp.getClassInfoName(interfaceEntry));
		}

		//
		// fields
		//
		
		short fieldsCount = dis.readShort();
		System.out.println("Field Counts: " + fieldsCount);
		for (int i = 0; i < fieldsCount; i++) {
			final short fieldAccessFlags = dis.readShort();
			final short nameIndex = dis.readShort();
			final short descriptorIndex = dis.readShort();
			final ClassField classField = new ClassField(dejaClass.cp.getText(nameIndex), fieldAccessFlags, dejaClass.cp.getText(descriptorIndex));

			System.out.println("\tField " + classField);

			Map<String, Attribute> attributes = attributeFactory.getAttributes(dis);
			classField.addAttribute(attributes);

			dejaClass.classFields.add(classField);
		}
		
		//
		// methods
		//
		
		short methodsCount = dis.readShort();
		System.out.println("Method Counts: " + methodsCount);
		for (int i = 0; i < methodsCount; i++) {
			final short methodAccessFlags = dis.readShort();
			final short nameIndex = dis.readShort();
			final short descriptorIndex = dis.readShort();			
			final ClassMethod classMethod = new ClassMethod(dejaClass.cp.getText(nameIndex), methodAccessFlags, dejaClass.cp.getText(descriptorIndex));

			System.out.println("\tMethod " + classMethod);

			final Map<String, Attribute> attributes = attributeFactory.getAttributes(dis);
			classMethod.addAttribute(attributes);

			dejaClass.classMethods.add(classMethod);
		}
		
		//
		// attributes
		//
		
		final short classAttributes = dis.readShort();
		System.out.println("Class Attributes: " + classAttributes);		
		for (int i = 0; i < classAttributes; i++) {
			final short attributeNameIndex = dis.readShort();
			final int attributeLength = dis.readInt();
			final String name = dejaClass.cp.getText(attributeNameIndex);
			final byte[] bytes = new byte[attributeLength];
			dis.read(bytes); //TODO loop
			System.out.println("\t\tAttribute '" + name + "' len=" + attributeLength);
			
			if ("InnerClasses".equals(name)) {
				System.err.println("TODO InnerClasses Attribute");
			} else if ("EnclosingMethod".equals(name)) {
				System.err.println("TODO EnclosingMethod Attribute");
			} else if ("Synthetic".equals(name)) {
				System.err.println("TODO Synthetic Attribute");
			}
			
			
		}

		dis.close();
		return dejaClass;
	}
	
		
}
