// Version: 2.0

const Base64 = require("./Base64.js");

module.exports = class BitaWalletCard {
  constructor(cardreaderTransmit) {
    this.cardreaderTransmit = cardreaderTransmit;
  }

  ////Begin of Utils

  parseResponseAPDU(responseAPDU) {
    responseAPDU = responseAPDU.toUpperCase();
    const data = responseAPDU.substring(0, responseAPDU.length - 4);
    const sw = responseAPDU.substring(
      responseAPDU.length - 4,
      responseAPDU.length
    );
    return { data, sw };
  }

  async transmit(commandAPDU) {
    const cardreaderTransmit = this.cardreaderTransmit;
    try {
      let res = await cardreaderTransmit(commandAPDU);
      const responseAPDU = this.parseResponseAPDU(res);
      if (responseAPDU.sw === "9000") {
        if (this.data === undefined) {
          this.data = "";
        }
        responseAPDU.data = this.data + responseAPDU.data;
        this.data = "";
        return responseAPDU;
      } else if (responseAPDU.sw.substring(0, 2) === "61") {
        const apduGetResponse = "00 C0 00 00 00";
        this.data += responseAPDU.data;
        return this.transmit(apduGetResponse);
      } else {
        throw { responseAPDU };
      }
    } catch (error) {
      throw { error };
    }
  }

  static hex2Ascii(hex) {
    hex = hex.toString();
    let str = "";
    for (var i = 0; i < hex.length && hex.substr(i, 2) !== "00"; i += 2)
      str += String.fromCharCode(parseInt(hex.substr(i, 2), 16));
    return str;
  }

  static ascii2hex(str) {
    var arr1 = [];
    for (var n = 0, l = str.length; n < l; n++) {
      var hex = Number(str.charCodeAt(n)).toString(16);
      arr1.push(hex);
    }
    return arr1.join("");
  }

  static hex2Bytes(hexStr) {
    for (var bytes = [], c = 0; c < hexStr.length; c += 2)
      bytes.push(parseInt(hexStr.substr(c, 2), 16));
    return bytes;
  }

  static bytes2Hex(byteArr) {
    for (var hex = [], i = 0; i < byteArr.length; i++) {
      hex.push((byteArr[i] >>> 4).toString(16));
      hex.push((byteArr[i] & 0xf).toString(16));
    }
    return hex.join("");
  }

  static padHex(hex, numberOfDigits) {
    const str = "0000000000000000" + hex;
    const r = str.substring(str.length - numberOfDigits);
    return r;
  }

  static b58Encode(C) {
    const A = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    let B = BitaWalletCard.hex2Bytes(C);
    var d = [],
      s = "",
      i,
      j,
      c,
      n;
    for (i in B) {
      (j = 0), (c = B[i]);
      s += c || s.length ^ i ? "" : 1;
      while (j in d || c) {
        n = d[j];
        n = n ? n * 256 + c : c;
        c = (n / 58) | 0;
        d[j] = n % 58;
        j++;
      }
    }
    while (j--) s += A[d[j]];
    return s;
  }

  static b58Decode(S) {
    const A = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    var d = [],
      b = [],
      i,
      j,
      c,
      n;
    for (i in S) {
      (j = 0), (c = A.indexOf(S[i]));
      if (c < 0) return undefined;
      c || b.length ^ i ? i : b.push(0);
      while (j in d || c) {
        n = d[j];
        n = n ? n * 58 + c : c;
        c = n >> 8;
        d[j] = n % 256;
        j++;
      }
    }
    while (j--) b.push(d[j]);
    return BitaWalletCard.bytes2Hex(b);
  }

  static rotateHex(hex) {
    let hex2 = "";
    for (let i = hex.length; i >= 2; i -= 2) {
      hex2 += hex.substring(i - 2, i);
    }
    return hex2;
  }

  static buildInputSection(spend, fee, addressInfo) {
    const requiredFund = spend + fee;
    let availableFund = 0;
    let txInputCount = 0;
    let inputSection = "";
    let signerKeyPaths = "";
    // for (
    //   let i = 0;
    //   i < addressInfo.length && availableFund < requiredFund;
    //   i++
    // ) {
    for (
      let i = addressInfo.length - 1;
      i >= 0 && availableFund < requiredFund;
      i--
    ) {
      if (addressInfo[i].txs != null) {
        for (
          let j = 0;
          j < addressInfo[i].txs.length && availableFund < requiredFund;
          j++
        ) {
          availableFund += parseInt(addressInfo[i].txs[j].value);
          txInputCount++;
          inputSection += BitaWalletCard.rotateHex(
            addressInfo[i].txs[j].txHash
          );
          inputSection += BitaWalletCard.rotateHex(
            BitaWalletCard.padHex(
              parseInt(addressInfo[i].txs[j].utxo).toString(16),
              8
            )
          );
          let publicKeyHash = BitaWalletCard.b58Decode(addressInfo[i].address);
          publicKeyHash = publicKeyHash.substring(2, 42);
          inputSection += "1976a914" + publicKeyHash + "88ac";
          inputSection += "FFFFFFFF";
          signerKeyPaths += addressInfo[i].keyPath;
        }
      }
    }

    if (availableFund < requiredFund) {
      return null;
    }

    inputSection =
      BitaWalletCard.padHex(txInputCount.toString(16), 2) + inputSection;

    const fund = availableFund;

    return { fund, inputSection, signerKeyPaths };
  }

  static satoshi2btc(satoshi) {
    return satoshi / 100000000;
  }

  static btc2satoshi(btc) {
    return parseInt(btc * 100000000);
  }

  ////End of Utils

  ////Begin of card functions

  async selectApplet() {
    const apduSelectApplet = "00 A4 04 00 06 FFBC00000001";
    await this.transmit(apduSelectApplet);
  }

  async getInfo() {
    const apduGetInfo = "00 11 00 00 00";
    let responseAPDU = await this.transmit(apduGetInfo);
    const serialNumber = responseAPDU.data.substr(0, 16);
    const version = BitaWalletCard.hex2Ascii(responseAPDU.data.substr(16, 10));
    const label = BitaWalletCard.hex2Ascii(responseAPDU.data.substr(26));
    return { serialNumber, version, label };
  }

  async verifyPIN(cardPIN) {
    const apduVerifyPIN = "00 20 00 00 04" + BitaWalletCard.ascii2hex(cardPIN);
    try {
      await this.transmit(apduVerifyPIN);
    } catch (res) {
      if (res.error.responseAPDU === undefined) throw res;
      else if (res.error.responseAPDU.sw.substring(0, 3) === "63C") {
        const leftTries = parseInt(res.error.responseAPDU.sw.substring(3), 16);
        return leftTries;
      } else {
        throw res;
      }
    }
  }

  async wipe(wipeType, label, masterSeed = "") {
    let payload = "";
    if (wipeType === "m")
      // main
      payload += "00";
    else if (wipeType === "b")
      // backup
      payload += "01";
    else if (wipeType === "i")
      // import master seed plain
      payload += "02";

    const hexLabel = BitaWalletCard.ascii2hex(label);
    const hexLabelLength = BitaWalletCard.padHex(
      (hexLabel.length / 2).toString(16),
      2
    );
    payload += hexLabelLength + hexLabel;

    if (masterSeed !== "") payload += masterSeed;

    const payloadLength = BitaWalletCard.padHex(
      (payload.length / 2).toString(16),
      2
    );

    const apduWipe = "00 E0 00 00" + payloadLength + payload;
    await this.transmit(apduWipe);
  }

  async setPIN(cardNewPIN) {
    const apduSetPIN = "00 21 00 00 04" + BitaWalletCard.ascii2hex(cardNewPIN);
    await this.transmit(apduSetPIN);
  }

  async changePIN() {
    const apduChangePIN = "00 24 00 00 00";
    await this.transmit(apduChangePIN);
  }

  async exportMasterSeed(backupCardTransportKeyPublic) {
    const backupCardTransportKeyPublicLength = BitaWalletCard.padHex(
      (backupCardTransportKeyPublic.length / 2).toString(16),
      2
    );
    const apduExportMS =
      "00 B1 00 00" +
      backupCardTransportKeyPublicLength +
      backupCardTransportKeyPublic;
    await this.transmit(apduExportMS);
  }

  async importMasterSeed(encryptedMasterSeedAndTransportKeyPublic) {
    const encryptedMasterSeedAndTransportKeyPublicLength = BitaWalletCard.padHex(
      (encryptedMasterSeedAndTransportKeyPublic.length / 2).toString(16),
      2
    );
    const apduImportMS =
      "00 B2 00 00 " +
      encryptedMasterSeedAndTransportKeyPublicLength +
      encryptedMasterSeedAndTransportKeyPublic;
    await this.transmit(apduImportMS);
  }

  async getXPubs(count, keyPaths) {
    const hexCount = BitaWalletCard.padHex(count.toString(16), 2);
    const length = BitaWalletCard.padHex(
      (keyPaths.length / 2 + 1).toString(16),
      2
    );
    const apduGetXPubs = "00 50 00 00" + length + hexCount + keyPaths;
    await this.transmit(apduGetXPubs);
  }

  async signTx(
    spend,
    fee,
    destAddress,
    fund,
    changeKeyPath,
    inputSection,
    signerKeyPaths
  ) {
    const destAddressHex = BitaWalletCard.b58Decode(destAddress);
    let payload =
      BitaWalletCard.padHex(spend.toString(16), 10) +
      BitaWalletCard.padHex(fee.toString(16), 10) +
      destAddressHex +
      BitaWalletCard.padHex(fund.toString(16), 10) +
      changeKeyPath +
      inputSection +
      signerKeyPaths;

    let payloadLength;
    if (payload.length / 2 <= 255) {
      payloadLength = BitaWalletCard.padHex(
        (payload.length / 2).toString(16),
        2
      );
    } else {
      //extended length
      payloadLength =
        "00" + BitaWalletCard.padHex((payload.length / 2).toString(16), 4);
    }

    const apduSignTx = "00 30 00 00 " + payloadLength + payload;
    await this.transmit(apduSignTx);
  }

  async testPicoLabel(epdBase64) {
    let apdu = "00 A4 04 00 05 0102030405 00";
    await this.transmit(apdu);
    apdu = "12 000000";
    await this.transmit(apdu);
    const epdHex = Base64.base64ToHex(epdBase64);
    for (let i = 0; i < epdHex.length; i += 492) {
      let chunk = epdHex.substr(i, 492);
      let len = BitaWalletCard.padHex((chunk.length / 2).toString(16), 2);
      apdu = "B5 20 04 00" + len + chunk;
      await this.transmit(apdu);
    }
    apdu = "B5 24 10 00 00";
    await this.transmit(apdu);
  }

  async cancel() {
    try {
      await this.getSerialNumber();
    } catch (error) {}
  }

  ////End of card functions
};
