DESCRIPTION = "Vector Packet Processing"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "gcc-runtime dpdk openssl python3-ply util-linux python3-ply-native"

SRC_URI = "git://github.com/nxp-qoriq/vpp.git;protocol=https;nobranch=1"
SRCREV = "4ff52a9204f05db6086ce8449c9238d98c12845c"

S = "${UNPACKDIR}/git"

inherit cmake pkgconfig python3-dir python3native

OECMAKE_SOURCEPATH = "${S}/src"

export ARCH ="aarch64"

EXTRA_OECONF = " \
        --with-libtool-sysroot=${STAGING_DIR_TARGET} \
	--srcdir=${S}/src \
        --with-pre-data=128 \
        --without-libnuma \
        --without-ipv6sr \
"

CFLAGS:append = " -ftls-model=local-dynamic -DCLIB_LOG2_CACHE_LINE_BYTES=6 -latomic -Wno-address-of-packed-member"
LDFLAGS:append = " -I${RECIPE_SYSROOT}/usr/include -L${RECIPE_SYSROOT}/usr/lib -Wl,--dynamic-linker=${base_libdir}/ld-linux-aarch64.so.1"

do_configure:prepend() {
	echo "@@@@ Creating libdpdk.a in ${RECIPE_SYSROOT}//usr/lib"
	cd ${RECIPE_SYSROOT}/usr/lib && \
	echo "GROUP ( "$(ls librte*.a)" )" > libdpdk.a && cd -
}

do_install:append() {
        mkdir -p ${D}/etc/vpp
        cp ${S}/src/vpp/conf/startup.conf ${D}/etc/vpp/startup.conf
        rm -rf ${D}/usr/lib/python*/
}

BBCLASSEXTEND = "native nativesdk"

PACKAGES =+ "${PN}-plugins ${PN}-data ${PN}-plugins-data"

FILES:${PN} += " \
		${prefix}${sysconfdir} \
		${sysconfdir}/vpp \
		${sysconfdir}/rc.local \
		"

FILES:${PN}-dev += " \
                ${libdir}/cmake/vpp/*.cmake \
		"

FILES:${PN}-data = " \
		${datadir}/vpp/api/core/*.json \
		${datadir}/vpp/C.py \
		${datadir}/vpp/JSON.py  \
                ${datadir}/vpp/vppapigen_json.py \
                ${datadir}/vpp/vppapigen_c.py \
		"

FILES:${PN}-plugins-data = " \
		${datadir}/vpp/api/plugins/*.json \
                ${datadir}/vpp/plugins/perfmon/PerfmonTables.tar.xz \
		"

FILES:${PN}-plugins = " \
                ${libdir}/vpp_plugins/*.so \
                ${libdir}/vat2_plugins/*.so \
                ${libdir}/vpp_api_test_plugins/*.so \
                "

INSANE_SKIP:${PN} += " buildpaths"
COMPATIBLE_MACHINE:class-target = "(qoriq)"
