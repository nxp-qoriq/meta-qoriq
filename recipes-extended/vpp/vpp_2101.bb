DESCRIPTION = "Vector Packet Processing"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "dpdk openssl python3-ply util-linux vpp-native"
DEPENDS_class-native = "openssl-native  python3-ply-native util-linux-native"

SRC_URI = "git://bitbucket.sw.nxp.com/dqns/vpp;protocol=ssh;nobranch=1 \
        file://0001-vpp-core-fix-package_qa-error.patch \
"
SRCREV = "0c66c07f256f29928ae6a07a099624b9e98ebb24"

S = "${WORKDIR}/git"

inherit cmake
inherit pkgconfig
inherit python3-dir python3native

OECMAKE_SOURCEPATH = "${S}/src"

export ARCH="arm64"
export OPENSSL_PATH = "${RECIPE_SYSROOT}"
export DPDK_PATH= "${RECIPE_SYSROOT}" 

EXTRA_OECONF = " \
	--with-libtool-sysroot=${SYSROOT} \
	--srcdir=${S}/src \
        --with-pre-data=128 \
        --without-libnuma \
        --without-ipv6sr \
"

CFLAGS += " -mtls-dialect=trad -DCLIB_LOG2_CACHE_LINE_BYTES=6 -I${OPENSSL_PATH}/usr/include  -L${OPENSSL_PATH}/lib"

do_install_append() {
        mkdir -p ${D}/etc/vpp
        cp ${S}/src/vpp/conf/startup.conf ${D}/etc/vpp/startup.conf
}

include vpp-pkgs.inc

BBCLASSEXTEND = "native nativesdk"

COMPATIBLE_MACHINE_class-target = "(qoriq)"
