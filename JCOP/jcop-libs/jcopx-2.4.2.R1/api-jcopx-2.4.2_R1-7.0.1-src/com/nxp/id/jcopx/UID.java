/*******************************************************************************
*                                                                              *
*     IBM Confidential                                                         *
*                                                                              *
*     WebSphere Everyplace Chip Operating System                               *
*                                                                              *
*     (C) COPYRIGHT IBM CORP 2001,2006                                         *
*                                                                              *
*     LICENSED MATERIALS - PROPERTY OF IBM                                     *
*                                                                              *
*     All Rights Reserved                                                      *
*                                                                              *
*     U.S. Government Users Restricted Rights - Use, duplication or            *
*     disclosure restricted by GSA ADP Schedule Contract with IBM Corp.        *
*                                                                              *
*     See Copyright Instructions (C-S 0-6045-002)                              *
*                                                                              *
*******************************************************************************/
package com.nxp.id.jcopx;

import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.NullPointerException;

import javacard.framework.SystemException;
import javacard.framework.Util;

import javacardx.external.ExternalException;

/**
 * Class to provide UID functionality. The contained methods can be used to: 
 * <ul>
 *     <li>Get the static UID from the devices security row</li>
 *     <li>Enable a random UID for devices in Mifare configuration A</li>
 * </ul>
 */
public final class UID {

    /** 
     * Enable random UID for contactless activation in Mifare configuration A. 
     * Once this functionality has been activated, it cannot be set back to normal 
     * UID mode. 
     *  
     * Note: The UID returned in random UID mode is always a single UID with a 
     * length of four byte. No matter what UID has been configured in the devices 
     * security row before.
     *  
     * Random UID functionality is not available on devices in Mifare 
     * configuration B1 or B4. If this method gets called on such devices, the 
     * return value will be <code>false</code>. 
     *  
     * Since JCOP version 2.4.2 R0 an authentication password is no longer 
     * required. 
     *  
     * @return <code>true</code> if the activation was successful, 
     *         <code>false</code> otherwise
     *  
     * @throws ExternalException with reason code 
     * <code>ExternalException.INVALID_PARAM</code> if the random UID cannot be 
     * enabled due to an internal error.
     */
    public static boolean enableRandomUID( )
            throws ExternalException {
        return false;
    }

    /** 
     * Enable the random UID of a card.
     * 
     * @param key random UID password
     * @param offset offset of the first byte of the password
     * 
     * @return <code>true</code> if the activation was successful, 
     *         <code>false</code> otherwise
     *  
     * @deprecated A password is no longer required. Use {@link 
     *             UID#enableRandomUID()} instead.
     */
    public static boolean enableRandomUID( byte[] key, short offset ) 
            throws ExternalException {
        return false;
    }

    /** 
     * Get the static UID from devices security row. This method always returns 
     * the devices static UID from security row. No matter what UID activation is 
     * configured on contactless activation. (i.e. even for devices in Mifare 
     * configuration A with random UID enabled, the static UID from security row 
     * is returned. 
     * 
     * @param dest destination buffer to which the UID gets copied. The buffer 
     *             must provide space for at least eight byte.
     * @param offset of the first byte in <code>dest</code>. 
     *  
     * @throws ExternalException with reason code ExternalException.INTERNAL_ERROR 
     *                           in case of an internal error.
     * @throws ArrayIndexOutOfBoundsException if the destination buffer does not 
     *                                        provide enough space.
     * @throws NullPointerException in case the destination buffer is 
     *                              <code>null</code> 
     *  
     * @since JCOP 2.4.2 R0
     */
    public static void getStaticUID( byte[] dest, short offset ) 
            throws  ExternalException, 
                    ArrayIndexOutOfBoundsException, 
                    NullPointerException {
    }

    private UID() {
        // static usage only
  }
}
