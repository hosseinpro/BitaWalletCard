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
*   @module           $RCSfile: DESFire.java.rca $
*   @revision         $Revision: 1.2 $
*   @date             $Date: Thu May 12 16:27:48 2011 $
*                                                                              * 
*   @description                                                               * 
*                                                                              * 
*   This class implements the DESFire-API.                                     * 
*                                                                              * 
********************************************************************************/

package com.nxp.id.jcopx;

/**
 * This class allows to execute DESFire<sup>TM</sup> commands from an applet.
 */ 
public class DESFire {

	/**
	 * Processes the native DESFire command stored in <CODE>inData</CODE> with a given length and offset stored in <CODE>inDataOffset</CODE> and <CODE>inDataLength</CODE>.
	 *
	 * @param inData
	 *            Reference to a buffer containing the native DESFire command.
    *
	 * @param inDataOffset
	 *            Offset where the native DEFire command starts within <CODE>inData</CODE>.
	 *
	 * @param inDataLength
	 *            Length of the native DESFire command.
	 *
	 * @param outData
	 *           Reference to a buffer where the DESFire response should be stored.
	 *
	 * @param outDataOffset
	 *           Offset where the DESFire response should be stored within <CODE>outData</CODE>.
	 *
	 * @return Length of response stored in <CODE>outData</CODE>.
	 *
	 * @throws
	 *             <ul>
	 *             <li>NullPointerException if <CODE>inData</CODE> or <CODE>outData</CODE> is NULL.</li>
	 *             <li>ArrayIndexOutOfBoundsException if a start condition fails, <CODE>inDataLength</CODE> is 0 or the buffer size of <CODE>outData</CODE> is less than 62 byte.</li>
	 *             <li>JCOPXException (DF_FEATURE_NOT_AVAILABLE) if the DESFire-API is not enabled on the current target.</li>
	 *             </ul>
	 *
	 */
    public final static short processData( byte [] inData, short inDataOffset, short inDataLength, byte [] outData, short outDataOffset )
    {
      return( (short) 0 );
    }

}
