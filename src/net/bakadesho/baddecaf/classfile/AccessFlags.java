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

/**
 * Utility Class to process Access flags
 * 
 * @author "gidouin"
 *
 */
public class AccessFlags {

	/**
	 * enum of possible flags
	 *
	 */
	public static enum Flags {
		ACC_PUBLIC(0x0001, "public"),
		ACC_PRIVATE(0x0002, "private"),
		ACC_PROTECTED(0x0004, "protected"),
		ACC_STATIC(0x0008, "static"),
		ACC_FINAL(0x0010, "final"),
		ACC_SYNCHRONIZED(0x0020, "synchronized"),
		ACC_VOLATILE(0x0040, "volatile"),	//TODO also BRIDGE
		ACC_TRANSCIENT(0x0080, "transcient"),	//TODO also VARARGS
		ACC_NATIVE(0x0100, "native"),		
		ACC_INTERFACE(0x200, "interface"),
		ACC_ABSTRACT(0x400, "abstract"),
		ACC_STRICT(0x800, "strictfp"),
		ACC_SYNTHETIC(0x1000, "synthetic"),
		ACC_ANNOTATION(0x2000, "annotation"),
		ACC_ENUM(0x4000, "enum");
		
		/** access flag */
		private final int flag;
		
		/** modifier corresponding to the flag */
		private final String modifier;
		
		/**
		 * Create a new flag with modifier name
		 * 
		 * @param flag
		 * @param modifier
		 */
		private Flags(final int flag, final String modifier) {
			this.flag = flag;
			this.modifier = modifier;
		}

		public int getValue() {
			return flag;
		}

		public String getModifier() {
			return modifier;
		}

		/**
		 * tests if the flag is set
		 * 
		 * @param flags
		 * @return true is the flag is set
		 */
		public boolean isSet(short flags) {
			return (flag == (flags & flag));
		}
		
		
	}
	
	/** modifier flags */
	private short flags;
	
	public AccessFlags(final short flags) {
		this.flags = flags;
	}
	
	/**
	 * Returns the modifier corresponding to the access flags, in the order
	 * recommanded in the Java Language specification, sections 8.1.1, 8.3.1 and 8.4.3.
	 * 
	 * @return modifiers
	 */
	public static String getModifiers(short flags) {
		StringBuilder sb = new StringBuilder(40);

		if (Flags.ACC_PUBLIC.isSet(flags)) {
			sb.append(Flags.ACC_PUBLIC.getModifier()).append(' ');
		}
		
		if (Flags.ACC_PROTECTED.isSet(flags)) {
			sb.append(Flags.ACC_PROTECTED.getModifier()).append(' ');
		}

		if (Flags.ACC_PRIVATE.isSet(flags)) {
			sb.append(Flags.ACC_PRIVATE.getModifier()).append(' ');
		}

		if (Flags.ACC_ABSTRACT.isSet(flags)) {
			sb.append(Flags.ACC_ABSTRACT.getModifier()).append(' ');
		}

		if (Flags.ACC_STATIC.isSet(flags)) {
			sb.append(Flags.ACC_STATIC.getModifier()).append(' ');
		}

		if (Flags.ACC_FINAL.isSet(flags)) {
			sb.append(Flags.ACC_FINAL.getModifier()).append(' ');
		}

		if (Flags.ACC_TRANSCIENT.isSet(flags)) {
			sb.append(Flags.ACC_TRANSCIENT.getModifier()).append(' ');
		}

		if (Flags.ACC_VOLATILE.isSet(flags)) {
			sb.append(Flags.ACC_VOLATILE.getModifier()).append(' ');
		}

		if (Flags.ACC_SYNCHRONIZED.isSet(flags)) {
			sb.append(Flags.ACC_SYNCHRONIZED.getModifier()).append(' ');
		}

		if (Flags.ACC_NATIVE.isSet(flags)) {
			sb.append(Flags.ACC_NATIVE.getModifier()).append(' ');
		}

		if (Flags.ACC_STRICT.isSet(flags)) {
			sb.append(Flags.ACC_STRICT.getModifier()).append(' ');
		}

		
		return sb.toString();
	}

	
}
