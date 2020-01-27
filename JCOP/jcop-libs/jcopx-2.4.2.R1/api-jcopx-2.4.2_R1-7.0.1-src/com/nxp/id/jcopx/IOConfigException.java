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
*   @module           $RCSfile: IOConfigException.java.rca $
*   @revision         $Revision: 1.1 $
*   @date             $Date: Thu May  5 13:37:25 2011 $
*                                                                              *
*   @description                                                               *
*                                                                              *
*   This class contains the API to the IO-Config/-Control interface of JCOP    *
*                                                                              *
*   \ingroup API_JCOPX                                                         *
********************************************************************************/

package com.nxp.id.jcopx;

import javacard.framework.CardRuntimeException;

/**
 * Exception class used by  {@link com.nxp.id.jcopx#IOConfig} class.
 * 
 */
public final class IOConfigException extends CardRuntimeException {

    /** 
     * An invalid interface identifier has been given. 
     */
    public final static short IF_INVALID = (short)0xb5a9;
    
    /** 
     * Failed to enable/disable an interface
     */
    public final static short IF_ENABLED_DISABLED_FAILED = (short)0xb5b8;
    
    /** 
     * An invalid value for config ID has been used. 
     */
    public final static short CONFIG_ID_INVALID  = (short)0xb5c7;
    
    /** 
     * An invalid config value has been used. 
     */
    public final static short CONFIG_VALUE_INVALID  = (short)0xb5d6;
    
    /** 
     * An invalid reference value for the array has been used. 
     */
    public final static short ARRAY_REFERENCE_INVALID  = (short)0xb5e5;

    /** 
     * Package visible constructor. 
     * @param reason code that led to the exception. 
     */
    IOConfigException( short reason ) {
        super(reason);
    }

    /**
     * Throws the JCRE owned instance of IOConfigException with the specified
     * reason. <p>
     * @param reason the reason for the exception.
     * @exception IOConfigException always.
     */
    public static void throwIt(short reason) throws IOConfigException { 
    }
}


