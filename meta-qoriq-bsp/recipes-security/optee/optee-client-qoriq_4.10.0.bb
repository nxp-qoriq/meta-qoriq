# Copyright 2025-2026 NXP
require recipes-security/optee-qoriq/optee-client.nxp.inc

OPTEE_CLIENT_BRANCH = "lf-6.18.20_2.0.0"
SRCREV = "${AUTOREV}"
PV:append = "+git"
