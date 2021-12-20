DESCRIPTION = "Vector Packet Processing"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "dpdk openssl python3-ply util-linux"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/vpp;nobranch=1 \
        file://0001-vpp-core-fix-package_qa-error.patch \
"
SRCREV = "2dc49092878205ab062a00d4d46240aa0759ba4e"

S = "${WORKDIR}/git"

inherit cmake
inherit pkgconfig
inherit python3-dir

OECMAKE_SOURCEPATH = "${S}/src"

export ARCH ="aarch64"
export OPENSSL_PATH = "${RECIPE_SYSROOT}/usr"
export DPDK_PATH = "${RECIPE_SYSROOT}/usr"

EXTRA_OECONF = " \
	--with-libtool-sysroot=${SYSROOT} \
	--srcdir=${S}/src \
        --with-pre-data=128 \
        --without-libnuma \
        --without-ipv6sr \
"

CFLAGS += " -ftls-model=local-dynamic -DCLIB_LOG2_CACHE_LINE_BYTES=6 -I${OPENSSL_PATH}/usr/include  -L${OPENSSL_PATH}/lib -Wl,--dynamic-linker=/lib/ld-linux-aarch64.so.1"

do_install:append() {
        mkdir -p ${D}/etc/vpp
        cp ${S}/src/vpp/conf/startup.conf ${D}/etc/vpp/startup.conf
}

do_configure:prepend() {
	echo "@@@@ Creating libdpdk.a in ${DPDK_PATH}/lib"
	cd ${DPDK_PATH}/lib && \
	echo "GROUP ( "$(ls librte*.a)" )" > libdpdk.a && cd -
}

include vpp-pkgs.inc

BBCLASSEXTEND = "native nativesdk"

COMPATIBLE_MACHINE:class-target = "(qoriq)"
