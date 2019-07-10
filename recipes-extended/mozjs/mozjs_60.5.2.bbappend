FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI = "https://dev.gentoo.org/~axs/distfiles/mozjs-60.5.2.tar.bz2 \
           file://0001-js.pc.in-do-not-include-RequiredDefines.h-for-depend.patch \
           file://0010-fix-cross-compilation-on-i586-targets.patch \
           file://0001-do-not-create-python-environment.patch \
           file://0002-fix-cannot-find-link.patch \
           file://0003-workaround-autoconf-2.13-detection-failed.patch \
           file://0004-do-not-use-autoconf-2.13-to-refresh-old.configure.patch \
           file://0005-fix-do_compile-failed-on-mips.patch \
           file://add-riscv-support.patch \
           file://0001-mozjs-fix-coredump-caused-by-getenv.patch \
           file://format-overflow.patch \
           file://0001-To-fix-build-error-on-arm32BE.patch \
           file://JS_PUBLIC_API.patch \
           file://0001-riscv-Disable-atomic-operations.patch \
           "


