FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/linux;nobranch=1 \
    file://0001-lsdk.config-fix-issue-for-unset-ramdisk-size-in-LSDK.patch \
    file://0001-perf-Make-perf-able-to-build-with-latest-libbfd.patch \
"
SRCREV = "408e5a0deaa82f3a3596863f0847bee68f0807fc"

do_deploy_append_lx2160a () {
    rm -fr ${DEPLOYDIR}/fitImage*
}
