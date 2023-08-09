# Copyright 2020-2023 NXP

require recipes-security/optee/optee-client.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/optee-client:"

DEPENDS = "util-linux-libuuid"

SRC_URI:remove = "git://github.com/OP-TEE/optee_client.git;branch=master;protocol=https"
SRC_URI:prepend = "${OPTEE_CLIENT_SRC};branch=${OPTEE_CLIENT_BRANCH} "

OPTEE_CLIENT_SRC ?= "git://github.com/nxp-qoriq/optee_client.git;protocol=https"

inherit pkgconfig
