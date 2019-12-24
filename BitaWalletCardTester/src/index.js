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

var rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
  completer: completer
});

var recursiveAsyncReadLine = function() {
  rl.question("> ", async function(answer) {
    const inputs = answer.split(" ");
    let response = "";
    try {
      switch (inputs[0]) {
        case "test":
          // wipe i negar 4c445bc98dc8cb9c6815fae9f7786581fed6731cd9054e0a261cdf0ef4e8e1e32aed630b7293382b5f281c729441448af131b70505363bc9a5e025f553bb36e0
          // filladdressinfo 6D2C0000000000 343161f8cd4dc2a891ed9e6b2194f2661809f345b5c92ea13cbca1ddf7cf0f3c 1 29992000
          // signtx 2000 500 mrxgs8WQsVkD6EbYGw5FBSRZTzQ8d2p7CQ 6D2C0100010001
          break;
        case "testpicolabel":
          cardreaderList = listReaders();
          connect("0");
          setTimeout(() => {
            let epdImage =
              "MgEIALABAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAeAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA+YIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAzYIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAjYIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAzYIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/4IAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/wIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/gIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAf/IAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//IAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA4AIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwYIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwYIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/7oAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/7oAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==";
            bitaWalletCard.testPicoLabel(epdImage).catch(err => {
              print(err);
            });
          }, 500);
          break;
        case "boot":
          cardreaderList = listReaders();
          connect("0");
          setTimeout(() => {
            bitaWalletCard.selectApplet();
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
        case "transmit": //apdu
          response = await bitaWalletCard.transmit(inputs[1]);
          console.log(response.data);
          break;
        case "getinfo":
          let log = "";
          response = await bitaWalletCard.getInfo();
          log += "SN: " + response.serialNumber;
          log += " | Version: " + response.version;
          log += " | Label: " + response.label;
          print(log);
          break;
        case "wipe": //wipeType(m, b, i), label, masterSeed
          await bitaWalletCard.wipe(inputs[1], inputs[2], inputs[3]);
          break;
        case "verifypin": //pin
          await bitaWalletCard.verifyPIN(inputs[1]);
          break;
        case "setpin": //newPin
          await bitaWalletCard.setPIN(inputs[1]);
          break;
        case "changepin":
          await bitaWalletCard.changePIN(inputs[1]);
          break;
        case "exportmasterseed": //backupCardTransportKeyPublic
          await bitaWalletCard.exportMasterSeed(inputs[1]);
          break;
        case "importmasterseed": //encryptedMasterSeedAndTransportKeyPublic
          await bitaWalletCard.importMasterSeed(inputs[1]);
          break;
        case "getxpubs": //count (dec), keyPaths[5]
          bitaWalletCard.getXPubs(inputs[1], inputs[2]);
          break;
        case "filladdressinfo": //keyPath, prevTxHash, prevTxUTXO, prevTxValue
          {
            addressInfo[0] = {};
            addressInfo[0].keyPath = inputs[1];
            addressInfo[0].txs = [];
            let tx = {};
            tx.txHash = inputs[2];
            tx.utxo = inputs[3];
            tx.value = inputs[4];
            addressInfo[0].txs[0] = Object.assign({}, tx);
          }
          break;
        case "signtx": //spend, fee, destAddress, changeKeyPath
          {
            const spend = parseInt(inputs[1]);
            const fee = parseInt(inputs[2]);
            const destAddress = inputs[3];
            const changeKeyPath = inputs[4];

            const result = BitaWalletCard.buildInputSection(
              spend,
              fee,
              addressInfo
            );
            if (result == null) {
              print("Error: Not enough fund!");
              break;
            }

            bitaWalletCard.signTx(
              spend,
              fee,
              destAddress,
              result.fund,
              changeKeyPath,
              result.inputSection,
              result.signerKeyPaths
            );

            // const fund = 2550000;
            // const changeKeyPath = "6D2C0000010000";
            // const inputSection1 =
            //   "011179c473e6edbdd22d6f41f585ee24ac1ef5f754743dd27a1c490dace4e8e04a000000001976a914cea064ea822a6de4441ce2b77ad9f15f8e29523d88acFFFFFFFF";
            // const signerKeyPaths = "6D2C0000000002";

            // bitaWalletCard
            //   .signTx(
            //     yesCode,
            //     fund,
            //     changeKeyPath,
            //     inputSection1,
            //     signerKeyPaths
            //   )
            //   .then(res => {
            //     print(res.signedTx);
            //   })
            //   .catch(err => {
            //     print(err);
            //   });
          }
          break;
        case "exit":
          rl.close();
          process.exit();
          break;
        default:
          print("undefined command!");
      }
    } catch (error) {
      print(error);
    }
    recursiveAsyncReadLine();
  });
};

recursiveAsyncReadLine();

function print(message) {
  process.stdout.write(message + "\n" + "> ");
}

function completer(line) {
  const completions = "test testpicolabel boot listreaders connect disconnect transmit getinfo wipe verifypin setpin changepin exportmasterseed importmasterseed getxpubs filladdressinfo signtx exit".split(
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
