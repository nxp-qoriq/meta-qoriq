LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

DEPENDS += "python3-pyelftools-native python3-pycryptodome-native dtc-native"
DEPENDS_remove = "python3-pycrypto-native"

FILESEXTRAPATHS_prepend_qoriq := "${THISDIR}/${BPN}:"

SRC_URI = "git://bitbucket.sw.nxp.com/dash/optee_os.git;protocol=ssh;nobranch=1 \
           file://0001-allow-setting-sysroot-for-libgcc-lookup.patch \
           file://0001-scripts-sign_encrypt.py-Correct-the-Crypto-module-na.patch \
           file://0001-arm64-Disable-outline-atomics-when-compiling.patch \
          "
SRCREV = "0cb01f7f6aee552ead49990c06f69f73f459cc65"

EXTRA_OEMAKE_append_lx2162aqds = " CFG_EMBED_DTB_SOURCE_FILE=fsl-lx2160a-qds.dts CFG_EMBED_DT=y"

OPTEEMACHINE_lx2162aqds = "lx2160aqds"
