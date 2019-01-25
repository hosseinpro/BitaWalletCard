#!/usr/bin/env node

"use strict";

const readline = require("readline");
const pcsclite = require("pcsclite");
const pcsc = pcsclite();
const request = require("request");
const BitaWalletCard = require("../src/BitaWalletCard.js");
const bitaWalletCard = new BitaWalletCard(transmit.bind(this));

const jcardsim = "jcardsim";
let cardreaderList;
let cardreader = null;
let protocol;

let addressInfo = [];
let inputSection;

var rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
  completer: completer
});

var recursiveAsyncReadLine = function() {
  rl.question("> ", function(answer) {
    const inputs = answer.split(" ");
    switch (inputs[0]) {
      case "test":
        console.log("Hello test");
        break;
      case "boot":
        cardreaderList = listReaders();
        connect("0");
        setTimeout(() => {
          bitaWalletCard.selectApplet();
          setTimeout(() => {
            bitaWalletCard.verifyPIN("1234").catch(err => {
              print(err);
            });
          }, 500);
        }, 500);
        break;
      case "listreaders":
        cardreaderList = listReaders();
        let s = "";
        for (let i = 0; i < cardreaderList.length; i++)
          s += "[" + i.toString() + "] " + cardreaderList[i] + "\n";
        print(s);
        break;
      case "connect":
        connect(Number.parseInt(inputs[1]));
        break;
      case "disconnect":
        disconnect();
        break;
      case "transmit":
        transmit(inputs[1]);
        break;
      case "selectapplet":
        let log = "";
        bitaWalletCard.selectApplet().then(() => {
          bitaWalletCard.getSerialNumber().then(res => {
            log += "SN: " + res.serialNumber;
            bitaWalletCard.getVersion().then(res => {
              log += " | Type: " + res.type + " | Version: " + res.version;
              print(log);
            });
          });
        });
        break;
      case "perso":
        bitaWalletCard.setPUK(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "verifypin":
        bitaWalletCard.verifyPIN(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "changepin":
        bitaWalletCard.changePIN(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "unblockpin":
        bitaWalletCard.unblockPIN(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "getlabel":
        bitaWalletCard.getLabel().catch(err => {
          print(err);
        });
        break;
      case "setlabel":
        bitaWalletCard.setLabel(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "importmasterseedplain":
        bitaWalletCard.importMasterSeedPlain(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "generatemasterseed":
        bitaWalletCard.generateMasterSeed().catch(err => {
          print(err);
        });
        break;
      case "requestremovemasterSeed":
        bitaWalletCard.requestRemoveMasterSeed().catch(err => {
          print(err);
        });
        break;
      case "removemasterseed":
        bitaWalletCard.removeMasterSeed(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "requestexportmasterseed":
        bitaWalletCard.requestExportMasterSeed().catch(err => {
          print(err);
        });
        break;
      case "exportmasterseed":
        bitaWalletCard.exportMasterSeed(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "importmasterseed":
        bitaWalletCard.importMasterSeed(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "getaddresslist": //keyPath, count
        bitaWalletCard
          .getAddressList(inputs[1], inputs[2])
          .then(res => {
            print(JSON.stringify(res.addressInfo).replace(",{", ",\n{"));
          })
          .catch(err => {
            print(err);
          });
        break;
      case "getsubwalletaddresslist": //numOfSub, firstSubWalletNumber
        bitaWalletCard
          .getSubWalletAddressList(inputs[1], inputs[2])
          .then(res => {
            print(JSON.stringify(res.addressInfo).replace(",{", ",\n{"));
          })
          .catch(err => {
            print(err);
          });
        break;
      case "settxinput": //prevTxHash, prevTxUTXO, prevTxValue
        {
          addressInfo[0].txs = [];
          let tx = {};
          tx.txHash = inputs[1];
          tx.utxo = inputs[2];
          tx.value = inputs[3];
          addressInfo[0].txs[0] = Object.assign({}, tx);
        }
        break;
      case "requestgeneratesubwallettx": //spend, fee, numOfSub, firstSubWalletNumber
        {
          const spend = parseInt(inputs[1]);
          const fee = parseInt(inputs[2]);
          const numOfSub = parseInt(inputs[3]);
          const firstSubWalletNumber = inputs[4];

          bitaWalletCard
            .requestGenerateSubWalletTx(
              spend,
              fee,
              numOfSub,
              firstSubWalletNumber
            )
            .catch(err => {
              print(err);
            });

          inputSection = BitaWalletCard.buildInputSection(
            spend,
            fee,
            addressInfo
          );
          if (inputSection == null) {
            print("Error: Not enough fund!");
            break;
          }
        }
        break;
      case "generatesubwallettx": //yesCode, changeKeyPath
        {
          const yesCode = inputs[1];
          const changeKeyPath = inputs[2];

          bitaWalletCard
            .generateSubWalletTx(
              yesCode,
              inputSection.fund,
              changeKeyPath,
              inputSection.inputSection,
              inputSection.signerKeyPaths
            )
            .then(res => {
              print(res.signedTx);
            })
            .catch(err => {
              print(err);
            });
        }
        break;
      case "requestexportsubwallet": //subWalletNumber
        bitaWalletCard.requestExportSubWallet(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "exportsubwallet": //yesCode
        bitaWalletCard.exportSubWallet(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "generatetransportkey":
        bitaWalletCard.generateTransportKey().catch(err => {
          print(err);
        });
        break;
      case "importtransportkeypublic": //backupCardTransportKeyPublic
        bitaWalletCard.importTransportKeyPublic(inputs[1]).catch(err => {
          print(err);
        });
        break;
      case "requestsigntx": //spend, fee, destAddress
        {
          const spend = parseInt(inputs[1]);
          const fee = parseInt(inputs[2]);
          bitaWalletCard.requestSignTx(spend, fee, inputs[3]).catch(err => {
            print(err);
          });

          inputSection = BitaWalletCard.buildInputSection(
            spend,
            fee,
            addressInfo
          );
          if (inputSection == null) {
            print("Error: Not enough fund!");
            break;
          }
        }
        break;
      case "signtx": //yesCode, changeKeyPath
        {
          const yesCode = inputs[1];
          const changeKeyPath = inputs[2];

          bitaWalletCard
            .signTx(
              yesCode,
              inputSection.fund,
              changeKeyPath,
              inputSection.inputSection,
              inputSection.signerKeyPaths
            )
            .then(res => {
              print(res.signedTx);
            })
            .catch(err => {
              print(err);
            });
        }
        break;
      case "exit":
        rl.close();
        process.exit();
        break;
      default:
        console.log("undefined command!");
    }
    recursiveAsyncReadLine();
  });
};

recursiveAsyncReadLine();

function print(message) {
  process.stdout.write(message + "\n" + "> ");
}

function completer(line) {
  const completions = "test boot listreaders connect disconnect transmit selectapplet perso verifypin changepin unblockpin getlabel setlabel importmasterseedplain generatemasterseed requestremovemasterSeed removemasterseed requestexportmasterseed exportmasterseed importmasterseed getaddresslist getsubwalletaddresslist settxinput requestgeneratesubwallettx generatesubwallettx requestexportsubwallet exportsubwallet generatetransportkey importtransportkeypublic requestsigntx signtx exit".split(
    " "
  );
  const hits = completions.filter(c => c.startsWith(line));
  // show all completions if none found
  return [hits.length ? hits : completions, line];
}

function listReaders() {
  const cardreaderList = [];
  for (let i = 0; i < Object.keys(pcsc.readers).length; i++) {
    cardreaderList[i] = Object.keys(pcsc.readers)[i];
  }
  cardreaderList[cardreaderList.length] = jcardsim;
  return cardreaderList;
}

function connect(readerIndex) {
  if (cardreaderList[readerIndex] === jcardsim) {
    request.post(
      {
        headers: { "content-type": "application/x-www-form-urlencoded" },
        url: "http://localhost:4444/jcardsim",
        body: "connect"
      },
      (error, response, body) => {
        if (error) {
          print("Error: " + error.message);
        } else {
          cardreader = jcardsim;
          print("Connected");
        }
      }
    );
  } else {
    const reader = pcsc.readers[cardreaderList[readerIndex]];
    if (!reader) {
      print("Card reader " + readerIndex.toString() + " not found.");
      return;
    }
    reader.connect((error, protocol1) => {
      if (error) {
        print("Error: " + error.message);
      } else {
        protocol = protocol1;
        cardreader = reader;
        print("Connected");
      }
    });
  }
}

function disconnect() {
  if (cardreader === null) print("Disconnected");
  else if (cardreader === jcardsim) {
    request.post(
      {
        headers: { "content-type": "application/x-www-form-urlencoded" },
        url: "http://localhost:4444/jcardsim",
        body: "disconnect"
      },
      (error, response, body) => {
        if (error) {
          print("Error: " + error.message);
        } else {
          print("Disconnected");
        }
      }
    );
  } else {
    cardreader.disconnect(error => {
      if (error) print("Error: " + error.message);
      else {
        print("Disconnected");
      }
    });
  }
}

function transmit(commandAPDU) {
  return new Promise((resolve, reject) => {
    commandAPDU = commandAPDU.toUpperCase().replace(/\s/g, "");
    const timeCommand = new Date();

    if (cardreader === null) reject("Disconnected");
    if (cardreader === jcardsim) {
      request.post(
        {
          headers: { "content-type": "application/x-www-form-urlencoded" },
          url: "http://localhost:4444/jcardsim",
          body: commandAPDU
        },
        (error, response, body) => {
          if (error) reject("Error: " + error.message);
          const responseAPDU = body;
          logAPDU(commandAPDU, timeCommand, responseAPDU);
          resolve(responseAPDU);
        }
      );
      return;
    }
    if (!cardreader.connected) reject("Card not found");
    let inBuffer = Buffer.from(commandAPDU, "hex");
    let cardResponseBufferSize = 500;
    cardreader.transmit(
      inBuffer,
      cardResponseBufferSize,
      protocol,
      (error, outBuffer) => {
        if (error) reject("Error: " + error.message);
        const responseAPDU = outBuffer.toString("hex");
        logAPDU(commandAPDU, timeCommand, responseAPDU);
        resolve(responseAPDU);
      }
    );
  });
}

function logAPDU(commandAPDU, timeCommand, responseAPDU) {
  const timeResponse = new Date();
  const executionTime = timeResponse.getTime() - timeCommand.getTime();
  print(
    "\n" +
      timeCommand.toLocaleTimeString("it-IT") +
      " << " +
      commandAPDU +
      "\n" +
      timeResponse.toLocaleTimeString("it-IT") +
      " >> " +
      responseAPDU +
      " [" +
      executionTime.toString() +
      "ms]" +
      "\n"
  );
}
