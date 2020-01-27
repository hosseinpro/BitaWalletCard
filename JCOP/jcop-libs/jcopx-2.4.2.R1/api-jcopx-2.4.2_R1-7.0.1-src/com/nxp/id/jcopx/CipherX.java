/*******************************************************************************
 *                                                                              *
 *     IBM Confidential                                                         *
 *                                                                              *
 *     WebSphere Everyplace Chip Operating System                               *
 *                                                                              *
 *     (C) COPYRIGHT IBM CORP 2001,2006                                         *
 *                                                                              *
 *     LICENSED MATERIALS - PROPERTY OF IBM                                     *
 *                                                                              *
 *     All Rights Reserved                                                      *
 *                                                                              *
 *     U.S. Government Users Restricted Rights - Use, duplication or            *
 *     disclosure restricted by GSA ADP Schedule Contract with IBM Corp.        *
 *                                                                              *
 *     See Copyright Instructions (C-S 0-6045-002)                              *
 *                                                                              *
 ********************************************************************************
 *                                                                              *
 *   @module           $RCSfile: CipherX.java.rca $
 *   @revision         $Revision: 1.8 $
 *   @date             $Date: Fri Jun  4 13:54:41 2010 $
 *                                                                              *
 *   @description                                                               *
 *   This is a class that defines JCOP specific extensions to the JavaCard      *
 *   standard. 
 *                                                                              *
 ********************************************************************************/

package com.nxp.id.jcopx;

import javacardx.crypto.Cipher;

/**
 * The class Extensions defines static methods that can be used to generate
 * Javacard-objects which implement JCOP-specific algorithms. It currently
 * defines the following fields and methods:
 */
public final class CipherX {
	/**
	 * Cipher algorithm <code>ALG_KOREAN_SEED_CBC_NRPAD</code> provides a
	 * cipher using the Korean SEED algorithm specified in the Korean SEED
	 * Algorithm specification provided by KISA, Korea Information Security
	 * Agency in ECB mode.
	 */
	public static final byte ALG_KOREAN_SEED_CBC_NRPAD = (byte) 0x61;

	/**
	 * Cipher algorithm <code>ALG_KOREAN_SEED_CBC_FPAD</code> provides a
	 * cipher using the Korean SEED algorithm specified in the Korean SEED
	 * Algorithm specification provided by KISA, Korea Information Security
	 * Agency in ECB mode .
	 */
	public static final byte ALG_KOREAN_SEED_CBC_FPAD = (byte) 0x62;

	/**
	 * Cipher algorithm <code>ALG_KOREAN_SEED_ECB_NRPAD</code> provides a
	 * cipher using the Korean SEED algorithm specified in the Korean SEED
	 * Algorithm specification provided by KISA, Korea Information Security
	 * Agency in ECB mode.
	 */
	public static final byte ALG_KOREAN_SEED_ECB_NRPAD = (byte) 0x64;

	/**
	 * Cipher algorithm <code>ALG_KOREAN_SEED_ECB_FPAD</code> provides a
	 * cipher using the Korean SEED algorithm specified in the Korean SEED
	 * Algorithm specification provided by KISA, Korea Information Security
	 * Agency in ECB mode.
	 */
	public static final byte ALG_KOREAN_SEED_ECB_FPAD = (byte) 0x65;

	/**
	 * Creates a Cipher object instance of the selected algorithm.
	 * 
	 * @param algorithm
	 *            the desired Cipher algorithm. Valid codes listed in ALG_ ..
	 *            constants above e.g. <code>ALG_KOREAN_SEED_CBC_NOPAD</code>
	 * @param externalAccess
	 *            <code>true</code> indicates that the instance will be shared
	 *            among multiple applet instances and that the Cipher instance
	 *            will also be accessed (via a <code>Shareable</code>
	 *            interface) when the owner of the Signature instance is not the
	 *            currently selected applet.
	 * 
	 * @return the <code>Cipher</code> object instance of the requested
	 *         algorithm
	 * 
	 * @throws CryptoException
	 *             with the following reason codes:
	 *             <ul>
	 *             <li><code>CryptoException.NO_SUCH_ALGORITHM</code> if the
	 *             requested algorithm or shared access mode is not supported.
	 */
	public static Cipher getInstance(byte algorithm, boolean externalAccess) {
		return (null);
	}

	/**
	 * Private constructor to prevent the java-compiler declaring a standard
	 * (public) constructor.
	 */
	private CipherX() {
	}
}
