FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://bitbucket.sw.nxp.com/dnnpi/lx2-linux.git;protocol=ssh;branch=bb3 \
    file://0001-lsdk.config-fix-issue-for-unset-ramdisk-size-in-LSDK.patch \
    file://0001-perf-Make-perf-able-to-build-with-latest-libbfd.patch \
"
SRCREV = "4f3f3d841323ad9f23449365110a4bb6e0fe8711"

do_deploy_append_lx2160a () {
    rm -fr ${DEPLOYDIR}/fitImage*
}
