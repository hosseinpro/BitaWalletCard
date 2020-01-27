javac -cp ./lib/jcardsim-3.0.5-SNAPSHOT.jar ./com/nxp/id/jcopx/KeyAgreementX.java
jar -cvf ./bin/jcopx.jar ./com/nxp/id/jcopx/KeyAgreementX.class
jar -uf ./bin/jcopx.jar ./com/nxp/id/jcopx/javacard/jcopx.exp