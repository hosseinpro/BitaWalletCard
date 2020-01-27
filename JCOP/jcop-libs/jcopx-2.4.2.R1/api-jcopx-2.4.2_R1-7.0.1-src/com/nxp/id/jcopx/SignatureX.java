/*******************************************************************************
 *  Copyright (c), NXP Semiconductors                                           *   
 *                                                                              * 
 *  (c) NXP B.V.2007                                                            * 
 *                                                                              * 
 *  All rights are reserved. Reproduction in whole or in part is prohibited     * 
 *  without the written consent of the copyright owner. NXP reserves the right  * 
 *  to make changes without notice at any time.                                 * 
 *  NXP makes no warranty, expressed, implied or statutory, including but not   * 
 *  limited to any implied warranty of merchantability or fitness for any       * 
 *  particular purpose, or that the use will not infringe any third party       * 
 *  patent, copyright or trademark.                                             * 
 *  NXP must not be liable for any loss or damage arising from its use.         * 
 *                                                                              * 
 ********************************************************************************
 *                                                                              *
 *   @module           $RCSfile: SignatureX.java.rca $
 *   @revision         $Revision: 1.14 $
 *   @date             $Date: Fri Jun  4 13:54:41 2010 $
 *                                                                              *
 *   @description                                                               *
 *   This is a class that defines JCOP specific Signature algorithms to the 
 *    JavaCard                                                                  *
 *   standard.                                                                  *
 *                                                                              *
 ********************************************************************************/

package com.nxp.id.jcopx;

import javacard.security.Signature;

/**
 * The class SignatureX defines static methods that can be used to generate
 * Javacard-objects which implement JCOP-specific algorithms. It currently
 * defines the following fields and methods:
 */
public final class SignatureX {

	/**
	 * Signature algorithm <code>ALG_KOREAN_SEED_MAC_NRPAD</code> generates an
	 * 16-byte MAC using Korean SEED in CBC mode.
	 * <p>
	 * Note:
	 * <ul>
	 * <li><em>This algorithm must not be implemented if export restrictions apply.</em>
	 * </ul>
	 */
	public static final byte ALG_KOREAN_SEED_MAC_NRPAD = (byte) 0x67;

	/**
	 * Signature algorithm <code>ALG_KOREAN_SEED_MAC_NOPAD</code> generates an
	 * 16-byte MAC using Korean SEED in CBC mode.
	 * <p>
	 * Note:
	 * <ul>
	 * <li><em>This algorithm must not be implemented if export restrictions apply.</em>
	 * </ul>
	 */
	public static final byte ALG_KOREAN_SEED_MAC_FPAD = (byte) 0x68;

	/**
	 * Signature algorithm signs/verifies the message without hashing using
	 * ECDSA. The signature is encoded as an ASN.1 sequence of two INTEGER
	 * values, r and s, in that order: SEQUENCE ::= { r INTEGER, s INTEGER }
	 */ 
    public static final byte ALG_ECDSA_PLAIN  = (byte) 0x7C; 
	
	/**
	 * Signature algorithm ALG_DES_MAC8_ISO9797_1_M1_ALG3 generates an 8-byte
	 * MAC using a 2-key DES3 key according to ISO9797-1 MAC algorithm 3 with
	 * method 1 (also EMV'96, EMV'2000), where input data is padded using method
	 * 1 and the data is processed as described in MAC Algorithm 3 of the ISO
	 * 9797-1 specification. The left key block of the triple DES key is used as
	 * a single DES key(K) and the right key block of the triple DES key is used
	 * as a single DES Key (K') during MAC processing. The final result is
	 * truncated to 8 bytes as described in ISO9797-1.
	 */
	public static final byte ALG_DES_MAC8_ISO9797_1_M1_ALG3 = (byte) 0x7F;

	/**
	 * Signature algorithm ALG_DES_CMAC8 generates 8 bytes MAC based on NIST 
	 * Special Publication 800-38B  (Cipher=DES)  
	 */		
  public static final byte ALG_DES_CMAC8 = (byte) 0x79;
  
	/**
	 * Signature algorithm ALG_AES_CMAC16 generates 16 bytes MAC based on NIST 
	 * Special Publication 800-38B  (Cipher=AES)  
	 */	  
  public static final byte ALG_AES_CMAC16 = (byte) 0x78; 	

	/**
	 * Creates a Signature object instance of the selected algorithm.
	 * 
	 * @param algorithm
	 *            the desired Signature algorithm. Valid codes listed in ALG_ ..
	 *            constants above e.g.
	 *            <code>ALG_DES_MAC8_ISO9797_1_M1_ALG3</code>
	 * @param externalAccess
	 *            <code>true</code> indicates that the instance will be shared
	 *            among multiple applet instances and that the Signature
	 *            instance will also be accessed (via a <code>Shareable</code>
	 *            interface) when the owner of the Signature instance is not the
	 *            currently selected applet.
	 * 
	 * @return the <code>Signature</code> object instance of the requested
	 *         algorithm
	 * 
	 * @throws CryptoException
	 *             with the following reason codes:
	 *             <ul>
	 *             <li><code>CryptoException.NO_SUCH_ALGORITHM</code> if the
	 *             requested algorithm or shared access mode is not supported.
	 */
	public static Signature getInstance(byte algorithm, boolean externalAccess) {
		return (null);
	}

	/**
	 * Private constructor to prevent the java-compiler declaring a standard
	 * (public) constructor.
	 */
	private SignatureX() {
	}
}
