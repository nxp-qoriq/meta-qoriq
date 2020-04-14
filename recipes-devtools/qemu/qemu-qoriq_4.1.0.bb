FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

require recipes-devtools/qemu/qemu.inc

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/qemu;nobranch=1"
SRCREV= "521a0dcf59f1ca11e7d9e2f4e1ef3d2dfaebc0e4"
LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=8c5efda6cf1e1b03dcfd0e6c0d271c7f"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(qoriq|imx)"

PROVIDES = "qemu"

python() {
    pkgs = d.getVar('PACKAGES').split()
    for p in pkgs:
        if 'qemu-qoriq' in p:
            d.appendVar("RPROVIDES_%s" % p, p.replace('qemu-qoriq', 'qemu'))
            d.appendVar("RCONFLICTS_%s" % p, p.replace('qemu-qoriq', 'qemu'))
            d.appendVar("RREPLACES_%s" % p, p.replace('qemu-qoriq', 'qemu'))
}

DISABLE_STATIC = ""

DEPENDS = "glib-2.0 zlib pixman bison-native"

RDEPENDS_${PN}_class-target += "bash"

EXTRA_OECONF_append_class-target = " --target-list=${@get_qemu_target_list(d)}"
EXTRA_OECONF_append_class-target_mipsarcho32 = "${@bb.utils.contains('BBEXTENDCURR', 'multilib', ' --disable-capstone', '', d)}"
EXTRA_OECONF_append_class-nativesdk = " --target-list=${@get_qemu_target_list(d)}"

do_install_append_class-nativesdk() {
     ${@bb.utils.contains('PACKAGECONFIG', 'gtk+', 'make_qemu_wrapper', '', d)}
}

do_compile_prepend () {
        export HTTP_PROXY=${http_proxy}
        export HTTPS_PROXY=${https_proxy}
        export http_proxy=${http_proxy}
        export https_proxy=${https_proxy}
}

PACKAGECONFIG ??= " \
    fdt sdl kvm \
    ${@bb.utils.filter('DISTRO_FEATURES', 'alsa xen', d)} \
"
PACKAGECONFIG_class-nativesdk ??= "fdt sdl kvm"

INSANE_SKIP_${PN} += "already-stripped"

FILES_${PN} += "/usr/share/qemu/* /usr/var/*"

# FIXME: Avoid WARNING due missing patch for native/nativesdk
BBCLASSEXTEND = ""
