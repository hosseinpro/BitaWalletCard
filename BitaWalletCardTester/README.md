# install bitcoin test node

download bitcoind, extract it, copy 'bin' files to /usr/local/bin

# start bitcoin test node

bitcoind -regtest -daemon

# generate 101 blocks and mine (50 BTC for 1st time)

bitcoin-cli -regtest generate 101

# check balance

bitcoin-cli -regtest getbalance

# send 1 btc to an address

bitcoin-cli -regtest sendtoaddress mvyQZq6UvkMB97K9bUeHp4VVS1N7SeDzRX 1.00

# generate a new block to confirm the last transaction

bitcoin-cli -regtest generate 1

# import an address to local wallet to monitor

bitcoin-cli -regtest importaddress mvyQZq6UvkMB97K9bUeHp4VVS1N7SeDzRX

# list unspent values of the given address with 0 to 100 confirmation

bitcoin-cli -regtest listunspent 0 100 "[\"mvyQZq6UvkMB97K9bUeHp4VVS1N7SeDzRX\"]"

# push tx (should call generate 1 to confirm after that)

bitcoin-cli -regtest sendrawtransaction 0100...

# decode tx

bitcoin-cli -regtest decoderawtransaction 0100...
