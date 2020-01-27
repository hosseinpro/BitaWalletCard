/*******************************************************************************
 *  Copyright (c), NXP Semiconductors                                           *   
 *                                                                              * 
 *  (c) NXP B.V.2008                                                            * 
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
 *   @module           $RCSfile: BACAcceleratorException.java.rca $
 *   @revision         $Revision: 1.11 $
 *   @date             $Date: Mon Aug 24 16:52:55 2009 $
 *                                                                              *
 *   @description                                                               *
 *                                                                              *
 *   This class implements contains the API to the BACAccelerator exception.    *
 ********************************************************************************/

package com.nxp.id.jcopx;

/**
 * Exception class that is used by the BACAccelerator for BAC compliant secure
 * messaging.
 * 
 * @see BACAccelerator BACAccelerator
 */
public class BACAcceleratorException extends Exception {

	/**
	 * APDU MAC verification failed
	 */
	public static final short BAC_MAC_VERIFICATION_FAILED = 0x0010;

	/**
	 * Wrong padding in decryption of the APDU command data This might be:
	 * <ul>
	 * <li>Wrong padding indicator (0x01) in DO87</li>
	 * <li>Padding does not end with 0x80 0x00*</li>
	 * </ul>
	 */
	public static final short BAC_BAD_PADDING = 0x0011;

	/**
	 * Mandatory secure messaging Data Object (DO8E) not found in command APDU
	 */
	public static final short BAC_DO8E_MISSING = 0x0020;

	/**
	 * Wrong combination of INS byte and DO87/DO85 For DO87 the INS byte needs
	 * to be odd For DO85 the INS byte needs to be even
	 */
	public static final short BAC_DO_UNEXPECTED = 0x0021;

	/**
	 * An error occured in the decoding of a DER data object (wrong tag bytes,
	 * length of BER length field larger than latest ISO7816-4 specifications
	 * allow).
	 */
	public static final short BAC_DER_ENCODING_ERROR = 0x0022;

	/**
	 * The length encoding in DO97 is not supported
	 */
	public static final short BAC_ERROR_UNSUPPORTED_DO97 = 0x23;
    /**
     * The length encoding in DO8E is not supported
     */
    public static final short  BAC_ERROR_DO8E_ENCODING = 0x24; 

	/**
	 * Invalid length value of an array. This can be the case if
	 * <ul>
	 * <li> the DO87 has a to long lenth encoded or</li>
	 * <li> the Lc in extended length APDU is longer than data fit into APDU
	 * buffer</li>
	 * <li> a parsing error would lead to a ArrayIndexOutOfBoundsException
	 * </ul>
	 */
	public static final short BAC_WRONG_LENGTH = 0x0030;

	/**
	 * An error occured while creating a MAC or decrypting data
	 */
	public static final short BAC_CRYPTO_OPERATION_FAILED = 0x0040;

	/**
	 * An error during DES key loading occured
	 */
	public static final short BAC_ERROR_DES_LOADKEY = 0xC8; // 200

	/**
	 * An error occured while encrypting the response message
	 */
	public static final short BAC_ERROR_DES_CRYPT = 0xC9; // 201

	/**
	 * An error occured while MAC calculation of response message
	 */
	public static final short BAC_ERROR_DES_MAC = 0xCA; // 202

	/**
	 * The data length to calculate MAC is too long
	 */
	public static final short BAC_ERROR_DES_MAC_TOO_MANY_BLOCKS = 0xCB; // 203

	/**
	 * The data length for encryption or decryption using DES is too long
	 */
	public static final short BAC_ERROR_DES_CRYPT_TOO_MANY_BLOCKS = 0xCD; // 205

	/**
	 * The calculated response message length is too long for standard APDU buffer size
	 */
	public static final short BAC_ERROR_RESPONSE_APDU_OVERFLOW = 0xCE; 	// 206

	/**
	 * The calculated response message length does not fit into APDU buffer
	 */
	public static final short BAC_ERROR_APDU_BUFFER_OVERFLOW = 0xCF; 	// 207

	private BACAcceleratorException() {
	}

	/**
	 * Get reason code.
	 * 
	 * @return the reason for the exception.
	 */
	public short getReason() {
		return (short) 0;
	}

	/**
	 * Set reason code.
	 * 
	 * @param reason
	 *            the reason for the exception.
	 */
	public void setReason(short reason) {
	}

	/**
	 * Throws a BACAcceleratorException with the given reason.
	 * 
	 * @param reason
	 *            the reason code, should map to one of the static fields of
	 *            this class
	 * @exception BACAcceleratorException
	 *                with the given reason code
	 */
	public static void throwIt(short reason) throws BACAcceleratorException {
	}
}
