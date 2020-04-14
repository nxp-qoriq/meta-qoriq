LIC_FILES_CHKSUM = "file://COPYING;md5=228c72f2a91452b8a03c4cab30f30ef9"

SRC_URI = "http://www.nxp.com/lgfiles/sdk/lsdk2004/gpu-module-lsdk2004.bin;fsl-eula=true \
           file://0001-Makfile-add-modules_install.patch \
           "
SRC_URI[md5sum] = "d4351aa3b369d1295da551d6e5da2b"
SRC_URI[sha256sum] = "3a9c6843ac335d6eb4f3ddad168b938b05c5ce528c827b0c2eff1cd7f0e80061"

S = "${WORKDIR}/gpu-module-lsdk2004"

