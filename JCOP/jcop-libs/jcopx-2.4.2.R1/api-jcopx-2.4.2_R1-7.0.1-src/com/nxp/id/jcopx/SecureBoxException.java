/******************************************************************************* 
*  Copyright (c), NXP Semiconductors                                           * 
*                                                                              * 
*  (c) NXP B.V.2009                                                            * 
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
*   @module           $RCSfile: SecureBoxException.java.rca $
*   @revision         $Revision: 1.5 $
*   @date             $Date: Tue Jun  1 18:41:52 2010 $
*                                                                              * 
*   @description                                                               * 
*                                                                              * 
*   This class implements contains the API to the SecureBox exception.         * 
*                                                                              * 
********************************************************************************/


package com.nxp.id.jcopx;

import javacard.framework.*;

/**
 * Exception class that is used by NXP JCOP's SecureBox implementation to
 * indicate invalid usage. If this exception gets caught, the provided reason
 * code will give an indication about the error occurred.
 */
public class SecureBoxException extends javacard.framework.CardRuntimeException {
    /**
    * The number of bytes to initialize the RAM or FXRAM of the native library is too high
    */
    public static final short INITIALIZATION_OVERFLOW	= (short) 0x3bD0;

    /**
    * The result data located in RAM that needs to be copied back from the native library does not fit in the result array
    */
    public static final short RESULT_OVERFLOW = (short) 0x3bC2;

    /**
    * The array handed over to runNativeLib is not located in EEPROM area
    */
    public static final short ARRAY_IS_NOT_MAPABLE	= (short) 0x3BAB;

    /**
    * Raised by runNativeLib if one of the given EEPROM arrays is not of a valid length.
    */
    public static final short PERSISTENT_BYTE_ARRAY_WRONG_LENGTH	= (short) 0x3bA1;

    /**
    * One object in the Object[] given to the native library is not a byte[]
    */
    public static final short INVALID_OBJECT	= (short) 0x3bE5;

    /**
    * This indicates, that either SecureBox is disabled or not available on the product.
    */
    public static final short NOT_AVAILABLE	= (short) 0x4b0C;

    /**
    * The requested native library is not installed on the card.
    */
    public static final short NATIVE_LIBRARY_ID_NOT_IMPLEMENTED	= (short) 0x3A36;

  	/** 
  	* The native library raised a hardware exception during execution. 
  	*/
  	public static final short HW_VIOLATION = (short) 0x2C33;

  	/** 
  	* The native Library tried to call an unimplemented SVEC. 
  	*/
  	public static final short HW_UNAUTHORIZED_SYSCALL = (short) 0x2C95;

    /**
    * Exception triggered by the native library. 
    * The high byte is fixed (0x33).
    * The lower byte is given by the native library.
    */
    public static final short NATIVE_LIB_RUNTIME	= (short) 0x3300;
    
    /**
     * This exception is thrown only by the system.
     */
    private SecureBoxException( short reason ) {
      super(reason);
    }

    /**
     * Throws a SecureBoxException with the given reason.
     * 
     * @param reason
     *            the reason code, should map to one of the static fields of
     *            this class
     * @exception SecureBoxException
     *                with the given reason code
     */
     
    public static void throwIt( short  reason ) throws SecureBoxException {
    }
}
