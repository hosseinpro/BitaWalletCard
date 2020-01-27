/*******************************************************************************
 *  Copyright (c), NXP Semiconductors                                           *
 *                                                                              *
 *  (c) NXP B.V.2010                                                            *
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
 *   @module           $RCSfile: RSAPrivateKeyX.java.rca $
 *   @revision         $Revision: 1.1 $
 *   @date             $Date: Wed Jun  2 20:10:24 2010 $
 *                                                                              *
 *   @description                                                               *
 *                                                                              *
 *   This class contains the API to the extension of RSAPrivateKey.             *
 ********************************************************************************/

package com.nxp.id.jcopx;

import javacard.security.RSAPrivateKey;

/**
 * The RSAPrivateKeyX class is an extension of RSAPrivateKey class, which also allow the setting
 * of the public exponent.
 *
 * @see RSAPrivateKey
 */
public interface RSAPrivateKeyX extends RSAPrivateKey
{
    /**
     * Returns the public exponent value of the key in plain text. The data format is big-endian
     * and right-aligned (the least significant bit is the least significant bit of last byte).
     * 
     * @param buffer the output buffer
     * @param offset the offset into the output buffer at which the exponent value begins
     * @return the byte length of the public exponent value returned
     * @throws CryptoException with the following reason code:
     * <ul>
     * <li>CryptoException.UNINITIALIZED_KEY if the public exponent value of the key has not been
     * successfully initialized since the time the initialized state of the key was set to false.</li>
     * </ul>
     * 
     * @see Key
     */
    public short getPublicExponent(byte[] buffer, short offset);

    /**
     * Sets the public exponent value of the key. The plain text data format is big-endian and
     * right-aligned (the least significant bit is the least significant bit of last byte). Input 
     * exponent data is copied into the internal representation.
     * <p>
     * 
     * @param buffer the input buffer
     * @param offset the offset into the input buffer at which the exponent value begins
     * @param length the length of the exponent
     * @throws CryptoException with the following reason code:
     * <ul>
     * <li>CryptoException.ILLEGAL_VALUE if the input exponent data length is inconsistent with the
     * implementation.</li>
     * </ul>
     */
    public void setPublicExponent(byte[] buffer, short offset, short length);
}
