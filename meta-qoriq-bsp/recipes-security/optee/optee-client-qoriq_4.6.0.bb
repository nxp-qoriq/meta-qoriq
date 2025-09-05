# Copyright 2022-2024 NXP
require optee-client-qoriq.inc

OPTEE_CLIENT_BRANCH = "lf-6.12.34_2.1.0"
SRCREV = "1d91b8933a899fe50d6e650bd9a47603598e22ff"

PV:append = "+git${SRCPV}"
