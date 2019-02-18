// Version: 1.4

const sha = require("jssha");

("use strict");

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

  transmit(commandAPDU, responseFunction) {
    const cardreaderTransmit = this.cardreaderTransmit;
    return new Promise((resolve, reject) => {
      cardreaderTransmit(commandAPDU)
        .then(res => {
          const responseAPDU = this.parseResponseAPDU(res);
          if (responseAPDU.sw === "9000") {
            if (this.data === undefined) {
              this.data = "";
            }
            responseAPDU.data = this.data + responseAPDU.data;
            this.data = "";
            const result = responseFunction(responseAPDU);
            resolve(result);
          } else if (responseAPDU.sw.substring(0, 2) === "61") {
            const apduGetResponse = "00 BF 00 00 00";
            this.data += responseAPDU.data;
            this.transmit(apduGetResponse, responseFunction)
              .then(res => resolve(res))
              .catch(error => {
                reject(error);
              });
          } else {
            //reject({ sw: responseAPDU.sw });
            reject(responseAPDU.sw);
          }
        })
        .catch(error => {
          reject(error);
        });
    });
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
    for (
      let i = 0;
      i < addressInfo.length && availableFund < requiredFund;
      i++
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

  static generateKCV(data) {
    const sha256 = new sha("SHA-1", "HEX");
    sha256.update(data);
    const hash = sha256.getHash("HEX");
    const b58 = BitaWalletCard.b58Encode(hash);
    const kcv = b58.substring(0, 4);
    return kcv;
  }

  ////End of Utils

  ////Begin of card functions

  selectApplet() {
    const apduSelectApplet = "00 A4 04 00 06 FFBC00000001";
    return this.transmit(apduSelectApplet, responseAPDU => {
      return { result: true };
    });
  }

  requestWipe() {
    const apduRequestWipe = "00 E1 00 00 00";
    return this.transmit(apduRequestWipe, responseAPDU => {
      return { result: true };
    });
  }

  wipe(yesCode, newPIN, newLabel, genMasterSeed = true) {
    const apduWipe = "00 E2 00 00 04" + BitaWalletCard.ascii2hex(yesCode);
    return new Promise((resolve, reject) => {
      this.transmit(apduWipe, responseAPDU => {
        this.verifyPIN("1234")
          .then(() => this.changePIN(newPIN))
          .then(() => this.verifyPIN(newPIN))
          .then(() => this.setLabel(newLabel))
          .then(() => {
            if (genMasterSeed) this.generateMasterSeed();
          })
          .then(resolve({ result: true }))
          .catch(error => {
            reject(error);
          });
      }).catch(error => {
        reject(error);
      });
    });
  }

  getSerialNumber() {
    //ISO/IEC 7816-4 2005 Section 7.2.3
    //P1-P2: FID (2FE2: EFiccid)
    //Le=00: read entire file
    const apduGetSerialNumber = "00 B0 2F E2 00";
    return this.transmit(apduGetSerialNumber, responseAPDU => {
      return { serialNumber: responseAPDU.data };
    });
  }

  getVersion() {
    //ISO/IEC 7816-4 2005 Section 7.2.3
    //P1-P2: FID
    //Le=00: read entire file
    const apduGetVersion = "00 B0 BC 01 00";
    return this.transmit(apduGetVersion, responseAPDU => {
      const splitData = BitaWalletCard.hex2Ascii(responseAPDU.data).split(" ");
      const type = splitData[0];
      const version = splitData[1];
      return { type, version };
    });
  }

  getLabel() {
    //ISO/IEC 7816-4 2005 Section 7.2.3
    //P1-P2: FID
    //Le=00: read entire file
    const apduGetLabel = "00 B0 BC 02 00";
    return this.transmit(apduGetLabel, responseAPDU => {
      const label = BitaWalletCard.hex2Ascii(responseAPDU.data);
      return { label };
    });
  }

  setLabel(newLabel) {
    //ISO/IEC 7816-4 2005 Section 7.2.4
    //P1-P2: FID
    //Le=00: write entire file
    const hexLabel = BitaWalletCard.ascii2hex(newLabel);
    const hexLabelLength = BitaWalletCard.padHex(
      (hexLabel.length / 2).toString(16),
      2
    );
    const apduSetLabel = "00 D0 BC 02" + hexLabelLength + hexLabel;
    return this.transmit(apduSetLabel, responseAPDU => {
      return { result: true };
    });
  }

  verifyPIN(cardPIN) {
    //ISO/IEC 7816-4 2005 Section 7.5.6
    //P2=00: global PIN
    const apduVerifyPIN = "00 20 00 00 04" + BitaWalletCard.ascii2hex(cardPIN);
    return new Promise((resolve, reject) => {
      this.transmit(apduVerifyPIN, responseAPDU => {
        resolve({ result: true });
      }).catch(sw => {
        if (sw.substring(0, 3) === "63C") {
          const leftTries = parseInt(sw.substring(3), 16);
          reject({ leftTries });
        } else {
          reject({ error: sw });
        }
      });
    });
  }

  changePIN(cardNewPIN) {
    //ISO/IEC 7816-4 2005 Section 7.5.7
    //p1=01: just new pin is included
    //P2=00: global PIN
    const apduChangePIN =
      "00 24 01 00 04" + BitaWalletCard.ascii2hex(cardNewPIN);
    return this.transmit(apduChangePIN, responseAPDU => {
      return { result: true };
    });
  }

  generateMasterSeed() {
    //ISO/IEC 7816-8 2004 Section 5.1
    //P1=84: key generation with no output
    //P2=01: reference to master seed
    const apduGenerateMS = "00 C0 BC 03";
    return this.transmit(apduGenerateMS, responseAPDU => {
      return { result: true };
    });
  }

  requestExportMasterSeed() {
    //ISO/IEC 7816-8 2004 Section 5.2 and 5.9
    //P1=86: plain value encryption
    //P2=80: plain input data (on card)
    //Lc=len of publicKey and Le=len of encrypted data
    const apduRequestExportMS = "00 B1 BC 03 00";
    return this.transmit(apduRequestExportMS, responseAPDU => {
      return { result: true };
    });
  }

  exportMasterSeed(yesCode) {
    //ISO/IEC 7816-8 2004 Section 5.2 and 5.9
    //P1=86: plain value encryption
    //P2=80: plain input data (on card)
    //Lc=len of publicKey and Le=len of encrypted data
    const apduExportMS = "00 B2 BC 03 04" + BitaWalletCard.ascii2hex(yesCode);
    return this.transmit(apduExportMS, responseAPDU => {
      const encryptedMasterSeedAndTransportKeyPublic = responseAPDU.data;
      return { encryptedMasterSeedAndTransportKeyPublic };
    });
  }

  importMasterSeed(encryptedMasterSeedAndTransportKeyPublic) {
    //ISO/IEC 7816-8 2004 Section 5.2 and 5.10
    //P1=80: plain input data (on card)
    //P2=86: plain value encryption
    //Lc=len of encrypted data and Le=null
    const encryptedMasterSeedAndTransportKeyPublicLength = BitaWalletCard.padHex(
      (encryptedMasterSeedAndTransportKeyPublic.length / 2).toString(16),
      2
    );
    const apduImportMS =
      "00 D0 BC 03 " +
      encryptedMasterSeedAndTransportKeyPublicLength +
      encryptedMasterSeedAndTransportKeyPublic;
    return this.transmit(apduImportMS, responseAPDU => {
      return { result: true };
    });
  }

  importMasterSeedPlain(masterSeed) {
    //ISO/IEC 7816-4 2005 Section 7.2.4
    //P1-P2: FID
    //Le=00: write entire file

    const masterSeedLength = BitaWalletCard.padHex(
      (masterSeed.length / 2).toString(16),
      2
    );
    const apduImportMSPlain = "00 DD BC 03" + masterSeedLength + masterSeed;
    return this.transmit(apduImportMSPlain, responseAPDU => {
      return { result: true };
    });
  }

  getAddressList(keyPath, count) {
    //ISO/IEC 7816-4 2005 Section 7.2.3
    //P1-P2: FID
    //Le=00: read entire file
    const countHex = BitaWalletCard.padHex(count.toString(16), 2);
    const apduGetAddressList = "00 C0 BC 07 08" + keyPath + countHex;
    return this.transmit(apduGetAddressList, responseAPDU => {
      let addressList = [];
      const addressLength = parseInt(responseAPDU.data.substring(0, 2), 16) * 2;
      for (let i = 0; i < count; i++) {
        addressList[i] = responseAPDU.data.substring(
          i * addressLength + 2,
          (i + 1) * addressLength + 2
        );
      }

      const keyPathFirst = keyPath;
      const address_index = parseInt(keyPathFirst.substring(10, 14), 16);
      const keyPath_no_index = keyPathFirst.substring(0, 10);

      let addressInfo = [];
      for (let i = 0; i < addressList.length; i++) {
        const address = BitaWalletCard.b58Encode(addressList[i]);
        const keyPath =
          keyPath_no_index +
          BitaWalletCard.padHex((address_index + i).toString(16), 4);
        addressInfo[i] = { address, keyPath };
      }

      return { addressInfo };
    });
  }

  getSubWalletAddressList(numOfSub, firstSubWalletNumber) {
    //ISO/IEC 7816-4 2005 Section 7.2.3
    //P1-P2: FID
    //Le=00: read entire file
    const numOfSubHex = BitaWalletCard.padHex(numOfSub.toString(16), 2);
    const firstSubWalletNumberHex = BitaWalletCard.padHex(
      firstSubWalletNumber.toString(16),
      4
    );
    const apduGetSubWalletAddressList =
      "00 C0 BC 08 03" + numOfSubHex + firstSubWalletNumberHex;
    return this.transmit(apduGetSubWalletAddressList, responseAPDU => {
      let addressList = [];
      const addressLength = parseInt(responseAPDU.data.substring(0, 2), 16) * 2;
      for (let i = 0; i < numOfSub; i++) {
        addressList[i] = responseAPDU.data.substring(
          i * addressLength + 2,
          (i + 1) * addressLength + 2
        );
      }

      let addressInfo = [];
      for (let i = 0; i < addressList.length; i++) {
        const address = BitaWalletCard.b58Encode(addressList[i]);
        addressInfo[i] = { address };
      }

      return { addressInfo };
    });
  }

  requestGenerateSubWalletTx(spend, fee, numOfSub, firstSubWalletNumber) {
    //ISO/IEC 7816-8 2004 Section 5.2 and 5.4
    //INS=2A
    //P1=9F: 9E is digital signature
    //P2=9A: plain data to be signed
    //LC=00 XXXX: data length
    //LE=0000: max response length

    let payload =
      BitaWalletCard.padHex(spend.toString(16), 16) +
      BitaWalletCard.padHex(fee.toString(16), 16) +
      BitaWalletCard.padHex(numOfSub.toString(16), 2) +
      BitaWalletCard.padHex(firstSubWalletNumber.toString(16), 4);

    let payloadLength = BitaWalletCard.padHex(
      (payload.length / 2).toString(16),
      2
    );
    const apduRequestGenerateSubWalletTx =
      "00 C1 BC 06 " + payloadLength + payload;
    return this.transmit(apduRequestGenerateSubWalletTx, responseAPDU => {
      return { result: true };
    });
  }

  generateSubWalletTx(
    yesCode,
    fund,
    changeKeyPath,
    inputSection,
    signerKeyPaths
  ) {
    //ISO/IEC 7816-8 2004 Section 5.2 and 5.4
    //INS=2A
    //P1=9F: 9E is digital signature
    //P2=9A: plain data to be signed
    //LC=00 XXXX: data length
    //LE=0000: max response length
    let payload =
      BitaWalletCard.ascii2hex(yesCode) +
      BitaWalletCard.padHex(fund.toString(16), 16) +
      changeKeyPath +
      inputSection +
      signerKeyPaths;

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
    const apduGenerateSubWalletTx = "00 C2 BC 06 " + payloadLength + payload; // + "0000";
    return this.transmit(apduGenerateSubWalletTx, responseAPDU => {
      const signedTx = responseAPDU.data;
      return { signedTx };
    });
  }

  requestExportSubWallet(subWalletNumber) {
    //ISO/IEC 7816-8 2004 Section 5.2 and 5.9
    //P1=86: plain value encryption
    //P2=80: plain input data (on card)
    //Lc=len of publicKey and Le=len of encrypted data
    const subWalletNumberHex = BitaWalletCard.padHex(
      subWalletNumber.toString(16),
      4
    );
    const apduRequestExportSubWallet = "00 B1 BC 06 02" + subWalletNumberHex;
    return this.transmit(apduRequestExportSubWallet, responseAPDU => {
      return { result: true };
    });
  }

  exportSubWallet(yesCode) {
    //ISO/IEC 7816-8 2004 Section 5.2 and 5.9
    //P1=86: plain value encryption
    //P2=80: plain input data (on card)
    //Lc=len of publicKey and Le=len of encrypted data
    const apduExportSubWallet =
      "00 B2 BC 06 04" + BitaWalletCard.ascii2hex(yesCode);
    return this.transmit(apduExportSubWallet, responseAPDU => {
      const encryptedSeedAndTransportKeyPublic = responseAPDU.data;
      return { encryptedSeedAndTransportKeyPublic };
    });
  }

  generateTransportKey() {
    //ISO/IEC 7816-8 2004 Section 5.1
    //P1=84: key generation with no output
    //P2=00: reference to transport Key
    //Lc=null and Le=256
    //just returns modulus, public exponent = 0x10001
    const apduGenerateTK = "00 C0 BC 04 00";
    return this.transmit(apduGenerateTK, responseAPDU => {
      const transportKeyPublic = responseAPDU.data;
      return { transportKeyPublic };
    });
  }

  importTransportKeyPublic(backupCardTransportKeyPublic) {
    //ISO/IEC 7816-4 2005 Section 7.2.4
    //P1-P2: FID
    //Le=00: write entire file
    const backupCardTransportKeyPublicLength = BitaWalletCard.padHex(
      (backupCardTransportKeyPublic.length / 2).toString(16),
      2
    );
    const apduImportTKPub =
      "00 D0 BC 05 " +
      backupCardTransportKeyPublicLength +
      backupCardTransportKeyPublic;
    return this.transmit(apduImportTKPub, responseAPDU => {
      return { result: true };
    });
  }

  requestSignTx(spend, fee, destAddress) {
    //ISO/IEC 7816-8 2004 Section 5.2 and 5.4
    //INS=2A
    //P1=9E: digital signature
    //P2=9A: plain data to be signed

    const destAddressHex = BitaWalletCard.b58Decode(destAddress);

    let payload =
      BitaWalletCard.padHex(spend.toString(16), 16) +
      BitaWalletCard.padHex(fee.toString(16), 16) +
      destAddressHex;

    let payloadLength = BitaWalletCard.padHex(
      (payload.length / 2).toString(16),
      2
    );
    const apduRequestSignTx = "00 31 00 01 " + payloadLength + payload;
    return this.transmit(apduRequestSignTx, responseAPDU => {
      return { result: true };
    });
  }

  signTx(yesCode, fund, changeKeyPath, inputSection, signerKeyPaths) {
    //ISO/IEC 7816-8 2004 Section 5.2 and 5.4
    //INS=2A
    //P1=9E: digital signature
    //P2=9A: plain data to be signed

    let payload =
      BitaWalletCard.ascii2hex(yesCode) +
      BitaWalletCard.padHex(fund.toString(16), 16) +
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
    const apduSignTx = "00 32 00 01 " + payloadLength + payload; // + "0000";
    return this.transmit(apduSignTx, responseAPDU => {
      const signedTx = responseAPDU.data;
      return { signedTx };
    });
  }

  ////End of card functions
};

//export default BitaWalletCard;
