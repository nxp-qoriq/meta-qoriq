# Copyright 2022-2023 NXP
require optee-client-qoriq.inc

OPTEE_CLIENT_BRANCH = "lf_3.21.y"
SRCREV = "${AUTOREV}"

PV:append = "+git${SRCPV}"
