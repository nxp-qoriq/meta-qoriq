UEFI_BRANCH ?= "master"
UEFI_SRC ?= "git://bitbucket.sw.nxp.com/dash/qoriq-uefi-binary;protocol=ssh"
SRC_URI = "${UEFI_SRC};branch=${UEFI_BRANCH}"
SRCREV = "6574766eb82484af284b47a141d4b5d7002a1731"

do_install_lx2160ardb-rev2 () {
       install -d ${D}/uefi
       cp -r  ${B}/grub ${D}/uefi
       cp -r  ${B}/lx2160ardb ${D}/uefi/
}

do_deploy_lx2160ardb-rev2 () {
       install -d ${DEPLOYDIR}/uefi
       cp -r  ${B}/grub   ${DEPLOYDIR}/uefi
       cp -r  ${B}/lx2160ardb ${DEPLOYDIR}/uefi/
}
