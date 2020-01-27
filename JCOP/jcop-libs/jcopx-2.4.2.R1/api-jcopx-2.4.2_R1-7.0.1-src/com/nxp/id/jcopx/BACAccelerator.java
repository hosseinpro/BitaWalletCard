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
 *   @module           $RCSfile: BACAccelerator.java.rca $
 *   @revision         $Revision: 1.7 $
 *   @date             $Date: Thu Sep 10 17:43:36 2009 $
 *                                                                              *
 *   @description                                                               *
 *                                                                              *
 *   This class contains the API to the BAC Accelerator interface of JCOP .     *
 ********************************************************************************/

package com.nxp.id.jcopx;

import com.nxp.id.jcopx.BACAcceleratorException;
import javacard.framework.SystemException;

/**
 * <code>BACAccelerator</code> is a JCOP propietary extension for the secure
 * messaging operations wrap and unwrap. Several independent instances can be
 * created at the same time
 */
public final class BACAccelerator {
	/**
	 * Private constructor. To get an instance of BACAccelerator the method
	 * <code>getInstance()</code> has to be called.
	 */
	private BACAccelerator() throws SystemException {
	}

	/**
	 * Creates an instance of the BACAccelerator. The keys and the send sequence
	 * counter of the BACAccelerator instance are set to zero and need to get
	 * initialized.
	 * 
	 * @return a BACAccelerator instance
	 * @exception SystemException
	 *                is thrown there are no resources left to create the
	 *                instance
	 */
	public static final BACAccelerator getInstance() throws SystemException {
		return (null);
	}

	/**
	 * Sets the encryption and decryption session key. The key is a 16 byte 3DES
	 * ABA key. This method may be called at any time and will immediately
	 * replace the session key.
	 * 
	 * @param encryptionKeyBuffer
	 *            the buffer containing the encryption key. The left most key
	 *            byte is locatet the lowest array index (=<code>encryptionKeyOffset</code>)
	 *            in <code>encryptionKeyBuffer</code>.
	 * @param encryptionKeyOffset
	 *            the offset in array <code>encryptionKeyBuffer</code> where
	 *            the key starts.
	 * @exception ArrayIndexOutOfBoundsException
	 *                is thrown, if <code>encryptionKeyOffset</code>+8 is
	 *                less than <code>encryptionKeyBuffer.length</code>
	 * @exception NullPointerException
	 *                is thrown, if <code>encryptionKeyBuffer</code> is
	 *                <code>null</code>.
	 */
	public void setEncryptionKey(byte[] encryptionKeyBuffer,
			short encryptionKeyOffset) throws ArrayIndexOutOfBoundsException,
			NullPointerException {
	};

	/**
	 * Sets the MAC session key. The key is a 16 byte 3DES ABA keys. This method
	 * may be called at any time and will immediately replace the session key.
	 * 
	 * @param macKeyBuffer
	 *            the buffer containing the mac key. The left most key byte is
	 *            locatet the lowest array index (=<code>encryptionKeyOffset</code>)
	 *            in <code>encryptionKeyBuffer</code>.
	 * @param macKeyOffset
	 *            the offset in array <code>encryptionKeyBuffer</code> where
	 *            the key starts.
	 * @exception ArrayIndexOutOfBoundsException
	 *                is thrown, if <code>macKeyOffset</code>+8 is less than
	 *                <code>macKeyBuffer.length</code>
	 * @exception NullPointerException
	 *                is thrown, if <code>macKeyBuffer</code> is
	 *                <code>null</code>.
	 */
	public void setMacKey(byte[] macKeyBuffer, short macKeyOffset)
			throws ArrayIndexOutOfBoundsException, NullPointerException {
	};

