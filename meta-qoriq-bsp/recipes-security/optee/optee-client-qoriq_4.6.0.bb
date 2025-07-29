# Copyright 2022-2024 NXP
require optee-client-qoriq.inc

OPTEE_CLIENT_BRANCH = "lf-6.12.34_2.1.0"
SRCREV = "02e7f9213b0d7db9c35ebf1e41e733fc9c5a3f75"

PV:append = "+git${SRCPV}"
