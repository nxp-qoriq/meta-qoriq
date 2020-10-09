FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://bitbucket.sw.nxp.com/dnnpi/lx2-linux.git;protocol=ssh;branch=bb3 \
    file://0001-lsdk.config-fix-issue-for-unset-ramdisk-size-in-LSDK.patch \
    file://0001-perf-Make-perf-able-to-build-with-latest-libbfd.patch \
"
SRCREV = "96f14097e92f7cf50a384a9b92617ce030bb73af"

do_deploy_append_lx2160a () {
    rm -fr ${DEPLOYDIR}/fitImage*
}