	/**
	 * Unwraps (verify and decrypt) the command APDU located in the APDU buffer.
	 * The command buffer has to be filled by the APDU.setIncomingAndReceive()
	 * method beforehand. The verified and decrypted command data get placed at
	 * the start of the APDU buffer.
	 * 
	 * An encrypted standart APDU results in a standart decrypted APDU and an
	 * extended length APDU gets decrypted to extended length representation. In
	 * case of standart APDU (not extended length) with a Le larger than 0xFF
	 * (taken from DO97) a 2 byte Le is appended to the standart APDU instead of
	 * a one byte Le. Nevertheless, Le is also returned by the function call.
	 * 
	 * @exception BACAcceleratorException
	 *                is thrown, if checks on the protected command APDU are
	 *                failing or an error occurs during the decryption process.
	 * @return the length value encoded by DO97, if this object is missing 0.
	 * @throws BACAcceleratorException
	 *             if an error occured during unwrapping
	 */
	public short unwrapAPDU() throws BACAcceleratorException {
		return ((short) 0);
	}

	/**
	 * Wraps (encrypts and build MAC) the response data and places it in the
	 * APDU buffer starting at offset 0. The buffer can be any buffer including
	 * the APDU buffer itself. If the length is zero the buffer will not be
	 * addressed and no response data will be present in the wrapped output.
	 * 
	 * @param dataBuffer
	 *            array with plain data which has to be packed into a secure
	 *            message response APDU
	 * @param dataOffset
	 *            offset in array <code>dataBuffer</code> where the
	 *            unencrypted source data starts
	 * @param dataLength
	 *            length of the unencrypted source data in array
	 *            <code>dataBuffer</code>
	 * @param sw
	 *            the status word which is part of the wrapped secure message
	 * @exception BACAcceleratorException
	 *                is thrown if an error while wrapping the plain data to a
	 *                secure message occurs. In case of an error the APDU buffer
	 *                might be filled with an uncomplete response APDU
	 * @exception ArrayIndexOutOfBoundsException
	 *                is thrown if <code>dataOffset</code>+<code>dataLength</code>
	 *                is longer than the array length of <code>dataBuffer</code>
	 * @exception NullPointerException
	 *                is thrown, if dataBuffer is <code>null</code>.
	 * 
	 * @return the length of the wrapped data in the <apdu> buffer
	 */
	public short wrapResponse(byte[] dataBuffer, short dataOffset,
			short dataLength, short sw) throws BACAcceleratorException,
			ArrayIndexOutOfBoundsException, NullPointerException {
		return ((short) 0);
	}

	/**
	 * Sets the secure session counter (SSC)
	 * 
	 * @param sscBuffer
	 *            the array containing the 8 byte value for the session counter
	 * @param sscOffset
	 *            the offset in array <code>sscBuffer</code> to the left most
	 *            (msb) byte of the session counter
	 * @exception ArrayIndexOutOfBoundsException
	 *                if array <code>sscBuffer</code> starting from index
	 *                <code>sscOffset</code> does not have at least 8 byte
	 *                left
	 * @exception NullPointerException
	 *                if <code>sscBuffer</code> is <code>null</code>.
	 */
	public void setSSC(byte[] sscBuffer, short sscOffset)
			throws ArrayIndexOutOfBoundsException, NullPointerException {
	};

	/**
	 * Sets the 3DES session keys for en/decryption and MAC to zero.
	 */
	public void clearSessionKeys() {
	};

	/**
	 * Sets the value of the Secure Session Counter (SSC) to zero.
	 */
	public void resetSSC() {
	};

	/**
	 * Calculates the maximum response data length depending on the incoming
	 * APDU The result will differ from standart size APDU's to extended length
	 * APDU's. It is necessary to call unprotectAPDU before calling this method.
	 * This is because one calculation factor is the missing padding byte in
	 * case of DO85.
	 * 
	 * @return the maximum data length in bytes to be wrapped in the response
	 *         APDU.
	 */
	public short getMaximumResponseSize() {
		// the calculation is done on c level to save apdu instanciation
		return (0);
	}
}
