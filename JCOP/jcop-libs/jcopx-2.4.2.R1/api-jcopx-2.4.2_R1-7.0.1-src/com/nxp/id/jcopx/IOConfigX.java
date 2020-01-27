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
*   @module           $RCSfile: IOConfigX.java.rca $
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

/**
 * Class to Config the IO resources from within JAVA
 */
public final class IOConfigX
{

    /** value to identify the I2C interface to be used in a {@link
     *  IOConfigX#enableInterface(byte)} call. */
    public static final byte I2C_IF = (byte)0xda;

    /** value to identify the SPI interface to be used in a {@link
     *  IOConfigX#enableInterface(byte)} call. */
    public static final byte SPI_IF = (byte)0xb5;

    /** configId to set the address of the I2C interface */
    public static final byte I2C_SLAVEADDRESS = (byte)0x69;

    /** configId to set the clock mode for the SPI interface */
    public static final byte SPI_CLOCKMODE = (byte)0x97;

    /** config values for the SPI interface */
    public static final byte CPOL_0_CPHA_0 = (byte)0x01;
    public static final byte CPOL_0_CPHA_1 = (byte)0x02;
    public static final byte CPOL_1_CPHA_0 = (byte)0x03;
    public static final byte CPOL_1_CPHA_1 = (byte)0x04;
    /** Identifier to indicate exceptions in the C-layer implementation */
    public static final byte IOCon_CLayerException_NONE                         = (byte)0x00;
    public static final byte IOCon_CLayerException_IF_INVALID                   = (byte)0xa9;
    public static final byte IOCon_CLayerException_IF_ENABLED_DISABLED_FAILED   = (byte)0xb8;
    public static final byte IOCon_CLayerException_CONFIG_ID_INVALID            = (byte)0xc7;
    public static final byte IOCon_CLayerException_CONFIG_VALUE_INVALID         = (byte)0xd6;
    public static final byte IOCon_CLayerException_ARRAY_REFERENCE_INVALID      = (byte)0xe5;

    private IOConfigX() {
        // static usage only.
    }

    
    /**
     * Enable the given interface.
     *  
     * @param ifId of the interface
     *  
     * @throws IOConfigException with reason codes 
     * <ul>
     *     <li>{@link IOConfigException#IF_INVALID} in case
     *     an invalid ifId has been given.</li>
     *     <li>{@link IOConfigException#IF_ENABLED_DISABLED_FAILED} in case
     *     the given ifId can't be enabled.</li>
     * </ul>
     */
    public static void enableInterface( byte ifId ) throws IOConfigException
    {
    }

    
    /**
     * Disable the given interface.
     *  
     * @param ifId of the interface
     *  
     * @throws IOConfigException with reason codes 
     * <ul>
     *     <li>{@link IOConfigException#IF_INVALID} in case
     *     an invalid ifId has been given.</li>
     *     <li>{@link IOConfigException#IF_ENABLED_DISABLED_FAILED} in case
     *     the given ifId can't be disabled.</li>
     * </ul>
     */
    public static void disableInterface( byte ifId ) throws IOConfigException
    {
    }

    
    /**
     * Sets a new configuration.
     *  
     * @param configId identifies the configuration item
     * @param configVal byte array incorporating the value to be configured
     * @param offset offset of the value within the array
     * @param len length of the array elements presenting the value to be configured
     *  
     * @throws IOConfigException with reason codes 
     * <ul>
     *     <li>{@link IOConfigException#CONFIG_ID_INVALID} in case
     *     an configId ifId has been given.</li>
     *     <li>{@link IOConfigException#CONFIG_VALUE_INVALID} in case
     *     the given configVal is invalid.</li>
     *     <li>{@link IOConfigException#ARRAY_REFERENCE_INVALID} in case
     *     one of the array parameters is invalid.</li>
     * </ul>
     */
    public static void setIOConfig( byte configId, byte[] configVal, short offset, short len ) throws IOConfigException
    {
    }

    
    /**
     * Gets a configuration.
     *  
     * @param configId identifies the configuration item
     * @param configVal byte array into which the current value shall be inserted
     * @param offset offset of the value within the array
     * 
     * @return length of the current value stored in byte array
     *  
     * @throws IOConfigException with reason codes 
     * <ul>
     *     <li>{@link IOConfigException#CONFIG_ID_INVALID} in case
     *     an configId ifId has been given.</li>
     *     <li>{@link IOConfigException#ARRAY_REFERENCE_INVALID} in case
     *     one of the array parameters is invalid.</li>
     * </ul>
     */
    public static short getIOConfig( byte configId, byte[] configVal, short offset ) throws IOConfigException
    {
        return (short)0;
    }

}

