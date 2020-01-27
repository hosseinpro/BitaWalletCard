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
*   @module           $RCSfile: JCOPXException.java.rca $
*   @revision         $Revision: 1.1 $
*   @date             $Date: Fri Aug 27 15:01:34 2010 $
*                                                                              * 
*   @description                                                               * 
*                                                                              * 
*   This class implements contains the implementation of the JCOPX-Exception.  * 
*                                                                              * 
********************************************************************************/


package com.nxp.id.jcopx;

import javacard.framework.*;

/**
 * Generic exception class that is used by features out of NXPs JCOPX.* to
 * indicate invalid usage. If this exception gets caught, the provided reason
 * code will give an indication about the error occurred.
 * 
 * Excpetion-Code Usage:
 *  o) DESFire-Emulation: 0x0100 - 0x01FF
 *  o) ....
 */
public class JCOPXException extends javacard.framework.CardRuntimeException {
    /**
    * DESFire: DESFire-API not available.
    */
    public static final short DF_FEATURE_NOT_AVAILABLE = (short) 0x01CD;

    /**
    * DESFire: Internal firewall error
    */
    public static final short DF_RESSOURCE_MISSMATCH = (short) 0x01CF;

    /**
    * DESFire: Internal parameter error (e.g. Write-access to RFU-bits)
    */
    public static final short DF_WRONG_PARAMETER = (short) 0x01D4;

    /**
    * DESFire: Internal generic error (e.g. Memory violation)
    */
    public static final short DF_GENERIC_ERROR = (short) 0x01CA;

    /**
    * DESFire: Internal timeout error 
    */
    public static final short DF_TIMEOUT = (short) 0x01C7;

    /**
    * DESFire: Internal buffer-size error 
    */
    public static final short DF_WRONG_BUFFER_SIZE = (short) 0x01D6;

    /**
    * DESFire: Internal buffer-size error 
    */
    public static final short DF_INITIALIZATION_ERROR = (short) 0x01D0;
   
    /**
     * This exception is thrown only by the system.
     */
    private JCOPXException( short reason ) {
      super(reason);
    }

    /**
     * Throws a JCOPXException with the given reason.
     * 
     * @param reason
     *            the reason code, should map to one of the static fields of
     *            this class
     * @exception JCOPX
     *                with the given reason code
     */
     
    public static void throwIt( short  reason ) throws JCOPXException {
    }
}
