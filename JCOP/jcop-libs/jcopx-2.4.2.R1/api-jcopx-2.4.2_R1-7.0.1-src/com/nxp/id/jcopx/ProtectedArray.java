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
*   @module           $RCSfile: ProtectedArray.java.rca $											 *
*   @revision         $Revision: 1.6 $																				 *
*   @date             $Date: Tue Jul  5 08:26:21 2011 $												 *
*                                                                              *
*   @description                                                               *
*                                                                              *
*   This class contains the API to the Protected Array interface of JCOP .     *
********************************************************************************/


package com.nxp.id.jcopx;

import javacard.framework.JCSystem;
import javacard.framework.SystemException;
import javacard.framework.TransactionException;
import java.lang.SecurityException;

/**
 * <code>ProtectedArray</code> is a JCOP proprietary extension to create and access a data array,
 * protected with integrity check.
 * <code>ProtectedArray</code> is used for integrity protection.  It is not implemented to be
 * used for sensitive data (eg. KEY and PIN).
 * Several independent instances can be created at the same time.
 */
public class ProtectedArray
{
    
//******************************************************************************************************
//                                          PROTECTED ARRAY API starts here
//******************************************************************************************************

/**
    *  Public: This is the state of <cod>verifyIntegrity<cod> when verification is successful.
    */
    public static final short VERIFY_OK  = (short)0x5AA5;   
    
/**
    *  Public: This is the state of <cod>verifyIntegrity<cod> when verification fails.
    */
    public static final short VERIFY_NOK = (short)0xA55A;

/**
    * <code>ProtectedArray</code> is a JCOP proprietary extension to create and access a data array,
    * protected with integrity check.
    * <code>ProtectedArray</code> is used for integrity protection.  It is <b>NOT</b> implemented to be
    * used for sensitive data (eg. KEY and PIN).
    */
    public ProtectedArray(short MaxDataLength) throws SystemException {};
        
/**
    * Get the maximum data size of the protected array available.
    *   @return  (MaxDataLength)
    */   
    public final short getMaxDataLength() {
        return  ((short)0x00);
    }
    
/**
    * Get the actual data size of the data in used in protected array.
    *   @return  (dataLength)
    */   
    public final short getDataLength() {
            return ((short)0x00);   
    }

/**
    * Initialize the ProtectedArray with the maximum data array length.
    *   @param dataLength           length of data to be protected
    *   @param src                  source data array
    *   @param srcoffset            offset in source data array containing the first byte
    *   @return  (dataLength)
    */  
    public final short install (short len, byte[] src, short srcOffset)
                throws SystemException, TransactionException, ArrayIndexOutOfBoundsException, NullPointerException {
        return ((short)0x00);
    }
    
/**
    * Put complete data to the protected array.
    *   @param src                  source data array
    *   @param srcOffset            offset in source data array
    *   @return  (dataLength) if successful, Exception otherwise
    */
    public final short putData(byte[] src, short srcOffset)
                throws SystemException, ArrayIndexOutOfBoundsException, NullPointerException   {
        return ((short)0x00);
    }   

/**
    * Put data to the protected array.
    *   @param src                  source data array
    *   @param srcOffset            offset in source data array
    *   @param len                  length of the data to be written into protected array
    *   @param paOffset             offset in the protected array
    *   @return  (paOffset+len) if sucessful, Exception otherwise
    */
    public final short putData(byte[] src, short srcOffset, short len, short paOffset)
                throws SystemException, ArrayIndexOutOfBoundsException, NullPointerException   {
        return ((short)0x00);
    }   
    
/**
    * Deposits the short value as two successive bytes at the specified offset in the protected array. 
    *   @param paOffset             offset within protected array to deposit the first byte (the high order byte)
    *   @param sValue               short value to set into array
    *   @return  (paOffset+2) if successful, Exception otherwise
    */
    public final short setShort(short paOffset, short sValue)
                                             throws SystemException, ArrayIndexOutOfBoundsException {
        return ((short)0x00);
    }   

/**
    * Deposits the byte value at the specified offset in the protected array. 
    *   @param paOffset             offset within protected array to deposit the byte value
    *   @param bValue               byte value to set into array
    *   @return  (paOffset+1) if successful, Exception otherwise
    */
    public final short setByte(short paOffset, byte bValue)
                throws SystemException, ArrayIndexOutOfBoundsException {
        return ((short)0x00);
    }   
    
/**
    * Get complete data from the protected array.
    *   @param dest                 destination data array
    *   @param destOffset           offset in destination data array
    *   @return  (destOffset+dataLength) if successful, Exception otherwise
    */
    public final short getDataNonAtomic(byte[] dest, short destOffset)
                throws SystemException, SecurityException, ArrayIndexOutOfBoundsException, NullPointerException    {
        return ((short)0x00);
    }   

/**
    * Get data from the protected array.
    *   @param paOffset               offset in protected array
    *   @param len                  length to be read from protected array
    *   @param dest                 destination data array
    *   @param destOffset           offset in destination data array
    *   @return  (destOffset+len) if successful, Exception otherwise
    */
    public final short getDataNonAtomic(short paOffset, short len, byte[] dest, short destOffset)
                throws SystemException, SecurityException, ArrayIndexOutOfBoundsException, NullPointerException    {
        return ((short)0x00);
    }   

/**
    * Concatenates two bytes in a protected array to form a short value. 
    *   @param paOffset             offset in protected array
    *   @return  (sResult) if successful, Exception otherwise
    */
    public final short getShort(short paOffset)
                throws SystemException, SecurityException, ArrayIndexOutOfBoundsException  {
        return ((short)0x00);
    }   

/**
    * Return one byte in a protected array. 
    *   @param paOffset             offset in protected array
    *   @return  (bResult) if successful, Exception otherwise
    */
    public final byte getByte(short paOffset)
                throws SystemException, SecurityException, ArrayIndexOutOfBoundsException  {
        return ((byte)0x00);
    }   

/**
    * Verify integrity of the protected array.
    *   @return VERIFY_OK if verification is successful, Exception otherwise
    */
  public final short verifyIntegrity() throws SystemException, SecurityException {
        return ((short)0x00);
    }   

/**
    * Verify/compare source array against the protected array.
    *  @param paOffset             offset in protected array
    *  @param len                  length in source data array to be verified/compared
    *  @param src                  source data array
    *  @param srcOffset            offset in source data array
    *  @return VERIFY_OK if verification is successful, VERIFY_NOK otherwise
  */
  public final short verifyIntegrity(short paOffset, short len, byte[] src, short srcOffset) 
                throws SystemException, SecurityException, ArrayIndexOutOfBoundsException {
        return ((short)0x00);
    }   

}

