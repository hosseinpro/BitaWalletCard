/*******************************************************************************
*                                                                              *
*     IBM Confidential                                                         *
*                                                                              *
*                                                                              *
*     See Copyright Instructions (C-S 0-6045-002)                              *
*                                                                              *
********************************************************************************
*                                                                              *
*   @module           $RCSfile: ECPointBuilder.java.rca $
*   @revision         $Revision: 1.3 $
*   @date             $Date: Thu Sep 10 17:43:36 2009 $
*                                                                              *
*   @description                                                               *
*   This is a class that defines JCOP specific extensions to the JavaCard      *
*   standard. 														           *
*                                                                              *
********************************************************************************/

package com.nxp.id.jcopx;

  /**
	 * ECPointBuilder which builds an ECPoint object
	 */

public class ECPointBuilder
{
	
	 /**
     * <code>TYPE_EC_FP_POINT</code> creates an ECPoint object in EEPROM.
     */
	 public static final byte TYPE_EC_FP_POINT            					      = 127;
	 /**
     * <code>TYPE_EC_FP_POINT_CLEAR_ON_DESELECT</code> creates an ECPoint object in RAM. The object is cleared when the applet is deselected.
     */
	 public static final byte TYPE_EC_FP_POINT_CLEAR_ON_DESELECT          = 126;
	 /**
     * <code>TYPE_EC_FP_POINT_CLEAR_ON_RESET</code> creates an ECPoint object in RAM. The object is cleared on reset.
     */
	 public static final byte TYPE_EC_FP_POINT_CLEAR_ON_RESET            	= 125;

    /**
     * Creates uninitialized ECPoint object for arithmetic operations over finite fields GF(p).
     *
     * @param keyType the type of key to be generated. Valid codes listed in TYPE.. constants.
     *
     * @param keyLength the key size in bits. The valid key bit lengths are key type dependent.
     *        Some common key lengths are listed above above in the LENGTH_.. constants.
     *
     * @return ECPoint object instance of the requested key type and length.
     *
     * @throws CryptoException - with the following reason codes:
     * <ul>
     * <li>CryptoException.NO_SUCH_ALGORITHM if the requested algorithm associated with the
     * specified type, size of key and key encryption interface is not supported.</li>
     * </ul>
     *
     */
  public static ECPoint buildECPoint(byte keyType, short keyLength)
  {
	    return null;
  }
}

