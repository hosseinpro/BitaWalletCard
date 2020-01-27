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
*   @module           $RCSfile: KeyBuilderX.java.rca $
*   @revision         $Revision: 1.2 $
*   @date             $Date: Fri Jun 24 16:32:29 2011 $
*                                                                              *
*   @description                                                               *
*   This class is used to build the JCOP extended key objects.                 *
*                                                                              *
********************************************************************************/

package com.nxp.id.jcopx;

import javacard.security.Key;

/**
 * The KeyBuilder class is a key object factory.
 */

public class KeyBuilderX
{
    /**
     * Key object which implements interface type RSAPrivateCrtKeyX which uses Chinese
     * Remainder Theorem.
     */
    public static final byte TYPE_RSA_CRT_PRIVATE_X = (byte)0x86;
    /**
     * Key object which implements interface type RSAPrivateKeyX which uses
     * modulus/exponent form.
     */
    public static final byte TYPE_RSA_PRIVATE_X = (byte)0x85;
    /**
     * Key object which implements interface type AESStaticKeyX in persistent
     * memory type.
     */
    public static final byte  TYPE_AES_STATIC = (byte)0x43;
    /**
     * Key object which implements interface type AESStaticKeyX in transient
     * memory type. Object cleared while DESELECT. 
     */ 
    public static final byte TYPE_AES_TRANSIENT_DESELECT_STATIC = (byte)0x42;
    /**
     * Key object which implements interface type AESStaticKeyX in transient
     * memory type. Object cleared while RESET.
     */
    public static final byte TYPE_AES_TRANSIENT_RESET_STATIC = (byte)0x41;
    
    /**
     * A private constructor to prevent the java-compiler to create a public one.
     */
    private KeyBuilderX()
    {
    }

    /**
     * Creates uninitialized cryptographic keys for signature and cipher algorithms. Only 
     * instances created by this method may be the key objects used to initialize instances 
     * of Signature, Cipher and KeyPair. Note that the object returned must be cast to their 
     * appropriate key type interface.
     *
     * @param keyType the type of key to be generated. Valid codes listed in TYPE.. constants.
     * @param keyLength the key size in bits. The valid key bit lengths are key type dependent.
     * @param keyEncryption if true this boolean requests a key implementation which implements
     * the javacardx.crypto.KeyEncryption interface. The key implementation returned may implement
     * the javacardx.crypto.KeyEncryption interface even when this parameter is false.
     * @return the key object instance of the requested key type, length and encrypted access.
     * 
     * @throws CryptoException - with the following reason codes:
     * <ul>
     * <li>CryptoException.NO_SUCH_ALGORITHM if the requested algorithm associated with the
     * specified type, size of key and key encryption interface is not supported.</li>
     * </ul>
     */
    public static Key buildKey (byte keyType, short keyLength, boolean keyEncryption)
    {
        return null;
    }
}
