# Copyright 2025 NXP
require recipes-security/optee-qoriq/optee-client.nxp.inc

OPTEE_CLIENT_BRANCH = "lf-6.18.2_1.0.0"
SRCREV = "${AUTOREV}"
PV:append = "+git"
