SRC_URI = "git://bitbucket.sw.nxp.com/dash/qoriq-uefi-binary;protocol=ssh;nobranch=1"
SRCREV= "eda1d59099195a423a62bd40b3b7a74be045aa04"

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
