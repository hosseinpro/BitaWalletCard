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
*   @module           $RCSfile: KeyPairX.java.rca $
*   @revision         $Revision: 1.1 $
*   @date             $Date: Wed Jun  2 20:10:24 2010 $
*                                                                              *
*   @description                                                               *
*   This class is used to create and initalize a key pair object.              *
*                                                                              *
********************************************************************************/

package com.nxp.id.jcopx;

import javacard.security.*;

/**
 * This class is a container for a key pair (a public key and a private key). It does not enforce
 * any security, and, when initialized, should be treated like a PrivateKey.
 * <p>
 * In addition, this class features a key generation method.</p>
 * 
 * @see PublicKey, PrivateKey
 */
public final class KeyPairX
{
    /**
     * KeyPair object containing a secure RSA key pair.
     */
    public static final byte ALG_RSA_X = 1;

    /**
     * KeyPair object containing a secure RSA key pair with private key
     * in its Chinese Remainder Theorem form.
     */
    public static final byte ALG_RSA_CRT_X = 2;

    /**
     * Package visible constructor with no parameters to be able to create KeyPair
     * instances internally and to prevent the Java-compiler from creating a public 
     * constructor automatically.
     */
    KeyPairX()
    {
    }

    /**
     * Constructs a KeyPair instance for the specified algorithm and keylength; 
     * the encapsulated keys are uninitialized. To initialize the KeyPair instance 
     * use the genKeyPair() method.
     * <p>
     * The encapsulated key objects are of the specified keyLength size and implement 
     * the appropriate Key interface associated with the specified algorithm (example 
     * - RSAPublicKey interface for the public key and RSAPrivateKey interface for 
     * the private key within an ALG_RSA key pair).</p>
     * <p>
     * Notes:
     * <ul>
     * <li>The key objects encapsulated in the generated KeyPair object need not 
     * support the KeyEncryption interface.</li>
     * </ul></p>
     * 
     * @param algorithm the type of algorithm whose key pair needs to be generated. 
     *        Valid codes listed in ALG_.. constants above. See ALG_RSA
     * @param keyLength the key size in bits. The valid key bit lengths are key type 
     *        dependent. See the KeyBuilder class.
     * @throws CryptoException with the following reason codes:
     * <ul>
     * <li>CryptoException.NO_SUCH_ALGORITHM if the requested algorithm associated 
     * with the specified type, size of key is not supported.</li>
     * </ul>
     * 
     * @see KeyBuilder, Signature, javacardx.crypto.Cipher, 
     *      javacardx.crypto.KeyEncryption
     */
    public KeyPairX(byte algorithm, short keyLength) throws CryptoException
    {
    }

    /**
     * Constructs a new KeyPair object containing the specified public key and private key.
     * <p>
     * Note that this constructor only stores references to the public and private key 
     * components in the generated KeyPair object. It does not throw an exception if the 
     * key parameter objects are uninitialized.</p>
     * 
     * @param publicKey the public key.
     * @param privateKey the private key.
     * @throws CryptoException with the following reason codes:
     * <ul>
     * <li>CryptoException.ILLEGAL_VALUE if the input parameter key objects are inconsistent 
     * with each other - i.e mismatched algorithm, size etc.</li>
     * <li>CryptoException.NO_SUCH_ALGORITHM if the algorithm associated with the specified 
     * type, size of key is not supported.</li>
     * </ul>
     */
    public KeyPairX(PublicKey publicKey, PrivateKey privateKey) throws CryptoException
    {
    }

    /** 
     * (Re)Initializes the key objects encapsulated in this KeyPair instance with new key values.
     * The initialized public and private key objects encapsulated in this instance will then be 
     * suitable for use with the Signature, Cipher and KeyAgreement objects. An internal secure 
     * random number generator is used during new key pair generation.
     * <p>
     * Notes:
     * <ul>
     * <li>For the RSA algorithm, if the exponent value in the public key object is pre-initialized,
     * it will be retained. Otherwise, a default value of 65537 will be used.</li>
     * <li>If the time taken to generate the key values is excessive, the implementation may 
     * automatically request additional APDU processing time from the CAD.</li>
     * </ul></p>
     * 
     * @throws CryptoException with the following reason codes:
     * <ul>
     * <li>CryptoException.ILLEGAL_VALUE if the exponent value parameter in RSA is invalid.</li>
     * </ul>
     * 
     * @see javacard.framework.APDU, Signature, javacardx.crypto.Cipher, 
     *      RSAPublicKey, ECKey, DSAKey
     */
    public final void genKeyPair() throws CryptoException 
    {
    }

    /**
     * Returns a reference to the private key component of this KeyPair object.
     * 
     * @return a reference to the private key.
     */
    public PrivateKey getPrivate() throws CryptoException 
    {
        return null;
    }

    /**
     * Returns a reference to the public key component of this KeyPair object.
     * 
     * @return a reference to the public key.
     */
    public PublicKey getPublic() throws CryptoException 
    {
        return null;
    }
}
