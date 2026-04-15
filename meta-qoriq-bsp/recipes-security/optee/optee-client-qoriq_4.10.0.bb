# Copyright 2025-2026 NXP
require recipes-security/optee-qoriq/optee-client.nxp.inc

OPTEE_CLIENT_BRANCH = "lf_4.10.y"
SRCREV = "${AUTOREV}"
PV:append = "+git"
