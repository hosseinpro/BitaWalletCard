/*******************************************************************************
*                                                                              *
*     IBM Confidential                                                         *
*                                                                              *
*                                                                              *
*     See Copyright Instructions (C-S 0-6045-002)                              *
*                                                                              *
********************************************************************************
*                                                                              *
*   @module           $RCSfile: NFCModem.java.rca $
*   @revision         $Revision: 1.1 $
*   @date             $Date: Tue Jun 28 19:03:55 2011 $
*                                                                              *
*   @description                                                               *
*   This is a class that defines JCOP specific extensions to the JavaCard      *
*   standard. 														                                     *
*                                                                              *
********************************************************************************/


package com.nxp.id.jcopx;

// import package JPACKAGE(java.io);IOException

/**
 * The class Extensions define static methods that can be used to generate
 * Javacard-objects which implement JCOP-specific algorithms. It currently
 * defines the following fields and methods:
 */
public class NFCModem //throws IOException this may not be sufficient. The C layer throws a variety of exceptions
{
//<CODE></CODE>
	/**
	 * Writes <CODE>transmitLength</CODE> bytes to device specified by address <CODE>command</CODE>
	 * Reads one byte from device specified by address <CODE>command</CODE>
	 * into the buffer <CODE>receiveBuff</CODE> at offset <CODE>receiveOffset</CODE>.
	 *
	 * @param command
	 *             the address of target NFC device register connected to the bus.
	 *             The address is one byte long. The LSB is R/W selector.
	 *             The other 7 bits are the actual address of the device.
	 * @param transmitBuff
	 *             a byte array containing the data to be written to the NFC device.
	 *
	 * @param transmitOffset
	 *            the start offset from the above array.
	 *
	 * @param transmitLength
	 *           number of bytes to be written to the NFC device.
	 *
	 * @param receiveBuff
	 *             a byte array where the read data from the NFC device is written to.
	 *
	 * @param receiveOffset
	 *            the start offset from the above array.
	 *
	 * @param receiveLength
	 *           number of bytes to be read from the NFC device.

	 *
	 * @return short
	 *
	 * @throws
	 *             <ul>
	 *             <li>APDUException_BUFFER_BOUNDS_VAL if start condition fails.(busy)</li>
	 *             <li>APDUException_BAD_LENGTH_VAL in case of no ACK on slave-address by writing a the address to the bus.</li>
	 *             <li>APDUException_IO_ERROR_VAL in case of no ACK on data byte while attempting to write a data byte to the device.</li>
	 *             </ul>
	 *
	 */
	
	/**
  * HOST and RF command queries the state of the NFC device
  * 
  */
	public static final byte HOST_AND_RF_CMD =(byte) 0x12;
	
	/**
  * SVDD control command requests power cycle on SVDD
  * 
  */	
	public static final byte SVDD_CNTLR_CMD  =(byte) 0x14;
	
	/**
  * PUSH DATA commands writes data to NFC device
  * 
  */
	public static final byte PUSH_DATA_CMD   =(byte) 0x16;
	
	/**
  * RESET IF command sent to NFC device handed over to HCI
  * 
  */  
	public static final byte RESET_IF_CMD    =(byte) 0x18;
	 
    public static short exchange(byte command, byte[] transmitBuff, short transmitOffset, short transmitLength, byte[] receiveBuff, short receiveOffset, short receiveLength)
   	{
		   return (short)0;
		}
}

