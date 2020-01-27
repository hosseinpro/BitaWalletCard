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
 *   @module           $RCSfile: KeyAgreementX.java.rca $
 *   @revision         $Revision: 1.9 $
 *   @date             $Date: Fri Jul  1 14:49:36 2011 $
 *                                                                              *
 *   @description                                                               *
 *   This is a class that defines JCOP specific extensions to the JavaCard      *
 *   standard. It is currently used to define the signature algorithm           *
 *   ALG_DES_MAC8_ISO9797_1_M1_ALG3 and  ALG_EC_SVDP_DH_PLAIN                   *
 *                                                                              *
 ********************************************************************************/

package com.nxp.id.jcopx;

import javacard.security.KeyAgreement;

/**
 * The class Extensions defines static methods that can be used to generate
 * Javacard-objects which implement JCOP-specific algorithms. It currently
 * defines the following fields and methods:
 */
public final class KeyAgreementX {
	/**
	 * KeyAgreement algorithm ALG_EC_SVDP_DH_PLAIN_XY is the same as
	 * ALG_EC_SVDP_DH_PLAIN but but with additional Y component and a leading 0x04,
	 * which is the format of an uncompressed point.
	 */
	public static final byte ALG_EC_SVDP_DH_PLAIN_XY = 126; // (0x7E)
	public static final byte ALG_DH = 127; // (0x7F)

	/**
	 * Creates a KeyAgreement object instance of the selected algorithm.
	 * 
	 * @param algorithm      the desired key agreement algorithm Valid codes listed
	 *                       in ALG_EC.. constants above, for example,
	 *                       ALG_EC_SVDP_DH_PLAIN_XY
	 * @param externalAccess if true indicates that the instance will be shared
	 *                       among multiple applet instances and that the
	 *                       KeyAgreement instance will also be accessed (via a
	 *                       Shareable interface) when the owner of the KeyAgreement
	 *                       instance is not the currently selected applet. If true
	 *                       the implementation must not allocate CLEAR_ON_DESELECT
	 *                       transient space for internal data.
	 * @return the KeyAgreement object instance of the requested algorithm
	 * @throws CryptoException with the following reason codes:
	 *                         <ul>
	 *                         <li>CryptoException.NO_SUCH_ALGORITHM if the
	 *                         requested algorithm or shared access mode is not
	 *                         supported.</li>
	 *                         </ul>
	 */
	public static KeyAgreement getInstance(byte algorithm, boolean externalAccess) {
		if (algorithm == ALG_EC_SVDP_DH_PLAIN_XY)
			algorithm = KeyAgreement.ALG_EC_SVDP_DH_PLAIN_XY;
		return KeyAgreement.getInstance(algorithm, externalAccess);
	}

	/**
	 * Private constructor to prevent the java-compiler declaring a standard
	 * (public) constructor.
	 */
	private KeyAgreementX() {
	}
}
