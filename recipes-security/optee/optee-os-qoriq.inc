# Copyright 2020-2023 NXP
require recipes-security/optee/optee-os.inc

DEPENDS:append = " dtc-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/optee-os:"

SRC_URI:remove = "git://github.com/OP-TEE/optee_os.git;branch=master;protocol=https"
SRC_URI:prepend = "${OPTEE_OS_SRC};branch=${OPTEE_OS_BRANCH} "
SRC_URI:remove = "file://0007-allow-setting-sysroot-for-clang.patch"
SRC_URI:remove = "file://0008-no-warn-rwx-segments.patch"

OPTEE_OS_SRC ?= "git://github.com/nxp-qoriq/optee_os.git;protocol=https"
SRCREV = "${AUTOREV}"

REQUIRED_MACHINE_FEATURES = "optee"

inherit features_check

OPTEEMACHINE = "ls-${MACHINE}"
OPTEEMACHINE:ls1088ardb-pb   = "ls-ls1088ardb"
OPTEEMACHINE:ls1046afrwy     = "ls-ls1046ardb"
OPTEEMACHINE:lx2162aqds      = "ls-lx2160aqds"
OPTEEMACHINE:lx2160ardb-rev2 = "ls-lx2160ardb"

EXTRA_OEMAKE:append = " \
    CFG_TEE_TA_LOG_LEVEL=0 \
    CFG_TEE_CORE_LOG_LEVEL=1 \
"
COMPATIBLE_MACHINE = "(qoriq-arm64)"

