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
*   @module           $RCSfile: IOControlException.java.rca $
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
 * Exception class used by  {@link com.nxp.id.jcopx#IOControl} class.
 * 
 */
public final class IOControlException extends CardRuntimeException {

    /** 
     * An invalid IO pin identifier has been given. 
     * Only the following values are allowed:
     * <ul>
     *     <li>{@link IOControlX#IOID_P1}</li>
     *     <li>{@link IOControlX#IOID_P2}</li>
     *     <li>{@link IOControlX#IOID_P3}</li>
     *     <li>{@link IOControlX#IOID_P4}</li>
     * </ul>
     */
    public final static short INVALID_IO_ID = (short)0x65a9;
    
    /** 
     * An invalid value has been used. 
     * Only the following values are allowed:
     * <ul>
     *     <li>{@link IOControlX#IO_HIGH}</li>
     *     <li>{@link IOControlX#IO_LOW}</li>
     * </ul>
     */
    public final static short INVALID_VALUE = (short)0x65b8;

    /**
     * setIo didn't succeed
     */
    public final static short SET_IO_FAILED = (short)0x65c7;

    /**
     * getIo didn't succeed
     */
    public final static short GET_IO_FAILED = (short)0x65d6;

    /** 
     * Package visible constructor. 
     * @param reason code that led to the exception. 
     */
    IOControlException( short reason ) {
        super(reason);
    }

    /**
     * Throws the JCRE owned instance of IOControlException with the specified
     * reason. <p>
     * @param reason the reason for the exception.
     * @exception IOControlException always.
     */
    public static void throwIt(short reason) throws IOControlException { 
    }
}


