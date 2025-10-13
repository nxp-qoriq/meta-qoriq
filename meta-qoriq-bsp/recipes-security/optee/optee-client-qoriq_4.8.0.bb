# Copyright 2025 NXP
require recipes-security/optee-qoriq/optee-client.nxp.inc

OPTEE_CLIENT_BRANCH = "lf_4.8.y"
SRCREV = "${AUTOREV}"
PV:append = "+git"
