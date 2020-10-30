SRC_URI = "git://bitbucket.sw.nxp.com/dash/qoriq-uefi-binary;protocol=ssh;nobranch=1"
SRCREV= "f50f60bc01da829cb37aa606ec7ee418a692f5ec"

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
