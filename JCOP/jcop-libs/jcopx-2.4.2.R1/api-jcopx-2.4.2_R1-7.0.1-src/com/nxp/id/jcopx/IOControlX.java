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
*   @module           $RCSfile: IOControlX.java.rca $
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
 * Class to control the IO resources from within JAVA
 */
public final class IOControlX
{


    /** Pin value to set a pin to high signal */
    public static final byte IO_HIGH = (byte)0xA5;

    /** Pin value to set a pin to low signal */
    public static final byte IO_LOW = (byte)0xCA;

    /** Pin identifier of GPIO pin 1 to be used in a {@link
     *  IOControlX#setIo(byte,byte)} call. */
    public static final byte IOID_P1 = (byte)0x01;
    /** Pin identifier of GPIO pin 2 to be used in a {@link
     *  IOControlX#setIo(byte,byte)} call. */
    public static final byte IOID_P2 = (byte)0x02;
    /** Pin identifier of GPIO pin 3 to be used in a {@link
     *  IOControlX#setIo(byte,byte)} call. */
    public static final byte IOID_P3 = (byte)0x03;
    /** Pin identifier of GPIO pin 4 to be used in a {@link
     *  IOControlX#setIo(byte,byte)} call. */
    public static final byte IOID_P4 = (byte)0x04;
    /** configuration value to set the pin to act as 'open collector', to be used in a {@link
     *  IOControlX#setIOConfig(byte,byte)} call. */
    public static final byte IO_CONF_OPENCOLLECTOR = (byte)0xAF;
    /** configuration value to set the pin to act as 'push-pull', to be used in a {@link
     *  IOControlX#setIOConfig(byte,byte)} call. */
    public static final byte IO_CONF_PUSHPULL = (byte)0x65;
    
    /** Identifier to indicate exceptions in the C-layer implementation */
    public static final byte IO_CLayerException = (byte)0xff;

    private IOControlX() {
        // static usage only.
    }
    
    /**
     * Set the IO level of identified pin to given value.
     *  
     * @param id of the pin to set the value for
     * @param value to be set on the pin
     *  
     * @throws IOControlException with reason codes 
     * <ul>
     *     <li>{@link IOControlException#INVALID_VALUE} in case
     *     an invalid value has been given.</li>
     *     <li>{@link IOControlException#INVALID_IO_ID} in case
     *     an invalid pin identifier has been given.</li>
     * </ul>
     */
    public static void setIO( byte id, byte value ) throws IOControlException
    {
    }

    /** 
     * Get the current pin value. <br /> 
     * Example: 
     * <pre> 
     *     ...
     *     // Get the current pin value from IO Pin 1
     *     byte pinValue = IOControlX.getIO( IOControlX.IOID_P1 );
     *     
     *     // Do something with the pin value
     *     ...
     * </pre> 
     * 
     * @param id of the pin to get the value for. Only the pin 
     *           identifier from this class are allowed.
     * 
     * @return byte 
     *  
     * @throws IOControlException with reason codes 
     * <ul>
     *     <li>{@link IOControlException#INVALID_IO_ID} in case
     *     an invalid pin identifier has been given.</li>
     * </ul>
     *  
     */
    public static byte getIO( byte id ) throws IOControlException
    {
        return (byte)0x00;
    }
    
    /**
     * Set the IO configuration of the identified pin to given value.
     *  
     * @param id of the pin to set the value for
     * @param value to be configured on the pin
     *  
     * @throws IOControlException with reason codes 
     * <ul>
     *     <li>{@link IOControlException#INVALID_VALUE} in case
     *     an invalid value has been given.</li>
     *     <li>{@link IOControlException#INVALID_IO_ID} in case
     *     an invalid pin identifier has been given.</li>
     * </ul>
     */
    public static void setIOConfig( byte id, byte value ) throws IOControlException
    {
    }

}

