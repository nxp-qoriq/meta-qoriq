DEPENDS += "python3-pycryptodomex-native"
DEPENDS_remove = "python3-pycrypto-native"

CFLAGS += "${TOOLCHAIN_OPTIONS}"

SRC_URI = "git://bitbucket.sw.nxp.com/dndev/secure_obj;protocol=ssh;nobranch=1"
SRCREV = "71a6eb33b58a8578a60995da3896f8f2d638c916"
