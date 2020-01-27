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
*   @module           $RCSfile: SecureBox.java.rca $
*   @revision         $Revision: 1.3 $
*   @date             $Date: Tue Jun  1 18:41:52 2010 $
*                                                                              * 
*   @description                                                               * 
*                                                                              * 
*   This class implements the SecureBox.                                       * 
*                                                                              * 
********************************************************************************/

package com.nxp.id.jcopx;

import  com.nxp.id.jcopx.SecureBoxException;

    /**
     * Secure Box JavaCard implementation to be used by a JavaCard applet. The
     * secure box functionality uses dedicated memory areas that are provided or
     * made accessible by functions of this class.
     * 
     * @since JCOP v2.4.2
     */

public class SecureBox {

    /**
    * The maximum number of persistent arrays, that can be mapped into the 
    * memory space visible to the native library.
    */
    public static final byte MAX_NUM_PERSISTENT_ARRAYS = 5;

    /**
     * Invokes the native library function.
     * 
     * @param libraryID
     *            The identification number of the library.
     * @param functionID
     *            This function ID is passed to the native library. 
     *            The library can decide with this parameter, what branch it will access.
     *            It is not mandatory for the native library to make use of this parameter.
     * @param eepromArrays
     *            The array contains the references to all EEPROM arrays, that can be accessed by the native library.
     *            At most {@link SecureBox#MAX_NUM_PERSISTENT_ARRAYS} can be mapped. 
     *            Only the first {@link SecureBox#MAX_NUM_PERSISTENT_ARRAYS} indexes starting from index zero are searched for byte[] objects. 
     *            <ul>
     *            <li>If eepromArrays contains more references to arrays than the maximum allowed number, 
     *            all object references after index {@link SecureBox#MAX_NUM_PERSISTENT_ARRAYS} will be ignored without throwing an error.</li>
     *            <li>If an object reference in the array is null, no mapping will be done.</li>
     *            <li>If the object references an object different than a byte[] in EEPROM, an exception with reason 
     *            <code>SecureBoxException.INVALID_OBJECT</code> will be thrown.
     *            <li>If one of the arrays has a length, that is not MMU granularity aligned, 
     *            <code>SecureBoxException.PERSISTENT_BYTE_ARRAY_WRONG_LENGTH</code> is thrown.</li>
     *            <li>If one of the arrays is located in transient memory, the exception with reason <code>SecureBoxException.ARRAY_IS_NOT_MAPABLE</code> is thrown.
     *            <li>If eepromArrays is null, no EEPROM arrays will be mapped to the native library.</li>
     *            <li>The EEPROM arrays are mapped to one single memory block available for the native library. </li>
     *            <li>The memory blocks are mapped to the native library in the same order as they are in eepromArrays.  </li>
     *            </ul>
     * @param ramInitializationArray
     *            This array contains the initialization data, that is copied to the RAM memory part accessible by the 
     *            native library right before the native library is called.
     *            If <code>ramInitializationArray</code> is null, RAM will not get initialized with data.
     *            The array can be located either in persistent or transient memory.
     * @param ramInitializationArray Offset
     *            Offset of initialization data in <code>ramInitializationArray</code>.
     * @param ramInitializationArrayLength
     *            Number of bytes to copy from ramInitializationData to the RAM of the native library.
     *            <ul> 
     *            <li>If ramInitializationDataLength is zero, no data is copied.</li>
     *            <li>If ramInitializationDataLength is larger than the RAM  accessible by the native library, an exception with reason 
     *            <code>SecureBoxException.INITIALIZATION_OVERFLOW</code> is thrown.</li>
     *            <li>If <code>ramInitializationDataLength + ramInitializationDataOffset > ramInitializationData.length</code>, 
     *            an <code>ArrayIndexOutOfBoundsException</code> is thrown.</li>
     *            </ul>
     * @param cryptoramInitializationArray
     *            This array contains the initialization data, that is copied to the FXRAM memory part accessible by the 
     *            native library right before the native library is called.
     *            If <code>cryptoramInitializationArray</code> is null, FXRAM will not get initialized with data.
     *            The array can be located either in persistent or transient memory.
     * @param cryptoramInitializationArrayOffset
     *            Offset of initialization data in <code>cryptoramInitializationArrayOffset</code>.
     * @param cryptoramInitializationArrayLength
     *            Number of bytes to copy from ramInitializationData to the RAM of the native library.
     *            <ul> 
     *            <li>If ramInitializationDataLength is zero, no data is copied.</li>
     *            <li>If ramInitializationDataLength is larger than the RAM  accessible by the native library, an exception with reason 
     *            <code>SecureBoxException.INITIALIZATION_OVERFLOW</code> is thrown.</li>
     *            <li>If <code>ramInitializationDataLength + ramInitializationDataOffset > ramInitializationData.length</code>, 
     *            an <code>ArrayIndexOutOfBoundsException</code> is thrown.</li>
     *            </ul>
     * @param resultArray1
     *            Array to return calculation results back to the applet after the native library exits.
     *            It is the responsibility of the native library to decide, how many bytes will get copied
     *            into this array.
     *            <ul>
     *            <li>If <code>resultArray1</code> is <code>null</code>, no data will be copied back even if data is provided by the native library.</li>
     *            <li>If the native library wants to return more data than space is available in <code>resultArray1</code>, 
     *            an <code>SecureBoxException.RESULT_OVERFLOW</code> is thrown.</li>
     *            <li>If the array is located in EEPROM, a TransactionException can be thrown, if the native library
     *            wants the data to be copied as atomic operation</li>
     *            </ul>
     * @param resultArray1Offset
     *            Offset in <code>resultArray1</code>, where return data will be placed.
     * @param resultArray2
     *            Array to return calculation results back to the applet after the native library exits.
     *            It is the responsibility of the native library to decide, how many bytes will get copied
     *            into this array.
     *            <ul>
     *            <li>If <code>resultArray2</code> is <code>null</code>, no data will be copied back even if data is provided by the native library.</li>
     *            <li>If the native library wants to return more data than space is available in <code>resultArray2</code>, 
     *            an <code>SecureBoxException.RESULT_OVERFLOW</code> is thrown.</li>
     *            <li>If the array is located in EEPROM, a TransactionException can be thrown, if the native library
     *            wants the data to be copied as atomic operation</li>
     *            </ul>
     * @param resultArray2Offset
     *            Offset in <code>resultArray2</code>, where return data will be placed.

     * @throws SecureBoxException
     *             in case of invalid parameter or malfunction of the native
     *             library.
     */

    public final static short runNativeLib(
                                      byte      libraryID, 
	                                    byte 	    functionID,

                                      Object[]  eepromArrays,

                                     	byte[] 	  ramInitializationArray,
                                    	short	    ramInitializationArrayOffset,
                                    	short 	  ramInitializationArrayLength,
                                    
                                      byte[] 	  cryptoramInitializationArray,
                                    	short	    cryptoramInitializationArrayOffset,
                                    	short 	  cryptoramInitializationArrayLength,
                                    
                                    	byte[]	  resultArray1,
                                    	short 	  resultArray1Offset,
                                    
                                    	byte[]	  resultArray2,
                                    	short 	  resultArray2Offset
                                     ) throws SecureBoxException
    {
      return( (short) 0 );
    }

}
