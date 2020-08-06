DESCRIPTION = "Data Plane Development Kit"
HOMEPAGE = "http://dpdk.org"
LICENSE = "BSD-3-Clause & LGPLv2 & GPLv2"
LIC_FILES_CHKSUM = "file://license/README;md5=3383def2d4c82237df281174e981a492"

SRC_URI = "git://bitbucket.sw.nxp.com/gitam/dpdk.git;protocol=ssh;branch=China_key_account_support \
           file://add-RTE_KERNELDIR_OUT-to-split-kernel-bu.patch \
           file://0001-add-Wno-cast-function-type.patch \
           file://0001-Add-RTE_KERNELDIR_OUT.patch \
"
SRCREV= "${AUTOREV}"

datadir[unexport] = "1"

RDEPENDS_${PN} += "python-subprocess"
DEPENDS = "virtual/kernel openssl"
DEPENDS_append_x86-64 = " numactl"
do_configure[depends] += "virtual/kernel:do_shared_workdir"

inherit module

COMPATIBLE_HOST = '(aarch64|arm|i.86|x86_64).*-linux'
COMPATIBLE_HOST_libc-musl = 'null'
COMPATIBLE_HOST_armv4 = 'null'
COMPATIBLE_HOST_armv5 = 'null'
COMPATIBLE_HOST_armv6 = 'null'

COMPATIBLE_MACHINE = "(imx|qoriq)"

DPDK_RTE_TARGET_x86-64 = "x86_64-native-linuxapp-gcc"
DPDK_RTE_TARGET_x86 = "i686-native-linuxapp-gcc"
DPDK_RTE_TARGET_armv7a = "${ARCH}-armv7a-linuxapp-gcc"
DPDK_RTE_TARGET_armv7ve = "${ARCH}-armv7a-linuxapp-gcc"
DPDK_RTE_TARGET ?= "${ARCH}-dpaa-linuxapp-gcc"
export INSTALL_PATH = "${prefix}/share/dpdk"
export RTE_KERNELDIR = "${STAGING_KERNEL_DIR}"
export RTE_KERNELDIR_OUT = "${STAGING_KERNEL_BUILDDIR}"
export RTE_TARGET = "${DPDK_RTE_TARGET}"
export RTE_OUTPUT = "${S}/${RTE_TARGET}"
export MODULE_DIR = "/lib/modules/${KERNEL_VERSION}/kernel/drivers/net"
export RTE_SDK = "${S}"
export ETHTOOL_LIB_PATH = "${S}/examples/ethtool/lib/${RTE_TARGET}/"
export EXAMPLES_BUILD_DIR = "${RTE_TARGET}"
export MODULE_CFLAGS += "-Wno-incompatible-pointer-types"

S = "${WORKDIR}/git"

do_configure () {
	#############################################################
	### default value for prefix is "usr", unsetting it, so it
	### will not be concatenated in ${RTE_TARGET}/Makefile
	### which will cause compilation failure
	#############################################################
	unset prefix
	oe_runmake O=$RTE_TARGET T=$RTE_TARGET config
}

do_compile () {
	unset LDFLAGS TARGET_LDFLAGS BUILD_LDFLAGS

	cd ${S}/${RTE_TARGET}
	oe_runmake  CONFIG_RTE_EAL_IGB_UIO=n CONFIG_RTE_KNI_KMOD=y \
	            CONFIG_RTE_LIBRTE_PMD_OPENSSL=y \
                   EXTRA_LDFLAGS="-L${STAGING_LIBDIR} --hash-style=gnu" \
		   EXTRA_CFLAGS="${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -I${STAGING_INCDIR}" \
		   CROSS="${TARGET_PREFIX}" \
		   prefix=""  LDFLAGS="${TUNE_LDARGS}"  WERROR_FLAGS="-w" V=1

	cd ${S}/examples/
	oe_runmake EXTRA_LDFLAGS="-L${STAGING_LIBDIR} --hash-style=gnu -fuse-ld=bfd" \
		   EXTRA_CFLAGS="${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -O3 -I${STAGING_INCDIR}" \
		   CROSS="${TARGET_PREFIX}" O="${S}/examples/$@/"

        oe_runmake EXTRA_LDFLAGS="-L${STAGING_LIBDIR} --hash-style=gnu -fuse-ld=bfd" \
                   EXTRA_CFLAGS="${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -O3 -I${STAGING_INCDIR}" \
		   CROSS="${TARGET_PREFIX}"  -C ipsec-secgw CONFIG_RTE_LIBRTE_PMD_OPENSSL=y O="${S}/examples/ipsec-secgw"
	oe_runmake EXTRA_LDFLAGS="-L${STAGING_LIBDIR} --hash-style=gnu -fuse-ld=bfd" \
		   EXTRA_CFLAGS="${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -O3 -I${STAGING_INCDIR}" \
		   CROSS="${TARGET_PREFIX}" -C l2fwd-crypto CONFIG_RTE_LIBRTE_PMD_OPENSSL=y O="${S}/examples/l2fwd-crypto"
}

