FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/linux;nobranch=1 \
    file://0001-lsdk.config-fix-issue-for-unset-ramdisk-size-in-LSDK.patch \
    file://0001-perf-Make-perf-able-to-build-with-latest-libbfd.patch \
"
SRCREV = "27194a5af186bbda9a52029cd968a0fd9e798c2d"

do_deploy_append_lx2160a () {
    rm -fr ${DEPLOYDIR}/fitImage*
}
