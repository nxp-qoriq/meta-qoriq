# Copyright 2025 NXP
require recipes-security/optee-qoriq/optee-client.nxp.inc

OPTEE_CLIENT_BRANCH = "lf-6.12.49_2.2.0"
SRCREV = "${AUTOREV}"
PV:append = "+git"
