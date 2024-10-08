# Copyright 2022-2024 NXP
require optee-client-qoriq.inc

OPTEE_CLIENT_BRANCH = "lf_4.4.y"
SRCREV = "${AUTOREV}"

PV:append = "+git${SRCPV}"