do_install () {
	oe_runmake O=${RTE_OUTPUT} T= install-runtime DESTDIR=${D}
	oe_runmake O=${RTE_OUTPUT} T= install-kmod DESTDIR=${D} kerneldir=${MODULE_DIR}
	oe_runmake O=${RTE_OUTPUT} T= install-sdk DESTDIR=${D}
        # Install examples
	for dirname in ${S}/examples/*
	do
		install -m 0755 -d ${D}/${INSTALL_PATH}/examples/

		for appname in `find ${dirname} -regex ".*${EXAMPLES_BUILD_DIR}\/app\/[-0-9a-zA-Z0-9/_]*$"`
		do
			install -m 755 ${appname}	${D}/${INSTALL_PATH}/examples
		done
	done
        rm -rf  ${D}/${INSTALL_PATH}/examples/b* ${D}/${INSTALL_PATH}/examples/f* ${D}/${INSTALL_PATH}/examples/p*  ${D}/${INSTALL_PATH}/examples/qos_*  ${D}/${INSTALL_PATH}/examples/vhost_*  ${D}/${INSTALL_PATH}/examples/vm*  ${D}/${INSTALL_PATH}/examples/serv* 
        rm -rf ${D}/${INSTALL_PATH}/examples/ip_pipeline ${D}/${INSTALL_PATH}/examples/distributor ${D}/${INSTALL_PATH}/examples/ioat ${D}/${INSTALL_PATH}/examples/tep_termination ${D}/${INSTALL_PATH}/examples/ntb ${D}/${INSTALL_PATH}/examples/helloworld ${D}/${INSTALL_PATH}/examples/eventdev_pipeline ${D}/${INSTALL_PATH}/examples/l2fwd-cat ${D}/${INSTALL_PATH}/examples/l2fwd-jobstats ${D}/${INSTALL_PATH}/examples/l2fwd-jobstats ${D}/${INSTALL_PATH}/examples/l2fwd-event  ${D}/${INSTALL_PATH}/examples/l3fwd-power  ${D}/${INSTALL_PATH}/examples/vdpa  ${D}/${INSTALL_PATH}/examples/multi_process/mp_client   ${D}/${INSTALL_PATH}/examples/multi_process/mp_server ${D}/${INSTALL_PATH}/examples/multi_process/hotplug_mp 
         cp -rf ${S}/nxp/* ${D}/${INSTALL_PATH}/
	 cp -rf ${S}/${EXAMPLES_BUILD_DIR}/kmod/*.ko ${D}/${INSTALL_PATH}/examples
	 cp -rf ${S}/${EXAMPLES_BUILD_DIR}/app/testpmd ${D}/${INSTALL_PATH}/examples
	 cp -rf ${S}/examples/ipsec-secgw/ipsec-secgw ${D}/${INSTALL_PATH}/examples
	 cp -rf ${S}/examples/l2fwd-crypto/l2fwd-crypto ${D}/${INSTALL_PATH}/examples
	 cp -rf ${S}/${EXAMPLES_BUILD_DIR}/app/test ${D}/${INSTALL_PATH}/examples
	 cp -rf ${S}/${EXAMPLES_BUILD_DIR}/app/dpdk-test-crypto-perf  ${D}/${INSTALL_PATH}/examples
	 cp -rf ${S}/${EXAMPLES_BUILD_DIR}/app/dpdk-procinfo ${D}/${INSTALL_PATH}/examples
}

PACKAGES += "${PN}-examples"

FILES_${PN}-dbg += " \
	${INSTALL_PATH}/.debug \
	${INSTALL_PATH}/examples/*/.debug \
	"
FILES_${PN}-dev += " \
	${INSTALL_PATH}/${RTE_TARGET}/.config \
	${includedir} \
	${includedir}/exec-env \
	${INSTALL_PATH}/buildtools/ \
	${INSTALL_PATH}/${RTE_TARGET}/include \
	${INSTALL_PATH}/${RTE_TARGET}/lib \
	${INSTALL_PATH}/mk \
	"

FILES_${PN} += " ${INSTALL_PATH}/usertools/ \
		 ${prefix}/sbin/ \
		 ${prefix}/bin/ \
		 ${libdir}/ \
                 ${INSTALL_PATH}/*.sh \
		 ${INSTALL_PATH}/check_legal* \
		 ${INSTALL_PATH}/dpaa/* \
		 ${INSTALL_PATH}/dpaa2/* \
		 ${INSTALL_PATH}/ipsec/* \
		 ${INSTALL_PATH}/enetc/* \
                 ${INSTALL_PATH}/README* \
                 ${INSTALL_PATH}/examples/l2fwd \
                 ${INSTALL_PATH}/examples/l2fwd-crypto \
                 ${INSTALL_PATH}/examples/ipsec-secgw \
                 ${INSTALL_PATH}/examples/l3fwd \
		 "
FILES_${PN}-examples += " \
	${INSTALL_PATH}/examples/* \
	"
