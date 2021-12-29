SRC_URI = "git://bitbucket.sw.nxp.com/dash/qoriq-uefi-binary;protocol=ssh;nobranch=1"
SRCREV = "a92e8b495eb394315de78a4002fe8a651adbabd1"

do_install:lx2160ardb-rev2 () {
       install -d ${D}/uefi
       cp -r  ${B}/grub ${D}/uefi
       cp -r  ${B}/lx2160ardb ${D}/uefi/
}

do_deploy:lx2160ardb-rev2 () {
       install -d ${DEPLOYDIR}/uefi
       cp -r  ${B}/grub   ${DEPLOYDIR}/uefi
       cp -r  ${B}/lx2160ardb ${DEPLOYDIR}/uefi/
}
