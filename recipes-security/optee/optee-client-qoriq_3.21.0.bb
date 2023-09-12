# Copyright 2022-2023 NXP
require optee-client-qoriq.inc

OPTEE_CLIENT_BRANCH = "lf-6.1.36_2.1.0"
SRCREV = "8533e0e6329840ee96cf81b6453f257204227e6c"

PV:append = "+git${SRCPV}"
