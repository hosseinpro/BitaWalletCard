/*******************************************************************************
*                                                                              *
*     IBM Confidential                                                         *
*                                                                              *
*                                                                              *
*     See Copyright Instructions (C-S 0-6045-002)                              *
*                                                                              *
********************************************************************************
*                                                                              *
*   @module           $RCSfile: ECPoint.java.rca $
*   @revision         $Revision: 1.4 $
*   @date             $Date: Thu Sep 10 17:43:36 2009 $
*                                                                              *
*   @description                                                               *
*   This is a class that defines JCOP specific extensions to the JavaCard      *
*   standard. 														           *
*                                                                              *
********************************************************************************/

package com.nxp.id.jcopx;
import javacard.security.ECPublicKey;
/**
 * The interface ECPoint defines provides methods that can be used to generate
 * Javacard-objects which implement JCOP-specific algorithms. This interface inherits
 * all set and get methods available in <code>ECPublicKey</code>. A new method for
 * elliptic curve point addition over GF(p) is implemented. The first operand is set using the <code>setW()</code> method.
 * The second operand is passed via a byte array <code>buffer</code>to <code>addPoint</code>.
 * The operands are in the format of uncompressed points <code>04 | X | Y <code>.
 */

public interface ECPoint extends ECPublicKey
{
	/**
	 * Adds the operand in buffer to the operand in field W of the ECPoint object.
	 *
	 * @param buffer
	 *            byte array containing the point to be added to ECPoint
	 * @param offset
	 *            offset in to the point in buffer
	 * @param length
	 *            length of the point
	 * @return void
	 *
	 * @throws CryptoException
	 *             with the following reason codes:
	 *             <ul>
	 *             <li><code>it throws <code>CryptoException</code> with the reason code
	 *				<code>ILLEGAL_VALUE</code>. if the operand is not formatted as uncompressed point.
	 *				<code>ILLEGAL_VALUE</code>. if the length in incorrect.
	 *				<code>UNINITIALIZED_KEY</code>. if the point is not initialized similar to public key.
	 *				<code>ILLEGAL_USE</code>. if the result of addition is the neutral point.
	 *
	 * @internal error mutes the card
	 *
	 */
	public void addPoint(byte[] buffer, short offset, short length);	
}

