# Copyright 2022-2024 NXP
require optee-client-qoriq.inc

OPTEE_CLIENT_BRANCH = "lf-6.12.3_1.0.0"
SRCREV = "${AUTOREV}"

PV:append = "+git${SRCPV}"
